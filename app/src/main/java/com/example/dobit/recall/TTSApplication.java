package com.example.dobit.recall;

import android.app.Application;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by dobit on 3/18/2017.
 */

public class TTSApplication extends Application implements TextToSpeech.OnInitListener {

    TextToSpeech textToSpeech;

    @Override
    public void onCreate() {
        super.onCreate();
        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = textToSpeech.getLanguage();
            int result = textToSpeech.setLanguage(locale);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.speak(getString(R.string.welcome), TextToSpeech.QUEUE_FLUSH, null);
                }
            }, 1000);



            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This language is not supported");

            } else {

            }
        }else {
            Log.e("TTS", "Initialization failed");
        }
    }
}
