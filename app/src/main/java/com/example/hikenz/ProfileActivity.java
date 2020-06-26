package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    TextView firstName, lastName, email, favTrack, finTrack;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    ArrayList<String> favTracksAList;
    ArrayList<String> finTracksAList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstName = findViewById(R.id.profile_fName);
        lastName = findViewById(R.id.profile_lName);
        email = findViewById(R.id.profile_email);
        finTrack = findViewById(R.id.profile_finTracks);
        favTrack = findViewById(R.id.profile_favTracks);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        // Getting the current user
        final DocumentReference docReference = fStore.collection("Users").document(userID);

        // replacing the place holder text views with the current users information
       docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent( DocumentSnapshot documentSnapshot,  FirebaseFirestoreException e) {
                firstName.setText(documentSnapshot.getString("firstName"));
                lastName.setText(documentSnapshot.getString("lastName"));
                email.setText(documentSnapshot.getString("email"));
                favTracksAList = (ArrayList<String>) documentSnapshot.get("favorite");
                //think that needs to be made into a loop to print each value in the array?
                for (String s : favTracksAList) {
                    finTrack.setText(s);
                    Log.d("TAG", s);
                }
                finTracksAList = (ArrayList<String>) documentSnapshot.get("finished");
                for (String j : finTracksAList) {
                    favTrack.setText(j);
                    Log.d("TAG", j);
                    Toast.makeText(getApplicationContext(), j,Toast.LENGTH_SHORT).show();
                }
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
        switch (item.getItemId()){
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
}
