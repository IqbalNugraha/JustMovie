package com.example.justmovie.model.TvSeries;

import java.io.Serializable;

import io.realm.RealmObject;

public class TvSeriesTopRated extends RealmObject implements Serializable {
    private int Id;
    private String Name;
    private double VoteAverage;
    private String Overview;
    private String first_air_date;
    private String PosterPath;
    private String BackdropPath;
    private String Popularity;

    public TvSeriesTopRated(){}

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getVoteAverage() {
        return VoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        VoteAverage = voteAverage;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getReleaseDate() {
        return first_air_date;
    }

    public void setReleaseDate(String releaseDate) {
        first_air_date = releaseDate;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }

    public String getBackdropPath() {
        return BackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        BackdropPath = backdropPath;
    }

    public String getPopularity() {
        return Popularity;
    }

    public void setPopularity(String popularity) {
        Popularity = popularity;
    }
}
