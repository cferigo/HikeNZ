package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ShowUsersActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("Users");
    private UserAdapter adapter;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        myDialog = new Dialog(this);
        setUpUserRecyclerView();
    }

    // creates recycler view containing tracks that match the query passed to it
    private void setUpUserRecyclerView() {
        Query query = userRef;
        if (adapter != null) {
            adapter.stopListening();
        }
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        adapter = new UserAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.user_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                final String uid = documentSnapshot.getId();
                final DocumentReference docR = db.collection("Users").document(uid);
                final TextView popClose, popName;
                String nme = documentSnapshot.getString("firstName");
                String rol = documentSnapshot.getString("role");
                Button popDelete;
                final CheckBox popAdmin;
                myDialog.setContentView(R.layout.activity_pop);
                popClose = (TextView) myDialog.findViewById(R.id.popup_close);
                popName = (TextView) myDialog.findViewById(R.id.popup_name);
                popDelete = (Button) myDialog.findViewById(R.id.popup_delete);
                popAdmin = (CheckBox) myDialog.findViewById(R.id.popup_role);
                popName.setText(nme);
                if(rol.equals("1")){
                    popAdmin.setChecked(true);
                }
                popClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popAdmin.isChecked()){
                            Map<String, Object> user = new HashMap<>();
                            user.put("role", "1");
                            docR.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: admin added");
                                }
                            });
                        } else if (!popAdmin.isChecked()){
                            Map<String, Object> user = new HashMap<>();
                            user.put("role", "0");
                            docR.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: admin removed");
                                }
                            });
                        }
                        myDialog.dismiss();
                    }
                });
                popDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        docR.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ShowUsersActivity.this, "User Deleted!", Toast.LENGTH_SHORT).show();
                                myDialog.dismiss();
                            }
                        });
                    }
                });
                myDialog.show();
            }
        });
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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