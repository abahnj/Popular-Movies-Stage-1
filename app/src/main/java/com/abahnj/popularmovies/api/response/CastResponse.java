package com.abahnj.popularmovies.api.response;

import com.abahnj.popularmovies.data.CastEntry;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastResponse {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("cast")
    @Expose
    private List<CastEntry> cast;

    public CastResponse(Integer id, List<CastEntry> cast) {
        this.id = id;
        this.cast = cast;
    }

    public Integer getId() {
        return id;
    }

    public List<CastEntry> getCast() {
        return cast;
    }

}
