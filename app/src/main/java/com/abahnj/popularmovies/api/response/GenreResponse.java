package com.abahnj.popularmovies.api.response;

import com.abahnj.popularmovies.data.GenreEntry;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GenreResponse {

    @SerializedName("genres")
    @Expose
    private List<GenreEntry> genres = null;

    public List<GenreEntry> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreEntry> genres) {
        this.genres = genres;
    }

}
