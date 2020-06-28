package com.example.hikenz;

public class Review {
    String firstName, review;

    public Review()
    {
        //Firebase needs empty constructor to work
    }

    public Review(String firstName, String review) {
        this.firstName = firstName;
        this.review = review;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getReview() {
        return review;
    }
}
