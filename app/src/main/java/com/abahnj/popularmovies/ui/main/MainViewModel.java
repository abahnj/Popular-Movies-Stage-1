package com.abahnj.popularmovies.ui.main;



import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.data.source.AppRepository;
import com.abahnj.popularmovies.utils.Resource;

import java.util.List;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private final AppRepository appRepository;

    MainViewModel(AppRepository repository) {
        Log.d(TAG, "Actively retrieving the movies from the DataBase");
        appRepository = repository;
    }

    LiveData<Resource<List<MovieEntry>>> loadMovies(boolean forceLoad, String sortBy){
        return appRepository.loadMovies(forceLoad, sortBy);
    }

    LiveData<List<MovieEntry>> loadFavMoviesFromDb(){
        return appRepository.loadFavMoviesFromDb();
    }


}
