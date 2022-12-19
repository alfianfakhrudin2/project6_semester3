package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class updateActivity extends AppCompatActivity {
    private Notes notes;
    private String notesId;
    public static final String EXTRA_NOTES = "extra_notes";
    private ImageButton imageback, imagetrash;
    private EditText title, subtitle, content;
    private TextView dateTime;
    private String selectedNoteColor;
    View viewSubtitleIndicator;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imageback = findViewById(R.id.imageBack);
        imagetrash = findViewById(R.id.imageTrash);
        title = findViewById(R.id.inputNotesTitle);
        subtitle = findViewById(R.id.inputNotesSubtitle);
        content = findViewById(R.id.inputNotesContent);
        dateTime = findViewById(R.id.textDateTime);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);
        notes = getIntent().getParcelableExtra(EXTRA_NOTES);
        selectedNoteColor ="#333333";

        if(notes != null){
            notesId = notes.getId();
            title.setText(notes.getTitle());
            subtitle.setText(notes.getSubtitle());
            content.setText(notes.getNotes());
        } else{
            notes = new Notes();
        }

        //CALL SUBTITLE COLOR INDICATOR CHANGED
        indicatorChange();
        dateTime.setText(
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));

        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().isEmpty() && subtitle.getText().toString().isEmpty()
                        && content.getText().toString().isEmpty() && notes.getBgColor().isEmpty()){
                    Intent intent = new Intent(updateActivity.this, MainActivity.class);
                    startActivity(intent);
                }   else{
                    updateNotes();
                    Intent intent = new Intent(updateActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        // CLICK DELETE BUTTON
        imagetrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }

    // UPDATE NOTES
    private void updateNotes(){
        String noteTitle = title.getText().toString().trim();
        String noteSubtitle = subtitle.getText().toString().trim();
        String noteContent = content.getText().toString().trim();
        String timeStamp = dateTime.getText().toString();

        notes.setTimestamp(timeStamp);
        notes.setNotes(noteContent);
        notes.setSubtitle(noteSubtitle);
        notes.setTitle(noteTitle);
        notes.setBgColor(selectedNoteColor);

        DatabaseReference dbNotes = mDatabase.child("notes");

        // UPDATE DATA
        dbNotes.child(notesId).setValue(notes);
        finish();
    }

    //SHOW ALERT DIALOG
    private void showAlertDialog(){
        String dialogTitle = "Delete Notes";
        String dialogMessege = "Are you sure want tot delete this notes?";

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle(dialogTitle);
        alertBuilder.setMessage(dialogMessege)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference dbNotes = mDatabase.child("notes").child(notesId);
                        dbNotes.removeValue();
                        Toast.makeText(updateActivity.this, "Deleting notes",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(title.getText().toString().isEmpty() && subtitle.getText().toString().isEmpty()
                && content.getText().toString().isEmpty() && notes.getBgColor().isEmpty()){
            Intent intent = new Intent(updateActivity.this, MainActivity.class);
            startActivity(intent);
        }   else{
            updateNotes();
            Intent intent = new Intent(updateActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void setViewSubtitleIndicator(){
        GradientDrawable gd = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gd.setColor(Color.parseColor(selectedNoteColor));
    }

    // SET NOTES INDICATOR SUBTITLE COLOR CHANGE
    private void indicatorChange(){
        final LinearLayout modal = findViewById(R.id.modal);
        final ImageView imageColor1 = modal.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = modal.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = modal.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = modal.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = modal.findViewById(R.id.imageColor5);

        modal.findViewById(R.id.color1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicator();
            }
        });

        modal.findViewById(R.id.color2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor = "#808000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicator();
            }
        });

        modal.findViewById(R.id.color3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor = "#008080";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicator();
            }
        });

        modal.findViewById(R.id.color4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor = "#DC143C";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicator();
            }
        });

        modal.findViewById(R.id.color5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                setViewSubtitleIndicator();
            }
        });

        if(notes != null && notes.getBgColor() != null && !notes.getBgColor().trim().isEmpty()){
            switch (notes.getBgColor()){
                case "#333333":
                    modal.findViewById(R.id.color1).performClick();
                    break;
                case "#808000":
                    modal.findViewById(R.id.color2).performClick();
                    break;
                case "#008080":
                    modal.findViewById(R.id.color3).performClick();
                    break;
                case "#DC143C":
                    modal.findViewById(R.id.color4).performClick();
                    break;
                case "#000000":
                    modal.findViewById(R.id.color5).performClick();
                    break;
            }
        }
    }
}