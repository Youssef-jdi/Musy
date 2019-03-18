package com.youssef.yasmine.musy.Model;

public class Playlist {

    private String id ;
    private String title;
    private int nb_tracks;
    private String picture;
    private  String trackList;
    private String user_id;


    public Playlist() {
    }

    public Playlist(String id, String title, int nb_tracks, String picture, String trackList, String user_id) {
        this.id = id;
        this.title = title;
        this.nb_tracks = nb_tracks;
        this.picture = picture;
        this.trackList = trackList;
        this.user_id = user_id;
    }

    public Playlist(String title, int nb_tracks, String imgPlaylist, String tracklist, String user_id) {
        this.title = title;
        this.nb_tracks = nb_tracks;
        this.picture = imgPlaylist;
        this.trackList = tracklist;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public int getNb_tracks() {
        return nb_tracks;
    }

    public void setNb_tracks(int nb_tracks) {
        this.nb_tracks = nb_tracks;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTrackList() {
        return trackList;
    }

    public void setTrackList(String trackList) {
        this.trackList = trackList;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", nb_tracks=" + nb_tracks +
                ", picture='" + picture + '\'' +
                ", trackList='" + trackList + '\'' +
                '}';
    }
}
