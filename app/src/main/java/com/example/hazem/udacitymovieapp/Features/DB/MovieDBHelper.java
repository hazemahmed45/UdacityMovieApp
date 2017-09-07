package com.example.hazem.udacitymovieapp.Features.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hazem on 9/5/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="movie.db";
    private static final int DATABASE_VERSION=1;
    public MovieDBHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + MovieContract.FavoriteEntry.TABLE_NAME + " ("
                + MovieContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY, "
                + MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + MovieContract.FavoriteEntry.COLUMN_MOVIE_TAG + " TEXT , "
                + MovieContract.FavoriteEntry.COLUMN_MOVIE_DATE + " TEXT , "
                + MovieContract.FavoriteEntry.COLUMN_MOVIE_DURATION + " TEXT , "
                + MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, "
                + MovieContract.FavoriteEntry.COLUMN_MOVIE_RATE + " TEXT); ";
        sqLiteDatabase.execSQL (SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
