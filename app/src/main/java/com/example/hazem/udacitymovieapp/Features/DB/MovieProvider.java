package com.example.hazem.udacitymovieapp.Features.DB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Hazem on 9/5/2017.
 */

public class MovieProvider extends ContentProvider {
    private MovieDBHelper movieDBHelper;
    private UriMatcher uriMatcher;
    private static final int MOVIE=10042;
    private static final int MOVIE_ID=10067;
    @Override
    public boolean onCreate () {
        movieDBHelper =new MovieDBHelper (getContext ());
        uriMatcher=new UriMatcher (UriMatcher.NO_MATCH);
        uriMatcher.addURI (MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_FAVORITES,MOVIE);
        uriMatcher.addURI (MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_FAVORITES+"/*",MOVIE_ID);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        Cursor cursor=null;
        SQLiteDatabase database= movieDBHelper.getReadableDatabase ();
        int match=uriMatcher.match (uri);
        switch (match)
        {
            case MOVIE:
            {
                cursor=database.query (MovieContract.FavoriteEntry.TABLE_NAME,columns,selection,selectionArgs,null,null,orderBy);
                break;
            }
            case MOVIE_ID:
            {
                selection=MovieContract.FavoriteEntry._ID+"=?";
                selectionArgs= new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query (MovieContract.FavoriteEntry.TABLE_NAME,columns,selection,selectionArgs,null,null,orderBy);
                break;
            }
            default:
            {
                throw new IllegalArgumentException ("SOMETHING WRONG WITH SELECT");
            }
        }
        cursor.setNotificationUri (getContext ().getContentResolver (),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType (@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert (@NonNull Uri uri, @Nullable ContentValues contentValues)
    {
        int match=uriMatcher.match (uri);
        switch (match)
        {
            case MOVIE:
            {
                return insertMovie (uri,contentValues);
            }
            default:
            {
                throw new IllegalArgumentException ("SOMETHING WRONG WITH INSERT");
            }
        }
    }
    private Uri insertMovie(Uri uri, ContentValues values) {
        String title = values.getAsString(MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE);
        String poster=values.getAsString (MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER);
        int movieId=values.getAsInteger (MovieContract.FavoriteEntry._ID);
        if (title == null) {
            throw new IllegalArgumentException("Movie required title");
        }
        if(poster==null)
            throw new IllegalArgumentException ("Movie required a poster");
        if(movieId<0)
            throw new IllegalArgumentException ("Mobie requires an id");
        SQLiteDatabase database= movieDBHelper.getWritableDatabase();

        long id=database.insert(MovieContract.FavoriteEntry.TABLE_NAME,null,values);

        if(id==-1)
        {
            Log.i ("LOG","ERROR");
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete (@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowNumber;
        SQLiteDatabase database= movieDBHelper.getWritableDatabase ();
        int match=uriMatcher.match (uri);
        switch (match)
        {
            case MOVIE:
            {
                rowNumber=database.delete (MovieContract.FavoriteEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case MOVIE_ID:
            {
                selection=MovieContract.FavoriteEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf (ContentUris.parseId (uri))};
                rowNumber=database.delete (MovieContract.FavoriteEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:
            {
                throw new IllegalArgumentException ("SOMETHING WRONG WITH DELETE");
            }
        }
        if (rowNumber != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowNumber;
    }

    @Override
    public int update (@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowUpdated=-1;
        int match=uriMatcher.match (uri);
        switch (match)
        {
            case MOVIE:
            {
                rowUpdated=updateMovie (uri,contentValues,selection,selectionArgs);
                break;
            }
            case MOVIE_ID:
            {
                selection=MovieContract.FavoriteEntry._ID+"?=";
                selectionArgs=new String[]{String.valueOf (ContentUris.parseId (uri))};
                rowUpdated= updateMovie (uri,contentValues,selection,selectionArgs);
                break;
            }
            default:
            {
                throw new IllegalArgumentException ("SOMETHING WRONG WITH UPDATE");
            }
        }
        return rowUpdated;
    }
    private int updateMovie(Uri uri,ContentValues values,String selection,String[] selectionArgs)
    {
        if (values.containsKey(MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE)) {
            String title = values.getAsString(MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE);
            if (title == null) {
                throw new IllegalArgumentException("Movie requires a Title");
            }
        }

        if (values.containsKey(MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER)) {
            String poster = values.getAsString (MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER);
            if (poster == null ) {
                throw new IllegalArgumentException("Movie requires a poster");
            }
        }
        if (values.containsKey(MovieContract.FavoriteEntry._ID)) {
            String movieId = values.getAsString (MovieContract.FavoriteEntry._ID);
            if (movieId == null ) {
                throw new IllegalArgumentException("Movie requires a Id");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database= movieDBHelper.getWritableDatabase();
        int rowNumber=database.update(MovieContract.FavoriteEntry.TABLE_NAME,values,selection,selectionArgs);
        if(rowNumber!=0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowNumber;
    }


}
