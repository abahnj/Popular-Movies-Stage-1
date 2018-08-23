package com.abahnj.popularmovies;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class PopularMoviesApp extends Application {


    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        PopularMoviesApp.context = getApplicationContext();
    }

    public static Context getContext() {
        return PopularMoviesApp.context;
    }
}
