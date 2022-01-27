package Genuine.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Genuine.com.activity.AddAnnonce;
import Genuine.com.activity.AnnonceActivity;
import Genuine.com.activity.SignInActivity;
import Genuine.com.activity.SignUpActivity;
import Genuine.com.adapter.AdapterAnnonce;
import Genuine.com.model.AnnonceModel;
import Genuine.com.model.UserModel;
import Genuine.com.activity.ChckOutActivity;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterAnnonce mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initToolbar();

        get_data();


        }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            Intent i = new Intent(this, ChckOutActivity.class);
            this.startActivity(i);

        } else if (item.getItemId() == R.id.action_create_acc) {
            Intent i = new Intent(this, SignUpActivity.class);
            this.startActivity(i);
        } else if (item.getItemId() == R.id.action_login) {
            Intent i = new Intent(this, SignInActivity.class);
            this.startActivity(i);

        } else if (item.getItemId() == R.id.action_add_product) {
            Intent i = new Intent(this, AddAnnonce.class);
            this.startActivity(i);
        } else if (item.getItemId() == R.id.action_logout) {
            Toast.makeText(this, "Logging you out....", Toast.LENGTH_SHORT).show();
            try {
                UserModel.deleteAll(UserModel.class);
                Toast.makeText(this, "Logged you out successfully!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to Log you out because " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    List<AnnonceModel> products = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void get_data() {
        db.collection("PRODUCTS").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                products = queryDocumentSnapshots.toObjects(AnnonceModel.class);
                initComponents();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                initComponents();
            }
        });

    }

    ProgressBar progressBar;

    private void initComponents() {


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        mAdapter = new AdapterAnnonce(products, this, "0");
        recyclerView.setAdapter(mAdapter);


        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterAnnonce.OnItemClickListener() {
            @Override
            public void onItemClick(View view, AnnonceModel obj, int position) {
                Intent i = new Intent(MainActivity.this, AnnonceActivity.class);
                i.putExtra("id", obj.Annonce_id);
                MainActivity.this.startActivity(i);
            }
        });


    }


    private void initToolbar() {
        try {
            progressBar = findViewById(R.id.progressBar);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setVisibility(View.GONE);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Genuine");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setSystemBarColor(this);
        } catch (Exception e) {

        }
    }


    public static void setSystemBarColor(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }











}