package com.youssef.yasmine.musy.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String cover;
    private int nb_tracks;
    private int duration;
    private int nb_fans;
    private Date release_date;
    private String trackList;
    private Artist artist;
    private List<Track> lstTracks;
    private List<Genre> lstGenre;

    public Album(String id, String title, String cover, int nb_tracks, int duration, int nb_fans,
                 Date release_date, String trackList, Artist artist, List<Track> lstTracks, List<Genre> lstGenre) {
        this.id = id;
        this.title = title;
        this.cover = cover;
        this.nb_tracks = nb_tracks;
        this.duration = duration;
        this.nb_fans = nb_fans;
        this.release_date = release_date;
        this.trackList = trackList;
        this.artist = artist;
        this.lstTracks = lstTracks;
        this.lstGenre = lstGenre;
    }


    public Album() {
    }

    public Album(String id, String title, String cover) {
        this.id = id;
        this.title = title;
        this.cover = cover;
    }

    public Album(String title, int nb_tracks, String cover, String tracklist) {
        this.title = title;
        this.nb_tracks =nb_tracks;
        this.cover=cover;
        this.trackList= tracklist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getNb_tracks() {
        return nb_tracks;
    }

    public void setNb_tracks(int nb_tracks) {
        this.nb_tracks = nb_tracks;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getNb_fans() {
        return nb_fans;
    }

    public void setNb_fans(int nb_fans) {
        this.nb_fans = nb_fans;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public String getTrackList() {
        return trackList;
    }

    public void setTrackList(String tracklisturl) {
        this.trackList = tracklisturl;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<Track> getLstTracks() {
        return lstTracks;
    }

    public void setLstTracks(List<Track> lstTracks) {
        this.lstTracks = lstTracks;
    }

    public List<Genre> getLstGenre() {
        return lstGenre;
    }

    public void setLstGenre(List<Genre> lstGenre) {
        this.lstGenre = lstGenre;
    }

    public Album(String id, String title, String cover, int nb_fans, String trackList) {
        this.id = id;
        this.title = title;
        this.cover = cover;
        this.nb_fans = nb_fans;
        this.trackList = trackList;
    }
}
