package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText mFirstName, mLastName, mEmail, mPassword, mConfirmPassword;
    Button mRegisterBtn;
    TextView mBackBtn;
    FirebaseAuth fAuth;


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

        // This avoids return users needing to login
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        // Validates text fields when register button is clicked
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email) | TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Please Enter Correct Information",Toast.LENGTH_LONG).show();
                }

                // This creates a new user in firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        // on click event that takes user back to login activity
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}