package com.abahnj.popularmovies.data.source.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.abahnj.popularmovies.data.CastEntry;
import com.abahnj.popularmovies.data.FavMovieEntry;
import com.abahnj.popularmovies.data.GenreEntry;
import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.data.ReviewEntry;
import com.abahnj.popularmovies.data.VideoEntry;


import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface MoviesDao {

    @Insert
    void saveMoviesToDb(List<MovieEntry> movieList);

    @Query("SELECT * FROM movies ORDER BY _id ASC")
    LiveData<List<MovieEntry>> loadFromDb();

    @Update
    int updateMovie (MovieEntry movieEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveFavMovie(FavMovieEntry favMovieEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveFavReview(List<ReviewEntry> reviewEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveFavVideo(List<VideoEntry> videoEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveFavCast(List<CastEntry> castEntry);

    @Query("SELECT * FROM fav_movies ORDER BY createdAt")
    LiveData<List<MovieEntry>> loadFavMoviesFromDb();

    @Query("SELECT * FROM `cast` WHERE id IN (:castIds)")
    LiveData<List<CastEntry>> getCastsById(List<Integer> castIds);

    @Query("SELECT * FROM reviews where fav_movie_id = :favMovieId")
    LiveData<List<ReviewEntry>> getReviewsByMovie(int favMovieId);

    @Query("SELECT * FROM videos where fav_movie_id = :favMovieId")
    LiveData<List<VideoEntry>> getVideosByMovie(int favMovieId);

    @Query("DELETE FROM movies")
    void deleteMovies();

    @Query("SELECT * FROM movies WHERE movieId =:movieId")
    LiveData<MovieEntry> getMovieById(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveGenresToDb(List<GenreEntry> genreList);

    @Query("SELECT * FROM genres WHERE genreId IN (:genreIds)")
    LiveData<List<GenreEntry>> getGenresById(List<Integer> genreIds);

    @Query("Delete FROM reviews where fav_movie_id = :movieId")
    int deleteFavReview(int movieId);

    @Query("Delete FROM videos where fav_movie_id = :movieId")
    int deleteFavVideo(int movieId);

    @Delete
    int deleteFavCast(List<CastEntry> favMovieCastEntities);

    @Delete
    int deleteFavReview(List<ReviewEntry> favMovieReviewEntities);

    @Delete
    int deleteFavVideo(List<VideoEntry> favMovieVideoEntities);

    @Query("SELECT * FROM fav_movies WHERE movieId =:favMovieId")
    LiveData<FavMovieEntry> loadFavMoviesById(int favMovieId);

    @Query("DELETE FROM fav_movies WHERE movieId = :favMovieId")
    int deleteMovieById(int favMovieId);}
