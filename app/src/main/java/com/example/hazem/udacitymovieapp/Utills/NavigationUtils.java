package com.example.hazem.udacitymovieapp.Utills;

import android.app.Activity;
import android.content.Intent;

import com.example.hazem.udacitymovieapp.Base.Constants.NavConstants;
import com.example.hazem.udacitymovieapp.Features.favorites.FavoritesScreen;
import com.example.hazem.udacitymovieapp.Features.moviedetails.MovieDetailsActivity;
import com.example.hazem.udacitymovieapp.Features.search.SearchActivity;
import com.example.hazem.udacitymovieapp.Features.settings.SettingsActivity;

/**
 * Created by Hazem on 9/7/2017.
 */

public class NavigationUtils {
    private static Intent intent;
    public static void NavigateToSearchScreen(Activity activity)
    {
        intent=new Intent (activity, SearchActivity.class);
        activity.startActivity (intent);
    }
    public static void NavigateToMovieDetailsScreen(Activity activity,int ID)
    {
        intent=new Intent (activity, MovieDetailsActivity.class);
        intent.putExtra (NavConstants.MOVIE_ID,ID);
        activity.startActivity (intent);
    }
    public static void NavigateToSettingsScreen(Activity activity)
    {
        intent=new Intent (activity, SettingsActivity.class);
        activity.startActivity (intent);
    }
    public static void NavigateToFavoritesScreen(Activity activity)
    {
        intent=new Intent (activity, FavoritesScreen.class);
        activity.startActivity (intent);
    }
}
