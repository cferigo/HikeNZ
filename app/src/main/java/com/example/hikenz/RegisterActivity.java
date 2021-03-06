package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    CheckBox mCheckBox;
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
        mCheckBox = findViewById(R.id.register_checkBox);
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
        boolean isChecked = mCheckBox.isChecked();
        String cPwd = mConfirmPassword.getText().toString();
        // adds 2 empty lists to the users profile
        final List<String> favorite = Arrays.asList();
        final List<String> finished = Arrays.asList();

        // quick validation to ensure the fields are not empty
        if(email.isEmpty()){
            mEmail.setError("Please populate all fields");

        }
        if(password.isEmpty()){
            mPassword.setError("Please populate all fields");
        }
        if(firstName.isEmpty()) {
            mFirstName.setError("Please populate all fields");
        }
        if(lastName.isEmpty()) {
            mLastName.setError("Please populate all fields");
        }
        if(cPwd.isEmpty()) {
            mConfirmPassword.setError("Please populate all fields");
        }

        else if (!password.equals(cPwd)){
            mConfirmPassword.setError("Passwords do not match");
        }
        else if (isChecked != true){
            mCheckBox.setError("Please accept terms and conditions to create an account");
        }
        else {
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
                        user.put("role", "0");

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
    }

    // on click event that takes user back to login activity
    public void loginLink(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}