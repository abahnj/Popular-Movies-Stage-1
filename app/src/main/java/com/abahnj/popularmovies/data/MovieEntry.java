package com.abahnj.popularmovies.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies")
public class MovieEntry implements Serializable {

    public MovieEntry(Integer _id,
                      Integer movieId,
                      Boolean video,
                      Double voteAverage,
                      String title,
                      Double popularity,
                      String posterPath,
                      String originalLanguage,
                      String originalTitle,
                      List<Integer> genreIds,
                      String backdropPath,
                      Boolean adult,
                      String overview,
                      String releaseDate,
                      Integer voteCount) {
        this._id =_id;
        this.movieId = movieId;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
    }

    @PrimaryKey(autoGenerate = true)
    private Integer _id;
    @SerializedName("id")
    @Expose
    private Integer movieId;

    @SerializedName("video")
    @Expose
    private Boolean video;

    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("popularity")
    @Expose
    private Double popularity;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("adult")
    @Expose
    private Boolean adult;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    public Integer getId() {
        return _id;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public Boolean getVideo() {
        return video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getVoteCount() {
        return voteCount;
    }
    public boolean isEmpty() {
        return Strings.isNullOrEmpty(title) &&
                Strings.isNullOrEmpty(posterPath);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieEntry entry = (MovieEntry) o;
        return Objects.equal(movieId, entry.movieId) &&
                Objects.equal(title, entry.title) &&
                Objects.equal(posterPath, entry.posterPath);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(movieId, title, posterPath);
    }

    @Override
    public String toString() {
        return "Movie with title " + title;
    }

}