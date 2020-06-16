package com.example.hikenz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // when a user clicks logout it clears the current users data so that next time the user opens the app ahain they must login
    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    // button that takes the user to their profile page
    public void profileButton(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }
}
