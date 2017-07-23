package com.example.dobit.recall;

import android.content.Intent;
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
import java.util.Calendar;

/**
 * Created by dobit on 5/22/2017.
 */

public class NotesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotesAdapter adapter;
    DatabaseReference databaseNotes;
    ChildEventListener childEventListener;
    Calendar calendar;
    ArrayList<Notes> alNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Notes");
//        alNotes = new ArrayList<>();
        calendar = Calendar.getInstance();
        databaseNotes = FirebaseDatabase.getInstance().getReference("notes");
        recyclerView = (RecyclerView) findViewById(R.id.rvNotes);
        adapter = new NotesAdapter(this, alNotes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Notes notes =  dataSnapshot.getValue(Notes.class);


                String split[] = notes.getDate().split(" ");
                String monthString = split[0];
                int monthNumber = monthToMonthNumber(monthString);
                int monthDay = Integer.parseInt(split[1]);
                int year = Integer.parseInt(split[2]);

                int monthNow = calendar.get(Calendar.MONTH);
                int monthDayNow = calendar.get(Calendar.DAY_OF_MONTH);
                int yearNow = calendar.get(Calendar.YEAR);

                if(monthNumber == monthNow && monthDay == monthDayNow && year == yearNow){
                    alNotes.add(notes);
                }


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

    public int monthToMonthNumber(String month){
        int monthNo = 0;

        if(month.equals("January")){
            monthNo = 0;
        } else if(month.equals("February")){
            monthNo = 1;
        }else if(month.equals("March")){
            monthNo = 2;
        }else if(month.equals("April")){
            monthNo = 3;
        }else if(month.equals("May")){
            monthNo = 4;
        }else if(month.equals("June")){
            monthNo = 5;
        }else if(month.equals("July")){
            monthNo = 6;
        }else if(month.equals("August")){
            monthNo = 7;
        }else if(month.equals("September")){
            monthNo = 8;
        }else if(month.equals("October")){
            monthNo = 9;
        }else if(month.equals("November")){
            monthNo = 10;
        }else if(month.equals("December")){
            monthNo = 11;
        }

        return monthNo;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}