package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText mFirstName, mLastName, mEmail, mPassword, mConfirmPassword;
    Button mRegisterBtn;
    TextView mBackBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstName = findViewById(R.id.firstName_TextField);
        mLastName = findViewById(R.id.lastName_TextField);
        mEmail = findViewById(R.id.email_TextField);
        mPassword = findViewById(R.id.password_TextField);
        mConfirmPassword = findViewById(R.id.confirmPassword_TextField);
        mRegisterBtn = findViewById(R.id.register_Button);
        mBackBtn = findViewById(R.id.back_Link);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        }

        // register button method that will create a new user in the database
    // needs validation show email must be entered, and phone needs to be between 9-11 digits
    public void register(View view) {
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String firstName = mFirstName.getText().toString();
        final String lastName = mLastName.getText().toString();

        final String fav = "Favorite Tracks: ";
        String[] favArray = fav.split("\\s*,\\s*");
        final List<String> favorite = Arrays.asList(favArray);

        final String fin = "Finished Tracks: ";
        String[] finArray = fin.split("\\s*,\\s*");
        final List<String> finished = Arrays.asList(finArray);

        // quick validation to ensure the fields are not empty
        if(TextUtils.isEmpty(email) | TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Please Enter Correct Information",Toast.LENGTH_LONG).show();
        }

        // add a accepts terms n conditions or privacy or whatever check box that must be checked before sign in

        // This creates a new user in firebase fireStore
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference docReference = fStore.collection("Users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    user.put("email", email);
                    user.put("favorite", favorite);
                    user.put("finished", finished);

                    docReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "onSuccess: user profile created for" + userID);
                        }
                    });
                    Toast.makeText(RegisterActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // on click event that takes user back to login activity
    public void loginLink(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}