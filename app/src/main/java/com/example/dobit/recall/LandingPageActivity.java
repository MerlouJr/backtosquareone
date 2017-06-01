package com.example.dobit.recall;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class LandingPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, TextToSpeech.OnInitListener {

    Button send;
    TextView question;
    TextView answer;
    EditText etQuestion;
    ImageView mic;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mic = (ImageView) findViewById(R.id.ivMic);
        mic.setOnClickListener(this);
        send = (Button) findViewById(R.id.btnSend);
        send.setOnClickListener(this);
        question = (TextView) findViewById(R.id.tvQuestion);
        answer = (TextView) findViewById(R.id.tvA);
        etQuestion = (EditText) findViewById(R.id.etQ);
        textToSpeech = new TextToSpeech(this, this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent recordIntent = new Intent(LandingPageActivity.this, RecordActivity.class);
            startActivity(recordIntent);
        } else if (id == R.id.nav_gallery) {
            Intent notesIntent = new Intent(LandingPageActivity.this, NotesActivity.class);
            startActivity(notesIntent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(LandingPageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSend:
                question.setText(etQuestion.getText());
                String q = etQuestion.getText().toString();


                if(q.contains("wallet")){
                    answer.setText("You placed your wallet in your pocket");
                    textToSpeech.speak("You placed your wallet in your pocket", TextToSpeech.QUEUE_FLUSH, null);
                }else if(q.contains("appointment")){
                    answer.setText("Your appointment with the doctor is at 5 PM");
                    textToSpeech.speak("Your appointment with the doctor is at 5 PM", TextToSpeech.QUEUE_FLUSH, null);
                }

                etQuestion.setText("");
                break;

            case R.id.ivMic:
                promptSpeechInput();
                break;
        }


    }


    public void promptSpeechInput(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Ask your question!");

        try {
            startActivityForResult(i, 100);
        }catch (ActivityNotFoundException a){
            Toast.makeText(this, "Sorry your device doesn't support speech language!", Toast.LENGTH_SHORT).show();
        }

    }

    public void onActivityResult(int requestcode, int result_code, Intent i ){
        super.onActivityResult(requestcode,result_code,i);
        switch(requestcode) {
            case 100:
                if (result_code == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    question.setText(result.get(0));
                    String q = question.getText().toString();
                    if(q.contains("wallet")){
                        answer.setText("You placed your wallet in your pocket");
                        textToSpeech.speak("You placed your wallet in your pocket", TextToSpeech.QUEUE_FLUSH, null);
                    }else if(q.contains("appointment")){
                        answer.setText("Your appointment with the doctor is at 5 PM");
                        textToSpeech.speak("Your appointment with the doctor is at 5 PM", TextToSpeech.QUEUE_FLUSH, null);
                    }

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
