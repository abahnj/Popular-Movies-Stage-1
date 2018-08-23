package com.abahnj.popularmovies.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abahnj.popularmovies.R;
import com.abahnj.popularmovies.data.MovieEntry;
import com.abahnj.popularmovies.interfaces.MovieClickListener;
import com.abahnj.popularmovies.utils.AppConstants;
import com.abahnj.popularmovies.utils.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieListAdapter extends ListAdapter<MovieEntry, MovieListAdapter.MyViewHolder> {

    private Context context;
    private final MovieClickListener movieEntryClickListener;

   /* public MovieListAdapter(Context context) {
        this.context = context;
        try {
            this.movieEntryClickListener = ((MovieClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement the interfaces");
        }
    }*/

    MovieListAdapter(Context context, MovieClickListener movieEntryClickListener) {
        super(DIFF_CALLBACK);
        this.movieEntryClickListener = movieEntryClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_movie_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
    }

    private static final DiffUtil.ItemCallback<MovieEntry> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MovieEntry>() {
                @Override
                public boolean areItemsTheSame(@NonNull MovieEntry movieEntry, @NonNull MovieEntry t1) {
                    return movieEntry.getMovieId().equals(t1.getMovieId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull MovieEntry movieEntry, @NonNull MovieEntry t1) {
                    return movieEntry.equals(t1);
                }
            };


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_movie_poster)
        ImageView mImvMovieImage;

        @BindView(R.id.tv_movie_title)
        TextView mTvMovieTitle;

        @BindView(R.id.tv_release_date)
        TextView mTvReleaseDate;

        @BindView(R.id.tv_movie_rating)
        TextView mTvRatings;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(final int position) {

            final MovieEntry movieEntry = getItem(position);

            Bitmap placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_clapper);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), placeholder);
            roundedBitmapDrawable.setCornerRadius(25F);

            if (movieEntry.getPosterPath() != null) {
                Glide.with(context)
                        .load(AppConstants.POSTER_BASE_PATH + movieEntry.getPosterPath())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .placeholder(roundedBitmapDrawable)
                                .error(roundedBitmapDrawable))
                        .into(mImvMovieImage);
            }

            if (movieEntry.getTitle() != null) {
                mTvMovieTitle.setText(movieEntry.getTitle());
            }

            if (movieEntry.getReleaseDate() != null) {
                mTvReleaseDate.setText(AppUtils.convertDate(movieEntry.getReleaseDate(), AppConstants.DF1, AppConstants.DF3));
            }

            if (movieEntry.getVoteAverage() != null) {
                mTvRatings.setText(String.valueOf(movieEntry.getVoteAverage()));
            }
        }

        @OnClick
        void onClick(View view) {
            String transitionName = "movie_item_" + String.valueOf(getAdapterPosition());
            movieEntryClickListener.onMovieEntryClick(
                    getAdapterPosition(),
                    getItem(getAdapterPosition()).getMovieId(),
                    mImvMovieImage,
                    transitionName,
                    AppConstants.ACTIVITY_NORMAL);
        }
    }
}
