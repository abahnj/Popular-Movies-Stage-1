package com.abahnj.popularmovies.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


import com.abahnj.popularmovies.data.source.local.converter.IntegerListConvertor;

import java.util.List;


@Entity(tableName = "fav_movies")
public class FavMovieEntry implements Parcelable {
    @PrimaryKey
    @NonNull
    private final Integer movieId;
    private final Integer voteCount;
    private final Double voteAverage;
    private final String title;
    private final String posterPath;
    @TypeConverters(IntegerListConvertor.class)
    private final List<Integer> genreIds;
    private final String backdropPath;
    private final String overview;
    private final String releaseDate;
    @TypeConverters(IntegerListConvertor.class)
    private final List<Integer> castIds;
    private final Long createdAt;

    public FavMovieEntry(@NonNull final Integer movieId, final Integer voteCount, final Double voteAverage,
                         String title, String posterPath, final List<Integer> genreIds, String backdropPath,
                         String overview, String releaseDate, final List<Integer> castIds, final long createdAt) {
        this.movieId = movieId;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.title = title;
        this.posterPath = posterPath;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.castIds = castIds;
        this.createdAt = createdAt;
    }

    protected FavMovieEntry(Parcel in) {
        if (in.readByte() == 0) {
            movieId = null;
        } else {
            movieId = in.readInt();
        }
        if (in.readByte() == 0) {
            voteCount = null;
        } else {
            voteCount = in.readInt();
        }
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0) {
            createdAt = null;
        } else {
            createdAt = in.readLong();
        }
        castIds = in.readArrayList(Integer.class.getClassLoader());
        genreIds = in.readArrayList(Integer.class.getClassLoader());
    }

    public static final Creator<FavMovieEntry> CREATOR = new Creator<FavMovieEntry>() {
        @Override
        public FavMovieEntry createFromParcel(Parcel in) {
            return new FavMovieEntry(in);
        }

        @Override
        public FavMovieEntry[] newArray(int size) {
            return new FavMovieEntry[size];
        }
    };

    @NonNull
    public Integer getMovieId() {
        return movieId;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Integer> getCastIds() {
        return castIds;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (movieId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(movieId);
        }
        if (voteCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(voteCount);
        }
        if (voteAverage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(voteAverage);
        }
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        if (createdAt == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(createdAt);
        }
    }
}
