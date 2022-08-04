package com.example.justmovie.api;

public class ListAPI {

    //movies
    public static String Base_URL = "https://api.themoviedb.org/3/";
    public static String SEARCH_MOVIE = "search/movie?api_key=371ca9784e453f7eedfdddadd4b5d42b&language=en-US&page=1&query=";
    public static String IMAGEBASEURL = "https://image.tmdb.org/t/p/w500/";
    public static String URLFILM = "https://www.themoviedb.org/movie/";
    public static String MOVIE_VIDEO = "movie/{id}/videos?";
    public static String APIKEY = "api_key=371ca9784e453f7eedfdddadd4b5d42b";
    public static String Language = "&language=en-US";
    public static String Now_playing = "movie/now_playing?";
    public static String Top_Rated = "movie/top_rated?";
    public static String upComing = "movie/upcoming?";
    public static String NOTIF_DATE = "&primary_release_date.gte=";
    public static String REALESE_DATE = "&primary_release_date.lte=";
    public static String MOVIE_POPULAR = "discover/movie?";
    public static String Trending = "trending/all/day?";

    //tv_series
    public static String TV_Popular = "tv/popular?";
    public static String TV_OnAir = "tv/on_the_air?";
    public static String TV_TopRated = "tv/top_rated?";
    public static String Search_TvSeries = "search/tv?";
    public static String Query = "&query=";
    public static String TV_VIDEO = "tv/{id}/videos?";
    public static String TV_Lastest = "tv/latest?";

    //multi
    public static String Search_movie_tv = "search/multi?";

}
