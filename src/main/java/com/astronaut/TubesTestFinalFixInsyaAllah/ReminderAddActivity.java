package com.astronaut.TubesTestFinalFixInsyaAllah;

import android.app.AlertDialog;
import android.content.DialogInterface;
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


public class ReminderAddActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private Toolbar toolbar;
    private EditText et_title;
    private TextView tv_dateTime, tv_timeText, tv_repeattext, tv_repeatNotext, tv_repeatTipetext;
    private FloatingActionButton FAB1;
    private FloatingActionButton FAB2;
    private Calendar calendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private String title;
    private String time;
    private String date;
    private String repeat;
    private String repeatNo;
    private String repeatType;
    private String active;


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
        tv_dateTime = (TextView) findViewById(R.id.set_date);
        tv_timeText = (TextView) findViewById(R.id.set_time);
        tv_repeattext = (TextView) findViewById(R.id.set_repeat);
        tv_repeatNotext = (TextView) findViewById(R.id.set_repeat_no);
        tv_repeatTipetext = (TextView) findViewById(R.id.set_repeat_type);
        FAB1 = (FloatingActionButton) findViewById(R.id.alarmon);
        FAB2 = (FloatingActionButton) findViewById(R.id.alarmoff);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_add_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        active = "true";
        repeat = "true";
        repeatNo = Integer.toString(1);
        repeatType = "Jam";

        calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DATE);

        date = mDay + "/" + mMonth + "/" + mYear;
        time = mHour + ":" + mMinute;

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

        // text view data reminder
        tv_dateTime.setText(date);
        tv_timeText.setText(time);
        tv_repeatNotext.setText(repeatNo);
        tv_repeatTipetext.setText(repeatType);
        tv_repeattext.setText("Tiap " + repeatNo + " " + repeatType);

        // save
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            et_title.setText(savedTitle);
            title = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            tv_timeText.setText(savedTime);
            time = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            tv_dateTime.setText(savedDate);
            date = savedDate;

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            tv_repeattext.setText(saveRepeat);
            repeat = saveRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            tv_repeatNotext.setText(savedRepeatNo);
            repeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            tv_repeatTipetext.setText(savedRepeatType);
            repeatType = savedRepeatType;

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
    }

    //save
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, et_title.getText());
        outState.putCharSequence(KEY_TIME, tv_timeText.getText());
        outState.putCharSequence(KEY_DATE, tv_dateTime.getText());
        outState.putCharSequence(KEY_REPEAT, tv_repeattext.getText());
        outState.putCharSequence(KEY_REPEAT_NO, tv_repeatNotext.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, tv_repeatTipetext.getText());
        outState.putCharSequence(KEY_ACTIVE, active);
    }

    // time picker
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

    // get nilai dari time picker
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            time = hourOfDay + ":" + "0" + minute;
        } else {
            time = hourOfDay + ":" + minute;
        }
        tv_timeText.setText(time);
    }

    // get nilai dari date picker
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        date = dayOfMonth + "/" + monthOfYear + "/" + year;
        tv_dateTime.setText(date);
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

    // switch repeat
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            repeat = "true";
            tv_repeattext.setText("Tiap " + repeatNo + " " + repeatType);
        } else {
            repeat = "false";
            tv_repeattext.setText(R.string.repeat_off);
        }
    }

    // repeat type
    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "Menit";
        items[1] = "Jam";
        items[2] = "Hari";
        items[3] = "Minggu";
        items[4] = "Bulan";


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Tipe");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                repeatType = items[item];
                tv_repeatTipetext.setText(repeatType);
                tv_repeattext.setText("Tiap " + repeatNo + " " + repeatType );
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // interval repeat
    public void setRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Masukkan angka");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            repeatNo = Integer.toString(1);
                            tv_repeatNotext.setText(repeatNo);
                            tv_repeattext.setText("Every " + repeatNo + " " + repeatType + "(s)");
                        }
                        else {
                            repeatNo = input.getText().toString().trim();
                            tv_repeatNotext.setText(repeatNo);
                            tv_repeattext.setText("Every " + repeatNo + " " + repeatType + "(s)");
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }

    // save btn
    public void saveReminder(){
        ReminderDatabase rb = new ReminderDatabase(this);

        // create reminder
        int ID = rb.addReminder(new Reminder(title, date, time, repeat, repeatNo, repeatType, active));

        // create notif
        calendar.set(Calendar.MONTH, --mMonth);
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.SECOND, 0);

        // cek repeat type
        if (repeatType.equals("Menit")) {
            mRepeatTime = Integer.parseInt(repeatNo) * milMinute;
        } else if (repeatType.equals("Jam")) {
            mRepeatTime = Integer.parseInt(repeatNo) * milHour;
        } else if (repeatType.equals("Hari")) {
            mRepeatTime = Integer.parseInt(repeatNo) * milDay;
        } else if (repeatType.equals("Minggu")) {
            mRepeatTime = Integer.parseInt(repeatNo) * milWeek;
        } else if (repeatType.equals("Bulan")) {
            mRepeatTime = Integer.parseInt(repeatNo) * milMonth;
        }


        if (active.equals("true")) {
            if (repeat.equals("true")) {
                new AlarmReceiver().setRepeatAlarm(getApplicationContext(), calendar, ID, mRepeatTime);
            } else if (repeat.equals("false")) {
                new AlarmReceiver().setAlarm(getApplicationContext(), calendar, ID);
            }
        }

        // toast save
        Toast.makeText(getApplicationContext(), "tersimpan",
                Toast.LENGTH_SHORT).show();

        onBackPressed();
    }

    // back btn
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

            // update reminder
            case R.id.save_reminder:
                et_title.setText(title);

                if (et_title.getText().toString().length() == 0)
                    et_title.setError("Tidak boleh kosong!");

                else {
                    saveReminder();
                }
                return true;

            // cancel save
            case R.id.discard_reminder:
                Toast.makeText(getApplicationContext(), "Cancel",
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}