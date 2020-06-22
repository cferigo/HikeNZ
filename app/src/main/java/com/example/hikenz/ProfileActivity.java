package com.example.hikenz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {
    TextView firstName, lastName, email;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    ListView favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstName = findViewById(R.id.profile_fName);
        lastName = findViewById(R.id.profile_lName);
        email = findViewById(R.id.profile_email);
        favorites = findViewById(R.id.profile_favorite);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        // Getting the current user
        DocumentReference docReference = fStore.collection("Users").document(userID);

        // replacing the place holder text views with the current users information
        docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                firstName.setText(documentSnapshot.getString("firstName"));
                lastName.setText(documentSnapshot.getString("lastName"));
                email.setText(documentSnapshot.getString("email"));
                //favorites.setText(documentSnapshot.getString("favorites"));
            }
        });
    }

    // takes the user back to the home page
    public void homeButton(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
