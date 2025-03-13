package com.example.notespark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    private EditText emailup, passup;
    private TextView login;
    private Button signUp;
    String emailpattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().hide();

        emailup = findViewById(R.id.editTextUPEmailAddress);
        passup = findViewById(R.id.editTextUPPassword);
        login = findViewById(R.id.textViewSignUp);
        signUp = findViewById(R.id.signUpButton);

        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = emailup.getText().toString().trim();
                String pas = passup.getText().toString();

                if (em.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (pas.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                } else if (!em.matches(emailpattern)) {
                    emailup.setError("Please enter a valid email");
                } else if (pas.length() < 8) {
                    passup.setError("At least 8 characters required");
                } else {
                    auth.createUserWithEmailAndPassword(em, pas)
                            .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                        sendEmailVerification();

                                    } else {
                                        // Handle registration failure
                                        Exception exception = task.getException();
                                        if (exception instanceof FirebaseAuthException) {
                                            String errorCode = ((FirebaseAuthException) exception).getErrorCode();
                                            String errorMessage = exception.getMessage();
                                            Log.e("Registration", "Registration failed: " + errorCode + " - " + errorMessage);
                                            handleRegistrationError(errorCode);
                                        } else {
                                            Log.e("Registration", "Registration failed: " + exception.getMessage());
                                            Toast.makeText(getApplicationContext(), "Registration failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    private void sendEmailVerification(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        finish();
                        startActivity(new Intent(Registration.this,Login.class));

                    }

                }
            });
        }
    }

    private void handleRegistrationError(String errorCode) {
        switch (errorCode) {
            case "ERROR_EMAIL_ALREADY_IN_USE":
            case "auth/email-already-in-use":
                Toast.makeText(getApplicationContext(), "Email already in use", Toast.LENGTH_SHORT).show();
                emailup.setError("Email already in use");
                break;
            case "ERROR_INVALID_EMAIL":
            case "auth/invalid-email":
                Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                emailup.setError("Invalid email");
                break;
            case "ERROR_WEAK_PASSWORD":
            case "auth/weak-password":
                Toast.makeText(getApplicationContext(), "Weak password", Toast.LENGTH_SHORT).show();
                passup.setError("Weak password");
                break;
            case "ERROR_OPERATION_NOT_ALLOWED":
            case "auth/operation-not-allowed":
                Toast.makeText(getApplicationContext(), "Operation not allowed", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}