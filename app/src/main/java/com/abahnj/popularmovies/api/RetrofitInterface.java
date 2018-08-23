package com.abahnj.popularmovies.api;

import android.arch.lifecycle.LiveData;

import com.abahnj.popularmovies.api.response.MovieResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("movie/{type}")
    LiveData<ApiResponse<MovieResponse>> getMovies(@Path(value = "type", encoded = true) String type,
                                                   @Query("language") String language,
                                                   @Query("page") int page);
}
