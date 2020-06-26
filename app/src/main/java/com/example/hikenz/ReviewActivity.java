package com.example.hikenz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ReviewActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reviewRef = db.collection("Tracks");


    private ReviewAdapter adapter;
    Button addRevActivityBtn;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Query query = reviewRef;
        setUpRecyclerView(query);
        //Toast.makeText(getApplicationContext(), reviewRef, Toast.LENGTH_LONG).show();
        String value = getIntent().getStringExtra("trackid");
        id = value;
        //reviewRef = db.collection("Tracks").document(id).collection("Reviews");
        addRevActivityBtn = findViewById(R.id.addRev_buttonLink);
        addRevActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddReviewActivity.class);
                intent.putExtra("trackid", id);
                startActivity(intent);
            }
        });
    }
    private void setUpRecyclerView(Query query) {


        FirestoreRecyclerOptions<Review> options = new FirestoreRecyclerOptions.Builder<Review>().setQuery(query, Review.class).build();

        adapter = new ReviewAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.review_recycler_view);
        //recyclerView.setHasFixedSize(true);
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

