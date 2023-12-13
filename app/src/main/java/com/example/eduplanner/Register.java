package com.example.eduplanner;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/**
 * Register activity that will run on the register view of the application
 */
public class Register extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, confPass;
    TextView errorBar;
    Button reg, loginNow;
    FirebaseAuth mAuth;
    /**
     * Starting values that are set and will run to mainactivity if there isn't a user logged in
     */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    /**
     * Function that will set all of the variables and view objects as well as onClicks
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.userEmail);
        editTextPassword = findViewById(R.id.password);
        confPass = findViewById(R.id.confirmPassword);
        reg = findViewById(R.id.loadAccount);
        errorBar = findViewById(R.id.errorBar);
        loginNow = findViewById(R.id.loginNow);
        mAuth = FirebaseAuth.getInstance();

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextPassword.getText() != confPass.getText()){
                    String email, password;
                    email = String.valueOf(editTextEmail.getText());
                    password = String.valueOf(editTextPassword.getText());
                    if(TextUtils.isEmpty(email)){
                        Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                        errorBar.setText("Email is empty!");
                        return;
                    }
                    if(TextUtils.isEmpty(password)){
                        Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                        errorBar.setText("Password is empty!");
                        return;
                    }

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "Account Created.",
                                                Toast.LENGTH_SHORT).show();
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else{
                    errorBar.setText("Passwords needs to match!");
                }
            }
        });
    }
}