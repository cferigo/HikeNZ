package com.example.hikenz;

import java.util.List;

public class User {
    private String firstName;
    private  String lastName;
    private String email;
    private  String role;
    List<String> favorites;
    List<String> finished;

    public  User () {
        // firebase needs an empty constructor
    }

    public User(String firstName, String lastName, String email, List<String> favorites, List<String> finished, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.favorites = favorites;
        this.finished = finished;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() { return role; }

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
