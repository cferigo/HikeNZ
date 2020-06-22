package com.example.hikenz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TrackAdapter extends FirestoreRecyclerAdapter<Track, TrackAdapter.TrackHolder> {

    public TrackAdapter(@NonNull FirestoreRecyclerOptions<Track> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TrackHolder trackHolder, int i, @NonNull Track model) {
        trackHolder.textViewTitle.setText(model.getName());
        trackHolder.textViewTime.setText(model.getTime() + " hr(s)");
        trackHolder.textViewDistance.setText(model.getDistance() + " km");
        trackHolder.textViewDifficulty.setText(model.getDifficulty());
    }

    @NonNull
    @Override
    public TrackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new TrackHolder(view);
    }

    class TrackHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewTime, textViewDistance, textViewDifficulty;

        public TrackHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.title_textView);
            textViewTime = itemView.findViewById(R.id.card_time_textView);
            textViewDistance = itemView.findViewById(R.id.card_distance_textView);
            textViewDifficulty = itemView.findViewById(R.id.card_difficulty_textView);
        }
    }
}
