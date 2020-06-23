package com.example.hikenz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class TrackActivity extends AppCompatActivity {
    TextView name, time, distance, difficulty, description;
    ImageView dogFriendly;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        String value = getIntent().getStringExtra("trackid");

        name = findViewById(R.id.track_name);
        time = findViewById(R.id.track_time_textView);
        distance = findViewById(R.id.track_distance_textView);
        difficulty = findViewById(R.id.track_difficulty_textView);
        description = findViewById(R.id.track_description);
        dogFriendly = findViewById(R.id.dogFriendly_ic);
        fStore = FirebaseFirestore.getInstance();

        DocumentReference docReference = fStore.collection("Tracks").document(value);
        docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("Name"));
                time.setText(documentSnapshot.getLong("Time").toString() + " hr(s)");
                distance.setText(documentSnapshot.getLong("Distance").toString() + " km");
                difficulty.setText(documentSnapshot.getString("Difficulty"));
                description.setText(documentSnapshot.getString("Description"));
                boolean i = documentSnapshot.getBoolean("DogFriendly");
                if (i == true) {
                    dogFriendly.setBackgroundResource(R.drawable.ic_yes);
                }
            }
        });

    }


}