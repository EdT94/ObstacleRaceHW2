package com.example.obstacleracehw2;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.FrameLayout;


public class ActivityRecords extends FragmentActivity {
    private ListFragment listFragment = new ListFragment();
    private MapFragment mapFragment = new MapFragment();
    private FrameLayout records_FRM_recordsTable;
    private FrameLayout records_FRM_recordsLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        records_FRM_recordsTable = findViewById(R.id.records_FRM_recordsTable);
        records_FRM_recordsLocations = findViewById(R.id.records_FRM_recordsLocations);
        getSupportFragmentManager().beginTransaction().add(R.id.records_FRM_recordsTable, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.records_FRM_recordsLocations, mapFragment).commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}

