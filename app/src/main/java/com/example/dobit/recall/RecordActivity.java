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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView record;
    int hours;
    int minutes;
    FirebaseDatabase fbdb;
    DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        record = (ImageView) findViewById(R.id.ivRecord);
        record.setOnClickListener(this);

        fbdb = FirebaseDatabase.getInstance();
        dbr = fbdb.getReference().child("notes");

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
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                if (result_code == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String rec = result.get(0).toString();
                    String[] split = rec.split(" ");
                    Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);

                if(rec.matches(".*\\d+.*")) {

                    for (int count = 0; count < split.length; count++){
                        if(split[count].matches("[0-9]")){
                            hours = Integer.parseInt(split[count]);
                        }else if(split[count].matches("[0-60]")){
                            minutes = Integer.parseInt(split[count]);
                        }
                    }

                }
                    alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                    alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hours);
                    alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
                    if (rec.contains("PM") || rec.contains("p.m.")) {
                        alarmIntent.putExtra(AlarmClock.EXTRA_IS_PM, true);
                    } else if (rec.contains("AM") || rec.contains("a.m.")) {
                        alarmIntent.putExtra(AlarmClock.EXTRA_IS_PM, false);
                    }


                    alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, rec);
                    startActivity(alarmIntent);


                    ContentResolver cr = this.getContentResolver();
                    ContentValues cv = new ContentValues();
                    cv.put(CalendarContract.Events.TITLE, dateFormat.format(date));
                    cv.put(CalendarContract.Events.DESCRIPTION, rec);
                    cv.put(CalendarContract.Events.EVENT_LOCATION, "N/A");
                    cv.put(CalendarContract.Events.DTSTART, Calendar.getInstance().getTimeInMillis());
                    cv.put(CalendarContract.Events.DTEND, Calendar.getInstance().getTimeInMillis() + 60 * 60 * 1000);
                    cv.put(CalendarContract.Events.CALENDAR_ID, 1);
                    cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());

                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }


                    Notes notes = new Notes();
                    notes.setDate(dateFormat.format(date));
                    notes.setNote(rec);
                    dbr.setValue(notes);
                    dbr.push();


                    Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                    Toast.makeText(this, "Successfully recorded!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

