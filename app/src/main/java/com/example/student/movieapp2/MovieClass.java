package com.example.student.movieapp2;


import java.io.Serializable;

public class MovieClass implements Serializable {

    private String id;
    private String title;
    private String releaseDate;
    private String vote;
    private String synopsis;
    private String image;

    /**
     * No args constructor for use in serialization
     */
    public MovieClass() {
    }

    public MovieClass(String id, String title, String releaseDate, String vote,  String synopsis, String image) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.vote = vote;
        this.synopsis = synopsis;
        this.image = image;

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.title = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVote() {
        return vote;
    }
    public void setVote(String synopsis) {
        this.vote = vote;
    }


    public String getSynopsis() {
        return synopsis;
    }
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


}
