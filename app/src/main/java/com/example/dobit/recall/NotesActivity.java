package com.example.dobit.recall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by dobit on 5/22/2017.
 */

public class NotesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        recyclerView = (RecyclerView) findViewById(R.id.rvNotes);
        adapter = new NotesAdapter(this, NoteStaticData.getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
