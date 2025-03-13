package com.example.notespark;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton createfab;
    private FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<Firebasemodel, Noteviewholder> noteAdapter;

    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("notes");

        createfab = findViewById(R.id.creatnewnote);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        createfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateNote.class));
            }
        });

        Query query = firebaseFirestore.collection("notes").document(user.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<Firebasemodel>().setQuery(query, Firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<Firebasemodel, Noteviewholder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull Noteviewholder holder, int position, @NonNull Firebasemodel model) {
                if (model != null) {
                    holder.notetitel.setText(model.getTitle());
                    ImageView popupbtn = holder.itemView.findViewById(R.id.menupopup);

                    // Use the stored color code
                    holder.vnote.setBackgroundColor(holder.itemView.getResources().getColor(model.getColorCode(), null));

                    // Use the stored truncated body
                    holder.notebody.setText(model.getTruncatedBody());

                    String docId=noteAdapter.getSnapshots().getSnapshot(position).getId();

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //do later
                            Intent intent = new Intent(v.getContext(), notedetails.class);
                            intent.putExtra("title", model.getTitle());
                            intent.putExtra("body", model.getBody());
                            intent.putExtra("docId", docId);
                            v.getContext().startActivity(intent);

                            //Toast.makeText(MainActivity.this, "Make a edit menu thing ok ", Toast.LENGTH_SHORT).show();
                        }
                    });

                    popupbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                            popupMenu.setGravity(Gravity.END);
                            popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(@NonNull MenuItem item) {


                                    Intent intent = new Intent(v.getContext(), editnote.class);
                                    intent.putExtra("title", model.getTitle());
                                    intent.putExtra("body", model.getBody());
                                    intent.putExtra("docId", docId);
                                    v.getContext().startActivity(intent);


                                    return false;
                                }
                            });
                            popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(@NonNull MenuItem item) {
                                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(user.getUid()).collection("myNotes").document(docId);
                                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, "failed to delete", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return false;
                                }
                            });
                            popupMenu.show();
                        }
                    });
                }
            }

            @NonNull
            @Override
            public Noteviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nots_layout, parent, false);
                return new Noteviewholder(view);
            }
        };

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(noteAdapter);

        // Set the top padding of the RecyclerView
        recyclerView.setPadding(0, 250, 0, 0);

    }

    // Helper method to generate a random length between 50 and 160
    private int getRandomLength() {
        Random random = new Random();
        return random.nextInt(111) + 50; // 111 = 160 - 50 + 1
    }

    public class Noteviewholder extends RecyclerView.ViewHolder {

        private TextView notetitel;
        private TextView notebody;
        LinearLayout vnote;

        public Noteviewholder(@NonNull View itemView) {
            super(itemView);
            notetitel = itemView.findViewById(R.id.notestitel);
            notebody = itemView.findViewById(R.id.notecontent);
            vnote = itemView.findViewById(R.id.notelayout);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                auth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, Login.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (noteAdapter != null) {
            noteAdapter.notifyDataSetChanged(); // Refresh the adapter
            noteAdapter.startListening();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }

    private int randomColor() {
        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.color_one);
        colorcode.add(R.color.color_two);
        colorcode.add(R.color.color_three);
        colorcode.add(R.color.color_four);
        colorcode.add(R.color.color_five);
        Random random = new Random();
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);

    }
}