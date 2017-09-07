package com.example.hazem.udacitymovieapp.Features.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hazem.udacitymovieapp.Base.Constants.NavConstants;
import com.example.hazem.udacitymovieapp.Base.Constants.NetworkUtil;
import com.example.hazem.udacitymovieapp.Base.MovieApplication;
import com.example.hazem.udacitymovieapp.Features.favorites.FavoritesScreen;
import com.example.hazem.udacitymovieapp.Features.main.adapter.MovieAdapter;
import com.example.hazem.udacitymovieapp.Features.main.listeners.EndlessRecyclerViewScrollListener;
import com.example.hazem.udacitymovieapp.Features.moviedetails.MovieDetailsActivity;
import com.example.hazem.udacitymovieapp.Features.settings.SettingsActivity;
import com.example.hazem.udacitymovieapp.Models.DiscoverMovieDTO;
import com.example.hazem.udacitymovieapp.Models.GenresDAO;
import com.example.hazem.udacitymovieapp.Models.MovieListDTO;
import com.example.hazem.udacitymovieapp.R;
import com.example.hazem.udacitymovieapp.Utills.NavigationUtils;
import com.example.hazem.udacitymovieapp.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RequiresApi (api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity implements ConnectivityManager.OnNetworkActiveListener,SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String TAG="com.example.hazem.udacitymovieapp.Features.main.MainActivity";
    private static final int GRID_COLUMNS=2;
    private static final int LOAD_PAGE=1;
    ActivityMainBinding mBind;
    RecyclerView recyclerView;
    MovieAdapter adapter;
    ProgressBar progressBar;
    TextView connectionCheck;
    GridLayoutManager gridLayoutManager;
    Map<String,String>params;
    boolean adju=false;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mBind= DataBindingUtil.setContentView (this,R.layout.activity_main);

        PreferenceManager.getDefaultSharedPreferences (this).registerOnSharedPreferenceChangeListener (this);

        setIDs ();

        setEvents ();

        setParams ();

        loadData (LOAD_PAGE);
    }
    private void setIDs()
    {
        recyclerView=mBind.rvMovielist;
        progressBar=mBind.pbMovielist;
        connectionCheck=mBind.tvConnectionCheck;
        gridLayoutManager=new GridLayoutManager (this,GRID_COLUMNS);
        recyclerView.setLayoutManager (gridLayoutManager);
        adapter=new MovieAdapter (this,null);
        recyclerView.setAdapter (adapter);

    }
    private void setEvents()
    {
        recyclerView.addOnScrollListener (new EndlessRecyclerViewScrollListener (gridLayoutManager) {
            @Override
            public void onLoadMore (int page, int totalItemsCount, RecyclerView view) {
                if(adju)
                {
                    resetState ();
                    adju=false;
                }
                loadData (page);

            }
        });
        adapter.setOnItemClickListener (new MovieAdapter.ItemClickListener () {
            @Override
            public void onItemClick (View view, DiscoverMovieDTO d) {
                Intent intent=new Intent (MainActivity.this,MovieDetailsActivity.class);
                intent.putExtra (NavConstants.MOVIE_ID,d.getId ());
                startActivity (intent);
            }
        });
    }
    private void setParams()
    {
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences (this);
        if(params==null)
        {
            params=new HashMap<> ();
        }
        String genre= String.valueOf (preferences.getString (getString (R.string.pref_genre_list_key), getString (R.string.genre_action)));
        Log.i (TAG,genre);
        params.put (NetworkUtil.APIKeys.DISCOVER_WITH_GENRES, String.valueOf (GenresDAO.getGenreIndex (genre)));
        params.put (NetworkUtil.APIKeys.INCLUDE_ADULT, String.valueOf (preferences.getBoolean (getString (R.string.pref_adult_content_key), MovieApplication.getmInstance ().getResources ().getBoolean (R.bool.adult_trigger_bool))));
        String sortBy=preferences.getString (getString (R.string.pref_sortby_key),getString (R.string.sortby_popularity));
        Log.i (TAG,sortBy);
        params.put (NetworkUtil.APIKeys.DISCOVER_SORT_BY,sortBy);

    }
    private boolean checkConnectivity()
    {
        ConnectivityManager cm= (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
    private void setConnectivityCheck()
    {
        if(connectionCheck.isShown ())
        {
            connectionCheck.setVisibility (View.GONE);
        }
        else
        {
            connectionCheck.setVisibility (View.VISIBLE);
        }
    }

    private void loadData(int page)
    {
        URL queryUrl = NetworkUtil.getDiscoverUrlPagining (page, params);
        Log.i (TAG, queryUrl.toString ());
        new MovieTask ().execute (queryUrl);
    }
    @Override
    public void onNetworkActive () {
        Toast.makeText (this, "Active", Toast.LENGTH_SHORT).show ();
    }

    @Override
    public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key)
    {

        if(key.equals (getString (R.string.pref_adult_content_key)))
        {
            Log.i (TAG,String.valueOf (
                    sharedPreferences.getBoolean (
                            key,
                            MovieApplication.getmInstance ().getResources ().getBoolean (R.bool.adult_trigger_bool))
            ));

            params.put (NetworkUtil.APIKeys.INCLUDE_ADULT,
                    String.valueOf (
                            sharedPreferences.getBoolean (
                                    key,
                                    MovieApplication.getmInstance ().getResources ().getBoolean (R.bool.adult_trigger_bool))
                    )
            );
        }
        else if(key.equals (getString (R.string.pref_genre_list_key)))
        {
            Log.i (TAG,sharedPreferences.getString (key,null));
            params.put (
                    NetworkUtil.APIKeys.DISCOVER_WITH_GENRES,
                    String.valueOf (GenresDAO.getGenreIndex (
                            sharedPreferences.getString (
                                    getString (R.string.pref_genre_list_key),
                                    getString (R.string.pref_genre_list_key
                                    )
                            )
                    )
                    )
            );
        }
        else if(key.equals (getString (R.string.pref_sortby_key)))
        {
            params.put (NetworkUtil.APIKeys.DISCOVER_SORT_BY,sharedPreferences.getString (key,key));
        }
        adju=true;
        adapter.resetArray ();
        loadData (LOAD_PAGE);
    }
    private class MovieTask extends AsyncTask<URL,Void,String>
    {
        @Override
        protected void onPreExecute () {
            super.onPreExecute ();

            setLoader ();
        }

        @Override
        protected String doInBackground (URL... urls) {

            URL url=urls[0];
            Log.i (TAG,urls[0].toString ());

            String jsonObjectResult=null;
            try {
                jsonObjectResult=NetworkUtil.getResponseFromHttpUrl (url);
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return jsonObjectResult;
        }

        @Override
        protected void onPostExecute (String response) {
            if(response!=null)
            {
                progressBar.setVisibility (View.GONE);
                Gson gson=new Gson ();
                MovieListDTO movieListDTO =gson.fromJson (response,new TypeToken<MovieListDTO> (){}.getType ());
                adapter.setArrayList (movieListDTO.getResults ());

            }
        }
    }

    @Override
    protected void onResume () {
        super.onResume ();
        if (!checkConnectivity ())
        {
            setConnectivityCheck ();
        }

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.main_screen_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ())
        {
            case R.id.menu_mainscreen_favorite:
            {
                NavigationUtils.NavigateToFavoritesScreen (this);
                break;
            }
            case R.id.menu_mainscreen_search:
            {
                NavigationUtils.NavigateToSearchScreen (this);
                break;
            }
            case R.id.menu_mainscreen_settings:
            {
                NavigationUtils.NavigateToSettingsScreen (this);
                break;
            }
        }
        return super.onOptionsItemSelected (item);
    }

    @Override
    protected void onDestroy () {
        PreferenceManager.getDefaultSharedPreferences (this).unregisterOnSharedPreferenceChangeListener (this);
        super.onDestroy ();
    }
}
