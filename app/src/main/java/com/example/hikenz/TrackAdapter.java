package com.example.hikenz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class TrackAdapter extends FirestoreRecyclerAdapter<Track, TrackAdapter.TrackHolder> implements Filterable {
    private OnItemClickListener listener;

    public TrackAdapter(@NonNull FirestoreRecyclerOptions<Track> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TrackHolder trackHolder, int i, @NonNull Track model) {
        long l = model.getDistance();
        if ( l>10){
            trackHolder.textViewTime.setText(model.getTime() + " day(s)");
        }
        else {
            trackHolder.textViewTime.setText(model.getTime() + " hr(s)");
        }
        trackHolder.textViewTitle.setText(model.getName());
        trackHolder.textViewDistance.setText(l + " km");
        trackHolder.textViewDifficulty.setText(model.getDifficulty());
    }

    @NonNull
    @Override
    public TrackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new TrackHolder(view);
    }

    @Override
    public Filter getFilter() {
        return null;  // try use search query in here?
    }

    class TrackHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewTime, textViewDistance, textViewDifficulty;

        public TrackHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.title_textView);
            textViewTime = itemView.findViewById(R.id.card_time_textView);
            textViewDistance = itemView.findViewById(R.id.card_distance_textView);
            textViewDifficulty = itemView.findViewById(R.id.card_difficulty_textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface  OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}


