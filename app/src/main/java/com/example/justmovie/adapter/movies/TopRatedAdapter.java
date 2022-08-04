package com.example.justmovie.adapter.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.justmovie.R;
import com.example.justmovie.api.ListAPI;
import com.example.justmovie.model.movie.Popular;

import java.util.List;

public class TopRatedAdapter extends RecyclerView.Adapter<TopRatedAdapter.MyHolder> {
    List<Popular> topList;
    TopRatedAdapter.onSelectData onSelectData;
    Context mcontext;
    private double Rating;

    public interface onSelectData {
        void onSelected(Popular topList);
    }

    public TopRatedAdapter(Context context, List<Popular>topList, TopRatedAdapter.onSelectData xonSelectData){
        this.mcontext = context;
        this.topList = topList;
        this.onSelectData = xonSelectData;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_movie_vertical,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Popular topRated = topList.get(position);

        Rating = topRated.getVoteAverage();
        float newValue = (float)Rating;
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setStepSize((float) 0.5);
        holder.ratingBar.setRating(newValue / 2);

        holder.title.setText(topRated.getTitle());
        holder.desc.setText(topRated.getOverview());
        Glide
                .with(mcontext)
                .load(ListAPI.IMAGEBASEURL +topRated.Poster_path)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                .into(holder.imgPhoto);
        holder.releaseDate.setText(topRated.getRelease_date());

        holder.cvMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectData.onSelected(topRated);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView title, desc, releaseDate;
        ImageView imgPhoto;
        CardView cvMovie;
        RatingBar ratingBar;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            desc = itemView.findViewById(R.id.tvDesc);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            releaseDate = itemView.findViewById(R.id.tvRealeseDate);
            cvMovie = itemView.findViewById(R.id.cvMovie);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
