package com.appssquare.mahmoud.myshoppinglist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

/**
 * Created by mahmoud on 12/02/2018.
 */

public class RemindersManager {
        DatabaseHelper db;
        Preferences prefs;
    List<Item> remindersList;
    Long nextRemindTime;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;


    public  RemindersManager(Context con){
            db=new DatabaseHelper(con);
            prefs=Preferences.getInstance(con);
        alarmMgr = (AlarmManager)con.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(con, MyReciever.class);
        intent.setAction("REMINDING ACTION");
        alarmIntent = PendingIntent.getBroadcast(con, 11111111, intent, 0);


    }



    public void reschedule(){
        remindersList=db.getUserRemindingItems(prefs.getKey("userId"));
        if(remindersList.size()==0) {
            nextRemindTime = 0l;
            return ;
        }
        Log.e("size   ",String.valueOf(remindersList.size()));
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
        int i;
        Calendar cal=Calendar.getInstance();
        Log.e("system time   ",String.valueOf(System.currentTimeMillis()));
        for(i=0;i<remindersList.size();i++){
            Log.e("item long in list //",String.valueOf(remindersList.get(i).getReminder()));
            cal.setTimeInMillis(remindersList.get(i).getReminder());

            nextRemindTime=cal.getTimeInMillis(); // long date


            if(nextRemindTime>System.currentTimeMillis()) {
                Log.e("nextremind at  ",String.valueOf(nextRemindTime));
                alarmMgr.set(AlarmManager.RTC, nextRemindTime, alarmIntent);
                break;
            }
        }





        return;
    }
}
