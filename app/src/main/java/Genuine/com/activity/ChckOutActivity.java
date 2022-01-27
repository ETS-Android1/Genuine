package Genuine.com.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Genuine.com.MainActivity;
import Genuine.com.adapter.AdapterAnnonce;
import Genuine.com.model.CartModel;
import Genuine.com.model.AjoutModel;
import Genuine.com.model.AnnonceModel;
import Genuine.com.model.UserModel;
import Genuine.com.tools.Utils;
import Genuine.com.R;

public class ChckOutActivity extends AppCompatActivity {

    UserModel loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chck_out);

        loggedInUser = Utils.get_logged_in_user();
        if (loggedInUser == null) {
            Toast.makeText(this, "You are not logged in.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, SignUpActivity.class);
            this.startActivity(i);
            finish();
            return;
        }


        initToolbar();

        get_cart_data();
    }

    List<CartModel> cartModels = null;
    List<AnnonceModel> products = new ArrayList<>();

    private void get_cart_data() {
        try {
            cartModels = (List<CartModel>) CartModel.listAll(CartModel.class);
        } catch (Exception e) {
            Toast.makeText(this, "Failed because " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (cartModels == null) {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        for (CartModel c : cartModels) {
            AnnonceModel p = new AnnonceModel();
            p.title = c.product_name;
            p.category = "";
            p.details = "";
            try {
                p.price = Integer.valueOf(c.product_price);
            } catch (Exception e) {

            }
            p.Annonce_id = c.product_id;
            p.photo = c.product_photo;
            products.add(p);
        }


        feed_cart_data();
    }

    RecyclerView recyclerView;
    private AdapterAnnonce mAdapter;
    Button submit_order;

    private void feed_cart_data() {
        recyclerView = findViewById(R.id.cart_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new AdapterAnnonce(products, this, "1");
        recyclerView.setAdapter(mAdapter);

        submit_order = findViewById(R.id.submit_order);
        submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_order();
            }
        });

    }

    public static String CUSTOMER_ORDERS = "CUSTOMER_ORDERS";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    private void submit_order() {
        AjoutModel ajoutModel = new AjoutModel();
        ajoutModel.Ajout_id = db.collection(CUSTOMER_ORDERS).document().getId();
        ajoutModel.customer = loggedInUser;
        ajoutModel.cart = cartModels;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        db.collection(CUSTOMER_ORDERS).document(ajoutModel.Ajout_id).set(ajoutModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChckOutActivity.this, "Order Submitted successfully!", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                        progressDialog.dismiss();

                        try {
                            CartModel.deleteAll(CartModel.class);
                        } catch (Exception e) {
                            Toast.makeText(ChckOutActivity.this, "Failed to clear the cart.", Toast.LENGTH_SHORT).show();
                        }

                        finish();
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.hide();
                progressDialog.dismiss();
                Toast.makeText(ChckOutActivity.this, "Failed to submit order because " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MainActivity.setSystemBarColor(this);
        //setSystemBarLight(this);
    }

}