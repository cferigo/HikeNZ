package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout base;
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.loginEmail_TextField);
        mPassword = findViewById(R.id.loginPassword_TextField);
        mLoginBtn = findViewById(R.id.login_Button);
        mRegisterBtn = findViewById(R.id.register_Link);
        fAuth = FirebaseAuth.getInstance();
        base = findViewById(R.id.login_base);

        // This avoids return users needing to login
        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    // on click event that takes user to the register activity
    public void registerLink(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    // validates text fields when login button is clicked
    public void Login(View view) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if(email.isEmpty()){
            mEmail.setError("Please populate all fields");

        }else if (password.isEmpty()){
            mPassword.setError("Please populate all fields");
        }
        else {
        // authenticate the user
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Error! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}



