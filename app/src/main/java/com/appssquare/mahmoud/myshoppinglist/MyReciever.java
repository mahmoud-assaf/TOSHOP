package com.appssquare.mahmoud.myshoppinglist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by mahmoud on 12/02/2018.
 */

public class MyReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        RemindersManager remindersManager=new RemindersManager(context);

        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            remindersManager.reschedule();
            Toast.makeText(context, "BOOT COMPLETED ", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, "hello reminding  ", Toast.LENGTH_LONG).show();
            //show notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.cart)
                            .setContentTitle(context.getString(R.string.app_name))
                            .setContentText(context.getString(R.string.reminder_notification));
            Intent resultIntent = new Intent(context, MainActivity.class);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);
            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
            //reschedule next alarm
            Log.e("rescheduling ","rescheduling");
            remindersManager.reschedule();
        }
    }
}
