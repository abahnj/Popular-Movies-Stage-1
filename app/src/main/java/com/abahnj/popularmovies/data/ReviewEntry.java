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


@Entity(tableName = "reviews",
        indices = @Index("fav_movie_id"),
        foreignKeys = @ForeignKey(
                entity = FavMovieEntry.class,
                parentColumns = "movieId",
                childColumns = "fav_movie_id",
                onDelete = CASCADE))
public class ReviewEntry implements Parcelable {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private final String id;
    @SerializedName("author")
    @Expose
    private final String author;
    @SerializedName("content")
    @Expose
    private final String content;
    @SerializedName("url")
    @Expose
    private final String url;
    @ColumnInfo(name = "fav_movie_id")
    private final Integer favMovieId;

    public ReviewEntry(String author, String content, @NonNull String id, String url, final Integer favMovieId) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
        this.favMovieId = favMovieId;
    }

    @Ignore
    protected ReviewEntry(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
        if (in.readByte() == 0) {
            favMovieId = null;
        } else {
            favMovieId = in.readInt();
        }
    }

    public static final Creator<ReviewEntry> CREATOR = new Creator<ReviewEntry>() {
        @Override
        public ReviewEntry createFromParcel(Parcel in) {
            return new ReviewEntry(in);
        }

        @Override
        public ReviewEntry[] newArray(int size) {
            return new ReviewEntry[size];
        }
    };

    @NonNull
    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
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
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
        if (favMovieId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(favMovieId);
        }
    }
}
