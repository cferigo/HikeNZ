package com.example.hikenz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.location.LocationManager.*;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference trackRef = db.collection("Tracks");
    private TrackAdapter adapter;
    //LocationManager locationManager;
    LinearLayout searchDifficultyLayout;
    RelativeLayout searchNameLayout, searchDistanceLayout, searchLocationLayout;
    Button searchBtn, showDistanceSearch, showDifficultySearch, showNameSearch, searchLocationBtn,
           showNearMeSearch, searchBeginnerBtn, searchIntermediateBtn, searchAdvancedBtn, searchDistanceBtn;
    EditText searchEditText;
    TextView distanceCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // runtime permissions to use users location
       /* if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }*/

        searchBtn = findViewById(R.id.main_search_button);
        searchEditText = findViewById(R.id.main_search_editText);
        showDifficultySearch = findViewById(R.id.main_searchOptionDifficulty_Button);
        showDistanceSearch = findViewById(R.id.main_searchOptionDistance_Button);
        showNameSearch = findViewById(R.id.main_searchOptionName_Button);
        showNearMeSearch = findViewById(R.id.main_searchOptionNearBy_Button);
        searchDifficultyLayout = findViewById(R.id.main_searchDifficulty_layout);
        searchDistanceLayout = findViewById(R.id.main_searchDistance_layout);
        searchLocationLayout = findViewById(R.id.main_searchLocation_layout);
        searchNameLayout = findViewById(R.id.main_searchName_layout);
        searchBeginnerBtn = findViewById(R.id.main_searchBeginner_button);
        searchIntermediateBtn = findViewById(R.id.main_searchIntermediate_button);
        searchAdvancedBtn = findViewById(R.id.main_searchAdvanced_button);
        distanceCounter = findViewById(R.id.main_distanceCounter_textView);
        searchDistanceBtn = findViewById(R.id.main_searchDistance_button);
        searchLocationBtn = findViewById(R.id.main_searchLocation_button);

        // displays all tracks on activity start
        Query query = trackRef;
        setUpRecyclerView(query);

        //getLocation();

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
                Query q = trackRef.whereLessThan("Distance", num + 1);
                setUpRecyclerView(q);
            }
        });
        // listener that checks if user has allowed the app to use their location and runs the search location method
        searchLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                } else {
                    searchLocation();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                searchLocation();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void searchLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_LOW_POWER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(MainActivity.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double userLatitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double userLongitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    // needs to be converted into an address
                }
            }
        }, Looper.getMainLooper());


        // formula to work out distance between 2 geo-point locations
       /* const R = 6371e3; // metres
        const φ1 = lat1 * Math.PI/180; // φ, λ in radians
        const φ2 = lat2 * Math.PI/180;
        const Δφ = (lat2-lat1) * Math.PI/180;
        const Δλ = (lon2-lon1) * Math.PI/180;

        const a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        const d = R * c; // in metres*/
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
        if (adapter != null) {
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
        hideButtons();
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
        showNearMeSearch.setVisibility(View.VISIBLE);
    }

    // shows search by track name layout
    public void searchOptionName(View view) {
        // show search  by name
        searchNameLayout.setVisibility(View.VISIBLE);
        hideButtons();
        searchDistanceLayout.setVisibility(View.GONE);
        searchDifficultyLayout.setVisibility(View.GONE);
        searchLocationLayout.setVisibility(View.GONE);
    }

    // shows search by difficulty layout
    public void searchOptionDifficulty(View view) {
        // show search by difficulty
        searchDifficultyLayout.setVisibility(View.VISIBLE);
        hideButtons();
        searchDistanceLayout.setVisibility(View.GONE);
        searchNameLayout.setVisibility(View.GONE);
        searchLocationLayout.setVisibility(View.GONE);
    }

    // shows search by distance layout
    public void searchOptionDistance(View view) {
        // show search by distance
        searchDistanceLayout.setVisibility(View.VISIBLE);
        hideButtons();
        searchNameLayout.setVisibility(View.GONE);
        searchDifficultyLayout.setVisibility(View.GONE);
        searchLocationLayout.setVisibility(View.GONE);
    }

    public void searchOptionNearBy(View view) {
        // show search by distance
        searchLocationLayout.setVisibility(View.VISIBLE);
        hideButtons();
        searchNameLayout.setVisibility(View.GONE);
        searchDifficultyLayout.setVisibility(View.GONE);
        searchDistanceLayout.setVisibility(View.GONE);
    }

    // increases and decreases the value of the searchable distance
    public void decreaseDistance(View view) {
        int num = Integer.parseInt((String) distanceCounter.getText());
        if (num > 5) {
            String i = String.valueOf(num - 5);
            distanceCounter.setText(i);
        }
    }

    public void increaseDistance(View view) {
        int num = Integer.parseInt((String) distanceCounter.getText());
        if (num < 50) {
            String i = String.valueOf(num + 5);
            distanceCounter.setText(i);
        }
    }

    // hides all the search buttons
    public void hideButtons() {
        showDifficultySearch.setVisibility(View.GONE);
        showDistanceSearch.setVisibility(View.GONE);
        showNameSearch.setVisibility(View.GONE);
        showNearMeSearch.setVisibility(View.GONE);
    }
}
