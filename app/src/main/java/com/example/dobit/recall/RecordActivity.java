package com.example.dobit.recall;

import android.*;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class RecordActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    ImageView record;
    String time;
    String days = "";
    String weeks = "";
    String addHours = "";
    String timeStart = "";
    String timeEnd = "";
    int startRecord = 0;
    int hourStart;
    int hourEnd;
    int minutesStart;
    int minutesEnd;
    String dateStart = "";
    String dateEnd = "";
    boolean pm = false;
    boolean am = false;
    long dayToMs = 86400000;
    long weeksToMs = 604800000;
    long HourToMs = 3600000;
    long mils;
    long startMils;
    long endMils;
    int hours;
    int currentHour;
    int minutes;
    int counter = 0;
    int timeLength;
    Calendar calendar = Calendar.getInstance();
    String month = "n/a";
    String strMonth = "";
    int monthNumber = 0;
    int monthDay = 0;
    int length;
    DatabaseReference databaseNotes;
    DateFormat dateFormat;
    Date date;
    Date currentDate;
    String dateString;
    String[] cutDate;
    String[] cutTime;
    Cursor cursor;
    ArrayList<String> al = new ArrayList<>();
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        textToSpeech = new TextToSpeech(this, this);
        record = (ImageView) findViewById(R.id.ivRecord);
        record.setOnClickListener(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Record");
        databaseNotes = FirebaseDatabase.getInstance().getReference("notes");
        currentDate = new Date();
        cutDate = currentDate.toString().split(" ");
        cutTime = cutDate[3].split(":");
        currentHour = Integer.parseInt(cutTime[0]);

        monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        monthNumber = calendar.get(Calendar.MONTH)+1;

        Intent recordIntent = getIntent();
        try {
            startRecord = Integer.parseInt(recordIntent.getExtras().getString("Record"));
        }catch (NullPointerException e){

        }

        if(startRecord == 1){
            promptSpeechInput();
        }

        if(currentHour > 12){
            pm = true;
        }else{
            am = true;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()){
            if(cursor != null){
                int id_1 = cursor.getColumnIndex(CalendarContract.Events._ID);
                int id_2 = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                int id_3 = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);
                int id_4 = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
                int id_5 = cursor.getColumnIndex(CalendarContract.Events.DTSTART);

                String idVal = cursor.getColumnName(id_1);
                String title = cursor.getString(id_2);


                String desc = cursor.getString(id_3);
                String eventLoc = cursor.getString(id_4);
                String dateStart = cursor.getString(id_5);

                counter++;

                if(counter > 7) {
                    al.add(dateStart);
                }
                //Toast.makeText(this, desc + " " + dateStart, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Event does not exist", Toast.LENGTH_SHORT).show();
            }

            length++;
        }

    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        finish();
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
                    String[] thSplit;
                    Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);

                    //If the record has a number, get its time
                    if(rec.matches(".*\\d+.*")) {
                        for (int count = 0; count < split.length; count++) {

                            if (split[count].contains("January")) {
                                month = "January";
                                monthNumber = 1;
                            } else if (split[count].contains("February")) {
                                month = "February";
                                monthNumber = 2;
                            } else if (split[count].contains("March")) {
                                month = "March";
                                monthNumber = 3;
                            } else if (split[count].contains("April")) {
                                month = "April";
                                monthNumber = 4;
                            } else if (split[count].contains("May") || split[count].contains("may")) {
                                month = "May";
                                monthNumber = 5;
                            } else if (split[count].contains("June")) {
                                month = "June";
                                monthNumber = 6;
                            } else if (split[count].contains("July")) {
                                month = "July";
                                monthNumber = 7;
                            } else if (split[count].contains("August")) {
                                month = "August";
                                monthNumber = 8;
                            } else if (split[count].contains("September")) {
                                month = "September";
                                monthNumber = 9;
                            } else if (split[count].contains("October")) {
                                month = "October";
                                monthNumber = 10;
                            } else if (split[count].contains("November")) {
                                month = "November";
                                monthNumber = 11;
                            } else if (split[count].contains("December")) {
                                month = "December";
                                monthNumber = 12;
                            }

                            if (split[count].matches(".*\\d+.*")) {
                                if(split[count-1].contains(month)) {
                                    strMonth = split[count];
                                    Log.d("Month day", strMonth);
                                    thSplit = strMonth.split("th");
                                    monthDay = Integer.parseInt(thSplit[0]);
                                }
                            }


                            if(split[count].contains("p.m.") || split[count].contains("a.m.") && split[count-1].matches(".*\\d+.*")){

                                if(split[count-2].equals("to")){
                                    timeStart = split[count-4];
                                    timeEnd = split[count-1];
                                    timeLength = timeStart.length();

                                    if(timeLength > 1){
                                        timeSplit = timeStart.split(":");
                                        hourStart = Integer.parseInt(timeSplit[0]);
                                        minutesStart  = Integer.parseInt(timeSplit[1]);

                                    }else{
                                        hourStart = Integer.parseInt(timeStart);
                                        minutesStart = 0;
                                    }

                                    timeLength = timeEnd.length();

                                    if(timeLength > 1){
                                        timeSplit = timeEnd.split(":");
                                        hourEnd = Integer.parseInt(timeSplit[0]);
                                        minutesEnd = Integer.parseInt(timeSplit[1]);
                                    }else{
                                        hourEnd = Integer.parseInt(timeEnd);
                                        minutesEnd = 0;
                                    }
                                }else{
                                    timeStart = split[count-1];
                                    timeEnd = timeStart;
                                    timeLength = timeStart.length();

                                    if(timeLength > 1){
                                        timeSplit = timeStart.split(":");
                                        hourStart = Integer.parseInt(timeSplit[0]);
                                        hours = hourStart;
                                        minutesStart = Integer.parseInt(timeSplit[1]);
                                        minutes = minutesStart;
                                        hourEnd = hourStart;
                                        minutesEnd = minutesStart;

                                    }else{
                                        hourStart = Integer.parseInt(timeStart);
                                        hours = hourStart;
                                        minutesStart = 0;
                                        minutes = minutesStart;
                                        hourEnd = hourStart;
                                        minutesEnd = minutesStart;
                                    }
                                }

                            }


                            if(split[count].contains("days") && rec.contains("days from now")){
                                days = split[count-1];

                                if(days.equals("two")){
                                    days = "2";
                                }else if(days.equals("three")){
                                    days = "3";
                                }else if(split[count-1].contains("for") && split[count].contains("days") || days.equals("four")){
                                    days = "4";
                                }else if(days.equals("five")){
                                    days = "5";
                                }else if(days.equals("six")){
                                    days = "6";
                                }
                            }

                            if(split[count].contains("weeks") && rec.contains("weeks from now")){
                                weeks = split[count-1];

                                if(weeks.equals("two")){
                                    weeks = "2";
                                }else if(weeks.equals("three")){
                                    weeks = "3";
                                }else if(split[count-1].contains("for") && split[count].contains("weeks") || weeks.equals("four")){
                                    weeks = "4";
                                }
                            }
                        }







                        if (rec.contains("PM") || rec.contains("p.m.")) {
                            if (hours != 12) {
                                hours = hours + 12;
                                hourStart = hourStart + 12;
                                hourEnd = hourEnd + 12;
                            }

                        }else if (rec.contains("AM") || rec.contains("a.m.")) {
                            if (hours == 12) {
                                hours = 0;
                                hourStart = 0;
                                hourEnd = 0;
                            }
                            else {
                                hours = hours + 0;
                                hourStart = hourStart + 0;
                                hourEnd = hourEnd + 0;

                            }
                        }



                        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true); //Don't let the user get into the alarm app
                        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hours);
                        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
                        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, rec);
                        startActivity(alarmIntent); //Set alarm


                    }else{
                        mils = calendar.getTimeInMillis();
                    }

                    if(monthDay == 0){
                        monthDay = calendar.get(Calendar.DAY_OF_MONTH);
                    }

                    if(monthNumber == 0 ){
                        monthNumber = calendar.get(Calendar.MONTH)+1;
                    }




                    //For plotting calendar purposes

//                    dateString = (calendar.get(Calendar.YEAR) + "/" + monthNumber + "/" + monthDay + " " + hours + ":" + minutes + ":" + "00");


                    //Parse date then convert date to milliseconds
//                    Date parseDate = new Date();
//                    Date parse
//                    try {
//                        parseDate = dateFormat.parse(dateString);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    mils = parseDate.getTime();

                    dateStart = (calendar.get(Calendar.YEAR) + "/" + monthNumber + "/" + monthDay + " " + hourStart + ":" + minutesStart + ":" + "00");
                    dateEnd = (calendar.get(Calendar.YEAR) + "/" + monthNumber + "/" + monthDay + " " + hourEnd + ":" + minutesEnd + ":" + "00");

                    Date parseStart = new Date();
                    Date parseEnd = new Date();

                    try{
                        parseStart = dateFormat.parse(dateStart);
                        parseEnd = dateFormat.parse(dateEnd);
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    startMils = parseStart.getTime();
                    endMils = parseEnd.getTime();


                    ContentResolver cr = this.getContentResolver();
                    ContentValues cv = new ContentValues();
                    cv.put(CalendarContract.Events.TITLE, dateFormat.format(date));
                    cv.put(CalendarContract.Events.DESCRIPTION, rec);
                    cv.put(CalendarContract.Events.EVENT_LOCATION, "N/A");

                    //Future events
                    if(rec.contains("tomorrow") || rec.contains("a day from now")) {
                        cv.put(CalendarContract.Events.DTSTART, startMils + dayToMs);
                        cv.put(CalendarContract.Events.DTEND, endMils + dayToMs);
                    }else if(rec.contains("next week") || rec.contains("a week from now")){
                        cv.put(CalendarContract.Events.DTSTART, startMils + (dayToMs*7));
                        cv.put(CalendarContract.Events.DTEND, endMils + (dayToMs*7));
                    }else if(rec.contains("an hour from now") || rec.contains("in an hour")){
                        cv.put(CalendarContract.Events.DTSTART, calendar.getTimeInMillis() + HourToMs);
                        cv.put(CalendarContract.Events.DTEND, calendar.getTimeInMillis() + HourToMs);
                    }else if(!days.equals("")){
                        cv.put(CalendarContract.Events.DTSTART, startMils + (dayToMs*Integer.parseInt(days)));
                        cv.put(CalendarContract.Events.DTEND, endMils + (dayToMs*Integer.parseInt(days)));
                    }else if(!weeks.equals("")){
                        cv.put(CalendarContract.Events.DTSTART, startMils + (weeksToMs*Integer.parseInt(weeks)));
                        cv.put(CalendarContract.Events.DTEND, endMils + (weeksToMs*Integer.parseInt(weeks)));
                    }
                    else{
                        cv.put(CalendarContract.Events.DTSTART, startMils);
                        cv.put(CalendarContract.Events.DTEND, endMils);
                    }
                    //

                    cv.put(CalendarContract.Events.CALENDAR_ID, 1);
                    cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());

                    //reset
                    monthDay = 0;
                    monthNumber = 0;
                    startRecord = 0;

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
                    textToSpeech.speak("Okay. I got it", TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(this, "Successfully recorded!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = textToSpeech.getLanguage();
            int result = textToSpeech.setLanguage(locale);


            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This language is not supported");

            } else {

            }
        }else {
            Log.e("TTS", "Initialization failed");
        }
    }
}