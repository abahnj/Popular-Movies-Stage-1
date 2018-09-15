package com.abahnj.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abahnj.popularmovies.R;
import com.abahnj.popularmovies.data.CastEntry;
import com.abahnj.popularmovies.data.FavMovieEntry;
import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.data.ReviewEntry;
import com.abahnj.popularmovies.data.VideoEntry;
import com.abahnj.popularmovies.ui.adapter.CastAdapter;
import com.abahnj.popularmovies.ui.adapter.GenreAdapter;
import com.abahnj.popularmovies.ui.adapter.TrailersAdapter;
import com.abahnj.popularmovies.utils.AppBarStateChangeListener;
import com.abahnj.popularmovies.utils.AppUtils;
import com.abahnj.popularmovies.utils.Constants;
import com.abahnj.popularmovies.utils.GlideApp;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.abahnj.popularmovies.utils.AppBarStateChangeListener.State.COLLAPSED;
import static com.abahnj.popularmovies.utils.AppBarStateChangeListener.State.EXPANDED;
import static com.abahnj.popularmovies.utils.Constants.ACTIVITY_NORMAL;
import static com.abahnj.popularmovies.utils.Constants.ACTIVITY_TYPE;
import static com.abahnj.popularmovies.utils.Constants.BACKDROP_BASE_PATH;
import static com.abahnj.popularmovies.utils.Constants.MOVIE_ID_INTENT;
import static com.abahnj.popularmovies.utils.Constants.MOVIE_IMAGE_TRANSITION;
import static com.abahnj.popularmovies.utils.Constants.POSTER_BASE_PATH;
import static com.abahnj.popularmovies.utils.Constants.YOUTUBE;
import static com.abahnj.popularmovies.utils.Constants.YOUTUBE_THUMBNAIL;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.imv_back_drop)
    ImageView mImvBackDrop;
    @BindView(R.id.imv_poster)
    ImageView mImvPoster;
    @BindView(R.id.tv_title)
    TextView mTvMovieTitle;
    @BindView(R.id.tv_ratings)
    TextView mTvRating;
    @BindView(R.id.tv_release_date)
    TextView mTvReleaseDate;
    @BindView(R.id.tv_plot_details)
    TextView mTvPlotDetails;
    @BindView(R.id.tv_movie_name)
    TextView mToolbarMovieName;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(android.R.id.content)
    View snackBarView;
    @BindView(R.id.progress_rating)
    ProgressBar mProgressRating;
    @BindView(R.id.progress_detail)
    ProgressBar mProgressBar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.rv_genres)
    RecyclerView mRvGenres;
    @BindView(R.id.rv_cast)
    RecyclerView mRvCast;
    @BindView(R.id.lay_cast)
    View mLayCast;
    @BindView(R.id.lay_trailer)
    View mLayTrailer;
    @BindView(R.id.rv_trailers)
    RecyclerView mRvTrailers;
    @BindView(R.id.tv_review_person)
    TextView mTvReviewPerson;
    @BindView(R.id.tv_review_body)
    TextView mTvReviewBody;
    @BindView(R.id.tv_see_all_reviews)
    TextView mTvSeeAllReviews;
    @BindView(R.id.lay_reviews)
    View mLayReviews;
    @BindView(R.id.imv_favourite)
    ImageView mImvFavourite;


    private DetailViewModel detailViewModel;
    private CastAdapter castAdapter;
    private GenreAdapter genreAdapter;
    private RoundedBitmapDrawable roundedBitmapDrawable;
    private String transitionName;
    private String activityType;
    private int movieId;
    private List<VideoEntry> videos = new ArrayList<>();
    private String videoKey;
    private TrailersAdapter trailersAdapter;
    private List<ReviewEntry> reviews = new ArrayList<>();
    private boolean isMovieFav;
    private FavMovieEntry favMovieEntity, tempFavMovieEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            transitionName = extras.getString(MOVIE_IMAGE_TRANSITION);
            movieId = extras.getInt(MOVIE_ID_INTENT);
            activityType = extras.getString(ACTIVITY_TYPE);
        }
        initData();
        loadPlaceholder();
        populateUi();
        supportPostponeEnterTransition();

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.TRANSITION, transitionName);
        outState.putInt(Constants.MOVIE_ID, movieId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            transitionName = savedInstanceState.getString(Constants.TRANSITION);
            movieId = savedInstanceState.getInt(Constants.MOVIE_ID);
        }
    }

    private void initViews() {
        setContentView(R.layout.activity_detail);
        supportPostponeEnterTransition();
        ButterKnife.bind(this);
    }

    private void initData() {
        DetailViewModelFactory factory = DetailViewModelFactory.getInstance(this);

        detailViewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

        detailViewModel.start(movieId);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        genreAdapter = new GenreAdapter(this);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        mRvGenres.setLayoutManager(flowLayoutManager);
        mRvGenres.setAdapter(genreAdapter);

        LinearLayoutManager llmCast = new LinearLayoutManager(this);
        llmCast.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvCast.setLayoutManager(llmCast);
        castAdapter = new CastAdapter(this);
        mRvCast.setAdapter(castAdapter);
        mRvCast.setNestedScrollingEnabled(true);
        mRvCast.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildViewHolder(view).getAdapterPosition();
                int itemCount = state.getItemCount();

                outRect.left = dpToPx();
                outRect.right = position == itemCount - 1 ? dpToPx() : 0;
                outRect.top = 0;
                outRect.bottom = 0;
            }
        });

        LinearLayoutManager llmTrailer = new LinearLayoutManager(this);
        llmTrailer.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvTrailers.setLayoutManager(llmTrailer);
        trailersAdapter = new TrailersAdapter(this);
        mRvTrailers.setAdapter(trailersAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRvTrailers);

    }

    private void setUpToolbarTitle(String movieName) {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                mAppBarLayout.post(() -> {
                    if (state == COLLAPSED) {
                        mToolbarMovieName.setText(movieName);
                    } else if (state == EXPANDED || state == State.IDLE) {
                        mToolbarMovieName.setText(" ");
                    }
                });
            }
        });
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
                loadGenres(movie.getGenreIds());
            }
        });
        detailViewModel.loadFavMoviesById(movieId)
                .observe(this, favMovie -> {
                    mProgressBar.setVisibility(View.VISIBLE);
                    if (favMovie != null) {
                        isMovieFav = true;
                        mImvFavourite.setImageResource(R.drawable.ic_favorite);
                        getFavMovies(favMovie);
                    } else {
                        isMovieFav = false;
                        mImvFavourite.setImageResource(R.drawable.ic_favorite_border);
                        getData();
                    }
                });

    }

    private void setMoviePlotDetails(String overview) {
        mTvPlotDetails.setText(overview);
    }

    private void setMovieReleaseDate(String releaseDate) {
        mTvReleaseDate.setText(AppUtils.convertDate(releaseDate, Constants.DF1, Constants.DF2));
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

        if (activityType.equalsIgnoreCase(Constants.ACTIVITY_FAVOURITE)) {
            getWindow().setSharedElementExitTransition(null);
            mImvPoster.setTransitionName(null);
        }
    }


    private void loadBackDropImage(String backdropPath) {
        GlideApp.with(this)
                .load(BACKDROP_BASE_PATH + backdropPath)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_action_clapper)
                        .error(R.drawable.ic_action_clapper))
                .into(mImvBackDrop);
    }

    private void loadImageFromCache(boolean retrieveFromCache, String path) {
        GlideApp.with(this)
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

    private void loadGenres(List<Integer> genreIds) {
        detailViewModel.getGenresById(genreIds).observe(this, genreResource -> {
            if (genreResource != null) {
                switch (genreResource.getStatus()) {
                    case SUCCESS:
                        if (genreResource.getResponse() != null && !genreResource.getResponse().isEmpty()) {
                            mRvGenres.setVisibility(View.VISIBLE);
                            genreAdapter.addGenres(genreResource.getResponse());
                        }
                        break;
                    case LOADING:
                        mRvGenres.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        mRvGenres.setVisibility(View.GONE);
                        AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
                        break;
                }
            }
        });
    }


    private void getData() {
        detailViewModel.getCastResults().observe(this, castResults -> {
            if (castResults != null) {
                switch (castResults.getStatus()) {
                    case SUCCESS:
                        if (castResults.getResponse() != null && !castResults.getResponse().isEmpty()) {
                            mLayCast.setVisibility(View.VISIBLE);
                            List<CastEntry> castEntities = new ArrayList<>();
                            if (castResults.getResponse().size() > 10) {
                                for (int castIndex = 0; castIndex < 10; castIndex++) {
                                    castEntities.add(castResults.getResponse().get(castIndex));
                                }
                            } else {
                                castEntities.addAll(castResults.getResponse());
                            }
                            castAdapter.addCasts(castEntities);
                        }
                        break;
                    case LOADING:
                        mLayCast.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        mLayCast.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.GONE);
                        AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
                        break;
                }
            }
        });

        detailViewModel.getVideoResults().observe(this, videoResults -> {
            if (videoResults != null) {
                switch (videoResults.getStatus()) {
                    case SUCCESS:
                        if (videoResults.getResponse() != null && !videoResults.getResponse().isEmpty()) {
                            mLayTrailer.setVisibility(View.VISIBLE);
                            setVideoKey(videoResults.getResponse().get(Constants.FIRST_ITEM).getKey());
                            setVideos(videoResults.getResponse());
                            trailersAdapter.addTrailers(getVideos());
                        }
                        break;
                    case LOADING:
                        mLayTrailer.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        mLayTrailer.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.GONE);
                        AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
                        break;
                }
            }
        });

        detailViewModel.getReviewResult().observe(this, reviewResults -> {
            if (reviewResults != null) {
                switch (reviewResults.getStatus()) {
                    case SUCCESS:
                        if (reviewResults.getResponse() != null && !reviewResults.getResponse().isEmpty()) {
                            mLayReviews.setVisibility(View.VISIBLE);
                            mTvReviewPerson.setText(reviewResults.getResponse().get(0).getAuthor());
                            mTvReviewBody.setText(reviewResults.getResponse().get(0).getContent());
                            mTvSeeAllReviews.setVisibility(reviewResults.getResponse().size() < 2 ? View.GONE : View.VISIBLE);
                            setReviews(reviewResults.getResponse());
                        }
                        mProgressBar.setVisibility(View.GONE);
                        break;
                    case LOADING:
                        mProgressBar.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        mProgressBar.setVisibility(View.GONE);
                        AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
                        break;
                }
            }
        });

    }

    private void getFavMovies(FavMovieEntry favMovie) {
        detailViewModel.getFavCasts(favMovie.getCastIds()).observe(this, favMovieCasts -> {
            if (favMovieCasts != null && !favMovieCasts.isEmpty()) {
                mLayCast.setVisibility(View.VISIBLE);
                castAdapter.addCasts(favMovieCasts);
            }
        });

        detailViewModel.getFavReviews(favMovie.getMovieId()).observe(this, favMovieReviews -> {
            if (favMovieReviews != null && !favMovieReviews.isEmpty()) {
                mLayReviews.setVisibility(View.VISIBLE);
                mTvReviewPerson.setText(favMovieReviews.get(Constants.FIRST_ITEM).getAuthor());
                mTvReviewBody.setText(favMovieReviews.get(Constants.FIRST_ITEM).getContent());
                mTvSeeAllReviews.setVisibility(favMovieReviews.size() < 2 ? View.GONE : View.VISIBLE);
                setReviews(favMovieReviews);
            }
        });

        detailViewModel.getFavVideos(favMovie.getMovieId()).observe(this, favMoviesVideo -> {
            if (favMoviesVideo != null && !favMoviesVideo.isEmpty()) {
                mLayTrailer.setVisibility(View.VISIBLE);
                setVideoKey(favMoviesVideo.get(Constants.FIRST_ITEM).getKey());
                setVideos(favMoviesVideo);
                trailersAdapter.addTrailers(favMoviesVideo);
            }
        });

        mProgressBar.setVisibility(View.GONE);
    }

    private int dpToPx() {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void saveFavMovies(int movieId) {
        List<Integer> castIds = new ArrayList<>();
        List<CastEntry> favMovieCast = new ArrayList<>();
        List<ReviewEntry> favMovieReviews = new ArrayList<>();
        List<VideoEntry> favMovieTrailers = new ArrayList<>();

        detailViewModel.getCastResults().observe(this, castListResource -> {
            if (castListResource != null) {
                if (castListResource.getResponse() != null && !castListResource.getResponse().isEmpty()) {
                    for (CastEntry item : castListResource.getResponse()) {
                        castIds.add(item.getId());
                        favMovieCast.add(new CastEntry(item.getId(), item.getName(), item.getProfilePath()));
                        if (castIds.size() > 10) break;
                    }
                }
            }
        });

        detailViewModel.getReviewResult().observe(this, reviewListResource -> {
            if (reviewListResource != null) {
                if (reviewListResource.getResponse() != null) {
                    for (ReviewEntry item : reviewListResource.getResponse()) {
                        favMovieReviews.add(new ReviewEntry(
                                item.getAuthor(),
                                item.getContent(),
                                item.getId(),
                                item.getUrl(),
                                movieId));
                    }
                }
            }
        });

        detailViewModel.getVideoResults().observe(this, videoListResource -> {
            if (videoListResource != null) {
                if (videoListResource.getResponse() != null) {
                    for (VideoEntry results : videoListResource.getResponse()) {
                        favMovieTrailers.add(new VideoEntry(
                                results.getId(),
                                results.getKey(),
                                results.getName(),
                                results.getSite(),
                                results.getType(),
                                movieId));
                    }
                }
            }
        });

        detailViewModel.getMovieResult().observe(this, movie -> {
            if (movie != null) {
                favMovieEntity = new FavMovieEntry(
                        movie.getMovieId(),
                        movie.getVoteCount(),
                        movie.getVoteAverage(),
                        movie.getTitle(),
                        movie.getPosterPath(),
                        movie.getGenreIds(),
                        movie.getBackdropPath(),
                        movie.getOverview(),
                        movie.getReleaseDate(),
                        castIds,
                        System.currentTimeMillis());
            } else {
                detailViewModel.loadFavMoviesById(movieId).observe(this, favMovie -> {
                    if (favMovie != null) {
                        favMovieEntity = new FavMovieEntry(
                                favMovie.getMovieId(),
                                favMovie.getVoteCount(),
                                favMovie.getVoteAverage(),
                                favMovie.getTitle(),
                                favMovie.getPosterPath(),
                                favMovie.getGenreIds(),
                                favMovie.getBackdropPath(),
                                favMovie.getOverview(),
                                favMovie.getReleaseDate(),
                                castIds,
                                System.currentTimeMillis());
                    }
                });
            }
        });

        if (favMovieEntity != null) {
            detailViewModel.saveFavMovies(favMovieEntity);
        } else {
            detailViewModel.saveFavMovies(tempFavMovieEntity);
        }

        if (!castIds.isEmpty() && !favMovieCast.isEmpty()) {
            detailViewModel.saveFavCast(favMovieCast);
        }

        if (!favMovieReviews.isEmpty()) {
            detailViewModel.saveFavReviews(favMovieReviews);
        }

        if (!favMovieTrailers.isEmpty()) {
            detailViewModel.saveFavTrailers(favMovieTrailers);
        }

        AppUtils.setSnackBar(snackBarView, "Added to favorites");
    }


    @OnClick(R.id.imv_favourite)
    public void onFavClicked() {
        if (isMovieFav) {
            isMovieFav = false;
            deleteFavMovies();
            getData();
        } else {
            isMovieFav = true;
            saveFavMovies(movieId);
        }
    }

    private void deleteFavMovies() {
        detailViewModel.deleteMovieById(movieId).observe(this, integer -> AppUtils.setSnackBar(snackBarView, "Removed from favorites"));
    }

    public List<VideoEntry> getVideos() {
        return videos;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public void setVideos(List<VideoEntry> videos) {
        if (!this.videos.isEmpty()) {
            this.videos.clear();
        }
        this.videos = videos;
    }

    public List<ReviewEntry> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntry> reviews) {
        if (!this.reviews.isEmpty()) {
            this.reviews.clear();
        }
        this.reviews = reviews;
    }
}
