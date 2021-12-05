package com.example.obstacleracehw2.Models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class MyDB {
    private ArrayList<Record> allRecords = new ArrayList<>();

    public MyDB() {

    }

    public ArrayList<Record> getAllRecords() {
        return allRecords;
    }

    public void generateRecords() {
        for (int i = 0; i < 10; i++) {
            allRecords.add(
                    new Record()
                            .setName("Generated Record " + (i + 1))
                            .setScore(new Random().nextInt(20) * 100)
                            .setDistance(new Random().nextDouble() * i)
                            .setLat((Math.random() * 180.0) - 90.0)
                            .setLon((Math.random() * 360.0) - 180.0)
            );
        }
    }

    public void sort() {
        allRecords.sort(new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                if (o1.getScore() > o2.getScore())
                    return -1;
                if (o1.getScore() < o2.getScore())
                    return 1;
                return 0;
            }
        });
    }

    public boolean listIsFull() {
        if (getAllRecords().size() >= 10)
            return true;
        return false;
    }


    public void removeLast() {
        allRecords.remove(allRecords.size() - 1);
    }

}
