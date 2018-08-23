package com.abahnj.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abahnj.popularmovies.R;
import com.abahnj.popularmovies.utils.AppConstants;
import com.abahnj.popularmovies.utils.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


import butterknife.BindView;
import butterknife.ButterKnife;


import static com.abahnj.popularmovies.utils.AppConstants.ACTIVITY_TYPE;
import static com.abahnj.popularmovies.utils.AppConstants.BACKDROP_BASE_PATH;
import static com.abahnj.popularmovies.utils.AppConstants.MOVIE_ID_INTENT;
import static com.abahnj.popularmovies.utils.AppConstants.MOVIE_IMAGE_TRANSITION;
import static com.abahnj.popularmovies.utils.AppConstants.POSTER_BASE_PATH;

public class DetailActivity extends AppCompatActivity {

    private DetailViewModel detailViewModel;

    @BindView(R.id.imv_back_drop) ImageView mImvBackDrop;
    @BindView(R.id.imv_poster) ImageView mImvPoster;
    @BindView(R.id.tv_title) TextView mTvMovieTitle;
    @BindView(R.id.tv_ratings) TextView mTvRating;
    @BindView(R.id.tv_release_date) TextView mTvReleaseDate;
    @BindView(R.id.tv_plot_details) TextView mTvPlotDetails;
    @BindView(R.id.tv_movie_name) TextView mToolbarMovieName;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(android.R.id.content) View snackBarView;
    @BindView(R.id.progress_rating) ProgressBar mProgressRating;
    @BindView(R.id.app_bar) AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;

    private RoundedBitmapDrawable roundedBitmapDrawable;
    private String transitionName;
    private String activityType;
    private int movieId;


    private void initViews() {
        setContentView(R.layout.activity_detail);
        supportPostponeEnterTransition();
        ButterKnife.bind(this);
    }

    private void initData() {
        DetailViewModelFactory factory = DetailViewModelFactory.getInstance(this.getApplication());

        detailViewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

    }

    private void setUpToolbarTitle(String movieName) {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mToolbarMovieName.setText(movieName);
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mCollapsingToolbarLayout.setTitle(" ");
                    mToolbarMovieName.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle(" ");
                    mToolbarMovieName.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            transitionName = extras.getString(MOVIE_IMAGE_TRANSITION);
            movieId = extras.getInt(MOVIE_ID_INTENT);
            activityType = extras.getString(ACTIVITY_TYPE);
        }

        loadPlaceholder();
        populateUi();
        supportPostponeEnterTransition();

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(AppConstants.TRANSITION, transitionName);
        outState.putInt(AppConstants.MOVIE_ID, movieId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            transitionName = savedInstanceState.getString(AppConstants.TRANSITION);
            movieId = savedInstanceState.getInt(AppConstants.MOVIE_ID);
        }
    }

    private void populateUi() {
        detailViewModel.getMovieResult().observe(this, movie -> {
            if (movie != null) {
                setUpToolbarTitle(movie.getTitle());
                loadBackDropImage(movie.getBackdropPath());
                loadMainImage(movie.getPosterPath());
                setMovieTitle(movie.getTitle());
                setMovieRating(String.valueOf(movie.getVoteAverage()));
                setMovieReleaseDate(movie.getReleaseDate());
                setMoviePlotDetails(movie.getOverview());
            }
        });

    }

    private void setMoviePlotDetails(String overview) {
        mTvPlotDetails.setText(overview);
    }

    private void setMovieReleaseDate(String releaseDate) {
        mTvReleaseDate.setText(AppUtils.convertDate(releaseDate, AppConstants.DF1, AppConstants.DF2));
    }

    private void setMovieRating(String rating) {
        mTvRating.setText(rating);
        Double ratingVal = Double.parseDouble(rating) * 10;
        Integer ratingInt = ratingVal.intValue();
        mProgressRating.setProgress(ratingInt);
    }

    private void setMovieTitle(String title) {
        mTvMovieTitle.setText(title);
    }

    private void loadMainImage(String path) {
        mImvPoster.setTransitionName(transitionName);

        loadImageFromCache(true, path);

        TransitionSet set = new TransitionSet();
        set.addTransition(new ChangeImageTransform());
        set.addTransition(new ChangeBounds());
        set.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                loadImageFromCache(false, path);
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });

        getWindow().setSharedElementEnterTransition(set);

        if (activityType.equalsIgnoreCase(AppConstants.ACTIVITY_FAVOURITE)) {
            getWindow().setSharedElementExitTransition(null);
            mImvPoster.setTransitionName(null);
        }
    }

    private void loadBackDropImage(String backdropPath) {
        Glide.with(this)
                .load(BACKDROP_BASE_PATH + backdropPath)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_action_clapper)
                        .error(R.drawable.ic_action_clapper))
                .into(mImvBackDrop);
    }

    private void loadImageFromCache(boolean retrieveFromCache, String path) {
        Glide.with(this)
                .load(POSTER_BASE_PATH + path)
                .apply(new RequestOptions()
                        .placeholder(roundedBitmapDrawable)
                        .error(roundedBitmapDrawable)
                        .dontAnimate()
                        .dontTransform()
                        .onlyRetrieveFromCache(retrieveFromCache))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(mImvPoster);
    }

    private void loadPlaceholder() {
        Bitmap placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_clapper);
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), placeholder);
        roundedBitmapDrawable.setCornerRadius(25F);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

}
