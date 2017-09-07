package com.example.hazem.udacitymovieapp.Features.moviedetails;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hazem.udacitymovieapp.Base.Constants.NavConstants;
import com.example.hazem.udacitymovieapp.Base.Constants.NetworkUtil;
import com.example.hazem.udacitymovieapp.Base.MovieApplication;
import com.example.hazem.udacitymovieapp.Features.DB.MovieContract;
import com.example.hazem.udacitymovieapp.Features.moviedetails.adapter.MovieTrailerAdapter;
import com.example.hazem.udacitymovieapp.Models.DiscoverMovieDTO;
import com.example.hazem.udacitymovieapp.Models.SingleMovie;
import com.example.hazem.udacitymovieapp.Models.Video;
import com.example.hazem.udacitymovieapp.Models.VideoList;
import com.example.hazem.udacitymovieapp.R;
import com.example.hazem.udacitymovieapp.databinding.ActivityMovieDetailsBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG="com.example.hazem.udacitymovieapp.Features.moviedetails.MovieDetailsActivity";
    ActivityMovieDetailsBinding mBind;
    RecyclerView recyclerView;
    TextView movieName,movieYear,movieRate,movieDesc,movieDuration;
    ImageView moviePoster;
    ProgressBar progressBar;
    MovieTrailerAdapter adapter;
    Button addFavorite;
    SingleMovie singleMovie;
    private long id;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mBind= DataBindingUtil.setContentView (this,R.layout.activity_movie_details);
        setIDs ();
        if(getIntent ()!=null)
        {
            id= getIntent ().getIntExtra (NavConstants.MOVIE_ID,0);
            URL movieUrl= NetworkUtil.getMovieUrl (id+"");
            URL trailerUrl=NetworkUtil.getVideosURl (id+"");
            new SingleMovieTask ().execute (movieUrl);
            new MovieTrailerTask ().execute (trailerUrl);
            Log.i (TAG,trailerUrl.toString ());
        }
    }
    private void setLoader()
    {
        if(progressBar.isShown ())
        {
            progressBar.setVisibility (View.GONE);
        }
        else
        {
            progressBar.setVisibility (View.VISIBLE);
        }
    }
    private void setIDs()
    {
        movieName=mBind.tvMovieName;
        movieDesc=mBind.tvMovieDesc;
        movieYear=mBind.tvMovieYear;
        movieRate=mBind.tvMovieRate;
        movieDuration=mBind.tvMovieDuration;
        moviePoster=mBind.ivMoviePoster;
        recyclerView=mBind.rvMovieTrailer;
        progressBar=mBind.pbMovieTrailer;
        addFavorite=mBind.btnFavorite;
        recyclerView.setLayoutManager (new LinearLayoutManager (this,LinearLayoutManager.VERTICAL,false));
        adapter=new MovieTrailerAdapter (new ArrayList<Video> (0));
        recyclerView.setAdapter (adapter);
        adapter.setItemClickListener (new MovieTrailerAdapter.ItemClickListener () {
            @Override
            public void onItemClick (View view, Video video) {
                Intent intent=new Intent (Intent.ACTION_VIEW);
                Uri videoUrl=NetworkUtil.getYoutubeVideoUrl (video.getKey ());

                intent.setData (videoUrl);
                startActivity (intent);
            }
        });

    }


    public boolean checkForMovie(long ID)
    {
        Uri uri= ContentUris.withAppendedId (MovieContract.FavoriteEntry.CONTENT_URI,ID);
        Cursor cursor= this.getContentResolver ().query (uri, null, null, null, null);
        if(cursor.getCount ()<=0)
        {
            cursor.close ();
            return false;
        }
        return true;
    }
    private Uri addToFavorite(SingleMovie singleMovie)
    {
        ContentValues values=new ContentValues ();
        values.put (MovieContract.FavoriteEntry._ID,singleMovie.getId ());
        values.put (MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE,singleMovie.getTitle ());
        values.put (MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER,singleMovie.getPosterPath ());
        values.put (MovieContract.FavoriteEntry.COLUMN_MOVIE_DATE,singleMovie.getReleaseDate ());
        values.put (MovieContract.FavoriteEntry.COLUMN_MOVIE_DURATION,singleMovie.getRuntime ());
        values.put (MovieContract.FavoriteEntry.COLUMN_MOVIE_RATE,singleMovie.getVoteAverage ());
        values.put (MovieContract.FavoriteEntry.COLUMN_MOVIE_TAG,singleMovie.getTagline ());
        return this.getContentResolver ().insert (MovieContract.FavoriteEntry.CONTENT_URI,values);
    }
    private int deleteFromFavorite(long ID)
    {
        Uri uri= ContentUris.withAppendedId (MovieContract.FavoriteEntry.CONTENT_URI,ID);
        return getContentResolver ().delete (uri,null,null);
    }
    class SingleMovieTask extends AsyncTask<URL,Void,String>
    {
        @Override
        protected void onPreExecute () {
            setLoader ();
            super.onPreExecute ();
        }


        @Override
        protected String doInBackground (URL... urls) {
            URL requestUrl=urls[0];
            String jsonResponse=null;

            try {
                jsonResponse=NetworkUtil.getResponseFromHttpUrl (requestUrl);
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return jsonResponse;
        }
        @Override
        protected void onPostExecute (String response) {
            super.onPostExecute (response);
            if(response!=null)
            {
                Gson gson= MovieApplication.getmInstance ().getGson ();
                singleMovie=gson.fromJson (response,new TypeToken<SingleMovie>(){}.getType ());
                final SingleMovie movie=singleMovie;
                Picasso.with (MovieDetailsActivity.this)
                        .load (NetworkUtil.getSmallPosterURL (movie.getPosterPath ()))
                        .placeholder (R.mipmap.ic_launcher)
                        .into (moviePoster);
                movieDuration.setText (movie.getRuntime ()+" min");
                movieName.setText (movie.getTitle ());
                movieRate.setText (movie.getVoteAverage ()+"/10");
                String[] date=movie.getReleaseDate ().split ("-");
                String year=date[0];
                String month=date[1];
                String day=date[2];

                movieYear.setText (year);
                movieDesc.setText (movie.getOverview ());
                movieName.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View view) {
                        if(movie.getHomepage ()!=null)
                        {
                            Intent intent=new Intent (Intent.ACTION_VIEW);
                            Uri homePage=Uri.parse (movie.getHomepage ());
                            intent.setData (homePage);
                            startActivity (intent);
                        }
                    }
                });
                addFavorite.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View view) {
                        if(singleMovie!=null)
                        {
                            if(!checkForMovie(movie.getId ()))
                            {
                                Uri uri=addToFavorite (movie);
                                if(uri==null)
                                {
                                    Toast.makeText (MovieDetailsActivity.this, "Insert was unsuccessfull", Toast.LENGTH_SHORT).show ();
                                }
                                else
                                    Toast.makeText (MovieDetailsActivity.this, "Insert was successfull", Toast.LENGTH_SHORT).show ();
//                            Log.i (TAG,uri.toString ());

                                addFavorite.setText (MovieApplication.getmInstance ().getString (R.string.remove_favorite_button));
                            }
                            else
                            {
                                int deleteRows=deleteFromFavorite (movie.getId ());
                                if(deleteRows>0)
                                {
                                    Toast.makeText (MovieDetailsActivity.this, "Delete was successfull", Toast.LENGTH_SHORT).show ();
                                }
                                else
                                    Toast.makeText (MovieDetailsActivity.this, "Delete was unsuccessfull", Toast.LENGTH_SHORT).show ();
//                            Log.i (TAG,uri.toString ());
                                addFavorite.setText (MovieApplication.getmInstance ().getString (R.string.add_favorite_button));
                            }

                        }
                    }
                });
                if(checkForMovie(movie.getId ()))
                {
                    addFavorite.setText (MovieApplication.getmInstance ().getString (R.string.remove_favorite_button));
                }
                else
                {
                    addFavorite.setText (MovieApplication.getmInstance ().getString (R.string.add_favorite_button));
                }
            }
            else
            {
                Toast.makeText (MovieDetailsActivity.this, "null", Toast.LENGTH_SHORT).show ();
            }
        }

    }
    class MovieTrailerTask extends AsyncTask<URL,Void,String>
    {

        @Override
        protected void onPreExecute () {
            super.onPreExecute ();
        }


        @Override
        protected String doInBackground (URL... urls) {
            URL requestUrl=urls[0];
            String jsonResponse=null;
            try {
                jsonResponse=NetworkUtil.getResponseFromHttpUrl (requestUrl);
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return jsonResponse;
        }
        @Override
        protected void onPostExecute (String response) {
            super.onPostExecute (response);
            if(response!=null)
            {
                setLoader ();
                Gson gson=MovieApplication.getmInstance ().getGson ();
                VideoList videoList=gson.fromJson (response,new TypeToken<VideoList> (){}.getType ());
                adapter.setVideos (videoList.getResults ());
            }

        }

    }
}
