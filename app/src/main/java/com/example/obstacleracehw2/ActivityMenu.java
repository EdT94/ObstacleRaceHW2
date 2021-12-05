package com.example.obstacleracehw2;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;


import com.google.android.material.button.MaterialButton;

public class ActivityMenu extends AppCompatActivity {
    private MaterialButton menu_BTN_sensors;
    private MaterialButton menu_BTN_buttons;
    private MaterialButton menu_BTN_records;
    private MediaPlayer buttonSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        menu_BTN_sensors = findViewById(R.id.menu_BTN_sensors);
        menu_BTN_buttons = findViewById(R.id.menu_BTN_buttons);
        menu_BTN_records = findViewById(R.id.menu_BTN_records);
        buttonSound = MediaPlayer.create(ActivityMenu.this, R.raw.button);

        addListeners();

    }

    private void addListeners() {
        menu_BTN_sensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound.start();
                startGame("sensors");
            }
        });

        menu_BTN_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound.start();
                startGame("buttons");
            }
        });

        menu_BTN_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSound.start();
                showRecords();
            }
        });
    }

    private void showRecords() {
        Intent intent = new Intent(this, ActivityRecords.class);
        startActivity(intent);
    }

    private void startGame(String typeOfGame) {
        Intent intent = new Intent(this, ActivityGame.class);
        intent.putExtra(ActivityGame.TYPE_OF_GAME, typeOfGame);
        startActivity(intent);
    }
}