package com.example.hikenz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class AddReviewActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView fName;
    EditText fReview;
    String userID, value, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        fName = findViewById(R.id.addRev_firstName_textView);
        fReview = findViewById(R.id.addRev_review_editTExt);

        value = getIntent().getStringExtra("trackid");
        title = getIntent().getStringExtra("trackname");

        // Getting the current user
        final DocumentReference docReference = fStore.collection("Users").document(userID);

        // replacing the place holder text views with the current users information
        docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent( DocumentSnapshot documentSnapshot,  FirebaseFirestoreException e) {
                fName.setText(documentSnapshot.getString("firstName"));
            }
        });
    }

    public void addReview(View view) {
        String nme = fName.getText().toString();
        String rev = fReview.getText().toString();
        if(rev.isEmpty()) {
            fReview.setError("Please write a Review");
        }else {
            DocumentReference docReference = fStore.collection("Tracks").document(value).collection("Reviews").document();
            Map<String, Object> review = new HashMap<>();
            review.put("firstName", nme);
            review.put("review", rev);

            docReference.set(review).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("TAG", "onSuccess: review created");
                }
            });
            Toast.makeText(AddReviewActivity.this, "Review Created", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
            intent.putExtra("trackid", value);
            intent.putExtra("trackname", title);
            startActivity(intent);
        }
    }
}