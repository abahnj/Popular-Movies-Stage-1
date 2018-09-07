package com.abahnj.popularmovies.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.abahnj.popularmovies.data.CastEntry;
import com.abahnj.popularmovies.data.GenreEntry;
import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.data.ReviewEntry;
import com.abahnj.popularmovies.data.VideoEntry;
import com.abahnj.popularmovies.utils.Resource;

import java.util.List;

public interface AppRepositoryInterface {

    LiveData<Resource<List<MovieEntry>>> loadMovies(boolean forceLoad, String sortBy);

    LiveData<List<ReviewEntry>> getReviewsByMovie(int favMovieId);

    LiveData<List<VideoEntry>> getVideosByMovie(int favMovieId);

    LiveData<MovieEntry> loadMoviesById(int movieId);

    LiveData<Integer> deleteFavMovieReviews(int movieId);

    LiveData<Integer> deleteFavMovieVideos(int movieId);

    LiveData<Integer> deleteMovieById(int favMovieId);

    LiveData<List<MovieEntry>> loadFavMoviesFromDb();

    LiveData<Integer> updateMovie (MovieEntry movieEntry);

    LiveData<Resource<List<GenreEntry>>> loadGenres(List<Integer> genreIds);

    LiveData<Resource<List<CastEntry>>> loadCast(int movieId);

    LiveData<Resource<List<VideoEntry>>> loadVideos(int movieId);

    LiveData<Resource<List<ReviewEntry>>> loadReviews(int movieId);

    LiveData<List<CastEntry>> getCastsById(List<Integer> castIds);

    void saveFavMovieCast(List<CastEntry> favMovieCastEntities);

    void saveFavMovieReviews(List<ReviewEntry> favMovieReviewEntities);

    void saveFavMovieVideos(List<VideoEntry> favMovieVideoEntities);

    LiveData<Integer> deleteFavMovieCast(List<CastEntry> favMovieCastEntities);

    LiveData<Integer> deleteFavMovieReviews(List<ReviewEntry> favMovieReviewEntities);

    LiveData<Integer> deleteFavMovieVideos(List<VideoEntry> favMovieVideoEntities);

}
