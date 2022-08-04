package com.example.justmovie.adapter.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.justmovie.R;
import com.example.justmovie.api.ListAPI;
import com.example.justmovie.model.movie.Popular;

import java.util.List;

public class MoviesAdapterHorizontal extends RecyclerView.Adapter<MoviesAdapterHorizontal.MyHolder> {

    List<Popular>tlist;
    MoviesAdapterHorizontal.onSelectData onSelectData;
    Context context;
    private String BaseImg = "https://image.tmdb.org/t/p/w500/";

    public interface onSelectData{
        void onSelected (Popular popular);
    }

    public MoviesAdapterHorizontal(Context context, List<Popular> tlist, MoviesAdapterHorizontal.onSelectData xonSelectData){
        this.tlist = tlist;
        this.context = context;
        this.onSelectData = xonSelectData;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_movie_horizontal,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Popular popular = tlist.get(position);

        Glide
                .with(context)
                .load(ListAPI.IMAGEBASEURL + popular.Poster_path)
                .apply(new RequestOptions()
                .placeholder(R.drawable.ic_image)
                .transform(new RoundedCorners(16)))
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectData.onSelected(popular);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tlist.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPhotoHorizontal);
        }
    }
}
