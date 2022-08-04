package com.example.justmovie.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.justmovie.R;
import com.example.justmovie.activity.DetailActivityMovie.DetailMovieActivity;
import com.example.justmovie.adapter.movies.MoviesAdapterHorizontal;
import com.example.justmovie.adapter.movies.NowPlayingAdapter;
import com.example.justmovie.adapter.movies.TopRatedAdapter;
import com.example.justmovie.adapter.movies.UpComingAdapter;
import com.example.justmovie.api.ListAPI;
import com.example.justmovie.model.movie.Popular;
import com.example.justmovie.model.SearchModel;
import com.ramotion.cardslider.CardSliderLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesFragment extends Fragment implements MoviesAdapterHorizontal.onSelectData, NowPlayingAdapter.onSelectData, TopRatedAdapter.onSelectData, UpComingAdapter.onSelectData, AdapterView.OnItemSelectedListener{
    private NowPlayingAdapter nowPlayingAdapter;
    private MoviesAdapterHorizontal moviesAdapterHorizontal;
    private TopRatedAdapter topRatedAdapter;
    private UpComingAdapter upComingAdapter;
    private List<Popular> nowPlaying_list = new ArrayList<>();
    private List<Popular> horizontal_list = new ArrayList<>();
    private List<Popular> topRated_list = new ArrayList<>();
    private List<SearchModel>sList = new ArrayList<>();
    private List<Popular> upComing_list = new ArrayList<>();
    private ProgressDialog progressDialog;
    private SearchView searchView;
    Spinner spinner;

    @BindView(R.id.rvMoviesVertical)
    RecyclerView recyclerView;

    @BindView(R.id.rvMoviesHorizontal)
    RecyclerView recyclerViewHorizontal;

    String[] kategoriFilm = {"Choose Your Category Film","Now Playing", "Top Rated", "UpComing"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        ButterKnife.bind(this, view);

        moviesAdapterHorizontal = new MoviesAdapterHorizontal(getActivity(), horizontal_list, this);
        RecyclerView.LayoutManager layoutManagerHorizontal = new CardSliderLayoutManager(getActivity());
        recyclerViewHorizontal.setLayoutManager(layoutManagerHorizontal);
        recyclerViewHorizontal.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHorizontal.setAdapter(moviesAdapterHorizontal);

        nowPlayingAdapter = new NowPlayingAdapter(getActivity(), nowPlaying_list,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(nowPlayingAdapter);

        topRatedAdapter = new TopRatedAdapter(getActivity(), topRated_list, this);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(topRatedAdapter);

        upComingAdapter = new UpComingAdapter(getActivity(), upComing_list, this);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(upComingAdapter);

        searchView = view.findViewById(R.id.searchMovies);
        searchView.setQueryHint("Search a Film");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerView.setVisibility(View.VISIBLE);
                setSearchMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    loadMoviesVertical();
                }
                return false;
            }
        });
        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate != null){
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }

        spinner = (Spinner) view.findViewById(R.id.pilihFilm);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_fragment, kategoriFilm);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_fragment);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);


        loadMoviesHorizontal();
        loadMoviesVertical();
        loadMoviesTopRatedVertical();
        loadMoviesUpComing();

        return view;
    }

    private void setSearchMovie(String query) {

        progressDialog.show();
        AndroidNetworking.get(ListAPI.Base_URL+ListAPI.SEARCH_MOVIE+query)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            nowPlaying_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Popular popular = new Popular();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = formatter.format(dateFormat.parse(jsonObject.getString("release_date")));

                                popular.setId(jsonObject.getInt("id"));
                                popular.setTitle(jsonObject.getString("title"));
                                popular.setVoteAverage(jsonObject.getDouble("vote_average"));
                                popular.setOverview(jsonObject.getString("overview"));
                                popular.setReleaseDate(date);
                                popular.setPoster_path(jsonObject.getString("poster_path"));
                                popular.setBackdropPath(jsonObject.getString("backdrop_path"));
                                popular.setPopularity(jsonObject.getString("popularity"));
                                nowPlaying_list.add(popular);
                                showMovie();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void loadMoviesHorizontal() {
        AndroidNetworking.get(ListAPI.Base_URL + ListAPI.MOVIE_POPULAR + ListAPI.APIKEY + ListAPI.Language)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Popular popular = new Popular();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = formatter.format(dateFormat.parse(jsonObject.getString("release_date")));

                                popular.setId(jsonObject.getInt("id"));
                                popular.setTitle(jsonObject.getString("original_title"));
                                popular.setVoteAverage(jsonObject.getDouble("vote_average"));
                                popular.setOverview(jsonObject.getString("overview"));
                                popular.setReleaseDate(date);
                                popular.setPoster_path(jsonObject.getString("poster_path"));
                                popular.setBackdropPath(jsonObject.getString("backdrop_path"));
                                popular.setPopularity(jsonObject.getString("popularity"));
                                horizontal_list.add(popular);
                                showMovieHorizontal();
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void loadMoviesVertical() {
        progressDialog.show();
        AndroidNetworking.get(ListAPI.Base_URL + ListAPI.Now_playing + ListAPI.APIKEY + ListAPI.Language)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            nowPlaying_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Popular popular = new Popular();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = formatter.format(dateFormat.parse(jsonObject.getString("release_date")));

                                popular.setId(jsonObject.getInt("id"));
                                popular.setTitle(jsonObject.getString("title"));
                                popular.setVoteAverage(jsonObject.getDouble("vote_average"));
                                popular.setOverview(jsonObject.getString("overview"));
                                popular.setReleaseDate(date);
                                popular.setPoster_path(jsonObject.getString("poster_path"));
                                popular.setBackdropPath(jsonObject.getString("backdrop_path"));
                                popular.setPopularity(jsonObject.getString("popularity"));
                                nowPlaying_list.add(popular);
                                showMovie();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadMoviesTopRatedVertical() {
        progressDialog.show();
        AndroidNetworking.get(ListAPI.Base_URL + ListAPI.Top_Rated + ListAPI.APIKEY + ListAPI.Language)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            topRated_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Popular topRated = new Popular();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = formatter.format(dateFormat.parse(jsonObject.getString("release_date")));

                                topRated.setId(jsonObject.getInt("id"));
                                topRated.setTitle(jsonObject.getString("title"));
                                topRated.setVoteAverage(jsonObject.getDouble("vote_average"));
                                topRated.setOverview(jsonObject.getString("overview"));
                                topRated.setReleaseDate(date);
                                topRated.setPoster_path(jsonObject.getString("poster_path"));
                                topRated.setBackdropPath(jsonObject.getString("backdrop_path"));
                                topRated.setPopularity(jsonObject.getString("popularity"));
                                topRated_list.add(topRated);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadMoviesUpComing() {
        progressDialog.show();
        AndroidNetworking.get(ListAPI.Base_URL + ListAPI.upComing + ListAPI.APIKEY + ListAPI.Language)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            upComing_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Popular upComing = new Popular();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = formatter.format(dateFormat.parse(jsonObject.getString("release_date")));

                                upComing.setId(jsonObject.getInt("id"));
                                upComing.setTitle(jsonObject.getString("title"));
                                upComing.setVoteAverage(jsonObject.getDouble("vote_average"));
                                upComing.setOverview(jsonObject.getString("overview"));
                                upComing.setReleaseDate(date);
                                upComing.setPoster_path(jsonObject.getString("poster_path"));
                                upComing.setBackdropPath(jsonObject.getString("backdrop_path"));
                                upComing.setPopularity(jsonObject.getString("popularity"));
                                upComing_list.add(upComing);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showMovieHorizontal() {
        moviesAdapterHorizontal = new MoviesAdapterHorizontal(getActivity(), horizontal_list, this);
        recyclerViewHorizontal.setAdapter(moviesAdapterHorizontal);
        moviesAdapterHorizontal.notifyDataSetChanged();
    }

    private void showMovieTopRated() {
        topRatedAdapter = new TopRatedAdapter(getActivity(), topRated_list, this);
        recyclerView.setAdapter(topRatedAdapter);
        topRatedAdapter.notifyDataSetChanged();
    }

    private void showMovie() {
        nowPlayingAdapter = new NowPlayingAdapter(getActivity(), nowPlaying_list,this);
        recyclerView.setAdapter(nowPlayingAdapter);
        nowPlayingAdapter.notifyDataSetChanged();
    }

    private void showMovieUpComing() {
        upComingAdapter = new UpComingAdapter(getActivity(), upComing_list,this);
        recyclerView.setAdapter(upComingAdapter);
        upComingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelected(Popular popular) {
        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra("detailMovie", popular);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                Toast.makeText(getActivity().getApplicationContext(),
                                kategoriFilm[i],
                                Toast.LENGTH_SHORT)
                        .show();
                recyclerView.setVisibility(View.GONE);
                break;
            case 1:
                recyclerView.setVisibility(View.VISIBLE);
                showMovie();
                break;
            case 2:
                recyclerView.setVisibility(View.VISIBLE);
                showMovieTopRated();
                break;
            case 3:
                recyclerView.setVisibility(View.VISIBLE);
                showMovieUpComing();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getActivity().getApplicationContext(),
                        "Please Choose a Category Film",
                        Toast.LENGTH_SHORT)
                .show();
    }
}