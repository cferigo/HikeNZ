package com.example.hikenz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ReviewAdapter extends FirestoreRecyclerAdapter<Review, ReviewAdapter.ReviewHolder> {


    public ReviewAdapter(@NonNull FirestoreRecyclerOptions<Review> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i, @NonNull Review model) {
        reviewHolder.textViewName.setText(model.getFirstname());
        reviewHolder.textViewreview.setText(model.getReview());
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,
                parent, false);
        return new ReviewHolder(v);
    }

    class ReviewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewreview;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.review_textView_name);
            textViewreview = itemView.findViewById(R.id.review_textView_review);
        }
    }
}
