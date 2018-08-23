package com.abahnj.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.data.source.AppRepository;
import com.abahnj.popularmovies.utils.SharedPreferenceHelper;

public class DetailViewModel extends ViewModel {

    private AppRepository moviesRepo;
    private MutableLiveData<MovieEntry> movieResult = new MutableLiveData<>();

    public DetailViewModel(AppRepository moviesRepo) {
        this.moviesRepo = moviesRepo;

        moviesRepo.loadMoviesById(SharedPreferenceHelper.getSharedPreferenceInt("mId"))
                .observeForever(movieEntity -> movieResult.setValue(movieEntity));
    }


    public MutableLiveData<MovieEntry> getMovieResult() {
        return movieResult;
    }


    LiveData<Integer> deleteMovieById(int favMovieId) {
        return moviesRepo.deleteMovieById(favMovieId);
    }
}
