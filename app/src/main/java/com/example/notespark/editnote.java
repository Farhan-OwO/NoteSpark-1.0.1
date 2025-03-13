package com.example.notespark;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class editnote extends AppCompatActivity {

    private EditText notetitle, notebody;
    private FloatingActionButton updatenote;

    private FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editnote);

        notetitle = findViewById(R.id.notetitleedit);
        notebody = findViewById(R.id.notebodyedit);
        updatenote = findViewById(R.id.updatenoteedit);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbarnoteedit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();

        String noteTTl = data.getStringExtra("title");
        String noteBody = data.getStringExtra("body");

        notetitle.setText(noteTTl);
        notebody.setText(noteBody);

        updatenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newtitle = notetitle.getText().toString();
                String newbody = notebody.getText().toString();
                if (newtitle.isEmpty() || newbody.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "both fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(user.getUid()).collection("myNotes").document(data.getStringExtra("docId"));
                    Map <String, Object> note = new HashMap<>();
                    note.put("title", newtitle);
                    note.put("body", newbody);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(editnote.this,"Updated",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(editnote.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editnote.this,"Update Failed",Toast.LENGTH_SHORT).show();

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