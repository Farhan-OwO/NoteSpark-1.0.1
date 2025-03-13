package com.example.notespark;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx. appcompat. widget. Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    EditText notetitle,notebody;
        FloatingActionButton savebutton;
        FirebaseAuth auth;
        FirebaseFirestore db;
        FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);

        notetitle = findViewById(R.id.createnotetitle);
        notebody = findViewById(R.id.createnotebody);
        savebutton = findViewById(R.id.savebutton);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbarcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        savebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String title = notetitle.getText().toString();
                String body = notebody.getText().toString();
                if(title.isEmpty() || body.isEmpty()){
                    Toast.makeText(CreateNote.this,"please add text", Toast.LENGTH_SHORT).show();
                }else {

                    DocumentReference documentReference = db.collection("notes").document(user.getUid()).collection("myNotes").document();
                    Map<String , Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("body",body);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        Toast.makeText(CreateNote.this,"note created successfully",Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(CreateNote.this,MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateNote.this,"failed to create note",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish(); // Or navigateUp() if you're using Navigation component
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}