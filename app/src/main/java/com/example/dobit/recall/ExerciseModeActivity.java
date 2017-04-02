package com.example.dobit.recall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ExerciseModeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button visual;
    private Button audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_mode);
        visual = (Button) findViewById(R.id.btnVisual);
        visual.setOnClickListener(this);
        audio = (Button) findViewById(R.id.btnAudio);
        audio.setOnClickListener(this);
    }


    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.btnVisual:
                Intent VisualIntent = new Intent(ExerciseModeActivity.this, VisualExerciseActivity.class);
                startActivity(VisualIntent);
                break;
            case R.id.btnAudio:
                Intent AudioIntent = new Intent(this, AudioExerciseActivity.class);
                startActivity(AudioIntent);
        }

    }
}
