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
import com.example.justmovie.activity.DetailActivityTvSeries.DetailTVSeriesActivity;
import com.example.justmovie.adapter.tv_series.TvTopRatedAdapter;
import com.example.justmovie.model.TvSeries.TvSeriesModel;
import com.example.justmovie.realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;

public class TvSeriesFavFragment extends Fragment implements TvTopRatedAdapter.onSelectData{

    private RecyclerView rvMovieFav;
    private List<TvSeriesModel> modelTV = new ArrayList<>();
    private RealmHelper helper;
    private TextView txtNoData;

    public TvSeriesFavFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies_fav, container, false);

        helper = new RealmHelper(getActivity());

        txtNoData = view.findViewById(R.id.tvNotFound);
        rvMovieFav = view.findViewById(R.id.rvMovieFav);
        rvMovieFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovieFav.setAdapter(new TvTopRatedAdapter(getActivity(), modelTV, this));
        rvMovieFav.setHasFixedSize(true);

        setData();
        return view;
    }

    private void setData() {
        modelTV = helper.showFavoriteTV();
        if (modelTV.size() == 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvMovieFav.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvMovieFav.setVisibility(View.VISIBLE);
            rvMovieFav.setAdapter(new TvTopRatedAdapter(getActivity(), modelTV, this));
        }
    }

    @Override
    public void onSelected(TvSeriesModel tvSeriesModel1) {
        Intent intent = new Intent(getActivity(), DetailTVSeriesActivity.class);
        intent.putExtra("detail_tvSeries", tvSeriesModel1);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}