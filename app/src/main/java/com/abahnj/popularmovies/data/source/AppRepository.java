package com.abahnj.popularmovies.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.abahnj.popularmovies.api.ApiResponse;
import com.abahnj.popularmovies.api.RetrofitInterface;
import com.abahnj.popularmovies.api.response.CastResponse;
import com.abahnj.popularmovies.api.response.GenreResponse;
import com.abahnj.popularmovies.api.response.MovieResponse;
import com.abahnj.popularmovies.api.response.ReviewResponse;
import com.abahnj.popularmovies.api.response.VideoResponse;
import com.abahnj.popularmovies.data.CastEntry;
import com.abahnj.popularmovies.data.FavMovieEntry;
import com.abahnj.popularmovies.data.GenreEntry;
import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.data.ReviewEntry;
import com.abahnj.popularmovies.data.VideoEntry;
import com.abahnj.popularmovies.data.source.local.dao.MoviesDao;
import com.abahnj.popularmovies.utils.AbsentLiveData;
import com.abahnj.popularmovies.utils.Constants;
import com.abahnj.popularmovies.utils.AppExecutor;
import com.abahnj.popularmovies.utils.Resource;
import com.abahnj.popularmovies.utils.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class AppRepository implements AppRepositoryInterface {

    private final RetrofitInterface apiInterface;
    private final AppExecutor executor;
    private final MoviesDao moviesDao;

    public AppRepository(RetrofitInterface apiInterface, MoviesDao moviesDao, AppExecutor executor) {
        this.apiInterface = apiInterface;
        this.moviesDao = moviesDao;
        this.executor = executor;
    }

    public LiveData<Resource<List<MovieEntry>>> loadMovies(boolean forceLoad, String sortBy) {
        return new NetworkBoundResource<List<MovieEntry>, MovieResponse>(executor) {
            @Override
            protected void saveCallResult(@NonNull MovieResponse item) {
                SharedPreferenceHelper.setSharedPreferenceLong(Constants.DATA_SAVED_TIME, new Date(System.currentTimeMillis()).getTime());
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
                return apiInterface.getMovies(sortBy, Constants.LANGUAGE, Constants.PAGE);
            }
        }.asLiveData();
    }


    @Override
    public LiveData<Resource<List<GenreEntry>>> loadGenres(List<Integer> genreIds) {
        return new NetworkBoundResource<List<GenreEntry>, GenreResponse>(executor) {
            @Override
            protected void saveCallResult(@NonNull GenreResponse item) {
                moviesDao.saveGenresToDb(item.getGenres());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<GenreEntry> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<GenreEntry>> loadFromDb() {
                return moviesDao.getGenresById(genreIds);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenreResponse>> createCall() {
                return apiInterface.getGenres(Constants.LANGUAGE);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<List<ReviewEntry>>> loadReviews(int movieId) {
        return new NetworkBoundResource<List<ReviewEntry>, ReviewResponse>(executor) {
            private List<ReviewEntry> reviewResultsList = new ArrayList<>();

            @Override
            protected void saveCallResult(@NonNull ReviewResponse item) {
                reviewResultsList = item.getResults();
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ReviewEntry> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<ReviewEntry>> loadFromDb() {
                if (reviewResultsList == null) {
                    return AbsentLiveData.create();
                } else {
                    return new LiveData<List<ReviewEntry>>() {
                        @Override
                        protected void onActive() {
                            super.onActive();
                            setValue(reviewResultsList);
                        }
                    };
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ReviewResponse>> createCall() {
                return apiInterface.getReviews(movieId, Constants.LANGUAGE);
            }
        }.asLiveData();
    }


    @Override
    public LiveData<Resource<List<CastEntry>>> loadCast(int movieId) {
        return new NetworkBoundResource<List<CastEntry>, CastResponse>(executor) {
            private List<CastEntry> castList = new ArrayList<>();

            @Override
            protected void saveCallResult(@NonNull CastResponse item) {
                castList = item.getCast();
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CastEntry> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<CastEntry>> loadFromDb() {
                if (castList == null) {
                    return AbsentLiveData.create();
                } else {
                    return new LiveData<List<CastEntry>>() {
                        @Override
                        protected void onActive() {
                            super.onActive();
                            setValue(castList);
                        }
                    };
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<CastResponse>> createCall() {
                return apiInterface.getCast(movieId, Constants.LANGUAGE);
            }

        }.asLiveData();
    }

    @Override
    public LiveData<Resource<List<VideoEntry>>> loadVideos(int movieId) {
        return new NetworkBoundResource<List<VideoEntry>, VideoResponse>(executor) {
            private List<VideoEntry> videoResultsList = new ArrayList<>();

            @Override
            protected void saveCallResult(@NonNull VideoResponse item) {
                videoResultsList = item.getResults();
            }

            @Override
            protected boolean shouldFetch(@Nullable List<VideoEntry> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<VideoEntry>> loadFromDb() {
                if (videoResultsList == null) {
                    return AbsentLiveData.create();
                } else {
                    return new LiveData<List<VideoEntry>>() {
                        @Override
                        protected void onActive() {
                            super.onActive();
                            setValue(videoResultsList);
                        }
                    };
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<VideoResponse>> createCall() {
                return apiInterface.getVideos(movieId, Constants.LANGUAGE);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<List<CastEntry>> getCastsById(List<Integer> castIds) {
        return moviesDao.getCastsById(castIds);
    }

    @Override
    public LiveData<List<ReviewEntry>> getReviewsByMovie(int favMovieId) {
        return moviesDao.getReviewsByMovie(favMovieId);
    }

    @Override
    public LiveData<List<VideoEntry>> getVideosByMovie(int favMovieId) {
        return moviesDao.getVideosByMovie(favMovieId);
    }

    @Override
    public LiveData<MovieEntry> loadMoviesById(int movieId) {
        return moviesDao.getMovieById(movieId);
    }

    @Override
    public void saveFavouriteMovie(FavMovieEntry favMovieEntity) {
        executor.diskIO().execute(() -> moviesDao.saveFavMovie(favMovieEntity));
    }


    @Override
    public void saveFavMovieCast(List<CastEntry> favMovieCastEntities) {
        executor.diskIO().execute(() -> moviesDao.saveFavCast(favMovieCastEntities));
    }

    @Override
    public void saveFavMovieReviews(List<ReviewEntry> favMovieReviewEntities) {
        executor.diskIO().execute(() -> moviesDao.saveFavReview(favMovieReviewEntities));
    }

    @Override
    public void saveFavMovieVideos(List<VideoEntry> favMovieVideoEntities) {
        executor.diskIO().execute(() -> moviesDao.saveFavVideo(favMovieVideoEntities));
    }


    @Override
    public LiveData<Integer> deleteFavMovieCast(List<CastEntry> favMovieCastEntities) {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        executor.diskIO().execute(() -> liveData.postValue(moviesDao.deleteFavCast(favMovieCastEntities)));
        return liveData;
    }

    @Override
    public LiveData<Integer> deleteFavMovieReviews(List<ReviewEntry> favMovieReviewEntities) {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        executor.diskIO().execute(() -> liveData.postValue(moviesDao.deleteFavReview(favMovieReviewEntities)));
        return liveData;
    }

    @Override
    public LiveData<Integer> deleteFavMovieVideos(List<VideoEntry> favMovieVideoEntities) {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        executor.diskIO().execute(() -> liveData.postValue(moviesDao.deleteFavVideo(favMovieVideoEntities)));
        return liveData;
    }

    @Override
    public LiveData<FavMovieEntry> loadFavMovieById(int movieId) {
        return moviesDao.loadFavMoviesById(movieId);
    }

    @Override
    public LiveData<Integer> deleteFavMovieReviews(int movieId) {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        executor.diskIO().execute(() -> liveData.postValue(moviesDao.deleteFavReview(movieId)));
        return liveData;
    }

    @Override
    public LiveData<Integer> deleteFavMovieVideos(int movieId) {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        executor.diskIO().execute(() -> liveData.postValue(moviesDao.deleteFavVideo(movieId)));
        return liveData;
    }

    @Override
    public LiveData<Integer> deleteMovieById(int favMovieId) {
        return null;
    }

    @Override
    public LiveData<List<MovieEntry>> loadFavMoviesFromDb() {
        return moviesDao.loadFavMoviesFromDb();
    }


    @Override
    public LiveData<Integer> updateMovie(MovieEntry movieEntry) {
        MutableLiveData<Integer> liveData = new MutableLiveData<>();
        executor.diskIO().execute(() -> liveData.postValue(moviesDao.updateMovie(movieEntry)));
        return liveData;
    }

    private Boolean shouldFetchData(Long time) {
        boolean shouldFetch;
        long savedTime;
        if (SharedPreferenceHelper.contains(Constants.DATA_SAVED_TIME)) {
            savedTime = SharedPreferenceHelper.getSharedPreferenceLong(Constants.DATA_SAVED_TIME, 0L);
            shouldFetch = (time - savedTime) > TimeUnit.MINUTES.toMillis(Constants.FRESH_TIMEOUT_IN_MINUTES);
        } else {
            shouldFetch = false;
        }

       /* if (!AppUtils.isNetworkAvailable()) {
            shouldFetch = false;
        }*/
        return shouldFetch;
    }

}
