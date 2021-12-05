package com.example.obstacleracehw2.Adapters;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obstacleracehw2.MapFragment;
import com.example.obstacleracehw2.Models.Record;

import com.example.obstacleracehw2.R;
import com.example.obstacleracehw2.SharedPrefs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Record> records = new ArrayList<>();
    private MaterialButton records_BTN_location;

    public RecordAdapter(Context context, ArrayList<Record> records) {
        this.context = context;
        this.records = records;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_records_item, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecordViewHolder recordViewHolder = (RecordViewHolder) holder;
        Record record = getItem(position);

        recordViewHolder.records_LBL_name.setText(record.getName());
        recordViewHolder.records_LBL_score.setText("Score : " + record.getScore() + "$");
        recordViewHolder.records_LBL_distance.setText("Distance : " + new DecimalFormat("##.##").format(record.getDistance()) + "km");
        recordViewHolder.records_LBL_longitude.setText("Longitude : " + record.getLon() + "");
        recordViewHolder.records_LBL_latittude.setText("Latitude : " + record.getLat() + "");


    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    private Record getItem(int position) {
        return records.get(position);
    }



    public class RecordViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView records_LBL_latittude;
        private MaterialTextView records_LBL_longitude;
        private MaterialTextView records_LBL_distance;
        private MaterialTextView records_LBL_score;
        private MaterialTextView records_LBL_name;


        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            records_LBL_name = itemView.findViewById(R.id.records_LBL_Name);
            records_LBL_score = itemView.findViewById(R.id.records_LBL_Score);
            records_LBL_distance = itemView.findViewById(R.id.records_LBL_distance);
            records_LBL_longitude = itemView.findViewById(R.id.records_LBL_longitude);
            records_LBL_latittude = itemView.findViewById(R.id.records_LBL_latittude);

            itemView.findViewById(R.id.records_BTN_location).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                MapFragment.getMapCallBack().clearMap();
                                MapFragment.getMapCallBack().displayLocationOnMap(records.get(getAdapterPosition()));
                                MediaPlayer buttonSound = MediaPlayer.create(itemView.getContext(), R.raw.button);
                                buttonSound.start();
                        }
                    }
            );

        }
    }
}