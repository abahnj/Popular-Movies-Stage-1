package com.abahnj.popularmovies;

import android.app.Application;
import android.content.Context;

import com.abahnj.popularmovies.api.RetrofitInterface;
import com.abahnj.popularmovies.api.interceptors.AuthenticationInterceptor;
import com.abahnj.popularmovies.data.source.local.AppDatabase;
import com.abahnj.popularmovies.data.source.local.dao.MoviesDao;
import com.abahnj.popularmovies.data.source.AppRepository;
import com.abahnj.popularmovies.utils.Constants;
import com.abahnj.popularmovies.utils.AppExecutor;
import com.abahnj.popularmovies.utils.LiveDataCallAdapterFactory;
import com.abahnj.popularmovies.utils.MainThreadExecutor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Factory {
    public volatile static RetrofitInterface retrofit;

    //  REPOSITORY
    public static AppRepository provideAppRepository(RetrofitInterface apiInterface, MoviesDao moviesDao, AppExecutor executor) {
        return new AppRepository(apiInterface, moviesDao, executor);
    }

    // EXECUTOR
    public static AppExecutor provideAppExecutor() {
        return new AppExecutor(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
    }

    // Database
    public static AppDatabase provideDatabase(Context context) {
       return AppDatabase.getInstance(context);
    }

    public static MoviesDao provideUserDao(Context context) {
        return provideDatabase(context).moviesDao();
    }

    //  NETWORK
    public static RetrofitInterface provideApiInterface() {
        if (retrofit == null) {
            retrofit =  provideRetrofit().create(RetrofitInterface.class);
        }
        return retrofit;
    }


    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    private static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .client(provideOkHttpClient())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
    }

    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(provideAuthenticationInterceptor())
                .addInterceptor(provideHttpLoggingInterceptor())
                .build();
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


    private static AuthenticationInterceptor provideAuthenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

}