package com.learning.insane.wecheaters;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.learning.insane.wecheaters.model.Shortcut;

import java.util.Map;

public class RecentFragment extends Fragment {

    FirebaseRecyclerAdapter<Shortcut, ShortcutViewHolder> firebaseRecyclerAdapter;
    RecyclerView mRecyclerView;
    Query mDatabaseQuery;

    public RecentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mRecyclerView =  (RecyclerView) inflater.inflate(R.layout.fragment_recent, container, false);

        mRecyclerView.setHasFixedSize(true);
        mDatabaseQuery = FirebaseDatabase.getInstance().getReference().child("Shortcuts").limitToFirst(10);




        //RecyclerAdapter Code
        FirebaseRecyclerOptions<Shortcut> options = new FirebaseRecyclerOptions.Builder<Shortcut>()
                .setQuery(mDatabaseQuery, Shortcut.class).build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Shortcut, ShortcutViewHolder>(options) {

                    @Override
                    public ShortcutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.shortcut_view, parent, false
                        );
                        return new ShortcutViewHolder(v);
                    }

                    @Override
                    protected void onBindViewHolder(ShortcutViewHolder shortcutView, int position, Shortcut shortcut) {
                        shortcutView.setName(shortcut.getName());
                        shortcutView.setDescription(shortcut.getDescription());
                        shortcutView.setUploader(shortcut.getOwner());
                        shortcutView.setVote(shortcut.getVoteCount());
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));

        return mRecyclerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        firebaseRecyclerAdapter.stopListening();
        super.onStop();
    }

    public static class ShortcutViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView description;
        public TextView vote;
        public TextView uploader;
        public TextView timestamp;

        public ShortcutViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shortcut_name);
            description = itemView.findViewById(R.id.shortcut_description);
            vote = itemView.findViewById(R.id.shortcut_vote_count);
            uploader = itemView.findViewById(R.id.shortcut_uploader);
            timestamp = itemView.findViewById(R.id.shortcut_timestamp);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setDescription(String description) {
            this.description.setText(description);
        }

        public void setUploader(String uploader) {
            this.uploader.setText(uploader);
        }

        public void setVote(int vote) {
            this.vote.setText(String.valueOf(vote));
        }

        public void setTimestamp(Map<String, String> timestamp) {

        }
    }
}