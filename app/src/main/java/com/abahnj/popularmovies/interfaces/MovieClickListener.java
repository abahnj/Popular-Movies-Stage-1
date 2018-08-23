package com.abahnj.popularmovies.interfaces;

import android.widget.ImageView;

public interface MovieClickListener {
    void onMovieEntryClick(int position, int movieId, ImageView shareImageView, String transitionName, String activityType);
}
