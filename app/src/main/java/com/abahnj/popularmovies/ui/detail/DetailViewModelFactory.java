package com.abahnj.popularmovies.ui.detail;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.abahnj.popularmovies.Factory;
import com.abahnj.popularmovies.data.source.AppRepository;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile DetailViewModelFactory INSTANCE;

    private final AppRepository mAppRepository;

    public static DetailViewModelFactory getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (DetailViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DetailViewModelFactory(
                            Factory.provideAppRepository(Factory.provideApiInterface(), Factory.provideUserDao(context), Factory.provideAppExecutor()));
                }
            }
        }
        return INSTANCE;
    }


    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }


    private DetailViewModelFactory( AppRepository repository) {
        mAppRepository = repository;
    }

    // Note: This can be reused with minor modifications
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel( mAppRepository);
    }
}
