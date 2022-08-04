package com.example.justmovie.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.example.justmovie.R;
import com.example.justmovie.adapter.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;


public class FavFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav, container, false);

        ViewPager viewPager = view.findViewById(R.id.vpFavorite);
        viewPager.setAdapter(new ViewPageAdapter((getChildFragmentManager())));

        TabLayout tabLayout = view.findViewById(R.id.tabFavorite);
        tabLayout.setupWithViewPager(viewPager);

        (tabLayout.getTabAt(0)).setText("Movie Favorite");
        (tabLayout.getTabAt(1)).setText("TV Series Favorite");

        return view;
    }
}