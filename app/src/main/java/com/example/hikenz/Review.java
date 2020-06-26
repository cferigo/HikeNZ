package com.example.hikenz;

public class Review {
    String firstname, review;

    public Review()
    {
        //Firebase needs empty constructor to work
    }

    public Review(String firstname, String review) {
        this.firstname = firstname;
        this.review = review;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getReview() {
        return review;
    }
}
