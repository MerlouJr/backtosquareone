package com.example.dobit.recall;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by dobit on 6/5/2017.
 */

public class NotificationService extends Service {
    private int minute;
    private MediaPlayer player;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();
    String time = "";
    int length = 0;
    int counter = 0;
    Thread thread;
    Handler handler;
    long anHour = Calendar.getInstance().getTimeInMillis() + 3600000;
    Long alertTime;
    Cursor cursor;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
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
                    al.add(desc);
                    al2.add(dateStart);
                }
                //Toast.makeText(this, desc + " " + dateStart, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Event does not exist", Toast.LENGTH_SHORT).show();
            }

            length++;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//        minute = Calendar.getInstance().get(Calendar.MINUTE);
//        playSound();
//        showNotification();
        intent = new Intent(getApplicationContext(), AlertReceiver.class);

        for(String loop: al2){
            alertTime = Long.parseLong(loop)-3600000;
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, 1, intent,PendingIntent.FLAG_UPDATE_CURRENT));
            sendBroadcast(intent);
        }

//        alertTime = 1496637931852L;



        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


//    public void showNotification(){
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setContentTitle("This is a title");
//        builder.setContentText("Text goes here");
//        Intent intent = new Intent(this, MainActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, builder.build());
//    }
}
