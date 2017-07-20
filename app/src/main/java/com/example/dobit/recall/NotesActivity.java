package com.example.dobit.recall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by dobit on 5/22/2017.
 */

public class NotesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotesAdapter adapter;
    DatabaseReference databaseNotes;
    ChildEventListener childEventListener;
    ArrayList<Notes> alNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Notes");
//        alNotes = new ArrayList<>();
        databaseNotes = FirebaseDatabase.getInstance().getReference("notes");
        recyclerView = (RecyclerView) findViewById(R.id.rvNotes);
        adapter = new NotesAdapter(this, alNotes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Notes notes =  dataSnapshot.getValue(Notes.class);
                alNotes.add(notes);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseNotes.addChildEventListener(childEventListener);

    }
}