package com.youssef.yasmine.musy.Model;

public class Video {
    private String id;
    private String video_path;
    private String track;
    private String id_user;

    public Video(String video_path, String track, String id_user) {
        this.video_path = video_path;
        this.track = track;
        this.id_user = id_user;
    }

    public Video() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
