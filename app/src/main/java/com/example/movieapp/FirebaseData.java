package com.example.movieapp;

public class FirebaseData {
    String title;
    String url;
    String movieId;
    public FirebaseData() {
    }

    public FirebaseData(String movieId, String title, String url) {
        this.title = title;
        this.url = url;
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
