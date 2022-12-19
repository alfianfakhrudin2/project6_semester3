package com.example.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class create_activity extends AppCompatActivity{
    private static final int PICK_IMAGE_REQUEST = 1;

    private Notes notes;
    private ImageButton imageback, imagetrash;
    private EditText title, subtitle, content;
    private TextView dateTime;
    View viewSubtitleIndicator;
    private String selectedNoteColor;
    DatabaseReference mDatabase;
    private Notes color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        imageback = findViewById(R.id.imageBack);
        imagetrash = findViewById(R.id.imageTrash);
        title = findViewById(R.id.inputNotesTitle);
        subtitle = findViewById(R.id.inputNotesSubtitle);
        content = findViewById(R.id.inputNotesContent);
        dateTime = findViewById(R.id.textDateTime);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);
        selectedNoteColor ="#333333";

        //CALL SUBTITLE COLOR INDICATOR CHANGED
        indicatorChange();

        dateTime.setText(
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));

        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().isEmpty() && subtitle.getText().toString().isEmpty()
                    && content.getText().toString().isEmpty()){
                    Intent intent = new Intent(create_activity.this, MainActivity.class);
                    startActivity(intent);
                }   else{
                    saveNotes();
                    Intent intent = new Intent(create_activity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        notes = new Notes();
    }

    @Override
    public void onBackPressed() {
        if(title.getText().toString().isEmpty() && subtitle.getText().toString().isEmpty()
                && content.getText().toString().isEmpty()){
            Intent intent = new Intent(create_activity.this, MainActivity.class);
            startActivity(intent);
        }   else{
            saveNotes();
            Intent intent = new Intent(create_activity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void saveNotes(){
        String noteTitle = title.getText().toString().trim();
        String noteSubtitle = subtitle.getText().toString().trim();
        String noteContent = content.getText().toString().trim();
        String timeStamp = dateTime.getText().toString();

        Toast.makeText(this, "Saving data...", Toast.LENGTH_SHORT).show();

        DatabaseReference dbNotes = mDatabase.child("notes");

        String id = dbNotes.push().getKey();
        notes.setId(id);
        notes.setTimestamp(timeStamp);
        notes.setNotes(noteContent);
        notes.setSubtitle(noteSubtitle);
        notes.setTitle(noteTitle);
        notes.setBgColor(selectedNoteColor);

        dbNotes.child(id).setValue(notes);
        finish();
    }

    private void setViewSubtitleIndicator(){
        GradientDrawable gd = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gd.setColor(Color.parseColor(selectedNoteColor));
    }

    // SET NOTES INDICATOR SUBTITLE COLOR CHANGE
    private void indicatorChange() {
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
    }
}