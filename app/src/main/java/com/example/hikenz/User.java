package com.example.hikenz;

import java.util.List;

public class User {
    private String firstName;
    private  String lastName;
    private String email;
    List<String> favorites;
    List<String> finished;

    public User(String firstName, String lastName, String email, List<String> favorites, List<String> finished) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.favorites = favorites;
        this.finished = finished;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public List<String> getFinished() {
        return finished;
    }

    public void setFinished(List<String> finished) {
        this.finished = finished;
    }
}
