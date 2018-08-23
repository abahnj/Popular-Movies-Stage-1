package com.abahnj.popularmovies.data.source;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.abahnj.popularmovies.api.ApiResponse;
import com.abahnj.popularmovies.api.RetrofitInterface;
import com.abahnj.popularmovies.api.response.MovieResponse;
import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.data.source.local.dao.MoviesDao;
import com.abahnj.popularmovies.utils.AppConstants;
import com.abahnj.popularmovies.utils.AppExecutor;
import com.abahnj.popularmovies.utils.Resource;
import com.abahnj.popularmovies.utils.SharedPreferenceHelper;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class AppRepository implements AppRepositoryInterface {

    private final RetrofitInterface apiInterface;
    private final AppExecutor executor;
    private final MoviesDao moviesDao;
    private Context context;

    public AppRepository(RetrofitInterface apiInterface, MoviesDao moviesDao, AppExecutor executor) {
        this.apiInterface = apiInterface;
        this.moviesDao = moviesDao;
        this.executor = executor;
    }

    public LiveData<Resource<List<MovieEntry>>> loadMovies(boolean forceLoad, String sortBy) {
        return new NetworkBoundResource<List<MovieEntry>, MovieResponse>(executor) {
            @Override
            protected void saveCallResult(@NonNull MovieResponse item) {
                SharedPreferenceHelper.setSharedPreferenceLong(AppConstants.DATA_SAVED_TIME, new Date(System.currentTimeMillis()).getTime());
                moviesDao.deleteMovies();
                moviesDao.saveMoviesToDb(item.getResults());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<MovieEntry> data) {
                return (((data == null || data.isEmpty()) && forceLoad && shouldFetchData(new Date(System.currentTimeMillis()).getTime()))
                        || (data == null || data.isEmpty())
                        || forceLoad
                        || shouldFetchData(new Date(System.currentTimeMillis()).getTime()));
            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntry>> loadFromDb() {
                return moviesDao.loadFromDb();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MovieResponse>> createCall() {
                return apiInterface.getMovies(sortBy, AppConstants.LANGUAGE, AppConstants.PAGE);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<MovieEntry> loadMoviesById(int movieId) {
        return moviesDao.getMovieById(movieId);
    }

    @Override
    public LiveData<Integer> deleteMovieById(int favMovieId) {
        return null;
    }

    private Boolean shouldFetchData(Long time) {
        boolean shouldFetch;
        long savedTime;
        if (SharedPreferenceHelper.contains(AppConstants.DATA_SAVED_TIME)) {
            savedTime = SharedPreferenceHelper.getSharedPreferenceLong(AppConstants.DATA_SAVED_TIME, 0L);
            shouldFetch = (time - savedTime) > TimeUnit.MINUTES.toMillis(AppConstants.FRESH_TIMEOUT_IN_MINUTES);
        } else {
            shouldFetch = false;
        }

       /* if (!AppUtils.isNetworkAvailable()) {
            shouldFetch = false;
        }*/
        return shouldFetch;
    }

}
