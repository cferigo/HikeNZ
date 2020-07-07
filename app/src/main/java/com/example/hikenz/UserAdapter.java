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

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.UserHolder> implements Filterable {
    private OnItemClickListener listener;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserHolder userHolder, int i, @NonNull User model) {
        userHolder.fName.setText(model.getFirstName());
        userHolder.lName.setText(model.getLastName());
        userHolder.email.setText(model.getEmail());
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class UserHolder extends RecyclerView.ViewHolder {
        TextView fName, lName, email;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            fName = itemView.findViewById(R.id.userItem_fName_tv);
            lName = itemView.findViewById(R.id.userItem_lName_textView);
            email = itemView.findViewById(R.id.userItem_email_textView);

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


