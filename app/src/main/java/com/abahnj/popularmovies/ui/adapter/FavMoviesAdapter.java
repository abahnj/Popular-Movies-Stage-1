package com.abahnj.popularmovies.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abahnj.popularmovies.R;
import com.abahnj.popularmovies.data.FavMovieEntry;
import com.abahnj.popularmovies.interfaces.MovieClickListener;
import com.abahnj.popularmovies.utils.AppUtils;
import com.abahnj.popularmovies.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class FavMoviesAdapter extends RecyclerView.Adapter<FavMoviesAdapter.MyViewHolder> {

    private List<FavMovieEntry> movieList;
    private Context context;
    private final MovieClickListener movieItemClickListener;

    public FavMoviesAdapter(Context context) {
        this.context = context;
        try {
            this.movieItemClickListener = ((MovieClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement the interfaces");
        }
    }

    public void addMoviesList(List<FavMovieEntry> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        } else {
            return movieList.size();
        }
    }

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

            final FavMovieEntry data = movieList.get(position);

            Bitmap placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_clapper);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), placeholder);
            roundedBitmapDrawable.setCornerRadius(25F);

            if (data.getPosterPath() != null) {
                Glide.with(context)
                        .load(Constants.POSTER_BASE_PATH + data.getPosterPath())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .placeholder(roundedBitmapDrawable)
                                .error(roundedBitmapDrawable))
                        .apply(RequestOptions
                                .bitmapTransform(new RoundedCornersTransformation(25, 0)))
                        .into(mImvMovieImage);
            }

            if (data.getTitle() != null) {
                mTvMovieTitle.setText(data.getTitle());
            }

            if (data.getReleaseDate() != null) {
                mTvReleaseDate.setText(AppUtils.convertDate(data.getReleaseDate(), Constants.DF1, Constants.DF3));
            }

            if (data.getVoteAverage() != null) {
                mTvRatings.setText(String.valueOf(data.getVoteAverage()));
            }
        }

        @OnClick
        void onClick(View view) {
            String transitionName = "movie_item_" + String.valueOf(getAdapterPosition());
            movieItemClickListener.onMovieEntryClick(
                    getAdapterPosition(),
                    movieList.get(getAdapterPosition()).getMovieId(),
                    mImvMovieImage,
                    transitionName,
                    Constants.ACTIVITY_FAVOURITE);
        }
    }
}
