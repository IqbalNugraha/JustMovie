package com.example.justmovie.model;

import java.io.Serializable;

public class TvSeriesSearchModel implements Serializable {

    private int Id;
    private String Name;
    private double VoteAverage;
    private String Overview;
    private String First_air_date;
    private String PosterPath;
    private String BackdropPath;
    private String Popularity;
    public String original_title;
    public String Release_date;
    public String Media_type;

    public TvSeriesSearchModel(){}

    //id
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    //name
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    //title
    public String getTitle(){ return original_title;}

    public void setTitle(String title) {
        original_title = title;
    }

    //vote_average
    public double getVoteAverage() {
        return VoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        VoteAverage = voteAverage;
    }

    //overview
    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    //first_air_date
    public String getFirst_air_date() {
        return First_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        First_air_date = first_air_date;
    }

    //release_date
    public String getRelease_date(){ return Release_date;}

    public String setReleaseDate(String releaseDate) {
        Release_date = releaseDate;
        return releaseDate;
    }

    //poster
    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }

    //backdrop
    public String getBackdropPath() {
        return BackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        BackdropPath = backdropPath;
    }

    //popularitas
    public String getPopularity() {
        return Popularity;
    }

    public void setPopularity(String popularity) {
        Popularity = popularity;
    }

    //media type
    public String getMedia_type() {
        return Media_type;
    }

    public void setMedia_type(String media_type) {
        Media_type = media_type;
    }
}
