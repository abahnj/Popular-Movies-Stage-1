package com.abahnj.popularmovies.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abahnj.popularmovies.R;
import com.abahnj.popularmovies.data.CastEntry;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.abahnj.popularmovies.utils.Constants.POSTER_BASE_PATH;


public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {
    private List<CastEntry> castList;
    private Context context;

    public CastAdapter(Context context) {
        this.context = context;
    }

    public void addCasts(List<CastEntry> castList) {
        this.castList = castList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cast, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (castList != null) {
            return castList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_cast) ImageView imvCast;
        @BindView(R.id.tv_cast_name) TextView txvCast;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(final int position) {

            final CastEntry data = castList.get(position);

            MultiTransformation<Bitmap> multi = new MultiTransformation<>(
                    new CenterCrop(),
                    new CircleCrop());

            if (data.getProfilePath() != null) {
                Glide.with(context)
                        .load(POSTER_BASE_PATH + data.getProfilePath())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .placeholder(R.drawable.ic_action_clapper)
                                .error(R.drawable.ic_no_internet_connectivity))
                        .apply(RequestOptions
                                .bitmapTransform(multi))
                        .into(imvCast);
            }

            if (data.getName() != null) {
                txvCast.setText(data.getName());
            }
        }
    }
}
