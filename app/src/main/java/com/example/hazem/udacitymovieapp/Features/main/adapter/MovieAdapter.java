package com.example.hazem.udacitymovieapp.Features.main.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hazem.udacitymovieapp.Base.Constants.NetworkUtil;
import com.example.hazem.udacitymovieapp.Models.DiscoverMovieDTO;
import com.example.hazem.udacitymovieapp.R;
import com.example.hazem.udacitymovieapp.databinding.ItemMovieBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hazem on 9/5/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder>{

    private Context context;
    private ItemClickListener itemClickListener;
    private ArrayList<DiscoverMovieDTO> discoverMovieDTOs;
    public MovieAdapter (Context context, ArrayList<DiscoverMovieDTO> discoverMovieDTOs)
    {
        this.context=context;
        if (discoverMovieDTOs ==null)
            this.discoverMovieDTOs =new ArrayList<> (0);
        else
            this.discoverMovieDTOs = discoverMovieDTOs;
    }

    @Override
    public MovieHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view= LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_movie,parent,false);
        MovieHolder holder=new MovieHolder (view);
        view.setTag (holder);
        return new MovieHolder (view);
    }

    @Override
    public void onBindViewHolder (MovieHolder holder, int position) {
        holder.setData (position);
    }

    public void setArrayList(ArrayList<DiscoverMovieDTO> discoverMovyDTOs)
    {
        this.discoverMovieDTOs.addAll (discoverMovyDTOs);
        notifyDataSetChanged ();
    }
    @Override
    public int getItemCount () {
        return discoverMovieDTOs.size ();
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ItemMovieBinding mMovieBinding;
        ImageView poster;
        public MovieHolder (View itemView) {
            super (itemView);
            itemView.setOnClickListener (this);
            mMovieBinding=DataBindingUtil.bind (itemView);
            poster=mMovieBinding.ivMoviePoster;
        }
        private void setData(int position)
        {
            String posterUrl=NetworkUtil.getLargePosterURL (discoverMovieDTOs.get (position).getPosterPath ());
            Picasso.with (context).load (posterUrl).placeholder (R.mipmap.ic_launcher).into (poster);
        }

        @Override
        public void onClick (View view) {
            Log.i ("HERE","GERE");
            if(itemClickListener!=null)
            {

                MovieAdapter.this.itemClickListener.onItemClick (view, discoverMovieDTOs.get (getAdapterPosition ()));
            }
        }
    }
    public void resetArray()
    {
        discoverMovieDTOs=new ArrayList<> ();
        notifyDataSetChanged ();
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }
    public interface ItemClickListener
    {
        public void onItemClick(View view,DiscoverMovieDTO discoverMovieDTO);
    }
}
