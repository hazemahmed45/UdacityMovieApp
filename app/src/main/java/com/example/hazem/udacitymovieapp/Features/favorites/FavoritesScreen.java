package com.example.hazem.udacitymovieapp.Features.favorites;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hazem.udacitymovieapp.Base.Constants.NavConstants;
import com.example.hazem.udacitymovieapp.Features.DB.MovieContract;
import com.example.hazem.udacitymovieapp.Features.favorites.adapter.FavoritesAdapter;
import com.example.hazem.udacitymovieapp.Features.moviedetails.MovieDetailsActivity;
import com.example.hazem.udacitymovieapp.R;
import com.example.hazem.udacitymovieapp.databinding.ActivityFavoritesScreenBinding;

public class FavoritesScreen extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID=1123;
    FavoritesAdapter adapter;
    ListView listView;
    ActivityFavoritesScreenBinding mBind;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        mBind=DataBindingUtil.setContentView (this,R.layout.activity_favorites_screen);
        listView=mBind.lvFavorites;
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        Cursor cursor=getContentResolver ().query (MovieContract.FavoriteEntry.CONTENT_URI,null,null,null,null);
        adapter=new FavoritesAdapter (this,cursor);
        listView.setAdapter (adapter);
        adapter.setItemClickListener (new FavoritesAdapter.ItemClickListener () {
            @Override
            public void onItemClick (View view, int ID) {
                Intent intent=new Intent (FavoritesScreen.this, MovieDetailsActivity.class);
                intent.putExtra (NavConstants.MOVIE_ID,ID);
                startActivity (intent);
            }

            @Override
            public void onHeartClick (View view, int ID) {
                Uri deleteUri= ContentUris.withAppendedId (MovieContract.FavoriteEntry.CONTENT_URI,ID);
                int deleterow=getContentResolver ().delete (deleteUri,null,null);
                if(deleterow<=0)
                {
                    Toast.makeText (FavoritesScreen.this, "Fail to unfavorite", Toast.LENGTH_SHORT).show ();
                }

            }
        });
        listView.setEmptyView (mBind.llEmptyView);
        cursor.close ();
        getSupportLoaderManager ().initLoader (LOADER_ID,null,this);
    }

    @Override
    public boolean onKeyUp (int keyCode, KeyEvent event) {
        return super.onKeyUp (keyCode, event);
    }

    @Override
    public Loader<Cursor> onCreateLoader (int id, Bundle args) {
        switch (id)
        {
            case LOADER_ID:
            {
                return new android.support.v4.content.CursorLoader (this,MovieContract.FavoriteEntry.CONTENT_URI,null,null,null,null);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished (Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor (data);
    }

    @Override
    public void onLoaderReset (Loader<Cursor> loader) {
        adapter.swapCursor (null);
    }

    @Override
    protected void onDestroy () {
        getSupportLoaderManager ().restartLoader (LOADER_ID,null,this);
        super.onDestroy ();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ())
        {
            case android.R.id.home:
            {
                onBackPressed ();
                break;
            }
        }
        return super.onOptionsItemSelected (item);
    }
}
