package com.example.eduplanner;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eduplanner.util.FirebaseUtil;
import com.example.eduplanner.viewmodel.MainActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Collections;

/**
 * Main activity that will hold the view for all of the main UI for the different tasks and profile
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int RC_SIGN_IN = 9001;


    private static final int LIMIT = 50;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private MainActivityViewModel mViewModel;
    private FloatingActionButton addTask;
    View overDue;
    View education;
    View business;
    View family;
    View pastTasks;
    TextView email;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    Button changeButton, editProfile;
    EditText newCollege;
    String newCollegeText;
    TextView collegename;

    /**
     * Create function that will set of the views objects and any other helper variables used within the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View model
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        overDue = findViewById(R.id.overDue);
        education = findViewById(R.id.eduTasks);
        business = findViewById(R.id.businessTasks);
        family = findViewById(R.id.famTasks);
        pastTasks = findViewById(R.id.pastTasks);
        addTask = findViewById(R.id.addTask);
        email = findViewById(R.id.textView2);
        collegename = findViewById(R.id.collegeNameText);
        editProfile = findViewById(R.id.editP);
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
            email.setText(user.getEmail());
        }
        // Initialize Firestore and the main RecyclerView
        mFirestore = FirebaseUtil.getFirestore();
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a new task", Snackbar.LENGTH_LONG)
                        .setAction("Add Task", null).show();
                Intent intent = new Intent(getApplicationContext(), TaskCreation.class);
                startActivity(intent);
                finish();
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditDialog();
            }
        });
        overDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskDisplay.class);
                String type = "overdue";
                intent.putExtra("taskType", type);
                startActivity(intent);
                finish();
            }
        });
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskDisplay.class);
                String type = "Education";
                intent.putExtra("taskType", type);
                startActivity(intent);
                finish();
            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskDisplay.class);
                String type = "Business";
                intent.putExtra("taskType", type);
                startActivity(intent);
                finish();
            }
        });
        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskDisplay.class);
                String type = "Family";
                intent.putExtra("taskType", type);
                startActivity(intent);
                finish();
            }
        });
        pastTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskDisplay.class);
                String type = "pastTasks";
                intent.putExtra("taskType", type);
                startActivity(intent);
                finish();
            }
        });
        Query query = mFirestore.collection("tasks");
    }

    /**
     * Set the menu for the activity
     * @param menu The new menu
     * @return If the menu is set
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Function for setting the values for the menu
     * @param item item for menu
     * @return if the item is within it
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                FirebaseUtil.getAuthUI().signOut(this);
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper function to create a popup for the app to change values on the profile
     */
    public void createEditDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View confirmPopupView = getLayoutInflater().inflate(R.layout.activity_profile, null);
        newCollege = confirmPopupView.findViewById(R.id.newCollege);
        changeButton = confirmPopupView.findViewById(R.id.confirmChange);
        dialogBuilder.setView(confirmPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCollegeText = newCollege.getText().toString();
                changeCollege(newCollegeText);
            }
        });
    }

    /**
     * Changing the collegename function
     * @param college new college
     */
    public void changeCollege(String college){
        collegename.setText(college);
    }
}