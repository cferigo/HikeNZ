package com.example.hikenz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class ReviewActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reviewRef;


    private ReviewAdapter adapter;
    Button addRevActivityBtn;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            reviewRef = db.collection("Tracks").document(Objects.requireNonNull(getIntent().getStringExtra("trackid"))).collection("Reviews");
        }

        setUpRecyclerView();
        value = getIntent().getStringExtra("trackid");
        addRevActivityBtn = findViewById(R.id.addRev_buttonLink);
        addRevActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddReviewActivity.class);
                intent.putExtra("trackid", value);
                startActivity(intent);
            }
        });
    }
    private void setUpRecyclerView() {
        Query query = reviewRef;
        FirestoreRecyclerOptions<Review> options = new FirestoreRecyclerOptions.Builder<Review>().setQuery(query, Review.class).build();
        adapter = new ReviewAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.review_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

