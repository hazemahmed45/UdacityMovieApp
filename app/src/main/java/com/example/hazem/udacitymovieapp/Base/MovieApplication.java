package com.example.hazem.udacitymovieapp.Base;

import android.app.Application;

import com.google.gson.Gson;

/**
 * Created by Hazem on 9/4/2017.
 */

public class MovieApplication extends Application {
    private static MovieApplication mInstance;
    public static MovieApplication getmInstance()
    {
        return mInstance;
    }
    private Gson gson;
    public Gson getGson()
    {
        return gson;
    }
    @Override
    public void onCreate () {
        super.onCreate ();
        mInstance=this;
        gson=new Gson ();
    }
}
