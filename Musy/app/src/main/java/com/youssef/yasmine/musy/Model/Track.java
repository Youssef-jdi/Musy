package com.youssef.yasmine.musy.Model;

import java.io.Serializable;

public class Track implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String Title;
    private String preview;
    private  String duration;
    private Artist artist;
    private Album album;
    private String trackList;

    public String getTrackList() {
        return trackList;
    }

    public void setTrackList(String trackList) {
        this.trackList = trackList;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Artist getArtiste() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Track() {
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", Title='" + Title + '\'' +
                ", preview='" + preview + '\'' +
                ", duration='" + duration + '\'' +
                ", artist=" + artist +
                ", album=" + album +
                '}';
    }
}
