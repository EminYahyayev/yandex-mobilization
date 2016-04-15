package com.ewintory.yandex.mobilization.model;

import android.support.annotation.StringDef;

import java.util.List;
import java.util.Map;

/**
 * A model class for an artist
 */
public final class Artist {

    public static final String COVER_TYPE_SMALL = "small";
    public static final String COVER_TYPE_BIG = "big";

    @StringDef({COVER_TYPE_BIG, COVER_TYPE_SMALL})
    public @interface CoverType {}

    private long id;
    private String name;
    private List<String> genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private Map<String, String> covers;

    public long getId() {
        return id;
    }

    public Artist setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Artist setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getGenres() {
        return genres;
    }

    public Artist setGenres(List<String> genres) {
        this.genres = genres;
        return this;
    }

    public int getTracks() {
        return tracks;
    }

    public Artist setTracks(int tracks) {
        this.tracks = tracks;
        return this;
    }

    public int getAlbums() {
        return albums;
    }

    public Artist setAlbums(int albums) {
        this.albums = albums;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Artist setLink(String link) {
        this.link = link;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Artist setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<String, String> getCovers() {
        return covers;
    }

    public Artist setCovers(Map<String, String> covers) {
        this.covers = covers;
        return this;
    }

    public String getCover(@CoverType String coverType) {
        return covers.get(coverType);
    }

    @Override public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genres=" + genres +
                ", tracks=" + tracks +
                ", albums=" + albums +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", covers=" + covers +
                '}';
    }
}
