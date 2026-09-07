package com.example.mystats.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mystats.R;
import com.example.mystats.ui.MainActivity;

public class NotificationHelper {
    private static final String CHANNEL_ID = "workout_reminder";
    private static final String CHANNEL_NAME = "Напоминания о тренировках";
    private static final int NOTIFICATION_ID = 1;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Напоминания о тренировках и прогрессе");
            channel.enableVibration(true);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void showWorkoutReminder(Context context, int workoutCount) {
        createNotificationChannel(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        String message = "Сегодня в тренировочном плане " + workoutCount +
                " " + getDeclension(workoutCount, "тренировка", "тренировки", "тренировок") +
                ", успейте их выполнить";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_fire)
                .setContentTitle("💪 MyStats - План на сегодня")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private static String getDeclension(int number, String one, String two, String five) {
        int mod = number % 100;
        if (mod > 10 && mod < 20) return five;
        if (number % 10 == 1) return one;
        if (number % 10 >= 2 && number % 10 <= 4) return two;
        return five;
    }
}