package com.abahnj.popularmovies.data.source.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.abahnj.popularmovies.data.MovieEntry;


import java.util.List;

@Dao
public interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMoviesToDb(List<MovieEntry> movieList);

    @Query("SELECT * FROM movies ORDER BY _id ASC")
    LiveData<List<MovieEntry>> loadFromDb();

    @Query("DELETE FROM movies")
    void deleteMovies();

    @Query("SELECT * FROM movies WHERE movieId =:movieId")
    LiveData<MovieEntry> getMovieById(int movieId);


}
