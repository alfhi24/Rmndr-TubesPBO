package com.astronaut.TubesTestFinalFixInsyaAllah;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


public class ReminderEditActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private Toolbar toolbar;
    private EditText et_title;
    private TextView tv_date, tv_time, tv_repeat, tv_repeatno, tv_repeatType;
    private FloatingActionButton FAB1;
    private FloatingActionButton FAB2;
    private Switch switch_repeat;
    private String title;
    private String time;
    private String date;
    private String repeatNo;
    private String repeattype;
    private String active;
    private String repeat;
    private String[] datesplit;
    private String[] timesplit;
    private int ID;
    private int year, month, hour, minute, day;
    private long repeatTime;
    private Calendar cal;
    private Reminder rec_reminder;
    private ReminderDatabase rb;
    private AlarmReceiver alarmReceiver;


    public static final String EXTRA_REMINDER_ID = "Reminder_ID";


    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";


    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        et_title = (EditText) findViewById(R.id.reminder_title);
        tv_date = (TextView) findViewById(R.id.set_date);
        tv_time = (TextView) findViewById(R.id.set_time);
        tv_repeat = (TextView) findViewById(R.id.set_repeat);
        tv_repeatno = (TextView) findViewById(R.id.set_repeat_no);
        tv_repeatType = (TextView) findViewById(R.id.set_repeat_type);
        FAB1 = (FloatingActionButton) findViewById(R.id.alarmon);
        FAB2 = (FloatingActionButton) findViewById(R.id.alarmoff);
        switch_repeat = (Switch) findViewById(R.id.repeat_switch);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_edit_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title = s.toString().trim();
                et_title.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        ID = Integer.parseInt(getIntent().getStringExtra(EXTRA_REMINDER_ID));

        // Get reminder id
        rb = new ReminderDatabase(this);
        rec_reminder = rb.getReminder(ID);


        title = rec_reminder.getTitle();
        date = rec_reminder.getDate();
        time = rec_reminder.getTime();
        repeat = rec_reminder.getRepeat();
        repeatNo = rec_reminder.getRepeatNo();
        repeattype = rec_reminder.getRepeatType();
        active = rec_reminder.getActive();

        // text view
        et_title.setText(title);
        tv_date.setText(date);
        tv_time.setText(time);
        tv_repeatno.setText(repeatNo);
        tv_repeatType.setText(repeattype);
        tv_repeat.setText("Tiap" + repeatNo + " " + repeattype);

        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            et_title.setText(savedTitle);
            title = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            tv_time.setText(savedTime);
            time = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            tv_date.setText(savedDate);
            date = savedDate;

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            tv_repeat.setText(saveRepeat);
            repeat = saveRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            tv_repeatno.setText(savedRepeatNo);
            repeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            tv_repeatType.setText(savedRepeatType);
            repeattype = savedRepeatType;

            active = savedInstanceState.getString(KEY_ACTIVE);
        }

    // active btn
        if (active.equals("false")) {
            FAB1.setVisibility(View.VISIBLE);
            FAB2.setVisibility(View.GONE);

        } else if (active.equals("true")) {
            FAB1.setVisibility(View.GONE);
            FAB2.setVisibility(View.VISIBLE);
        }

        // Srepeat switch
        if (repeat.equals("false")) {
            switch_repeat.setChecked(false);
            tv_repeat.setText(R.string.repeat_off);

        } else if (repeat.equals("true")) {
            switch_repeat.setChecked(true);
        }


        cal = Calendar.getInstance();
        alarmReceiver = new AlarmReceiver();

        datesplit = date.split("/");
        timesplit = time.split(":");

        day = Integer.parseInt(datesplit[0]);
        month = Integer.parseInt(datesplit[1]);
        year = Integer.parseInt(datesplit[2]);
        hour = Integer.parseInt(timesplit[0]);
        minute = Integer.parseInt(timesplit[1]);
    }


    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, et_title.getText());
        outState.putCharSequence(KEY_TIME, tv_time.getText());
        outState.putCharSequence(KEY_DATE, tv_date.getText());
        outState.putCharSequence(KEY_REPEAT, tv_repeat.getText());
        outState.putCharSequence(KEY_REPEAT_NO, tv_repeatno.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, tv_repeatType.getText());
        outState.putCharSequence(KEY_ACTIVE, active);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    // timpe picker
    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    // date picker
    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        hour = hourOfDay;
        this.minute = minute;
        if (minute < 10) {
            time = hourOfDay + ":" + "0" + minute;
        } else {
            time = hourOfDay + ":" + minute;
        }
        tv_time.setText(time);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        day = dayOfMonth;
        month = monthOfYear;
        this.year = year;
        date = dayOfMonth + "/" + monthOfYear + "/" + year;
        tv_date.setText(date);
    }


    public void selectFab1(View v) {
        FAB1 = (FloatingActionButton) findViewById(R.id.alarmon);
        FAB1.setVisibility(View.GONE);
        FAB2 = (FloatingActionButton) findViewById(R.id.alarmoff);
        FAB2.setVisibility(View.VISIBLE);
        active = "true";
    }


    public void selectFab2(View v) {
        FAB2 = (FloatingActionButton) findViewById(R.id.alarmoff);
        FAB2.setVisibility(View.GONE);
        FAB1 = (FloatingActionButton) findViewById(R.id.alarmon);
        FAB1.setVisibility(View.VISIBLE);
        active = "false";
    }


    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            repeat = "true";
            tv_repeat.setText("Tiap " + repeatNo + " " + repeattype);

        } else {
            repeat = "false";
            tv_repeat.setText(R.string.repeat_off);
        }
    }


    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "Menit";
        items[1] = "Jam";
        items[2] = "Hari";
        items[3] = "Minggu";
        items[4] = "Month";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Tipe");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                repeattype = items[item];
                tv_repeatType.setText(repeattype);
                tv_repeat.setText("Tipe " + repeatNo + " " + repeattype );
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void setRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Masukkan angka " );


        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            repeatNo = Integer.toString(1);
                            tv_repeatno.setText(repeatNo);
                            tv_repeat.setText("Tiap " + repeatNo + " " + repeattype);
                        }
                        else {
                            repeatNo = input.getText().toString().trim();
                            tv_repeatno.setText(repeatNo);
                            tv_repeat.setText("Tiap " + repeatNo + " " + repeattype);
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }


    public void updateReminder(){
        // Set nilai baru
        rec_reminder.setTitle(title);
        rec_reminder.setDate(date);
        rec_reminder.setTime(time);
        rec_reminder.setRepeat(repeat);
        rec_reminder.setRepeatNo(repeatNo);
        rec_reminder.setRepeatType(repeattype);
        rec_reminder.setActive(active);

        // Update reminder
        rb.updateReminder(rec_reminder);


        cal.set(Calendar.MONTH, --month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);


        alarmReceiver.cancelAlarm(getApplicationContext(), ID);


        if (repeattype.equals("Menit")) {
            repeatTime = Integer.parseInt(repeatNo) * milMinute;
        } else if (repeattype.equals("Jam")) {
            repeatTime = Integer.parseInt(repeatNo) * milHour;
        } else if (repeattype.equals("Hari")) {
            repeatTime = Integer.parseInt(repeatNo) * milDay;
        } else if (repeattype.equals("Mingggu")) {
            repeatTime = Integer.parseInt(repeatNo) * milWeek;
        } else if (repeattype.equals("Bulan")) {
            repeatTime = Integer.parseInt(repeatNo) * milMonth;
        }

        // Create notification
        if (active.equals("true")) {
            if (repeat.equals("true")) {
                alarmReceiver.setRepeatAlarm(getApplicationContext(), cal, ID, repeatTime);
            } else if (repeat.equals("false")) {
                alarmReceiver.setAlarm(getApplicationContext(), cal, ID);
            }
        }

        // Create toast to confirm update
        Toast.makeText(getApplicationContext(), "Edited",
                Toast.LENGTH_SHORT).show();
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save_reminder:
                et_title.setText(title);

                if (et_title.getText().toString().length() == 0)
                    et_title.setError("Tidak boleh kosong!");

                else {
                    updateReminder();
                }
                return true;

            case R.id.discard_reminder:
                Toast.makeText(getApplicationContext(), "cancel",
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}