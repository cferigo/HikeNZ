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
    TextView firstName, lastName, email;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    ArraySet<String> favTracks;
    //List<String> favorites;
    //ArrayList<ArrayList<String>> favTrackArray;
    Map<Integer, String> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstName = findViewById(R.id.profile_fName);
        lastName = findViewById(R.id.profile_lName);
        email = findViewById(R.id.profile_email);
        //favorites = findViewById(R.id.profile_favorite);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

       // getFavTracks();

        // Getting the current user
        final DocumentReference docReference = fStore.collection("Users").document(userID);


        // replacing the place holder text views with the current users information
       docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent( DocumentSnapshot documentSnapshot,  FirebaseFirestoreException e) {
                firstName.setText(documentSnapshot.getString("firstName"));
                lastName.setText(documentSnapshot.getString("lastName"));
                email.setText(documentSnapshot.getString("email"));

                /*favorites.(documentSnapshot.get("Favorites"));
                favorites = Collections.(documentSnapshot.get("favorites"));
                Toast.makeText(getApplicationContext(),"favorites list" + favTrackArray ,Toast.LENGTH_SHORT).show();*/
            }
        });
    }

   /* public void getFavTracks(){
        DocumentReference docRef = fStore.collection("Users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Log.d("TAG", "DocumentSnapshot data: " + task.getResult().getData());
                        for (Object item : task.getResult().getData().values())
                            favTracks.add(item.toString());
                        Toast.makeText(getApplicationContext(), "favorites list" + favTracks, Toast.LENGTH_SHORT).show();
                    }
                    if (documentSnapshot != null) {
                        Log.d("TAG", "DocumentSnapshot data: " + task.getResult().getData());

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }*/

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
