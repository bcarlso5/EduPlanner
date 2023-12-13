package com.example.eduplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.eduplanner.model.Task;
import com.example.eduplanner.util.FirebaseUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Task creation activity that will help create a new task for the user
 */
public class TaskCreation extends AppCompatActivity{
    private FirebaseFirestore mFirestore;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    EditText title;
    EditText description;
    Spinner taskTypes;
    ImageButton closeTab;
    private Button dateButton, timeButton, createTask;
    ImageButton homeNavigate;
    private TextView setDate;
    private TextView setTime;
    String titleText;
    String descriptionText;
    String taskType;

    /**
     * Function for setting the views objects and running helper functions and setting onclick events
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);
        mFirestore = FirebaseUtil.getFirestore();
        initDatePicker();
        initTimePicker();
        homeNavigate = findViewById(R.id.closeTab);
        createTask = findViewById(R.id.createTask);
        dateButton = findViewById(R.id.dateSelect);
        timeButton = findViewById(R.id.timeSelect);
        setDate = findViewById(R.id.setDate);
        setTime = findViewById(R.id.setTime);
        closeTab = findViewById(R.id.closeTab);
        title = findViewById(R.id.titleName);
        description = findViewById(R.id.postTextDesc);
        taskTypes = findViewById(R.id.taskSelection);
        homeNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker();
            }
        });
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText = title.getText().toString();
                descriptionText = description.getText().toString();
                taskType = taskTypes.getSelectedItem().toString();
                String tempDate = setDate.getText().toString();
                String tempTime = setTime.getText().toString();
                String[] seperatedDate = tempDate.split("/");
                int month = Integer.parseInt(seperatedDate[0]);
                int day = Integer.parseInt(seperatedDate[1]);
                int year = Integer.parseInt(seperatedDate[2]);
                String[] seperatedTime = tempTime.split(":");
                int hour = Integer.parseInt(seperatedTime[0]);
                int minute = Integer.parseInt(seperatedTime[1].substring(0, 2));
                Toast.makeText(TaskCreation.this, String.valueOf(minute), Toast.LENGTH_LONG).show();
                Task newTask = new Task(titleText, taskType, descriptionText, year, month, day, hour, minute);
                CollectionReference tasks = mFirestore.collection("tasks");
                tasks.add(newTask);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        //setDate.setText(getTodaysDate());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.taskTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        taskTypes.setAdapter(adapter);
    }

    /**
     * Function to return string value of todays date
     * @return
     */
    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    /**
     * Function to initialize the date picker object for the UI of selecting a date
     */
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth,month,year);
                setDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    /**
     * Function to initialize the time picker function for the UI to set a time due
     */
    private void initTimePicker(){
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setTime.setText(getTime(hourOfDay, minute));
            }
        };
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        timePicker = new TimePickerDialog(this, style, timeSetListener, hour, minute, false);
    }

    /**
     * Function to help make a date into a string
     * @param day day due
     * @param month month due
     * @param year year due
     * @return the final string
     */
    private String makeDateString(int day, int month, int year){
        return month + "/" + day + "/" + year;
    }

    /**
     * Function to set the time into a string
     * @param hr hour due
     * @param min minute due
     * @return string of time
     */
    private String getTime(int hr,int min) {
        Time tme = new Time(hr,min,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }

    /**
     * Function to show the timepicker to select a time
     */
    public void openTimePicker(){
        timePicker.show();
    }

    /**
     * function to show the datepicker to show a date selection
     */
    public void openDatePicker(){
        datePicker.show();
    }
}