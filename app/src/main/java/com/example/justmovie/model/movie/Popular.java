package com.example.justmovie.model.movie;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class Popular extends RealmObject implements Serializable {
    private int Id;
    public String overview;
    public String Poster_path;
    public String Release_date;
    public String Title;
    private double VoteAverage;
    private String Popularity;
    private String BackdropPath;

    public Popular(){

    }

    public int getId() {
        return Id;
    }
    public double getVoteAverage() {
        return VoteAverage;
    }
    public String getTitle(){ return Title;}
    public String getOverview(){ return overview;}
    public String getPoster_path(){ return Poster_path;}
    public String getRelease_date(){ return Release_date;}
    public String getPopularity() {
        return Popularity;
    }
    public String getBackdropPath() {
        return BackdropPath;
    }

    public void setId(int id) {
        Id = id;
    }
    public void setTitle(String title) {
        Title = title;
    }
    public void setPoster_path(String posterPath){Poster_path = posterPath;}
    public void setOverview(String desc){overview = desc;}
    public void setBackdropPath(String backdropPath) {
        BackdropPath = backdropPath;
    }
    public void setPopularity(String popularity) {
        Popularity = popularity;
    }
    public void setVoteAverage(double voteAverage) {
        VoteAverage = voteAverage;
    }
    public void setReleaseDate(String releaseDate) {
        Release_date = releaseDate;
    }


}
