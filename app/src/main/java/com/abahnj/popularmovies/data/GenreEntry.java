package com.abahnj.popularmovies.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "genres")
public class GenreEntry implements Parcelable
{
    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer genreId;

    @SerializedName("name")
    @Expose
    private String genreName;
    public final static Parcelable.Creator<GenreEntry> CREATOR = new Creator<GenreEntry>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GenreEntry createFromParcel(Parcel in) {
            return new GenreEntry(in);
        }

        public GenreEntry[] newArray(int size) {
            return (new GenreEntry[size]);
        }

    };

    @Ignore
    protected GenreEntry(Parcel in) {
        this.genreId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.genreName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public GenreEntry(@NonNull Integer genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }


    @NonNull
    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(@NonNull Integer genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(genreId);
        dest.writeValue(genreName);
    }

    public int describeContents() {
        return 0;
    }

}