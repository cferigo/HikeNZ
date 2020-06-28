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
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference trackRef = db.collection("Tracks");
    private TrackAdapter adapter;
    LinearLayout searchDifficultyLayout;
    RelativeLayout searchNameLayout, searchDistanceLayout;
    Button searchBtn, showDistanceSearch, showDifficultySearch, showNameSearch,
    searchBeginnerBtn, searchIntermediateBtn, searchAdvancedBtn, searchDistanceBtn;
    EditText searchEditText;
    TextView distanceCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = findViewById(R.id.main_search_button);
        searchEditText = findViewById(R.id.main_search_editText);
        showDifficultySearch = findViewById(R.id.main_searchOptionDifficulty_Button);
        showDistanceSearch = findViewById(R.id.main_searchOptionDistance_Button);
        showNameSearch = findViewById(R.id.main_searchOptionName_Button);
        searchDifficultyLayout = findViewById(R.id.main_searchDifficulty_layout);
        searchDistanceLayout = findViewById(R.id.main_searchDistance_layout);
        searchNameLayout = findViewById(R.id.main_searchName_layout);
        searchBeginnerBtn = findViewById(R.id.main_searchBeginner_button);
        searchIntermediateBtn = findViewById(R.id.main_searchIntermediate_button);
        searchAdvancedBtn = findViewById(R.id.main_searchAdvanced_button);
        distanceCounter = findViewById(R.id.main_distanceCounter_textView);
        searchDistanceBtn = findViewById(R.id.main_searchDistance_button);

        // displays all tracks on activity start
        Query query = trackRef;
        setUpRecyclerView(query);

        // listener the displays a searched by name track
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = searchEditText.getText().toString();
                Query q = trackRef.whereEqualTo("Name", str);
                setUpRecyclerView(q);
            }
        });
        //listener that displays beginner tracks
        searchBeginnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query q = trackRef.whereEqualTo("Difficulty", "Beginner");
                setUpRecyclerView(q);
            }
        });
        //listener that displays Intermediate tracks
        searchIntermediateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query q = trackRef.whereEqualTo("Difficulty", "Intermediate");
                setUpRecyclerView(q);
            }
        });
        //listener that displays Advanced tracks
        searchAdvancedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query q = trackRef.whereEqualTo("Difficulty", "Advanced");
                setUpRecyclerView(q);
            }
        });
        //listener that displays tracks based off distance provided
        searchDistanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt((String) distanceCounter.getText());
                Query q = trackRef.whereLessThan("Distance", num);
                setUpRecyclerView(q);
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

    // creates recycler view containing tracks that match the query passed to it
    private void setUpRecyclerView(Query query) {
        if(adapter != null){
            adapter.stopListening();
        }
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
        adapter.startListening();
    }

    // whenever the activity starts options are all hidden and listener for db updates starts
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        // hide buttons on start
        showDifficultySearch.setVisibility(View.GONE);
        showDistanceSearch.setVisibility(View.GONE);
        showNameSearch.setVisibility(View.GONE);
        // hide searching layouts on start
        searchDistanceLayout.setVisibility(View.GONE);
        searchDifficultyLayout.setVisibility(View.GONE);
        searchNameLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // logs the current user out of the app so when user opens the app again they must sign in
    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    // displays search options buttons
    public void searchOptions(View view) {
        showDifficultySearch.setVisibility(View.VISIBLE);
        showDistanceSearch.setVisibility(View.VISIBLE);
        showNameSearch.setVisibility(View.VISIBLE);
    }

    // shows search by track name layout
    public void searchOptionName(View view) {
        // show search  by name
        searchNameLayout.setVisibility(View.VISIBLE);
        hideButtons();
        searchDistanceLayout.setVisibility(View.GONE);
        searchDifficultyLayout.setVisibility(View.GONE);
    }

    // shows search by difficulty layout
    public void searchOptionDifficulty(View view) {
        // show search by difficulty
        searchDifficultyLayout.setVisibility(View.VISIBLE);
        hideButtons();
        searchDistanceLayout.setVisibility(View.GONE);
        searchNameLayout.setVisibility(View.GONE);
    }

    // shows search by distance layout
    public void searchOptionDistance(View view) {
        // show search by distance
        searchDistanceLayout.setVisibility(View.VISIBLE);
        hideButtons();
        searchNameLayout.setVisibility(View.GONE);
        searchDifficultyLayout.setVisibility(View.GONE);
    }

    // increases and decreases the value of the searchable distance
    public void decreaseDistance(View view) {
        int num = Integer.parseInt((String) distanceCounter.getText());
        if (num > 5){
            String i = String.valueOf(num - 5);
            distanceCounter.setText(i);
        }
    }
    public void increaseDistance(View view) {
        int num = Integer.parseInt((String) distanceCounter.getText());
        if (num < 50){
            String i = String.valueOf(num + 5);
            distanceCounter.setText(i);
        }
    }

    // hides all the search buttons
    public void hideButtons(){
        showDifficultySearch.setVisibility(View.GONE);
        showDistanceSearch.setVisibility(View.GONE);
        showNameSearch.setVisibility(View.GONE);
    }

}
