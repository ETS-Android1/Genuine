package Genuine.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Genuine.com.adapter.AdapterAnnonces;
import Genuine.com.R;
import Genuine.com.model.AjoutModel;

import static Genuine.com.MainActivity.setSystemBarColor;
import static Genuine.com.activity.ChckOutActivity.CUSTOMER_ORDERS;

public class ListeAnnonceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_annonce);

        initToolbar();
        get_data();
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<AjoutModel> orders = new ArrayList<>();

    private void get_data() {
        db.collection(CUSTOMER_ORDERS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                orders = queryDocumentSnapshots.toObjects(AjoutModel.class);
                initComponents();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                initComponents();
            }
        });

    }


    private AdapterAnnonces mAdapter;

    private void initComponents() {


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        mAdapter = new AdapterAnnonces(orders, this, "0");
        recyclerView.setAdapter(mAdapter);


        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterAnnonces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, AjoutModel obj, int position) {
                Intent i = new Intent(ListeAnnonceActivity.this, ModifierAnnonceActivity.class);
                i.putExtra("order_id", obj.Ajout_id);
                ListeAnnonceActivity.this.startActivity(i);
            }
        });


    }


    ProgressBar progressBar;
    private RecyclerView recyclerView;

    private void initToolbar() {
        progressBar = findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSystemBarColor(this);
    }

}