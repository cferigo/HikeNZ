package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class TrackActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView name, time, distance, difficulty, description;
    Button update, delete;
    RelativeLayout mapView;
    ImageView dogFriendly;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID, tName, value, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        value = getIntent().getStringExtra("trackid");

        name = findViewById(R.id.track_name);
        time = findViewById(R.id.track_time_textView);
        distance = findViewById(R.id.track_distance_textView);
        difficulty = findViewById(R.id.track_difficulty_textView);
        description = findViewById(R.id.track_description);
        dogFriendly = findViewById(R.id.dogFriendly_ic);
        update = findViewById(R.id.track_update_btn);
        delete = findViewById(R.id.track_delete_btn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        mapView = findViewById(R.id.view1);

        DocumentReference docRef = fStore.collection("Users").document(userID);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent( DocumentSnapshot documentSnapshot,  FirebaseFirestoreException e) {
                role = (String) documentSnapshot.getString("role");
                if (role.equals("1")){
                    update.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }
            }
        });

        // populates track activity template with selected track information from passed track id
        final DocumentReference docReference = fStore.collection("Tracks").document(value);
        docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (docReference == null){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    long j = documentSnapshot.getLong("Distance");
                    if (j > 10) {
                        time.setText(documentSnapshot.getLong("Time").toString() + " day(s)");
                    } else {
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
                    tName = documentSnapshot.getString("Name");
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mapView.setVisibility(View.GONE);
        } else {
            mapView.setVisibility(View.VISIBLE);
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), updateTrackActivity.class);
                intent.putExtra("trackid", value);
                startActivity(intent);
            }
        });

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
        DocumentReference docReference = fStore.collection("Tracks").document(value);
        docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                GeoPoint geoPoint = documentSnapshot.getGeoPoint("Location");
                double lat = geoPoint.getLatitude();
                double lng = geoPoint.getLongitude();
                String nme = documentSnapshot.getString("Name");
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat , lng))
                        .title(String.valueOf(nme)));
            }
        });

    }

    // passes track id to new review activity
    public void reviewActivityButton(View view) {
        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
        intent.putExtra("trackid", value);
        intent.putExtra("trackname", tName);
        startActivity(intent);
    }

    // saves current track to users favorites list
    public void saveFavoriteButton(View view) {
        DocumentReference docRef = fStore.collection("Users").document(userID);
        docRef.update("favorite" , FieldValue.arrayUnion(name.getText().toString()));
        Toast.makeText(getApplicationContext(),"Track added to favorites ",Toast.LENGTH_SHORT).show();
    }

    // maybe try a check box to add or remove tracks from favorites or finished?

    // saves current track to users finished list
    public void saveFinishedButton(View view) {
        DocumentReference docRef = fStore.collection("Users").document(userID);
        docRef.update("finished" , FieldValue.arrayUnion(name.getText().toString()));
        Toast.makeText(getApplicationContext(),"Track added to finished ",Toast.LENGTH_SHORT).show();
    }

    public void photosButton(View view) {
        Intent intent = new Intent(getApplicationContext(), AddImageActivity.class);
        intent.putExtra("trackid", value);
        startActivity(intent);
    }

    public void deleteTrack(View view) {
        DocumentReference docR = fStore.collection("Tracks").document(value);
        docR.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TrackActivity.this, "Track Deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}