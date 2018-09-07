package com.abahnj.popularmovies.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abahnj.popularmovies.R;
import com.abahnj.popularmovies.data.VideoEntry;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.abahnj.popularmovies.utils.Constants.YOUTUBE;
import static com.abahnj.popularmovies.utils.Constants.YOUTUBE_THUMBNAIL;
import static com.abahnj.popularmovies.utils.Constants.YOUTUBE_URL;


public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MyViewHolder> {

    private List<VideoEntry> videoList;
    private Context context;

    public TrailersAdapter(Context context) {
        this.context = context;
    }

    public void addTrailers(List<VideoEntry> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (videoList != null) {
            return videoList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_video_thumb) ImageView imvVideoThumb;
        @BindView(R.id.tv_trailer_title) TextView tvTrailerTitle;
        //@BindView(R.id.tv_trailer_type) TextView tvTrailerType;
        //@BindView(R.id.imv_more) ImageView imvMore;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(final int position) {

            final VideoEntry data = videoList.get(position);

            if (data.getName() != null) {
                tvTrailerTitle.setText(data.getName());
            }

            if (data.getType() != null) {
                //tvTrailerType.setText(data.getType());
            }

            if (data.getKey() != null) {
                Glide.with(context)
                        .load(String.format(YOUTUBE_THUMBNAIL, data.getKey()))
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .placeholder(R.drawable.ic_action_clapper)
                                .error(R.drawable.ic_no_internet_connectivity))
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0)))
                        .into(imvVideoThumb);
            }

/*
            imvMore.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, imvMore);
                popup.inflate(R.menu.share);
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_share:
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Check out this video! Send from Popular Movies App\n" +
                                            Uri.parse(YOUTUBE_URL + data.getKey()));
                            context.startActivity(Intent.createChooser(shareIntent, "Share with"));
                            return true;
                        default:
                            popup.dismiss();
                            return false;
                    }
                });
                popup.show();
            });
*/

            imvVideoThumb.setOnClickListener(v -> {
                if (!videoList.isEmpty() && videoList.get(getAdapterPosition()).getSite().equals(YOUTUBE)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(YOUTUBE_URL + videoList.get(getAdapterPosition()).getKey()));
                    context.startActivity(intent);
                }
            });
        }

    }
}
