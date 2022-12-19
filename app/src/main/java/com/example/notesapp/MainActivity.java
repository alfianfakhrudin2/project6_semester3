package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recycleView;
    DatabaseReference dbNotes;
    EditText searchInput;
    ImageView addAction, searchIcon;
    NotesAdapter notesAdapter;
    ArrayList<Notes> notesArrayList;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycleView = findViewById(R.id.recycleView);
        addAction = findViewById(R.id.imageAddAction);

        searchView = findViewById(R.id.layoutSearch);

        addAction.setOnClickListener(this);

        dbNotes = FirebaseDatabase.getInstance().getReference("notes");

        notesArrayList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

        //untuk nampilkan data dari adapater
        dbNotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notesArrayList.clear();
                // DataSnapshot contain data from database.
                // Any time read the data on database, will receive the data as data snapshot.
                for(DataSnapshot notesSnapshot : snapshot.getChildren()){
                    // Getting the value from the notes class
                    Notes notes = notesSnapshot.getValue(Notes.class);
                    // Menyimpan data ke dalam array
                    notesArrayList.add(notes);
                }
                // Access class notes adapter
                notesAdapter = new NotesAdapter(MainActivity.this);
                // Set array data ke dalam Noteslist class NotesAdapter
                notesAdapter.setNotesList(notesArrayList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                recycleView.setLayoutManager(layoutManager);
                recycleView.setLayoutManager(
                        // untuk mengatur banyaknya span pada grid
                        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                );
                recycleView.setAdapter(notesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //untuk search
    private void search(String str){
        ArrayList<Notes> list = new ArrayList<>();
        for (Notes object : notesArrayList){
            if (object.getTitle().toLowerCase().contains(str.toLowerCase())){
                list.add(object);
            }
        }
        NotesAdapter adapterclass = new NotesAdapter(MainActivity.this);
        adapterclass.setNotesList(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        recycleView.setAdapter(adapterclass);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageAddAction) {
            Intent intent = new Intent(MainActivity.this, create_activity.class);
            Toast.makeText(this, "Press back button to saving notes", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }

    //Back Navigation
    public void back(View view)
    {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed(){
        if (doubleBackToExitPressedOnce){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getText(R.string.click_again), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                doubleBackToExitPressedOnce = true; }
        }, 1000);
    }
}