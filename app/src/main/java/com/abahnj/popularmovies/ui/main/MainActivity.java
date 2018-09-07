package com.abahnj.popularmovies.ui.main;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abahnj.popularmovies.R;
import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.interfaces.MovieClickListener;
import com.abahnj.popularmovies.ui.adapter.MovieListAdapter;
import com.abahnj.popularmovies.ui.detail.DetailActivity;
import com.abahnj.popularmovies.utils.AppUtils;
import com.abahnj.popularmovies.utils.Constants;
import com.abahnj.popularmovies.utils.GridSpacingItemDecoration;
import com.abahnj.popularmovies.utils.SharedPreferenceHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.abahnj.popularmovies.utils.Constants.ACTIVITY_FAVOURITE;
import static com.abahnj.popularmovies.utils.Constants.ACTIVITY_NORMAL;
import static com.abahnj.popularmovies.utils.Constants.ACTIVITY_TYPE;
import static com.abahnj.popularmovies.utils.Constants.MOVIE_ID_INTENT;
import static com.abahnj.popularmovies.utils.Constants.MOVIE_IMAGE_TRANSITION;

public class MainActivity extends AppCompatActivity implements MovieClickListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rv_movies)
    RecyclerView rvMovies;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbar;
    @BindView(R.id.no_internet_layout)
    View noInternet;
    @BindView(R.id.no_fav_layout)
    View noFav;
    private MainViewModel mViewModel;
    private MovieListAdapter movieListAdapter;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.txt_empty_string);
        tvToolbar.setText(R.string.movies);

        constraintLayout = findViewById(R.id.main);

        setupViewModel();
        loadFromSharedPrefs();

        movieListAdapter = new MovieListAdapter(this, this);
        int recyclerViewSpanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4;
        RecyclerView.LayoutManager mMoviesLayoutManager = new GridLayoutManager(this, recyclerViewSpanCount);
        rvMovies.setLayoutManager(mMoviesLayoutManager);
        rvMovies.addItemDecoration(new GridSpacingItemDecoration(recyclerViewSpanCount, dpToPx(), true));
        rvMovies.setItemAnimator(new DefaultItemAnimator());
        rvMovies.setAdapter(movieListAdapter);
    }

    private void setupViewModel() {
        MainViewModelFactory factory = MainViewModelFactory.getInstance(this);

        mViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);

    }

    private int dpToPx() {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
    }

    private void loadMovies(String sort, int loadingIdentifier) {

        mViewModel.loadFavMoviesFromDb().removeObservers(MainActivity.this);

        mViewModel.loadMovies(false, "null").removeObservers(MainActivity.this);

        Observer<List<MovieEntry>> observer = movieEntries -> {
            if ((movieEntries != null && !movieEntries.isEmpty())) {
                noFav.setVisibility(View.GONE);
                movieListAdapter.submitList(movieEntries);
            } else {
                movieListAdapter.submitList(movieEntries);
                noFav.setVisibility(View.VISIBLE);
            }
        };
        mViewModel.loadFavMoviesFromDb().removeObserver(observer);

        boolean forceLoad = loadingIdentifier == 1;
        noFav.setVisibility(View.GONE);


        if (sort.equalsIgnoreCase(Constants.SORT_BY_FAVORITE)) {
            mViewModel.loadFavMoviesFromDb().observe(this, observer);
        }
            else {
            mViewModel.loadMovies(forceLoad, sort).observe(this, movieResource -> {
                if (movieResource != null) {
                    switch (movieResource.getStatus()) {
                        case SUCCESS:
                            hideProgress();
                            if (movieResource.getResponse() != null) {
                                movieListAdapter.submitList(movieResource.getResponse());
                            }
                            rvMovies.setVisibility(View.VISIBLE);
                            break;
                        case LOADING:
                            showProgress();
                            break;
                        case ERROR:
                            hideProgress();
                            checkNetConnectivity(this);
                            break;
                    }
                }
            });
        }
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        rvMovies.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        rvMovies.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
    }

    private void checkNetConnectivity(Context context) {
        if (!AppUtils.isNetworkAvailable(context)) {
            noInternet.setVisibility(View.VISIBLE);
            AppUtils.setSnackBar(constraintLayout, getString(R.string.error_no_internet));
        }
    }

    @OnClick(R.id.btn_refresh)
    public void buttonRefresh() {
        if (AppUtils.isNetworkAvailable(this)) {
            loadFromSharedPrefs();
        } else {
            checkNetConnectivity(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        switch (SharedPreferenceHelper.getSharedPreferenceString(Constants.PREF_FILTER, null)) {
            case Constants.SORT_BY_TOP_RATED:
                menu.findItem(R.id.top_rated).setChecked(true);
                break;
            case Constants.SORT_BY_POPULAR:
                menu.findItem(R.id.popular).setChecked(true);
                break;
            case Constants.SORT_BY_FAVORITE:
                menu.findItem(R.id.favorite).setChecked(true);
                break;
        }
        return true;
    }

    private void loadFromSharedPrefs() {
        noInternet.setVisibility(View.GONE);

        int loadingIdentifier = SharedPreferenceHelper.contains(Constants.PREF_FILTER) ? 2 : 1;

        switch (loadingIdentifier) {
            case 1:
                SharedPreferenceHelper.setSharedPreferenceString(Constants.PREF_FILTER, Constants.SORT_BY_POPULAR);
                loadMovies(Constants.SORT_BY_POPULAR, loadingIdentifier);
                break;
            case 2:
                switch (SharedPreferenceHelper.getSharedPreferenceString(Constants.PREF_FILTER, null)) {
                    case Constants.SORT_BY_TOP_RATED:
                        loadMovies(Constants.SORT_BY_TOP_RATED, 2);
                        break;
                    case Constants.SORT_BY_POPULAR:
                        loadMovies(Constants.SORT_BY_POPULAR, 2);
                        break;
                    case Constants.SORT_BY_FAVORITE:
                        loadMovies(Constants.SORT_BY_FAVORITE, 1);
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular: {
                if (item.isChecked())
                    return true;
                else
                    item.setChecked(true);

                if (AppUtils.isNetworkAvailable(this)) {
                    SharedPreferenceHelper.setSharedPreferenceString(Constants.PREF_FILTER, Constants.SORT_BY_POPULAR);
                    loadMovies(Constants.SORT_BY_POPULAR, 1);
                } else {
                    AppUtils.setSnackBar(constraintLayout, getString(R.string.error_no_internet));
                }
                return true;
            }
            case R.id.top_rated: {
                if (item.isChecked())
                    return true;
                else
                    item.setChecked(true);

                if (AppUtils.isNetworkAvailable(this)) {
                    SharedPreferenceHelper.setSharedPreferenceString(Constants.PREF_FILTER, Constants.SORT_BY_TOP_RATED);
                    loadMovies(Constants.SORT_BY_TOP_RATED, 1);
                } else {
                    AppUtils.setSnackBar(constraintLayout, getString(R.string.error_no_internet));
                }
                return true;
            }
            case R.id.favorite: {
                if (item.isChecked())
                    return true;
                else
                    item.setChecked(true);

                SharedPreferenceHelper.setSharedPreferenceString(Constants.PREF_FILTER, Constants.SORT_BY_FAVORITE);
                loadMovies(Constants.SORT_BY_FAVORITE, 1);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onMovieEntryClick(int position, int movieId, ImageView shareImageView, String transitionName, String activityType) {
        Intent intent = new Intent(this, DetailActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_INTENT, movieId);
        bundle.putString(MOVIE_IMAGE_TRANSITION, transitionName);
        switch (SharedPreferenceHelper.getSharedPreferenceString(Constants.PREF_FILTER, null)) {
            case Constants.SORT_BY_FAVORITE: {
                bundle.putString(ACTIVITY_TYPE, ACTIVITY_FAVOURITE);
                break;
            }
            default:
                bundle.putString(ACTIVITY_TYPE, activityType);

        }
        intent.putExtras(bundle);

        SharedPreferenceHelper.setSharedPreferenceInt("mId", movieId);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                shareImageView,
                transitionName);
        startActivity(intent, options.toBundle());
    }
}
