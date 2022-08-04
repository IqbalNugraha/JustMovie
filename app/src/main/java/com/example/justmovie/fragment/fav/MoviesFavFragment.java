package com.example.justmovie.fragment.fav;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.justmovie.R;
import com.example.justmovie.activity.DetailActivityMovie.DetailMovieActivity;
import com.example.justmovie.adapter.movies.NowPlayingAdapter;
import com.example.justmovie.model.movie.Popular;
import com.example.justmovie.realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;


public class MoviesFavFragment extends Fragment implements NowPlayingAdapter.onSelectData {

    private RecyclerView rvMovieFav;
    private List<Popular> modelMovie = new ArrayList<>();
    private RealmHelper helper;
    private TextView txtNoData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movies_fav, container, false);

        helper = new RealmHelper(getActivity());

        txtNoData = view.findViewById(R.id.tvNotFound);
        rvMovieFav = view.findViewById(R.id.rvMovieFav);
        rvMovieFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovieFav.setAdapter(new NowPlayingAdapter(getActivity(), modelMovie, this));
        rvMovieFav.setHasFixedSize(true);

        setData();

        return view;
    }

    private void setData() {
        modelMovie = helper.showFavoriteMovie();
        if (modelMovie.size() == 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvMovieFav.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvMovieFav.setVisibility(View.VISIBLE);
            rvMovieFav.setAdapter(new NowPlayingAdapter(getActivity(), modelMovie, this));
        }
    }

    @Override
    public void onSelected(Popular popular) {
        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra("detailMovie", popular);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        setData();
    }


}