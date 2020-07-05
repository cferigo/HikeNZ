package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImagesActivity extends AppCompatActivity {
        private RecyclerView mRecyclerView;
        private ImageAdapter mAdapter;

        private ProgressBar mProgressCircle;
        //FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        //CollectionReference imgRef;
        private DatabaseReference mDatabaseRef;
        private List<Upload> mUploads;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);


        mRecyclerView = findViewById(R.id.recycler_view_images);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);
       String value = getIntent().getStringExtra("trackid");
        mUploads = new ArrayList<>();
        //Database Reference
        //mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        //if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)){
            //imgRef = fStore.collection("Tracks").document(Objects.requireNonNull(value)).collection("Images");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener()
         {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                   Upload upload = postSnapshot.getValue(Upload.class);
                   mUploads.add(upload);
               }

               mAdapter = new ImageAdapter(ImagesActivity.this, mUploads);

               mRecyclerView.setAdapter(mAdapter);
               mProgressCircle.setVisibility(View.INVISIBLE);
                Toast.makeText(ImagesActivity.this, (CharSequence) mUploads, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImagesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

   /*private void setUpRecyclerView() {
        Query query = imgRef;
        FirestoreRecyclerOptions<Upload> uploads = new FirestoreRecyclerOptions.Builder<Upload>().setQuery(query, Upload.class).build();
        mAdapter = new ImageAdapter(uploads);
        RecyclerView recyclerView = findViewById(R.id.review_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

    } */
}