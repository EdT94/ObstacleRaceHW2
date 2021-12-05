package com.example.obstacleracehw2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.obstacleracehw2.Models.Record;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment {
    private SupportMapFragment supportMapFragment;
    private static GoogleMap map;


    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(googleMap -> {
            map = googleMap;
            map.setMyLocationEnabled(false);
        });
        return view;
    }


    public interface MapCallBack {

        void displayLocationOnMap(Record record);

        void moveCamera(Record record, LatLng latLng);

        LatLng latLngCreator(Record record);

        void clearMap();
    }


    public static MapCallBack getMapCallBack() {
        return mapCallBack;
    }

    private static MapCallBack mapCallBack = new MapCallBack() {
        @Override
        public void displayLocationOnMap(Record record) {
            moveCamera(record, latLngCreator(record));
        }

        @Override
        public void moveCamera(Record record, LatLng latLng) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            map.addMarker(new MarkerOptions().position(latLng)).setTitle(record.getName() + "'s record");

        }

        @Override
        public LatLng latLngCreator(Record record) {
            return new LatLng(record.getLat(), record.getLon());
        }

        @Override
        public void clearMap() {
            map.clear();
        }
    };


}


//-------------------------------------------------------------------------------------


