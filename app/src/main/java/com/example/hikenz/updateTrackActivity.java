package com.example.hikenz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class updateTrackActivity extends AppCompatActivity {
    FirebaseFirestore fStore;
    Button update, back;
    EditText name, desc, dif, time, dist, lat, lng;
    CheckBox dog;
    String value, uName, uDesc, uDifficulty, uLatit, uLongi;
    int uTime, uDist;
    boolean uDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_track);

        value = getIntent().getStringExtra("trackid");
        fStore = FirebaseFirestore.getInstance();
        update = findViewById(R.id.updateTrack_Button);
        name = findViewById(R.id.updateTrack_Name_TextField);
        desc = findViewById(R.id.updateTrack_Description_TextField);
        dif = findViewById(R.id.updateTrack_difficulty);
        time = findViewById(R.id.updateTrack_Time_TextField);
        dist = findViewById(R.id.updateTrack_Distance_TextField);
        lat = findViewById(R.id.updateTrack_Latitude);
        lng = findViewById(R.id.updateTrack_Longitude);
        dog = findViewById(R.id.updateTrack_Dog_checkBox);
        back = findViewById(R.id.updateTrack_back_Link);


        DocumentReference docReference = fStore.collection("Tracks").document(value);
        docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                long j = documentSnapshot.getLong("Distance");
                time.setText(documentSnapshot.getLong("Time").toString());
                name.setText(documentSnapshot.getString("Name"));
                dist.setText(Long.toString(j));
                dif.setText(documentSnapshot.getString("Difficulty"));
                desc.setText(documentSnapshot.getString("Description"));
                boolean i = documentSnapshot.getBoolean("DogFriendly");
                if (i == true) {
                    dog.setChecked(true);
                } else {
                    dog.setChecked(false);
                }
                GeoPoint geoPoint = documentSnapshot.getGeoPoint("Location");
                double latitude = geoPoint.getLatitude();
                double longitude = geoPoint.getLongitude();
                String k = Double.toString(latitude);
                String l = Double.toString(longitude);
                lat.setText(k);
                lng.setText(l);


            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uName = name.getText().toString().trim();
                uDesc = desc.getText().toString().trim();
                uDifficulty = dif.getText().toString().trim();
                uTime = Integer.parseInt(String.valueOf(time.getText()));
                uDist = Integer.parseInt(String.valueOf(time.getText()));
                uLatit = lat.getText().toString().trim();
                uLongi = lng.getText().toString().trim();
                Double tLat = Double.parseDouble(uLatit);
                Double tLong = Double.parseDouble(uLongi);
                if (dog.isChecked()){
                    uDog = true;
                } else {
                    uDog = false;
                }


                if(uName.isEmpty()){
                    name.setError("Please enter Track name");
                }
                if(uDesc.isEmpty()){
                    desc.setError("Please enter Track description");
                }
                if(uTime == 0){
                    time.setError("Please enter Track time");
                }
                if(uDist == 0){
                    desc.setError("Please enter Track distance");
                }
                if(tLat == 0){
                    desc.setError("Please enter Track Latitude");
                }
                if(tLat == 0){
                    desc.setError("Please enter Track Longitude");
                }else{

                DocumentReference docReference = fStore.collection("Tracks").document(value);
                Map<String, Object> track = new HashMap<>();
                track.put("Name", uName);
                track.put("Description", uDesc);
                track.put("Difficulty", uDifficulty);
                track.put("Distance", uDist);
                track.put("Time", uTime);
                track.put("DogFriendly", uDog);
                track.put("Location", new GeoPoint(tLat,tLong));

                docReference.set(track, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "onSuccess: track updated");
                    }
                });
                Toast.makeText(updateTrackActivity.this, "Track updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("trackid", value);
                startActivity(intent);

            }
            }
        });

    }

}