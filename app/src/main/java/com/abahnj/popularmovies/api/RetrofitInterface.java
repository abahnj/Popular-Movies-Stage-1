package com.abahnj.popularmovies.api;

import android.arch.lifecycle.LiveData;

import com.abahnj.popularmovies.api.response.CastResponse;
import com.abahnj.popularmovies.api.response.GenreResponse;
import com.abahnj.popularmovies.api.response.MovieResponse;
import com.abahnj.popularmovies.api.response.ReviewResponse;
import com.abahnj.popularmovies.api.response.VideoResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("movie/{type}")
    LiveData<ApiResponse<MovieResponse>> getMovies(@Path(value = "type", encoded = true) String type,
                                                   @Query("language") String language,
                                                   @Query("page") int page);

    @GET("genre/movie/list")
    LiveData<ApiResponse<GenreResponse>> getGenres(@Query("language") String language);

    @GET("movie/{movieId}/credits")
    LiveData<ApiResponse<CastResponse>> getCast(@Path(value = "movieId", encoded = true) int movieId,
                                                @Query("language") String language);

    @GET("movie/{movieId}/videos")
    LiveData<ApiResponse<VideoResponse>> getVideos(@Path(value = "movieId", encoded = true) int movieId,
                                                   @Query("language") String language);


    @GET("movie/{movieId}/reviews")
    LiveData<ApiResponse<ReviewResponse>> getReviews(@Path(value = "movieId", encoded = true) int movieId,
                                                     @Query("language") String language);
}
