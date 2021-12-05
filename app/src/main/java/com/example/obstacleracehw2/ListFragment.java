package com.example.obstacleracehw2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.obstacleracehw2.Adapters.RecordAdapter;
import com.example.obstacleracehw2.Models.MyDB;
import com.google.gson.Gson;

public class ListFragment extends Fragment {
    private RecyclerView list_RV_records;
    private RecordAdapter recordAdapter;
    private MyDB myDB;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        list_RV_records = view.findViewById(R.id.list_RV_records);

        String fromJSON = SharedPrefs.getInstance(getContext()).getStringSP("MY_DB", "");

        //DB is empty
        if (fromJSON.equals("")) {
            myDB = new MyDB();
            myDB.generateRecords();
            myDB.sort();
            String json = new Gson().toJson(myDB);
            SharedPrefs.getInstance(getContext()).putStringSP("MY_DB", json);

        } else {
            myDB = new Gson().fromJson(fromJSON, MyDB.class);
        }

        recordAdapter = new RecordAdapter(getContext(), myDB.getAllRecords());
        list_RV_records.setHasFixedSize(true);
        list_RV_records.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list_RV_records.setAdapter(recordAdapter);

        return view;
    }


}