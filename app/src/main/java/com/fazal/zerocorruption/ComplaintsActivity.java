package com.fazal.zerocorruption;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ComplaintsActivity extends Activity {
    private RecyclerView mComplaintsList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Button corruptionBtn;
    public static showUrlListener showUrlListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        corruptionBtn = findViewById(R.id.Kmeans_btn);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(ComplaintsActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("complaints");

        mComplaintsList = findViewById(R.id.complaints_list);
        mComplaintsList.setHasFixedSize(true);
        mComplaintsList.setLayoutManager(new LinearLayoutManager(this));




        corruptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComplaintsActivity.this,CorruptionResultActivity.class);
                startActivity(intent);
            }
        });

        setOnShowListener(new showUrlListener() {
            @Override
            public void onShow(Intent intent) {
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        FirebaseRecyclerAdapter<Complaints,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Complaints, BlogViewHolder>(
                Complaints.class,
                R.layout.complaints_row,
                BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Complaints model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setEvidence(model.getEvidence());
                viewHolder.btnClicked(model.getEvidence());
            }
        };
        mComplaintsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            TextView nameText = mView.findViewById(R.id.name);
            nameText.setText(name);
        }
        public void setDescription(String description){
            TextView descText = mView.findViewById(R.id.crime_description);
            descText.setText(description);
        }
        public void setEvidence(String url){
            TextView urlText = mView.findViewById(R.id.evidenceLink);
            urlText.setText(url);
        }
        public void btnClicked(final String url){
            Button button = mView.findViewById(R.id.connectToInternet);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    showUrlListener.onShow(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_logout){
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setOnShowListener(showUrlListener showListener){

        showUrlListener = showListener;

    }
}
