package com.example.obstacleracehw2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.obstacleracehw2.Models.MyDB;
import com.example.obstacleracehw2.Models.Record;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;


public class ActivityGetName extends AppCompatActivity {
    private MaterialButton getName_BTN_confirm;
    private TextInputLayout getName_EDT_name;
    private int score;
    private float distance;
    private Double userLatitude = 0.0;
    private Double userLongitude = 0.0;

    //get access to location permission
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);

        requestLocationPermission();
        getName_BTN_confirm = findViewById(R.id.getName_BTN_confirm);
        getName_EDT_name = findViewById(R.id.getName_EDT_name);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        score = bundle.getInt("Score");
        distance = bundle.getFloat("Distance");

        getName_BTN_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(getName() + ", your achievments saved.\nCheck the records!");
                saveRecord();
                setName("");
                finish();
            }
        });
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        getLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Permission denied.\n Latitude and longitude set to 0", Toast.LENGTH_SHORT)
                            .show();
                    userLongitude = 0.0;
                    userLatitude = 0.0;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null) {
            myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        if (myLocation == null) {
            Toast.makeText(this, "Last location unknown. Turn location services on,\n open google maps untill you see your location and get back to the game.", Toast.LENGTH_LONG).show();
            userLatitude = 0.0;
            userLongitude = 0.0;
        } else {
            userLongitude = myLocation.getLongitude();
            userLatitude = myLocation.getLatitude();
        }
    }


    private void saveRecord() {
        String fromJsonMyDb = SharedPrefs.getInstance(this).getStringSP("MY_DB", "");
        MyDB newDb = new Gson().fromJson(fromJsonMyDb, MyDB.class);


        if (newDb.listIsFull())
            if (newDb.getAllRecords().get(9).getScore() < score) {
                newDb.removeLast();
                newDb.getAllRecords().add(
                        new Record()
                                .setName(getName())
                                .setScore(score)
                                .setDistance(distance)
                                .setLat(userLatitude)
                                .setLon(userLongitude)
                );
                newDb.sort();
            }
        String json = new Gson().toJson(newDb);
        SharedPrefs.getInstance(this).putStringSP("MY_DB", json);
    }


    private String getName() {
        return getName_EDT_name.getEditText().getText().toString();
    }

    private void setName(String str) {
        this.getName_EDT_name.getEditText().setText(str);
    }

    private void toast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


}