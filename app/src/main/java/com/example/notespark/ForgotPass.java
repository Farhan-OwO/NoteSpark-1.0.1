package com.example.notespark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notespark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {

    //variables
    private EditText  textForgotEmailAddress;
    private Button resetPassButton;
    private TextView signin;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        getSupportActionBar().hide();

        textForgotEmailAddress = findViewById(R.id.editTextForgotEmailAddress);
        resetPassButton = findViewById(R.id.signResetPassButton);
        signin = findViewById(R.id.textView14);

        auth = FirebaseAuth.getInstance();



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ForgotPass.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        resetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = textForgotEmailAddress.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"pls enter email",Toast.LENGTH_SHORT).show();
                }else{
                    auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Password Reset Email Sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPass.this, Login.class));

                            }else{
                                Toast.makeText(getApplicationContext(), "Reset Email Not Sent", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

    }
}