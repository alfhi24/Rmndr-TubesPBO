package com.astronaut.TubesTestFinalFixInsyaAllah;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;


public class BCReceiver extends BroadcastReceiver {

    private String title;
    private String time;
    private String date;
    private String repeatNo;
    private String repeatType;
    private String active;
    private String repeat;
    private String[] dateSplit;
    private String[] timeSplit;
    private int year, month, hour, minute, day, receiveId;
    private long repeatTime;

    private Calendar calendar;
    private AlarmReceiver alarmReceiver;


    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            ReminderDatabase rb = new ReminderDatabase(context);
            calendar = Calendar.getInstance();
            alarmReceiver = new AlarmReceiver();

            List<Reminder> reminders = rb.getAllReminders();

            for (Reminder rm : reminders) {
                receiveId = rm.getID();
                repeat = rm.getRepeat();
                repeatNo = rm.getRepeatNo();
                repeatType = rm.getRepeatType();
                active = rm.getActive();
                date = rm.getDate();
                time = rm.getTime();

                dateSplit = date.split("/");
                timeSplit = time.split(":");

                day = Integer.parseInt(dateSplit[0]);
                month = Integer.parseInt(dateSplit[1]);
                year = Integer.parseInt(dateSplit[2]);
                hour = Integer.parseInt(timeSplit[0]);
                minute = Integer.parseInt(timeSplit[1]);

                calendar.set(Calendar.MONTH, --month);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);



                // Create a new notification
                if (active.equals("true")) {
                    if (repeat.equals("true")) {
                        alarmReceiver.setRepeatAlarm(context, calendar, receiveId, repeatTime);
                    } else if (repeat.equals("false")) {
                        alarmReceiver.setAlarm(context, calendar, receiveId);
                    }
                }

                // tipe repeat
                if (repeatType.equals("Minute")) {
                    repeatTime = Integer.parseInt(repeatNo) * milMinute;
                } else if (repeatType.equals("Hour")) {
                    repeatTime = Integer.parseInt(repeatNo) * milHour;
                } else if (repeatType.equals("Day")) {
                    repeatTime = Integer.parseInt(repeatNo) * milDay;
                } else if (repeatType.equals("Week")) {
                    repeatTime = Integer.parseInt(repeatNo) * milWeek;
                } else if (repeatType.equals("Month")) {
                    repeatTime = Integer.parseInt(repeatNo) * milMonth;
                }
            }
        }
    }
}