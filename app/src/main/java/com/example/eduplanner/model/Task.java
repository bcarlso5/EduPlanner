package com.example.eduplanner.model;

import com.google.type.Date;

/**
 * Class function used to keep the value for a task
 */
public class Task {
    private String title;
    /**
     *  Type of task that will be recorded
     */
    private String type;
    private String description;
    private Boolean completed;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    /**
     * Base task constructor
     */
    public Task() {}

    /**
     * Main task constructor that will create a task with the given parameters
     * @param title Title of the task
     * @param type Task type
     * @param description Description for task
     * @param year year the task is due
     * @param month month the task is due
     * @param day day the task is due
     * @param hour hour the task is due
     * @param minute minute the task is due
     */
    public Task(String title, String type, String description, int year, int month, int day, int hour, int minute){
        this.title = title;
        this.type = type;
        this.description = description;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        completed = false;
    }

    /**
     * Get function to help get the value of title
     * @return title value
     */
    public String getTitle(){
        return title;
    }
    /**
     * Get function to help get the value of type
     * @return type value
     */
    public String getType(){
        return type;
    }
    /**
     * Get function to help get the value of description
     * @return description value
     */
    public String getDescription() {
        return description;
    }
    /**
     * Get function to help get the value of year
     * @return year value
     */
    public int getYear(){
        return year;
    }
    /**
     * Get function to help get the value of month
     * @return month value
     */
    public int getMonth(){
        return month;
    }
    /**
     * Get function to help get the value of day
     * @return day value
     */
    public int getDay(){
        return day;
    }
    /**
     * Get function to help get the value of hour
     * @return hour value
     */
    public int getHour(){
        return hour;
    }
    /**
     * Get function to help get the value of minute
     * @return minute value
     */
    public int getMinute(){
        return minute;
    }
    /**
     * Get function to help get the value of completed
     * @return completed value
     */
    public Boolean getCompleted(){
        return completed;
    }
    /**
     * Set function to help set the value of task
     * @return new task type
     */
    public void setType(String type){
        this.type = type;
    }
}
