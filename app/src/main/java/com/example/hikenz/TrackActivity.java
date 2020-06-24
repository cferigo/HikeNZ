package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import javax.annotation.Nullable;

public class TrackActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView name, time, distance, difficulty, description;
    ImageView dogFriendly;
    FirebaseFirestore fStore;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        String value = getIntent().getStringExtra("trackid");
        id = value;

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
                long j = documentSnapshot.getLong("Distance");
                if ( j > 10){
                    time.setText(documentSnapshot.getLong("Time").toString() + " day(s)");
                }
                else{
                    time.setText(documentSnapshot.getLong("Time").toString() + " hr(s)");
                }
                name.setText(documentSnapshot.getString("Name"));
                distance.setText(j + " km");
                difficulty.setText(documentSnapshot.getString("Difficulty"));
                description.setText(documentSnapshot.getString("Description"));
                boolean i = documentSnapshot.getBoolean("DogFriendly");
                if (i == true) {
                    dogFriendly.setBackgroundResource(R.drawable.ic_yes);
                }
                GeoPoint geoPoint = documentSnapshot.getGeoPoint("Location");
                double lat = geoPoint.getLatitude();
                double lng = geoPoint.getLongitude();

            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            case R.id.actionbar_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        DocumentReference docReference = fStore.collection("Tracks").document(id);
        docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                GeoPoint geoPoint = documentSnapshot.getGeoPoint("Location");
                double lat = geoPoint.getLatitude();
                double lng = geoPoint.getLongitude();
                name.setText(documentSnapshot.getString("Name"));
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat , lng))
                        .title(String.valueOf(name)));
            }
        });

    }
    // need to create a save track button

}