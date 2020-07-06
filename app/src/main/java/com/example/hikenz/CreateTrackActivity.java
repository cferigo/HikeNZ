package com.example.hikenz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.protobuf.Int32Value;

import java.util.HashMap;
import java.util.Map;

public class CreateTrackActivity extends AppCompatActivity {
    FirebaseFirestore fStore;
    Button create, back;
    EditText name, desc, dif, time, dist, lat, lng;
    CheckBox dog;
    String tName, tDesc, tDifficulty, tLatit, tLongi;
    int tTime, tDist;
    boolean tDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_track);

        fStore = FirebaseFirestore.getInstance();
        create = findViewById(R.id.addTrack_createTrack_Button);
        name = findViewById(R.id.addTrack_Name_TextField);
        desc = findViewById(R.id.addTrack_Description_TextField);
        dif = findViewById(R.id.addTrack_difficulty);
        time = findViewById(R.id.addTrack_Time_TextField);
        dist = findViewById(R.id.addTrack_Distance_TextField);
        lat = findViewById(R.id.addTrack_Latitude);
        lng = findViewById(R.id.addTrack_Longitude);
        dog = findViewById(R.id.addTrack_Dog_checkBox);
        back = findViewById(R.id.addTrack_back_Link);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            tName = name.getText().toString().trim();
            tDesc = desc.getText().toString().trim();
            tDifficulty = dif.getText().toString().trim();
            tTime = Integer.parseInt(String.valueOf(time.getText()));
            tDist = Integer.parseInt(String.valueOf(time.getText()));
            tLatit = lat.getText().toString().trim();
            tLongi = lng.getText().toString().trim();
            Double tLat = Double.parseDouble(tLatit);
            Double tLong = Double.parseDouble(tLongi);
            if (dog.isChecked()){
                tDog = true;
            } else {
                tDog = false;
            }

                DocumentReference docReference = fStore.collection("Tracks").document();
                Map<String, Object> track = new HashMap<>();
                track.put("Name", tName);
                track.put("Description", tDesc);
                track.put("Difficulty", tDifficulty);
                track.put("Distance", tDist);
                track.put("Time", tTime);
                track.put("DogFriendly", tDog);
                track.put("Location", new GeoPoint(tLat,tLong));

                docReference.set(track).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "onSuccess: track created");
                    }
                });
                Toast.makeText(CreateTrackActivity.this, "Track Created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}