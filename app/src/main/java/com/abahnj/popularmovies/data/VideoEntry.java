package com.abahnj.popularmovies.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "videos",
        indices = @Index("fav_movie_id"),
        foreignKeys = @ForeignKey(
                entity = FavMovieEntry.class,
                parentColumns = "movieId",
                childColumns = "fav_movie_id",
                onDelete = CASCADE))
public class VideoEntry implements Parcelable {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private final String id;
    @SerializedName("key")
    @Expose
    private final String key;
    @SerializedName("name")
    @Expose
    private final String name;
    @SerializedName("site")
    @Expose
    private final String site;
    @SerializedName("type")
    @Expose
    private final String type;
    @ColumnInfo(name = "fav_movie_id")
    private final Integer favMovieId;

    public VideoEntry(@NonNull String id, String key, String name, String site, String type, Integer favMovieId) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
        this.favMovieId = favMovieId;
    }

    @Ignore
    protected VideoEntry(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        type = in.readString();
        if (in.readByte() == 0) {
            favMovieId = null;
        } else {
            favMovieId = in.readInt();
        }
    }

    public static final Creator<VideoEntry> CREATOR = new Creator<VideoEntry>() {
        @Override
        public VideoEntry createFromParcel(Parcel in) {
            return new VideoEntry(in);
        }

        @Override
        public VideoEntry[] newArray(int size) {
            return new VideoEntry[size];
        }
    };

    @NonNull
    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public Integer getFavMovieId() {
        return favMovieId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(type);
        if (favMovieId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(favMovieId);
        }
    }
}
