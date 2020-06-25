package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference trackRef = db.collection("Tracks");
    private TrackAdapter adapter;
    Query query = trackRef;
    Button searchBtn;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = findViewById(R.id.main_search_button);
        searchEditText = findViewById(R.id.main_search_editText);

        setUpRecyclerView(query);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Query q = trackRef.whereEqualTo("Name", "Goldie Bush Walkway");
                //setUpRecyclerView(q);
                //setUpRecyclerViewSearch();
                Toast.makeText(getApplicationContext(),"search pressed" ,Toast.LENGTH_SHORT).show();
            }
        });
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

    private void setUpRecyclerView(Query query) {
        FirestoreRecyclerOptions<Track> options = new FirestoreRecyclerOptions.Builder<Track>().setQuery(query, Track.class).build();
        adapter = new TrackAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TrackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //get id for track
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getApplicationContext(), TrackActivity.class);
                intent.putExtra("trackid", id);
                startActivity(intent);
            }
        });
        Toast.makeText(getApplicationContext(),"recycler view" ,Toast.LENGTH_SHORT).show();
    }

    /*private void setUpRecyclerViewSearch() {
        Query q = trackRef.whereEqualTo("Name", "Goldie Bush Walkway");
        FirestoreRecyclerOptions<Track> options = new FirestoreRecyclerOptions.Builder<Track>().setQuery(query, Track.class).build();
        adapter = new TrackAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TrackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //get id for track
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getApplicationContext(), TrackActivity.class);
                intent.putExtra("trackid", id);
                startActivity(intent);
            }
        });
        Toast.makeText(getApplicationContext(),"recycler view search" ,Toast.LENGTH_SHORT).show();
    }*/

    /*public void searchName(View view) {
        //String Tname = searchEditText.getText().toString();
        query = trackRef.whereEqualTo("Name", "Goldie Bush Walkway");
        setUpRecyclerView(query);
        Toast.makeText(getApplicationContext(),"search clicked" ,Toast.LENGTH_SHORT).show();
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }


}
