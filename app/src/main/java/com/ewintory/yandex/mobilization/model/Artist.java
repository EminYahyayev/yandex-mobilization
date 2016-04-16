package com.ewintory.yandex.mobilization.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * A model class for an artist
 */
public final class Artist implements Parcelable {

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        @Override public Artist createFromParcel(Parcel source) {return new Artist(source);}

        @Override public Artist[] newArray(int size) {return new Artist[size];}
    };
    private long id;
    private String name;
    private List<String> genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private String smallCover;
    private String bigCover;

    public Artist() {}

    protected Artist(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.genres = in.createStringArrayList();
        this.tracks = in.readInt();
        this.albums = in.readInt();
        this.link = in.readString();
        this.description = in.readString();
        this.smallCover = in.readString();
        this.bigCover = in.readString();
    }

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

    public String getSmallCover() {
        return smallCover;
    }

    public Artist setSmallCover(String smallCover) {
        this.smallCover = smallCover;
        return this;
    }

    public String getBigCover() {
        return bigCover;
    }

    public Artist setBigCover(String bigCover) {
        this.bigCover = bigCover;
        return this;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genres=" + genres +
                ", tracks=" + tracks +
                ", albums=" + albums +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", smallCover='" + smallCover + '\'' +
                ", bigCover='" + bigCover + '\'' +
                '}';
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeStringList(this.genres);
        dest.writeInt(this.tracks);
        dest.writeInt(this.albums);
        dest.writeString(this.link);
        dest.writeString(this.description);
        dest.writeString(this.smallCover);
        dest.writeString(this.bigCover);
    }
}
