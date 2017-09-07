package com.example.hazem.udacitymovieapp.Features.DB;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hazem on 9/5/2017.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.hazem.udacitymovieapp";
    public static final String PATH_FAVORITES = "favorites";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static class FavoriteEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVORITES);
        public static final String TABLE_NAME="favorites";
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_MOVIE_ID="movieid";
        public final static String COLUMN_MOVIE_TITLE ="title";

        /**
         * Breed of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_MOVIE_POSTER = "poster";

        /**
         * Gender of the pet.
         *
         * The only possible values are {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_MOVIE_DATE = "date";

        /**
         * Weight of the pet.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_MOVIE_DURATION = "duration";
        public final static String COLUMN_MOVIE_TAG = "tag";
        public final static String COLUMN_MOVIE_RATE="rate";

    }
}
