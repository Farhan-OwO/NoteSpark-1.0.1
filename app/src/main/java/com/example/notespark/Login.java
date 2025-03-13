package com.example.notespark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.splashscreen.SplashScreen;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText emailin,passin;
    private TextView createnew,forgot;
    private Button signIn;
    String emailpattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        emailin = findViewById(R.id.editTextLOGEmailAddress);
        passin = findViewById(R.id.editTextLOGPassword);
        createnew = findViewById(R.id.textViewSignUp);
        signIn = findViewById(R.id.signInButton);
        forgot = findViewById(R.id.textView6);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(Login.this,MainActivity.class));
        }


        createnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Registration.class);
                startActivity(intent);
                finish();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,ForgotPass.class);
                startActivity(intent);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = emailin.getText().toString().trim();
                String pas = passin.getText().toString();

                if (em.isEmpty()){
                    Toast.makeText(getApplicationContext(),"pls enter email",Toast.LENGTH_SHORT).show();
                }else if(pas.isEmpty()){
                    Toast.makeText(getApplicationContext(),"pls enter password",Toast.LENGTH_SHORT).show();
                }else if(!em.matches(emailpattern)){
                    emailin.setError("Pls enter a valid email");
                }else if(pas.length()<7){
                    passin.setError("at least 8 characters required");
                } else {
                    auth.signInWithEmailAndPassword(em, pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if(task.isComplete()){
                                checkmailverification();
                            }else{
                                Toast.makeText(getApplicationContext(), "Account not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void checkmailverification(){
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fbuser.isEmailVerified()==true){
            Toast.makeText(getApplicationContext(), "Logging in", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent (Login.this,MainActivity.class));;
        }else {
            Toast.makeText(getApplicationContext(), "email not verified", Toast.LENGTH_SHORT).show();
            auth.signOut();
        }
    }


}