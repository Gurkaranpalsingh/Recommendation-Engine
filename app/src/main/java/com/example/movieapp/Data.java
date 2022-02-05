package com.example.movieapp;

import java.util.Date;

public class Data {
    String title;
    String release_date;
    Double vote_average;
    String overview;

    public Data() {
        this.title = null;
        this.release_date = null;
        this.vote_average = null;
        this.overview = null;
    }
    public Data(String title, String release_date, Double vote_average, String overview) {
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.overview = overview;
    }
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }
}
