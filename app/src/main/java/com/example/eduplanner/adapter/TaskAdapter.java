package com.example.eduplanner.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduplanner.R;
import com.example.eduplanner.model.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class used to hold hold the tasks together in a recycler view on the app
 */
public class TaskAdapter extends FirestoreAdapter<TaskAdapter.ViewHolder>{
    /**
     * Constructor object that takes a query for the data
     * @param query holds values from the database
     */
    public TaskAdapter(Query query) {
        super(query);
    }

    /**
     * Viewholder function that will create a viewholder
     * @param parent viewgroup to get the view of the holder
     * @param viewType not used
     * @return The viewholder that will be set within the xml
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false));
    }

    /**
     * Sets the bind for the individual square of the task
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position));
    }

    /**
     * Class that will hold the information and bind for viewholder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        TextView descText;
        TextView monthDayText;
        TextView yearText;
        TextView timeDue;
        ImageButton setCompleted;

        /**
         *  Constructor used to set the views objects
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.taskTitle);
            descText = itemView.findViewById(R.id.taskDesc);
            monthDayText = itemView.findViewById(R.id.monthDay);
            yearText = itemView.findViewById(R.id.year);
            timeDue = itemView.findViewById(R.id.timeDueText);
            setCompleted = itemView.findViewById(R.id.markDone);
        }

        /**
         * Function used to set the values within the view
         * @param snapshot The values from the database
         */
        public void bind(final DocumentSnapshot snapshot) {
            Task task = snapshot.toObject(Task.class);
            Resources resources = itemView.getResources();
            titleText.setText(task.getTitle());
            descText.setText(task.getDescription());
            String hour = String.valueOf(task.getHour());
            String minute = String.valueOf(task.getMinute());
            String dueTimeText = getStringTime(hour, minute);
            timeDue.setText(dueTimeText);
            int month = task.getMonth();
            String day = String.valueOf(task.getDay());
            String monthDay = String.valueOf(getMonthName(month, day));
            monthDayText.setText(monthDay);
            String year = String.valueOf(task.getYear());
            yearText.setText(year);
            setCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open popup
                    //remove task if confirm removed
                    task.setType("pastTask");
                }
            });
        }

        /**
         * String helper function
         * @param hour hour due for task
         * @param minute minute due for task
         * @return string of the combined time
         */
        public String getStringTime(String hour, String minute){
            return hour + ":" + minute;
        }

        /**
         * String helper function used to get the month and day for view
         * @param month month value
         * @param day day string value
         * @return Final string used to present
         */
        public String getMonthName(int month, String day){
            String dayAbbrev = "";
            if(day.charAt(day.length() - 1) == '1'){
                dayAbbrev = "st";
            }
            else if (day.charAt(day.length() - 1) == '2'){
                dayAbbrev = "nd";
            }
            else if (day.charAt(day.length() - 1) == '3'){
                dayAbbrev = "rd";
            }
            else{
                dayAbbrev = "th";
            }
            if(month == 1){
                return "Jan " + day + dayAbbrev;
            }
            if(month == 2){
                return "Feb " + day + dayAbbrev;
            }
            if(month == 3){
                return "Mar " + day + dayAbbrev;
            }
            if(month == 4){
                return "Apr " + day + dayAbbrev;
            }
            if(month == 5){
                return "May " + day + dayAbbrev;
            }
            if(month == 6){
                return "Jun " + day + dayAbbrev;
            }
            if(month == 7){
                return "Jul " + day + dayAbbrev;
            }
            if(month == 8){
                return "Aug " + day + dayAbbrev;
            }
            if(month ==9){
                return "Sep " + day + dayAbbrev;
            }
            if(month == 10){
                return "Oct " + day + dayAbbrev;
            }
            if(month == 11){
                return "Nov " + day + dayAbbrev;
            }
            if(month == 12){
                return "Dec " + day + dayAbbrev;
            }
            return "Jan Test";
        }
    }
}
