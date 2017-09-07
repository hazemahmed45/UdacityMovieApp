package com.example.hazem.udacitymovieapp.Features.moviedetails.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hazem.udacitymovieapp.Models.Video;
import com.example.hazem.udacitymovieapp.R;
import com.example.hazem.udacitymovieapp.databinding.ItemMovieTrailerBinding;

import java.util.ArrayList;

/**
 * Created by Hazem on 9/5/2017.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailerHolder> {
    private ArrayList<Video> videos;
    private ItemClickListener itemClickListener;
    public MovieTrailerAdapter (ArrayList<Video> videos) {
        this.videos = videos;
    }

    @Override
    public TrailerHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view= LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_movie_trailer,parent,false);
        return new TrailerHolder (view);
    }

    @Override
    public void onBindViewHolder (TrailerHolder holder, int position) {
        holder.setData (position);
    }

    @Override
    public int getItemCount () {
        return videos.size ();
    }

    public void setVideos(ArrayList<Video> videos)
    {
        this.videos=videos;
        notifyDataSetChanged ();
    }
    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ItemMovieTrailerBinding mBind;
        TextView trailerName,trailerType;
        public TrailerHolder (View itemView) {
            super (itemView);
            mBind= DataBindingUtil.bind (itemView);
            trailerName=mBind.tvTrailerName;
            trailerType=mBind.tvTrailerType;
            itemView.setOnClickListener (this);
        }
        private void setData(int pos)
        {
            String name=videos.get (pos).getName ();
            String type=videos.get (pos).getType ();
            trailerName.setText (name);
            trailerType.setText (type);
        }
        @Override
        public void onClick (View view) {
            if(itemClickListener!=null)
            {
                itemClickListener.onItemClick (view,videos.get (getAdapterPosition ()));
            }
        }
    }
    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }
    public interface ItemClickListener
    {
        public void onItemClick(View view,Video video);
    }
}
