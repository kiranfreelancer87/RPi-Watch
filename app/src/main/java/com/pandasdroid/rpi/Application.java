package com.pandasdroid.rpi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

public class Application extends android.app.Application {
    private void sendNotification(String title, String messageBody, Map<String, String> data) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        String channelId = System.currentTimeMillis() + "CID";
        String channelName = System.currentTimeMillis() + "Cname";
        int importance = NotificationManager.IMPORTANCE_HIGH;


        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId, channelName, importance);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId).setContentTitle(title).setContentText(messageBody).setSound(defaultSoundUri).setAutoCancel(true);

        //Class route = MainActivity.class;

        Intent resultIntent = new Intent(this, MainActivity.class);

        resultIntent.putExtra("new_order", true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // mBuilder.extend(new NotificationCompat.WearableExtender().addAction(new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, null, resultPendingIntent).build()));
        //mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.addAction(new NotificationCompat.Action.Builder(R.drawable.icon_blank, null, resultPendingIntent).build());

        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        mBuilder.setColor(Color.parseColor("#ffffff"));

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                v.vibrate(VibrationEffect.createOneShot(6000, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }

        notificationManager.notify(notificationId, mBuilder.build());

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Map<String, String> data = new HashMap<>();
        sendNotification("Test", "Message Body", data);
    }
}
