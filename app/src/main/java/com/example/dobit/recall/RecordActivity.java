package com.example.dobit.recall;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView record;
    String time;
    String days;
    String weeks;
    String addHours;
    long dayToMs = 86400000;
    long HourToMs = 3600000;
    long mils;
    int hours;
    int minutes;
    DatabaseReference databaseNotes;
    DateFormat dateFormat;
    Calendar calendar;
    Date date;
    String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        record = (ImageView) findViewById(R.id.ivRecord);
        record.setOnClickListener(this);
        databaseNotes = FirebaseDatabase.getInstance().getReference("notes");

    }

    @Override
    public void onClick(View v) {
        promptSpeechInput();
    }

    public void promptSpeechInput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something to record!");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, "Sorry your device doesn't support speech language!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestcode, int result_code, Intent i) {
        super.onActivityResult(requestcode, result_code, i);
        switch (requestcode) {
            case 100:
                dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                date = new Date();

                if (result_code == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String rec = result.get(0).toString();
                    String[] split = rec.split(" ");
                    String[] timeSplit;
                    Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);

                    //If the record has a number, get its time
                    if(rec.matches(".*\\d+.*") && !rec.contains("tomorrow") && !rec.contains("days from now") && !rec.contains("weeks from now")) {
                        for (int count = 0; count < split.length; count++) {

                            if (split[count].contains(":") && rec.contains("p.m.") || rec.contains("a.m.")) {
                                time = split[count];
                            } else if (split[count].matches(".*\\d+.*") && rec.contains("p.m.") || rec.contains("a.m.")) {
                                time = split[count];
                            }

                            if(split[count].contains("days") && rec.contains("days from now")){
                                days = split[count-1];
                            }

                            if(split[count].contains("weeks") && rec.contains("week from now")){
                                days = split[count-1];
                            }

                            if(split[count].contains("hours") && rec.contains("hours from now")){
                                addHours = split[count-1];
                            }
                        }

                        //If the time string has ':' then split it and get the hours and minutes, else get only the hours, there is no minutes
                        if (time.contains(":")) {
                            timeSplit = time.split(":");
                            hours = Integer.parseInt(timeSplit[0]);
                            minutes = Integer.parseInt(timeSplit[1]);
                        } else{
                            hours = Integer.parseInt(time);
                            minutes = 0;
                        }


                        if (rec.contains("PM") || rec.contains("p.m.")) {
                            if (hours != 12)
                                hours = hours + 12;
                        } else if (rec.contains("AM") || rec.contains("a.m.")) {
                            if (hours == 12)
                                hours = 0;
                            else
                                hours = hours + 0;
                        }


                        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true); //Don't let the user get into the alarm app
                        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hours);
                        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
                        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, rec);
                        startActivity(alarmIntent); //Set alarm
                    }


                    //For plotting calendar purposes
                    calendar = Calendar.getInstance();
                    dateString = (calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + " " + hours + ":" + minutes + ":" + "00");

                    //Parse date then convert date to milliseconds
                    Date parseDate = new Date();
                    try {
                        parseDate = dateFormat.parse(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mils = parseDate.getTime();



                    ContentResolver cr = this.getContentResolver();
                    ContentValues cv = new ContentValues();
                    cv.put(CalendarContract.Events.TITLE, dateFormat.format(date));
                    cv.put(CalendarContract.Events.DESCRIPTION, rec);
                    cv.put(CalendarContract.Events.EVENT_LOCATION, "N/A");

                    //Future events
                    if(rec.contains("tomorrow") || rec.contains("a day from now")) {
                        cv.put(CalendarContract.Events.DTSTART, mils + dayToMs);
                        cv.put(CalendarContract.Events.DTEND, mils + dayToMs);
                    }else if(rec.contains("next week") || rec.contains("a week from now")){
                        cv.put(CalendarContract.Events.DTSTART, mils + (dayToMs*7));
                        cv.put(CalendarContract.Events.DTEND, mils + (dayToMs*7));
                    }else if(rec.contains("an hour from now") || rec.contains("in an hour")){
                        cv.put(CalendarContract.Events.DTSTART, mils + HourToMs);
                        cv.put(CalendarContract.Events.DTEND, mils + HourToMs);
                    }else{
                        cv.put(CalendarContract.Events.DTSTART, mils);
                        cv.put(CalendarContract.Events.DTEND, mils);
                    }
                    //

                    cv.put(CalendarContract.Events.CALENDAR_ID, 1);
                    cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());

                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    //Push notes to Firebase DB
                    String id = databaseNotes.push().getKey();
                    Notes notes = new Notes();
                    notes.setDate(dateFormat.format(date));
                    notes.setNote(rec);
                    databaseNotes.child(id).setValue(notes);


                    Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                    Toast.makeText(this, "Successfully recorded!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

