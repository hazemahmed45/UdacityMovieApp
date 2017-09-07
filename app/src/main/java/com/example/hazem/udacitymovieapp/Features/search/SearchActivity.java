package com.example.hazem.udacitymovieapp.Features.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hazem.udacitymovieapp.Base.Constants.NetworkUtil;
import com.example.hazem.udacitymovieapp.Base.MovieApplication;
import com.example.hazem.udacitymovieapp.Features.main.adapter.MovieAdapter;
import com.example.hazem.udacitymovieapp.Features.main.listeners.EndlessRecyclerViewScrollListener;
import com.example.hazem.udacitymovieapp.Models.DiscoverMovieDTO;
import com.example.hazem.udacitymovieapp.Models.MovieListDTO;
import com.example.hazem.udacitymovieapp.R;
import com.example.hazem.udacitymovieapp.Utills.NavigationUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG="com.example.hazem.udacitymovieapp.Features.search.SearchActivity";
    private static final int GRID_COLUMNS=2;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private GridLayoutManager gridLayoutManager;
    private boolean adju=false;
    String searchQuery="/";
    private Map<String,String > params;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_search);
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        setIDs ();
        setParams ();
    }
    private void setIDs()
    {
        progressBar= (ProgressBar) findViewById (R.id.pb_search);
        recyclerView= (RecyclerView) findViewById (R.id.rv_searchlist);
        gridLayoutManager=new GridLayoutManager (this,GRID_COLUMNS);
        recyclerView.setLayoutManager (gridLayoutManager);
        adapter=new MovieAdapter (this,new ArrayList<DiscoverMovieDTO> ());
        recyclerView.setAdapter (adapter);
        setEvents ();
    }
    private void setEvents()
    {
        adapter.setOnItemClickListener (new MovieAdapter.ItemClickListener () {
            @Override
            public void onItemClick (View view, DiscoverMovieDTO discoverMovieDTO) {
                NavigationUtils.NavigateToMovieDetailsScreen (SearchActivity.this,discoverMovieDTO.getId ());
            }
        });
//        recyclerView.addOnScrollListener (new EndlessRecyclerViewScrollListener (gridLayoutManager) {
//            @Override
//            public void onLoadMore (int page, int totalItemsCount, RecyclerView view) {
//                Toast.makeText (SearchActivity.this, page+"", Toast.LENGTH_SHORT).show ();
//                if(adju)
//                {
//                    resetState ();
//                    adju=false;
//                }
//                params.put (NetworkUtil.APIKeys.PAGE, String.valueOf (page));
//                URL searchUrl= NetworkUtil.getSearchQueryUrl (String.valueOf (searchView.getQuery ()),params);
//                new SearchTask ().execute (searchUrl);
//            }
//        });
    }
    private void setParams()
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences (this);
        if(params==null)
        {
            params=new HashMap<> ();
        }

        boolean includeAdult=preferences.getBoolean (getString (R.string.pref_adult_content_key),false);
        params.put (NetworkUtil.APIKeys.INCLUDE_ADULT, String.valueOf (includeAdult));
        params.put (NetworkUtil.APIKeys.PAGE,"1");
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.search_screen_menu,menu);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.menu_searchscreen_search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        }
        setSearchEvent ();
        return super.onCreateOptionsMenu (menu);
    }
    private void setSearchQuery(String searchQuery)
    {
        if(TextUtils.isEmpty (searchQuery) || searchQuery==null)
        {
            return;
        }
        URL searchUrl= NetworkUtil.getSearchQueryUrl (searchQuery,params);
        new SearchTask ().execute (searchUrl);
    }
    private void setSearchEvent()
    {
        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit (String query) {
                setSearchQuery (query);

                return true;
            }

            @Override
            public boolean onQueryTextChange (String newText) {
                setSearchQuery (newText);
                return true;
            }
        });
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ())
        {
            case R.id.menu_searchscreen_search:
            {
                break;
            }
            case android.R.id.home:
            {
                onBackPressed ();
                break;
            }
            case R.id.menu_searchscreen_favorite:
            {
                NavigationUtils.NavigateToFavoritesScreen (this);
                break;
            }
        }
        return super.onOptionsItemSelected (item);
    }
    private class SearchTask extends AsyncTask<URL,Void,String>
    {
        @Override
        protected void onPreExecute () {
            progressBar.setVisibility (View.VISIBLE);

            super.onPreExecute ();
        }

        @Override
        protected String doInBackground (URL... urls) {
            URL searchUrl=urls[0];
            String jsonResponse=null;
            try {
                jsonResponse=NetworkUtil.getResponseFromHttpUrl (searchUrl);
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute (String jsonResponse) {
            super.onPostExecute (jsonResponse);
            if(jsonResponse!=null)
            {
                if(!searchQuery.equals (searchView.getQuery ().toString ()))
                {
                    Log.i (TAG,searchView.getQuery ().toString ()+" "+searchQuery.equals (searchView.getQuery ().toString ())+" "+searchQuery);
                    adapter.resetArray ();
                    searchQuery=searchView.getQuery ().toString ();
                    adju=true;
                }
                progressBar.setVisibility (View.GONE);
                Gson gson= MovieApplication.getmInstance ().getGson ();
                MovieListDTO movieListDTO=gson.fromJson (jsonResponse,new TypeToken<MovieListDTO>(){}.getType ());
                adapter.setArrayList (movieListDTO.getResults ());
            }
            else
            {
                Toast.makeText (SearchActivity.this, "null", Toast.LENGTH_SHORT).show ();
            }

        }
    }
}
