package com.abahnj.popularmovies.data.source;

import android.arch.lifecycle.LiveData;

import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.utils.Resource;

import java.util.List;

public interface AppRepositoryInterface {

    LiveData<Resource<List<MovieEntry>>> loadMovies(boolean forceLoad, String sortBy);

    LiveData<MovieEntry> loadMoviesById(int movieId);

      LiveData<Integer> deleteMovieById(int favMovieId);
}
