package com.wgu_android.studentprogresstracker.Utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.wgu_android.studentprogresstracker.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {

    static int notificationID;
    String channel_id="test";
    public String notificationTitle;
    public String notificationCourseName;
    public String notificationType; //Course or Assessment


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Notification",Toast.LENGTH_LONG).show();
        createNotificationChannel(context,channel_id);

        Notification n= new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(notificationType + ":  " + notificationCourseName)
                .setContentTitle(notificationType + notificationTitle + "Alarm! ").build();

        NotificationManager notificationManager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++,n);

    }
    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        // Create the NotificationChannel

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationCourseName() {
        return notificationCourseName;
    }

    public void setNotificationCourseName(String notificationCourseName) {
        this.notificationCourseName = notificationCourseName;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
