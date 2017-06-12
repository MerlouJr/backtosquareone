package com.example.dobit.recall;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by dobit on 3/6/2017.
 */

public class MediaButtonIntentReceiver extends BroadcastReceiver {


    public MediaButtonIntentReceiver() {
        super();
    }

    static int d = 0;
    @Override
    public void onReceive(final Context context, Intent intent) {

        String intentAction = intent.getAction();
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }
        KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            // do something
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            final ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            d++;
            Handler handler = new Handler();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    // single click *******************************

                    if (d == 1) {
                        Toast.makeText(context, "Recall mode", Toast.LENGTH_SHORT).show();
                        Intent recordIntent = new Intent(context.getApplicationContext(), RecallModeActivity.class);
                        recordIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(recordIntent);
                    }

                    // double click *********************************
                    if (d == 2) {
                        Toast.makeText(context, "Exercise mode", Toast.LENGTH_SHORT).show();
                        Intent exerciseIntent = new Intent(context.getApplicationContext(), ExerciseModeActivity.class);
                        exerciseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(exerciseIntent);
                    }
                    d = 0;
                }
            };
            if (d == 1) {
                handler.postDelayed(r, 500);
            }
            //Intent intentone = new Intent(context.getApplicationContext(), TestActivity.class);
            //intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intentone);
        }


    }
}
