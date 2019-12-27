package com.example.supercalculator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.example.supercalculator.ui.Matrices;

public class NotificationReciever extends BroadcastReceiver {
    final int NOTIFICATION_ID = 105;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        Intent intent1 = new Intent(context, Matrices.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                .setTicker("Doing calculations is so much fun 😃")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Super Calculator")
                .setContentText("Let's do some calculations, my dear friend! 😏");
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        nm.notify(NOTIFICATION_ID, builder.build());
    }
}
