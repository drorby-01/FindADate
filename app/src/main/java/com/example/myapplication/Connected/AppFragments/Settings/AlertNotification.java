package com.example.myapplication.Connected.AppFragments.Settings;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.Connected.AppFragments.SearchDateActivity;
import com.example.myapplication.R;

public class AlertNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int time=intent.getIntExtra("time",5000);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "656");

        builder.setContentTitle("findAdate").
                setContentText(context.getString(R.string.come_and_see)).
                setSmallIcon(R.drawable.heartpicture);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("id_1", "Alarm channel",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId("id_1");
        }


        Intent intent1;
        intent1 = new Intent(context.getApplicationContext(), SearchDateActivity.class);
        intent.putExtra("time",time);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(1, notification);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +time, pendingIntent1);
    }
}

