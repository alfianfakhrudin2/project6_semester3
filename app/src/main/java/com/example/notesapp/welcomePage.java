package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class welcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        new Handler().postDelayed(new Runnable() {

            public void run() {
                startActivity(new Intent(welcomePage.this, MainActivity.class));
            }
        }, 2000);
    }
}