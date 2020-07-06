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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    TextView firstName, lastName, email, finTrack, favTrack;
    LinearLayout adminLayout, userLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID, role;
    ArrayList<String> favTracksAList;
    ArrayList<String> finTracksAList;
    Button showUsers, createTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstName = findViewById(R.id.profile_fName);
        lastName = findViewById(R.id.profile_lName);
        email = findViewById(R.id.profile_email);
        finTrack = findViewById(R.id.profile_finTracks);
        favTrack = findViewById(R.id.profile_favTracks);
        adminLayout = findViewById(R.id.admin_layout);
        userLayout = findViewById(R.id.profile_trackLists);
        showUsers = findViewById(R.id.profile_how_Users);
        createTrack = findViewById(R.id.profile_create_track);
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
                StringBuilder favStringBuilder = new StringBuilder();
                for (String s : favTracksAList) {
                    favStringBuilder.append(s + "\n");
                }
                favTrack.setText(favStringBuilder.toString());
                finTracksAList = (ArrayList<String>) documentSnapshot.get("finished");
                StringBuilder finStringBuilder = new StringBuilder();
                for (String s : finTracksAList) {
                    finStringBuilder.append(s + "\n");
                }
                finTrack.setText(finStringBuilder.toString());
                role = (String) documentSnapshot.getString("role");
                if (role.equals("1")){
                    userLayout.setVisibility(View.GONE);
                    adminLayout.setVisibility(View.VISIBLE);
                }
            }
        });

       createTrack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), CreateTrackActivity.class));
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
