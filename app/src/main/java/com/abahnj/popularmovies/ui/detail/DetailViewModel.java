package com.abahnj.popularmovies.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.abahnj.popularmovies.data.CastEntry;
import com.abahnj.popularmovies.data.FavMovieEntry;
import com.abahnj.popularmovies.data.GenreEntry;
import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.data.ReviewEntry;
import com.abahnj.popularmovies.data.VideoEntry;
import com.abahnj.popularmovies.data.source.AppRepository;
import com.abahnj.popularmovies.utils.Resource;

import java.util.List;

public class DetailViewModel extends ViewModel {

    private AppRepository moviesRepo;
    private LiveData<MovieEntry> movieResult;
    private LiveData<Resource<List<CastEntry>>> castResults;
    private LiveData<Resource<List<VideoEntry>>> videoResults;
    private LiveData<Resource<List<ReviewEntry>>> reviewResult;
    private LiveData<Boolean> isMovieFav;

    DetailViewModel(AppRepository moviesRepo) {
        this.moviesRepo = moviesRepo;
    }


    public void start(Integer movieId) {
        movieResult = moviesRepo.loadMoviesById(movieId);
        castResults = moviesRepo.loadCast(movieId);
        videoResults = moviesRepo.loadVideos(movieId);
        reviewResult = moviesRepo.loadReviews(movieId);
    }



    public LiveData<MovieEntry> getMovieResult() {
        return movieResult;
    }

    LiveData<Resource<List<GenreEntry>>> getGenresById(List<Integer> genreIds) {
        return moviesRepo.loadGenres(genreIds);
    }

    public LiveData<Resource<List<CastEntry>>> getCastResults() { return castResults; }

    public LiveData<Resource<List<VideoEntry>>> getVideoResults() { return videoResults; }

    public LiveData<Resource<List<ReviewEntry>>> getReviewResult() { return reviewResult; }

    void saveFavMovies(FavMovieEntry favMovieEntity) {
        moviesRepo.saveFavouriteMovie(favMovieEntity);
    }

    LiveData<Integer> deleteMovieById(int favMovieId) {
        return moviesRepo.deleteMovieById(favMovieId);
    }

    public void saveFavCast(List<CastEntry> favMovieCast) {
         moviesRepo.saveFavMovieCast(favMovieCast);
    }

    public void saveFavReviews(List<ReviewEntry> favMovieReviews) {
        moviesRepo.saveFavMovieReviews(favMovieReviews);
    }

    public void saveFavTrailers(List<VideoEntry> favMovieTrailers) {
        moviesRepo.saveFavMovieVideos(favMovieTrailers);
    }

    LiveData<List<CastEntry>> getFavCasts(List<Integer> castIds) {
        return moviesRepo.getCastsById(castIds);
    }

    LiveData<List<ReviewEntry>> getFavReviews(int favMovieId) {
        return moviesRepo.getReviewsByMovie(favMovieId);
    }

    LiveData<List<VideoEntry>> getFavVideos(int favMovieId) {
        return moviesRepo.getVideosByMovie(favMovieId);
    }

    LiveData<FavMovieEntry> loadFavMoviesById(int favMovieId) {
        return moviesRepo.loadFavMovieById(favMovieId);
    }
}
