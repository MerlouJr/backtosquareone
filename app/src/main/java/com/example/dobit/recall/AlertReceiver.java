package com.example.dobit.recall;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.NotificationCompat;


/**
 * Created by dobit on 6/5/2017.
 */

public class AlertReceiver extends BroadcastReceiver {

    MediaPlayer player;
    Context ReceiverContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context, "Title", "You have an appointment an hour from now", "Alert");
    }

    public void showNotification(Context context, String msg, String msgText, String msgAlert){
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(msg);
        builder.setTicker(msgAlert);
        builder.setContentText(msgText);
        builder.setContentIntent(notificIntent);
//        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

    public void playSound(){
        player = MediaPlayer.create(ReceiverContext.getApplicationContext(), R.raw.mayatheme);
        player.setLooping(true);
        player.start();
    }


}
