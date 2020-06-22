package com.example.hikenz;

public class Track {
    private String Name, Description, Difficulty;
    private int Distance, Time;
    private boolean DogFriendly;

    public  Track () {
        // firebase needs an empty constructor
    }

    public Track(String name, String description, String difficulty, int distance, int time, boolean dogFriendly) {
        Name = name;
        Description = description;
        Difficulty = difficulty;
        Distance = distance;
        Time = time;
        DogFriendly = dogFriendly;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public int getDistance() {
        return Distance;
    }

    public int getTime() {
        return Time;
    }

    public boolean isDogFriendly() {
        return DogFriendly;
    }
}
