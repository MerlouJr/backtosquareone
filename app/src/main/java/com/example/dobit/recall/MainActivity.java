package com.example.dobit.recall;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.*;
import com.example.dobit.recall.Voice;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    TextToSpeech textToSpeech;
    Button button;
    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    MediaSession.Callback callback = new MediaSession.Callback() {
        @Override
        public void onPlay() {
            super.onPlay();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((AudioManager) getSystemService(AUDIO_SERVICE)).registerMediaButtonEventReceiver(new ComponentName(
                this,
                MediaButtonIntentReceiver.class));

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.welcome);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(false);
        mp.start();

    }


}
