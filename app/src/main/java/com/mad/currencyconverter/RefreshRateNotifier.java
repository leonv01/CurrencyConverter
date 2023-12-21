package com.mad.currencyconverter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class RefreshRateNotifier {

    private static final int NOTIFICATION_ID = 123;

    private static String CHANNEL_ID = "exchange_refresh_channel";
    private static String CHANNEL_DESCRIPTION = "Show refresh update";

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public RefreshRateNotifier(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            Log.e("WORKS", "WOKRS");
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if(notificationChannel == null){
                notificationChannel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_DESCRIPTION, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

       notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
               .setSmallIcon(android.R.drawable.ic_dialog_info)
               .setContentTitle("Currencies Updated!")
               .setAutoCancel(false);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.setContentIntent(resultPendingIntent);
    }

    public void showOrUpdateNotification(){
        notificationBuilder.setContentText("Currencies Updated!");
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void removeNotification(){
        notificationManager.cancel(NOTIFICATION_ID);
    }
}


