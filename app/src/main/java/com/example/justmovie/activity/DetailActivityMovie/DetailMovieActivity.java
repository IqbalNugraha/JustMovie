package com.example.justmovie.activity.DetailActivityMovie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.justmovie.R;
import com.example.justmovie.adapter.TrailerAdapter;
import com.example.justmovie.api.ListAPI;
import com.example.justmovie.model.ModelTrailer;
import com.example.justmovie.model.movie.Popular;
import com.example.justmovie.realm.RealmHelper;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailMovieActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView judulToolbar, judulCard, ratingMovie, rilisMovie, popularitasMovie, deskripsiMovie;
    ImageView imgCover,imgPhoto;
    MaterialFavoriteButton imgFavorite;
    RatingBar ratingBar;
    String namaFilm, tanggalRilis, popularitas, deskripsi, cover, poster, movieURL;
    double Rating;
    Popular popular;
    ProgressDialog progressDialog;
    int Id;
    TrailerAdapter trailerAdapter;
    List<ModelTrailer> modelTrailer = new ArrayList<>();
    RecyclerView rvTrailer;
    RealmHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        toolbar = findViewById(R.id.toolbarDetail);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang Menampilkan Trailer");

        ratingBar = findViewById(R.id.ratingBar);
        imgCover = findViewById(R.id.imgCover);
        imgPhoto = findViewById(R.id.imgDetailPhoto);
        imgFavorite = findViewById(R.id.movieFav);
        judulToolbar = findViewById(R.id.judulMovie);
        judulCard = findViewById(R.id.movieName);
        ratingMovie = findViewById(R.id.rateMovie);
        rilisMovie = findViewById(R.id.movieRilis);
        popularitasMovie = findViewById(R.id.moviePopuler);
        deskripsiMovie = findViewById(R.id.movieDesc);
        rvTrailer = findViewById(R.id.movieTrailer);
        
        helper = new RealmHelper(this);

        popular = (Popular)getIntent().getSerializableExtra("detailMovie");
        if(popular != null){
            Id = popular.getId();
            namaFilm = popular.getTitle();
            Rating = popular.getVoteAverage();
            tanggalRilis = popular.getRelease_date();
            popularitas = popular.getPopularity();
            deskripsi = popular.getOverview();
            cover = popular.getBackdropPath();
            poster = popular.getPoster_path();
            movieURL = ListAPI.URLFILM + "" + Id;

            judulToolbar.setText(namaFilm);
            judulCard.setText(namaFilm);
            ratingMovie.setText(Rating + "/10");
            rilisMovie.setText(tanggalRilis);
            popularitasMovie.setText(popularitas);
            deskripsiMovie.setText(deskripsi);
            judulToolbar.setSelected(true);
            judulCard.setSelected(true);

            float newValue = (float)Rating;
            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float) 0.5);
            ratingBar.setRating(newValue / 2);

            Glide.with(this)
                    .load(ListAPI.IMAGEBASEURL+cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCover);

            Glide.with(this)
                    .load(ListAPI.IMAGEBASEURL+poster)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPhoto);

            rvTrailer.setHasFixedSize(true);
            rvTrailer.setLayoutManager(new LinearLayoutManager(this));

            getTrailer();
        }

        imgFavorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            Id = popular.getId();
                            namaFilm = popular.getTitle();
                            Rating = popular.getVoteAverage();
                            deskripsi = popular.getOverview();
                            tanggalRilis = popular.getRelease_date();
                            poster = popular.getPoster_path();
                            cover = popular.getBackdropPath();
                            popularitas = popular.getPopularity();
                            helper.addFavoriteMovie(Id, namaFilm, Rating, deskripsi, tanggalRilis, poster, cover, popularitas);
                            Snackbar.make(buttonView, popular.getTitle() + " Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            helper.deleteFavoriteMovie(popular.getId());
                            Snackbar.make(buttonView, popular.getTitle() + " Removed from Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }
        );


    }

    private void getTrailer() {
        progressDialog.show();
        AndroidNetworking.get(ListAPI.Base_URL + ListAPI.MOVIE_VIDEO + ListAPI.APIKEY + ListAPI.Language)
                .addPathParameter("id",String.valueOf(Id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelTrailer data = new ModelTrailer();
                                data.setKey(jsonObject.getString("key"));
                                data.setType(jsonObject.getString("type"));
                                modelTrailer.add(data);
                                showTrailer();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void showTrailer() {
        trailerAdapter = new TrailerAdapter(DetailMovieActivity.this, modelTrailer);
        rvTrailer.setAdapter(trailerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}