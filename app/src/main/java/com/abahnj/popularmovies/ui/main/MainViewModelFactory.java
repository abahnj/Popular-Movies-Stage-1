package com.abahnj.popularmovies.ui.main;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.abahnj.popularmovies.Factory;
import com.abahnj.popularmovies.data.source.AppRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    @SuppressLint("StaticFieldLeak")
    private static volatile MainViewModelFactory INSTANCE;

    private final Application mApplication;

    private final AppRepository mAppRepository;

    public static MainViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (MainViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainViewModelFactory(application,
                            Factory.provideAppRepository(Factory.provideApiInterface(), Factory.provideUserDao(application), Factory.provideAppExecutor()));
                }
            }
        }
        return INSTANCE;
    }


    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }


    private MainViewModelFactory(Application application, AppRepository repository) {
        mApplication = application;
        mAppRepository = repository;
    }

    // Note: This can be reused with minor modifications
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(mApplication, mAppRepository);
    }
}
