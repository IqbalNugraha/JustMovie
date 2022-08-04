package com.example.justmovie.adapter.tv_series;

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
import com.example.justmovie.model.TvSeries.TvSeriesModel;

import java.util.List;

public class TvOnAirHorizontalAdapter extends RecyclerView.Adapter<TvOnAirHorizontalAdapter.MyHolder> {
    List<TvSeriesModel> onAirList ;
    Context context;
    TvOnAirHorizontalAdapter.onSelectData onSelectData;

    public interface onSelectData{
        void onSelected(TvSeriesModel tvSeriesModel);
    }

    public TvOnAirHorizontalAdapter(Context context, List<TvSeriesModel>onAirList, onSelectData onSelectData){
        this.context = context;
        this.onSelectData = onSelectData;
        this.onAirList = onAirList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_movie_horizontal,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        TvSeriesModel onAirSeries = onAirList.get(position);

        Glide
                .with(context)
                .load(ListAPI.IMAGEBASEURL+onAirSeries.getPosterPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectData.onSelected(onAirSeries);
            }
        });
    }

    @Override
    public int getItemCount() {
        return onAirList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPhotoHorizontal);
        }
    }
}
