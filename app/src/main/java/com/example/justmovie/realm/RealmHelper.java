package com.example.justmovie.realm;

import android.content.Context;


import com.example.justmovie.model.TvSeries.TvSeriesModel;
import com.example.justmovie.model.TvSeries.TvSeriesTopRated;
import com.example.justmovie.model.movie.Popular;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;



public class RealmHelper {

    private Context mContext;
    private Realm realm;
    private RealmResults<Popular> modelMovie;
    private RealmResults<TvSeriesModel> modelTV;

    public RealmHelper(Context mContext) {
        this.mContext = mContext;
        Realm.init(mContext);
        realm = Realm.getInstance(RealmUtility.getDefaultConfig());
    }

    public ArrayList<Popular> showFavoriteMovie() {
        ArrayList<Popular> data = new ArrayList<>();
        modelMovie = realm.where(Popular.class).findAll();

        if (modelMovie.size() > 0) {
            for (int i = 0; i < modelMovie.size(); i++) {
                Popular movie = new Popular();
                movie.setId(modelMovie.get(i).getId());
                movie.setTitle(modelMovie.get(i).getTitle());
                movie.setVoteAverage(modelMovie.get(i).getVoteAverage());
                movie.setOverview(modelMovie.get(i).getOverview());
                movie.setReleaseDate(modelMovie.get(i).getRelease_date());
                movie.setPoster_path(modelMovie.get(i).getPoster_path());
                movie.setBackdropPath(modelMovie.get(i).getBackdropPath());
                movie.setPopularity(modelMovie.get(i).getPopularity());
                data.add(movie);
            }
        }
        return data;
    }

    public ArrayList<TvSeriesModel> showFavoriteTV() {
        ArrayList<TvSeriesModel> data = new ArrayList<>();
        modelTV = realm.where(TvSeriesModel.class).findAll();

        if (modelTV.size() > 0) {
            for (int i = 0; i < modelTV.size(); i++) {
                TvSeriesModel tv = new TvSeriesModel();
                tv.setId(modelTV.get(i).getId());
                tv.setName(modelTV.get(i).getName());
                tv.setVoteAverage(modelTV.get(i).getVoteAverage());
                tv.setOverview(modelTV.get(i).getOverview());
                tv.setReleaseDate(modelTV.get(i).getReleaseDate());
                tv.setPosterPath(modelTV.get(i).getPosterPath());
                tv.setBackdropPath(modelTV.get(i).getBackdropPath());
                tv.setPopularity(modelTV.get(i).getPopularity());
                data.add(tv);
            }
        }
        return data;
    }

    public void addFavoriteMovie(int Id, String Title, double VoteAverage, String Overview,
                            String ReleaseDate, String PosterPath, String BackdropPath, String Popularity) {
        Popular movie = new Popular();
        movie.setId(Id);
        movie.setTitle(Title);
        movie.setVoteAverage(VoteAverage);
        movie.setOverview(Overview);
        movie.setReleaseDate(ReleaseDate);
        movie.setPoster_path(PosterPath);
        movie.setBackdropPath(BackdropPath);
        movie.setPopularity(Popularity);

        realm.beginTransaction();
        realm.copyToRealm(movie);
        realm.commitTransaction();

        //Toast.makeText(mContext, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
    }

    public void addFavoriteTV(int Id, String Title, double VoteAverage, String Overview,
                              String ReleaseDate, String PosterPath, String BackdropPath, String Popularity) {
        TvSeriesModel tv = new TvSeriesModel();
        tv.setId(Id);
        tv.setName(Title);
        tv.setVoteAverage(VoteAverage);
        tv.setOverview(Overview);
        tv.setReleaseDate(ReleaseDate);
        tv.setPosterPath(PosterPath);
        tv.setBackdropPath(BackdropPath);
        tv.setPopularity(Popularity);

        realm.beginTransaction();
        realm.copyToRealm(tv);
        realm.commitTransaction();

        //Toast.makeText(mContext, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
    }

    public void deleteFavoriteMovie(int id) {
        realm.beginTransaction();
        RealmResults<Popular> modelMovie = realm.where(Popular.class).findAll();
        modelMovie.deleteAllFromRealm();
        realm.commitTransaction();


        //Toast.makeText(mContext, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
    }

    public void deleteFavoriteTV(int id) {
        realm.beginTransaction();
        RealmResults<TvSeriesModel> modelTV = realm.where(TvSeriesModel.class).findAll();
        modelTV.deleteAllFromRealm();
        realm.commitTransaction();

        //Toast.makeText(mContext, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
    }

}
