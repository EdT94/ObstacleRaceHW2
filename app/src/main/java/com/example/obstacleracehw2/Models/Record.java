package com.example.obstacleracehw2.Models;

public class Record {
    private String name = "";
    private int score = 0;
    private double distance = 0.0;
    private double lat = 0.0;
    private double lon = 0.0;


    public Record() {
    }

    public String getName() {
        return name;
    }

    public Record setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public Record setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Record setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Record setLon(double lon) {
        this.lon = lon;
        return this;
    }


}
