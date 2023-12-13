package com.example.eduplanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.eduplanner.adapter.TaskAdapter;
import com.example.eduplanner.model.Task;
import com.example.eduplanner.util.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Task display activity that will hold the recycler view for the tasks
 */
public class TaskDisplay extends AppCompatActivity {
    private static final String TAG = "TaskDisplay";
    private RecyclerView mRecycler;
    private TaskAdapter mAdapter;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private String taskType;
    private ListenerRegistration mTaskRegistration;
    private DocumentReference mTaskRef;

    /**
     * Function to set the views objects and call helper functions
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        mRecycler = findViewById(R.id.taskList);
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        getData();
        // Initialize Firestore and the main RecyclerView
        mFirestore = FirebaseUtil.getFirestore();
        mQuery = mFirestore.collection("tasks");
        initRecyclerView();
        onFilter();
    }

    /**
     * Function to help set parameters at the start and more specifically the listener for database values
     */
    @Override
    public void onStart() {
        super.onStart();

        mAdapter.startListening();
    }

    /**
     *  Function to stop the listener from reading the data
     */
    @Override
    public void onStop() {
        super.onStop();

        mAdapter.stopListening();
    }

    /**
     * Recycler view helper function that will setup the values
     */
    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new TaskAdapter(mQuery) {
            /**
             * Function for when the data inside the view changes
             */
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mRecycler.setVisibility(View.GONE);
                } else {
                    mRecycler.setVisibility(View.VISIBLE);
                }
            }

            /**
             * Function to help record any errors in the process of setting the adapter
             * @param e error
             */
            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
    }

    /**
     * Setting the menu for the task display activity
     * @param menu new menu
     * @return if the menu is set
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Function to add the items to the menu
     * @param item new item
     * @return if the item is set
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homepage:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *  Filter for which tasks should be displayed within the menu
     */
    public void onFilter() {
        Query query = mFirestore.collection("tasks");
        query = query.whereEqualTo("type", taskType);
        // new query
        mQuery = query;
        mAdapter.setQuery(query);
    }

    /**
     * Getting the data for the filter that will be set from the mainactivity
     */
    private void getData(){
        if(getIntent().hasExtra("taskType")){
            taskType = getIntent().getStringExtra("taskType");
        }
        //get Request for posts with movieTitle
        else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
            taskType = "";
        }
    }
}