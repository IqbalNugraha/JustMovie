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
import com.example.justmovie.activity.DetailActivityTvSeries.DetailTVSeriesActivity;
import com.example.justmovie.adapter.tv_series.TvPopularAdapter;
import com.example.justmovie.adapter.tv_series.TvTopRatedAdapter;
import com.example.justmovie.adapter.tv_series.TvOnAirHorizontalAdapter;
import com.example.justmovie.api.ListAPI;
import com.example.justmovie.model.TvSeries.TvSeriesModel;
import com.example.justmovie.model.TvSeries.TvSeriesTopRated;
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


public class TvSeriesFragment extends Fragment implements TvOnAirHorizontalAdapter.onSelectData, TvTopRatedAdapter.onSelectData, TvPopularAdapter.onSelectData, AdapterView.OnItemSelectedListener{

    TvOnAirHorizontalAdapter tvOnAirHorizontalAdapter;
    TvTopRatedAdapter tvTopRatedAdapter;
    TvPopularAdapter tvPopularAdapter;
    List<TvSeriesModel> popular_list = new ArrayList<>();
    List<TvSeriesModel> onAir_list = new ArrayList<>();
    List<TvSeriesModel> topRated_list = new ArrayList<>();
    List<TvSeriesTopRated>toList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private SearchView searchView;
    Spinner spinner;

    @BindView(R.id.rvMoviesVertical)
    RecyclerView recyclerView;

    @BindView(R.id.rvMoviesHorizontal)
    RecyclerView recyclerViewHorizontal;

    String[] kategoriTV = {"Choose Your Category TV Series","Popular","Top Rated"};

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

        tvOnAirHorizontalAdapter = new TvOnAirHorizontalAdapter(getActivity(), onAir_list, this);
        RecyclerView.LayoutManager layoutManagerHorizontal = new CardSliderLayoutManager(getActivity());
        recyclerViewHorizontal.setLayoutManager(layoutManagerHorizontal);
        recyclerViewHorizontal.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHorizontal.setAdapter(tvOnAirHorizontalAdapter);

        tvPopularAdapter = new TvPopularAdapter(getActivity(), popular_list, this);
        RecyclerView.LayoutManager layoutManagerLastest = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManagerLastest);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tvPopularAdapter);
        
        tvTopRatedAdapter = new TvTopRatedAdapter(getActivity(), topRated_list, this);
        RecyclerView.LayoutManager layoutManagerOnAir = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManagerOnAir);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tvTopRatedAdapter);

        spinner = (Spinner) view.findViewById(R.id.pilihFilm);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_fragment, kategoriTV);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_fragment);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        searchView = view.findViewById(R.id.searchMovies);
        searchView.setQueryHint("Search a Movie");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerView.setVisibility(View.VISIBLE);
                setSearchMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                    loadTvSeriesTopRatedVertical();
                return false;
            }
        });
        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate != null){
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }

        loadTVSeriesPopular();
        loadTVSeriesHorizontal();
        loadTvSeriesTopRatedVertical();

        return view;
    }

    private void setSearchMovie(String query) {
        progressDialog.show();
        AndroidNetworking.get(ListAPI.Base_URL+ListAPI.Search_TvSeries+ListAPI.APIKEY+ListAPI.Query+query)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            popular_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                TvSeriesModel seachModel = new TvSeriesModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = formatter.format(dateFormat.parse(jsonObject.getString("first_air_date")));

                                seachModel.setId(jsonObject.getInt("id"));
                                seachModel.setName(jsonObject.getString("name"));
                                seachModel.setVoteAverage(jsonObject.getDouble("vote_average"));
                                seachModel.setOverview(jsonObject.getString("overview"));
                                seachModel.setReleaseDate(date);
                                seachModel.setPosterPath(jsonObject.getString("poster_path"));
                                seachModel.setBackdropPath(jsonObject.getString("backdrop_path"));
                                seachModel.setPopularity(jsonObject.getString("popularity"));
                                popular_list.add(seachModel);
                                showTvPopular();
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

    private void loadTVSeriesHorizontal() {
        progressDialog.show();
        AndroidNetworking.get(ListAPI.Base_URL+ListAPI.TV_OnAir+ListAPI.APIKEY+ListAPI.Language)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                TvSeriesModel onAir = new TvSeriesModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = formatter.format(dateFormat.parse(jsonObject.getString("first_air_date")));

                                onAir.setId(jsonObject.getInt("id"));
                                onAir.setName(jsonObject.getString("name"));
                                onAir.setVoteAverage(jsonObject.getDouble("vote_average"));
                                onAir.setOverview(jsonObject.getString("overview"));
                                onAir.setReleaseDate(date);
                                onAir.setPosterPath(jsonObject.getString("poster_path"));
                                onAir.setBackdropPath(jsonObject.getString("backdrop_path"));
                                onAir.setPopularity(jsonObject.getString("popularity"));
                                onAir_list.add(onAir);
                                showTvHorizontal();
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

    private void loadTVSeriesPopular() {
        progressDialog.show();
        AndroidNetworking.get(ListAPI.Base_URL+ListAPI.TV_Popular+ListAPI.APIKEY+ListAPI.Language)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        popular_list = new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                TvSeriesModel lastest = new TvSeriesModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = formatter.format(dateFormat.parse(jsonObject.getString("first_air_date")));

                                lastest.setId(jsonObject.getInt("id"));
                                lastest.setName(jsonObject.getString("name"));
                                lastest.setVoteAverage(jsonObject.getDouble("vote_average"));
                                lastest.setOverview(jsonObject.getString("overview"));
                                lastest.setReleaseDate(date);
                                lastest.setPosterPath(jsonObject.getString("poster_path"));
                                lastest.setBackdropPath(jsonObject.getString("backdrop_path"));
                                lastest.setPopularity(jsonObject.getString("popularity"));
                                popular_list.add(lastest);
                                showTvPopular();
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

    private void loadTvSeriesTopRatedVertical() {
        progressDialog.show();
        AndroidNetworking.get(ListAPI.Base_URL+ListAPI.TV_TopRated+ListAPI.APIKEY+ListAPI.Language)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        topRated_list = new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                TvSeriesModel topRated = new TvSeriesModel();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = formatter.format(dateFormat.parse(jsonObject.getString("first_air_date")));

                                topRated.setId(jsonObject.getInt("id"));
                                topRated.setName(jsonObject.getString("name"));
                                topRated.setVoteAverage(jsonObject.getDouble("vote_average"));
                                topRated.setOverview(jsonObject.getString("overview"));
                                topRated.setReleaseDate(date);
                                topRated.setPosterPath(jsonObject.getString("poster_path"));
                                topRated.setBackdropPath(jsonObject.getString("backdrop_path"));
                                topRated.setPopularity(jsonObject.getString("popularity"));
                                topRated_list.add(topRated);
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


    private void showTvPopular() {
        tvPopularAdapter = new TvPopularAdapter(getActivity(), popular_list, this);
        recyclerView.setAdapter(tvPopularAdapter);
        tvPopularAdapter.notifyDataSetChanged();
    }

    private void showTvHorizontal() {
        tvOnAirHorizontalAdapter = new TvOnAirHorizontalAdapter(getActivity(), onAir_list, this);
        recyclerViewHorizontal.setAdapter(tvOnAirHorizontalAdapter);
        tvOnAirHorizontalAdapter.notifyDataSetChanged();
    }

    private void showTvTopRated() {
        tvTopRatedAdapter = new TvTopRatedAdapter(getActivity(), topRated_list, this);
        recyclerView.setAdapter(tvTopRatedAdapter);
        tvTopRatedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                Toast.makeText(getActivity().getApplicationContext(),
                        kategoriTV[i],
                        Toast.LENGTH_SHORT)
                        .show();
                recyclerView.setVisibility(View.GONE);
                break;
            case 1:
                recyclerView.setVisibility(View.VISIBLE);
                showTvPopular();
                break;
            case 2:
                recyclerView.setVisibility(View.VISIBLE);
                showTvTopRated();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getActivity().getApplicationContext(),
                "Please Choose a Category TV Series",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onSelected(TvSeriesModel tvSeriesModel) {
        Intent intent = new Intent(getActivity(), DetailTVSeriesActivity.class);
        intent.putExtra("detail_tvSeries", tvSeriesModel);
        startActivity(intent);
    }
}