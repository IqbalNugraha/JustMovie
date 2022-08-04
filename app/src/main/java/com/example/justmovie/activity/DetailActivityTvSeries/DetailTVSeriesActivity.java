package com.example.justmovie.activity.DetailActivityTvSeries;

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
import com.example.justmovie.model.TvSeries.TvSeriesModel;
import com.example.justmovie.realm.RealmHelper;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailTVSeriesActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView judulToolbar, judulCard, ratingTvSeries, rilisTvSeries, popularitasTvSeries, deskripsiTvSeries;
    ImageView imgCover,imgPhoto;
    MaterialFavoriteButton imgFavorite;
    RatingBar ratingBar;
    String namaTvSeries, tanggalRilis, popularitas, deskripsi, cover, poster, TvSeriesURL;
    double Rating;
    TvSeriesModel tvSeriesModel;
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
        ratingTvSeries = findViewById(R.id.rateMovie);
        rilisTvSeries = findViewById(R.id.movieRilis);
        popularitasTvSeries = findViewById(R.id.moviePopuler);
        deskripsiTvSeries = findViewById(R.id.movieDesc);
        rvTrailer = findViewById(R.id.movieTrailer);

        helper = new RealmHelper(this);

        tvSeriesModel = (TvSeriesModel) getIntent().getSerializableExtra("detail_tvSeries");
        if(tvSeriesModel != null){
            Id = tvSeriesModel.getId();
            namaTvSeries = tvSeriesModel.getName();
            Rating = tvSeriesModel.getVoteAverage();
            tanggalRilis = tvSeriesModel.getReleaseDate();
            popularitas = tvSeriesModel.getPopularity();
            deskripsi = tvSeriesModel.getOverview();
            cover = tvSeriesModel.getBackdropPath();
            poster = tvSeriesModel.getPosterPath();
            TvSeriesURL = ListAPI.URLFILM + "" + Id;

            judulToolbar.setText(namaTvSeries);
            judulCard.setText(namaTvSeries);
            ratingTvSeries.setText(Rating + "/10");
            rilisTvSeries.setText(tanggalRilis);
            popularitasTvSeries.setText(popularitas);
            deskripsiTvSeries.setText(deskripsi);
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

        imgFavorite.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite){
                    Id = tvSeriesModel.getId();
                    namaTvSeries = tvSeriesModel.getName();
                    Rating = tvSeriesModel.getVoteAverage();
                    deskripsi = tvSeriesModel.getOverview();
                    tanggalRilis = tvSeriesModel.getReleaseDate();
                    poster = tvSeriesModel.getPosterPath();
                    cover = tvSeriesModel.getBackdropPath();
                    popularitas = tvSeriesModel.getPopularity();
                    helper.addFavoriteTV(Id, namaTvSeries, Rating, deskripsi, tanggalRilis, poster, cover, popularitas);
                    Snackbar.make(buttonView, tvSeriesModel.getName()+" Added to Favorite ", Snackbar.LENGTH_SHORT).show();
                }else {
                    helper.deleteFavoriteTV(tvSeriesModel.getId());
                    Snackbar.make(buttonView, tvSeriesModel.getName() + " Removed from Favorite",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void getTrailer() {
        progressDialog.show();
        AndroidNetworking.get(ListAPI.Base_URL + ListAPI.TV_VIDEO + ListAPI.APIKEY + ListAPI.Language)
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
        trailerAdapter = new TrailerAdapter(DetailTVSeriesActivity.this, modelTrailer);
        rvTrailer.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();
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