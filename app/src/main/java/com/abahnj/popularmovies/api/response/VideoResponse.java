package com.abahnj.popularmovies.api.response;

import com.abahnj.popularmovies.data.VideoEntry;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<VideoEntry> results;

    public VideoResponse(Integer id, List<VideoEntry> results) {
        this.id = id;
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public List<VideoEntry> getResults() {
        return results;
    }
}
