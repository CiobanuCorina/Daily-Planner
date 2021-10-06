package com.example.lab2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {

    String title;
    String date;
    String time;
    EditText selectedDate;
    EditText selectedTime;
    Button pressUpdate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title = getIntent().getExtras().getString("Title");
        date = getIntent().getExtras().getString("Date");
        time = getIntent().getExtras().getString("Time");

        selectedDate = findViewById(R.id.editTextDate);
        selectedTime = findViewById(R.id.editTextTime);
        selectedDate.setFocusable(false);
        selectedDate.setClickable(true);
        selectedTime.setFocusable(false);
        selectedTime.setClickable(true);

        selectedDate.setOnClickListener(view -> showDateDialog());
        selectedTime.setOnClickListener(view -> showTimeDialog());

        pressUpdate = findViewById(R.id.button11);
        pressUpdate.setOnClickListener(view -> returnHome());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void returnHome() {
        try {
            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show();

        if(!selectedDate.getText().toString().equals("Date")) {
            date = selectedDate.getText().toString();
        }
        if(!selectedTime.getText().toString().equals("EnterTime")) {
            time = selectedTime.getText().toString();
        }
        String stringDate = date + " " + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateNotif = LocalDateTime.parse(stringDate, formatter);
        long timeInMillis = dateNotif.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Intent broadcastIntent = new Intent(UpdateActivity.this, NotificationReceiver.class);
        broadcastIntent.putExtra("Title", title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateActivity.this, 0, broadcastIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Log.println(Log.ASSERT, "time", String.valueOf(timeInMillis));
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent);
        Intent intent = new Intent(UpdateActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(intent);
    }

    private void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, i, i1) -> {
            Log.println(Log.ASSERT, "time", "clicked");
            calendar.set(Calendar.HOUR_OF_DAY, i);
            calendar.set(Calendar.MINUTE, i1);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            selectedTime.setText(simpleDateFormat.format(calendar.getTime()));
        };
        new TimePickerDialog(UpdateActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false).show();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            selectedDate.setText(simpleDateFormat.format(calendar.getTime()));
        };
        new DatePickerDialog(UpdateActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void update() throws IOException {
        ArrayList<Planner> plannerList = XMLOperator.parseXml(UpdateActivity.this);
        for(Planner planner: plannerList) {
            if(planner.title.equals(title) && planner.date.equals(date) && planner.time.equals(time)) {
                if(!selectedDate.getText().toString().equals("Date")) {
                    planner.date = selectedDate.getText().toString();
                }
                if(!selectedTime.getText().toString().equals("EnterTime")) {
                    planner.time = selectedTime.getText().toString();
                }
            }
        }
        XMLOperator.writeXml(UpdateActivity.this, plannerList);
    }
}