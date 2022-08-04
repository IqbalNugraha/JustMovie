package com.example.justmovie.model;

import com.google.gson.annotations.SerializedName;

public class SearchModel {
    @SerializedName("poster_path")
    public String poster_path;
    @SerializedName("original_title")
    public String original_title;
    @SerializedName("overview")
    public String overview;

    public String getPoster_path(){return poster_path;}
    public String getOriginal_title(){return original_title;}
    public String getOverview(){return overview;}

    public void setPoster_path(String poster){poster_path = poster;}
    public void setOriginal_title(String title){original_title = title;}
    public void setOverview(String desc){overview = desc;}
}
