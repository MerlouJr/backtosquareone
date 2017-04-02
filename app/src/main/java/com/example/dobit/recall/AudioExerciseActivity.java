package com.example.dobit.recall;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AudioExerciseActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_exercise);
        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {

    }
}
