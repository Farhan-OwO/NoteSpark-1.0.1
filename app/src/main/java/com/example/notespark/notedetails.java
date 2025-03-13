package com.example.notespark;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetails extends AppCompatActivity {

    private TextView notetitle, notebody;
    private FloatingActionButton gotoeditnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notedetails);

        notetitle = findViewById(R.id.notedetailstitle);
        notebody = findViewById(R.id.notedetailsbody);
        gotoeditnote = findViewById(R.id.gotoeditnote);

        Toolbar toolbar = findViewById(R.id.toolbarnotedetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();

        gotoeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), editnote.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("body",data.getStringExtra("body"));
                intent.putExtra("docId",data.getStringExtra("docId"));
                v.getContext().startActivity(intent);
            }
        });

        notetitle.setText(data.getStringExtra("title"));
        notebody.setText(data.getStringExtra("body"));


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