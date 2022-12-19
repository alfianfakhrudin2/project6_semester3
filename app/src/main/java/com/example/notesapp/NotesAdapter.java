package com.example.notesapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{
    private Activity activity;
    //Untuk menampung data dari model Notes
    private ArrayList<Notes> notesList = new ArrayList<>();
    // Berisi data dari class Notes
    public void setNotesList(ArrayList<Notes> notesList)
    {
        this.notesList = notesList;
    }

    public NotesAdapter(Activity activity){
        this.activity = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Membuat card view pada home page notes
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_items_note, parent, false);
        // Mengisi card dengan elemen card
        return new ViewHolder(view);
    }

    // Set Notes component to the card
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // SET NOTES TILE
        holder.title.setText(notesList.get(position).getTitle());
        // SET NOTES SUBTILE
        holder.subtitle.setText(notesList.get(position).getSubtitle());
        // SET NOTES TIME
        holder.timeStamp.setText(notesList.get(position).getTimestamp());
        // SET NOTES BACKGROUND
        holder.setNotes(notesList.get(position));

        // Take Note ID and move to Update Note Page
        holder.notesLayout.setOnClickListener((View v) -> {
            Intent intent = new Intent(activity, updateActivity.class);
            intent.putExtra(updateActivity.EXTRA_NOTES, notesList.get(position));
            Toast.makeText(activity, "Press back button to saving notes", Toast.LENGTH_SHORT).show();
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title, subtitle, timeStamp;
        private LinearLayout notesLayout;
        private RoundedImageView imageNotesItem;
        View view;

        // GETTING THE LAYOUT ID TO AND STORING TO THE VARIABLE
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            subtitle = itemView.findViewById(R.id.textSubtitle);
            timeStamp = itemView.findViewById(R.id.textDateTime);
            notesLayout = itemView.findViewById(R.id.layoutNote);
            imageNotesItem = itemView.findViewById(R.id.imageNoteItem);
            view = itemView;
        }

        // Set Background color of the card
        void setNotes(Notes notes){
            GradientDrawable gd = (GradientDrawable) notesLayout.getBackground();
            if(notes.getBgColor() != null){
                gd.setColor(Color.parseColor(notes.getBgColor()));
            } else{
                // DEFAULT BG COLOR
                gd.setColor(Color.parseColor("#"));
            }
        }
    }
}
