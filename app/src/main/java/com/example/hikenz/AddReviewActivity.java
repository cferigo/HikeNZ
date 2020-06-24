package com.example.hikenz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AddReviewActivity extends AppCompatActivity {
    TextView fname;

    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        String value = getIntent().getStringExtra("trackid");
        id = value;

        fname = findViewById(R.id.addRev_firstName_textView);
        fname.setText(id);

    }
}