package com.example.hazem.udacitymovieapp.Base.Constants;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Hazem on 6/17/2017.
 */

public class NetworkUtil {

    private static final String BASE_MOVIE_API="http://api.themoviedb.org/3/";
    private static final String MOVIE_LIST_API=BASE_MOVIE_API+"discover/movie/?";
    private static final String MOVIE_SEARCH_API=BASE_MOVIE_API+"search/movie/?";
    private static final String MOVIE_FIND_API=BASE_MOVIE_API+"find/movie/?";
    private static final String SINGLE_MOVIE_API=BASE_MOVIE_API+"movie/";
    private static final String SINGLE_MOVIE_VIDEO_API="videos";
    private static final String SINGLE_MOVIE_POPULAR_API="popular";
    private static final String SINGLE_MOVIE_REVIEWS_API="/reviews/";
    private static final String MOVIE_COLLECTION_API=BASE_MOVIE_API+"collection/";
    private static final String YOUTUBE_URL="https://www.youtube.com/watch";
    //Posters APIS
    private static final String BASE_POSTER_API="https://image.tmdb.org/t/p/";
    private static final String XXS_POSTER_API="w92/";
    private static final String XS_POSTER_API="w154/";
    private static final String S_POSTER_API="w185/";
    private static final String L_POSTER_API="w342/";
    private static final String XL_POSTER_API="w500/";
    private static final String XXL_POSTER_API="w780/";
    private static final String ORIGNAL_POSTER_API="original/";
    private static final String S_BACKGROUND_API="w300/";
    private static final String L_BACKGROUND_API="w780/";
    private static final String XL_BACKGROUND_API="w1280/";

    //GETTING POSTERS
    public static String getLargePosterURL(String imageString)
    {
        StringBuffer buffer=new StringBuffer ();
        buffer.append (BASE_POSTER_API);
        buffer.append (L_POSTER_API);
        buffer.append (imageString);
        return buffer.toString ();
    }
    public static String getSmallPosterURL(String imageString)
    {
        StringBuffer buffer=new StringBuffer ();
        buffer.append (BASE_POSTER_API);
        buffer.append (S_POSTER_API);
        buffer.append (imageString);
        return buffer.toString ();
    }
    public static String getXLargePosterURL(String imageString)
    {
        StringBuffer buffer=new StringBuffer ();
        buffer.append (BASE_POSTER_API);
        buffer.append (XL_POSTER_API);
        buffer.append (imageString);
        return buffer.toString ();
    }
    public static String getXXLargePosterURL(String imageString)
    {
        StringBuffer buffer=new StringBuffer ();
        buffer.append (BASE_POSTER_API);
        buffer.append (XXL_POSTER_API);
        buffer.append (imageString);
        return buffer.toString ();
    }
    public static String getOriginalPosterURL(String imageString)
    {
        StringBuffer buffer=new StringBuffer ();
        buffer.append (BASE_POSTER_API);
        buffer.append (ORIGNAL_POSTER_API);
        buffer.append (imageString);
        return buffer.toString ();
    }
    public static String getXXSmallPosterURL(String imageString)
    {
        StringBuffer buffer=new StringBuffer ();
        buffer.append (BASE_POSTER_API);
        buffer.append (XXS_POSTER_API);
        buffer.append (imageString);
        return buffer.toString ();
    }
    public static String getXSmallPosterURL(String imageString)
    {
        StringBuffer buffer=new StringBuffer ();
        buffer.append (BASE_POSTER_API);
        buffer.append (XS_POSTER_API);
        buffer.append (imageString);
        return buffer.toString ();
    }
    public static String getLargeBackgroundURL(String imageString)
    {
        StringBuffer buffer=new StringBuffer ();
        buffer.append (BASE_POSTER_API);
        buffer.append (L_BACKGROUND_API);
        buffer.append (imageString);
        return buffer.toString ();
    }
    public static String getXLargeBackgroundURL(String imageString)
    {
        StringBuffer buffer=new StringBuffer ();
        buffer.append (BASE_POSTER_API);
        buffer.append (XL_BACKGROUND_API);
        buffer.append (imageString);
        return buffer.toString ();
    }
    public static String getSmallBackgroundURL(String imageString)
    {
        StringBuffer buffer=new StringBuffer ();
        buffer.append (BASE_POSTER_API);
        buffer.append (S_BACKGROUND_API);
        buffer.append (imageString);
        return buffer.toString ();
    }

    public static Uri getYoutubeVideoUrl(String key)
    {
        Uri uri=Uri.parse (YOUTUBE_URL).buildUpon ().appendQueryParameter (APIKeys.YOUTUBE_VIDEO,key).build ();

        return uri;
    }
    public static URL getVideosURl(String ID)
    {
        Uri uri=Uri.parse (SINGLE_MOVIE_API).buildUpon ().appendPath (ID).appendPath (SINGLE_MOVIE_VIDEO_API).build ();
        URL url=null;
        try {
            url=new URL (APIKeys.getAPIKEY (uri.toString ()).toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }
    public static URL getReviewsURl(String ID)
    {
        Uri uri=Uri.parse (SINGLE_MOVIE_API).buildUpon ().appendPath (ID).appendPath (SINGLE_MOVIE_REVIEWS_API).build ();
        URL url=null;
        try {
            url=new URL (APIKeys.getAPIKEY (uri.toString ()).toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }
    public static URL getPopularURl()
    {
        Uri uri=Uri.parse (SINGLE_MOVIE_API).buildUpon ().appendPath (SINGLE_MOVIE_POPULAR_API).build ();
        URL url=null;
        try {
            url=new URL (APIKeys.getAPIKEY (uri.toString ()).toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }

    //GETTING APIS
    private static Uri getSearchMovieApi()
    {
        return APIKeys.getAPIKEY (MOVIE_SEARCH_API);
    }
    private static Uri getSingleMovieApi()
    {
        return APIKeys.getAPIKEY (SINGLE_MOVIE_API);
    }
    private static Uri getFindMovieApi()
    {
        return APIKeys.getAPIKEY (MOVIE_FIND_API);
    }
    private static Uri getCollectionMovieApi()
    {
        return APIKeys.getAPIKEY (MOVIE_COLLECTION_API);
    }
    private static Uri getDiscoverMovieApi(){return APIKeys.getAPIKEY (MOVIE_LIST_API);}

    //GETTING URLS
    public static URL getSearchQueryUrl(String value)
    {
        Uri uri=getSearchMovieApi ().buildUpon ().appendQueryParameter (APIKeys.QUERY,value).build ();
        URL url=null;
        try {
            url=new URL (uri.toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }
    public static URL getFindQueryUrl(String value)
    {
        Uri uri=getFindMovieApi ().buildUpon ().appendQueryParameter (APIKeys.QUERY,value).build ();
        URL url=null;
        try {
            url=new URL (uri.toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }
    public static URL getCollectionQueryUrl(String value)
    {
        Uri uri=getCollectionMovieApi ().buildUpon ().appendQueryParameter (APIKeys.QUERY,value).build ();
        URL url=null;
        try {
            url=new URL (uri.toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }
    public static URL getMovieUrl(String id)
    {
        Uri uri=getSingleMovieApi ().buildUpon ().appendPath (id).build ();
        URL url=null;
        try {
            url=new URL (uri.toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }
    public static URL getSearchQueryUrl(String value,Map<String,String> params)
    {
        Uri.Builder builder=Uri.parse (getSearchQueryUrl(value).toString ()).buildUpon ();
        for (Map.Entry<String,String> param:params.entrySet ())
        {
            builder.appendQueryParameter (param.getKey (),param.getValue ());
        }
        Uri uri=builder.build ();
        URL url=null;
        try {
            url=new URL (uri.toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }
    public static URL getDiscoverUrl(Map<String,String> params)
    {
        Uri.Builder builder=getDiscoverMovieApi ().buildUpon ();
        if(params!=null && params.size ()>0)
        {
            for (Map.Entry<String,String> param:params.entrySet ())
            {
                if(param.getValue ()!=null)
                {
                    builder.appendQueryParameter (param.getKey (),param.getValue ());

                }
            }
        }
        Uri uri=builder.build ();
        URL url=null;
        try {
            url=new URL (uri.toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return url;
    }
    public static URL getDiscoverUrlPagining(int page,Map<String,String> params)
    {
        Uri uri=Uri.parse (getDiscoverUrl (params).toString ())
                .buildUpon ()
                .appendQueryParameter (APIKeys.PAGE, String.valueOf (page))
                .build ();
        try {
            return new URL (uri.toString ());
        } catch (MalformedURLException e) {
            e.printStackTrace ();
        }
        return null;
    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    public static class APIKeys {
        public static final String ApiKey="api_key";
        public static final String ApiValue="##################";//TODO add your api key here
        public static final String QUERY ="query";//string
        public static final String PAGE="page";// integar defult=1 range 1-1000
        public static final String INCLUDE_ADULT="include_adult";//boolean
        public static final String REGION="region";//string
        public static final String YEAR="year";//integar
        public static final String PRIMARY_RELEASE_YEAR="primary_release_year";//integar
        public static final String LANGUAGE="language";//string {minLength:2 pattern: ([a-z]{2})-([A-Z]{2}) default: en-US}
        public static final String DISCOVER_SORT_BY="sort_by";//string
        public static final String DISCOVER_CERTIFICATION_COUNTRY="certification_country";//string {
        public static final String DISCOVER_CERTIFICATION="certification";//string
        public static final String DISCOVER_INCLUDE_VIDEO="include_video";//boolean
        public static final String DISCOVER_PRIMARY_RELEASE_DATE_GREATER_OR_EQUAL="primary_release_date.gte";//string
        public static final String DISCOVER_PRIMARY_RELEASE_DATE_LESS_OR_EQUAL="primary_release_date.lte";//string
        public static final String DISCOVER_CERTIFICATION_LESS_OR_EQUAL="certification.lte";//string
        public static final String DISCOVER_RELEASE_DATE_GREATER_OR_EQUAL="release_date.gte";//string
        public static final String DISCOVER_RELEASE_DATE_LESS_OR_EQUAL="release_date.lte";//string
        public static final String DISCOVER_VOTE_COUNT_GREATER_OR_EQUAL="vote_count.gte";//INTEGAR
        public static final String DISCOVER_VOTE_COUNT_LESS_OR_EQUAL="vote_count.lte";//INTEGAR
        public static final String DISCOVER_VOTE_AVERAGE_GREATER_OR_EQUAL="vote_average.gte";//NUMBER
        public static final String DISCOVER_VOTE_AVERAGE_LESS_OR_EQUAL="vote_average.lte";//NUMBER
        public static final String DISCOVER_WITH_CAST="with_cast";//string
        public static final String DISCOVER_WITH_CREW="with_crew";//string
        public static final String DISCOVER_WITH_COMPANIES="with_companies";//string
        public static final String DISCOVER_WITH_GENRES="with_genres";//string
        public static final String DISCOVER_WITH_KEYWORDS="with_keywords";//string
        public static final String DISCOVER_WITH_PEOPlE="with_people";//string

        public static final String YOUTUBE_VIDEO="v";
        private static Uri getAPIKEY(String api)
        {
            Uri apiUrl=null;
            apiUrl=Uri.parse (api).buildUpon ()
                    .appendQueryParameter (APIKeys.ApiKey,APIKeys.ApiValue).build ();
            return apiUrl;
        }
    }
}
