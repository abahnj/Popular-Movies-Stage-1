package com.abahnj.popularmovies.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "cast")
public class CastEntry implements Parcelable {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private final Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    public CastEntry(@NonNull Integer id, String name, String profilePath) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
    }

    @Ignore
    public CastEntry(Parcel in) {

        id = in.readInt();
        name = in.readString();
        profilePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) 1);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(profilePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CastEntry> CREATOR = new Creator<CastEntry>() {
        @Override
        public CastEntry createFromParcel(Parcel in) {
            return new CastEntry(in);
        }

        @Override
        public CastEntry[] newArray(int size) {
            return new CastEntry[size];
        }
    };

    @NonNull
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }
}
