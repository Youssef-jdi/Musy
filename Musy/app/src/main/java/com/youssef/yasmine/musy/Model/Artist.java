package com.youssef.yasmine.musy.Model;

import java.io.Serializable;

public class Artist implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String picture;
    private int nb_album;
    private int nb_fan;
    private String tracklist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getNb_album() {
        return nb_album;
    }

    public void setNb_album(int nb_album) {
        this.nb_album = nb_album;
    }

    public int getNb_fan() {
        return nb_fan;
    }

    public void setNb_fan(int nb_fan) {
        this.nb_fan = nb_fan;
    }

    public String getTracklist() {
        return tracklist;
    }

    public void setTracklist(String tracklist) {
        this.tracklist = tracklist;
    }

    public Artist() {
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", nb_album=" + nb_album +
                ", nb_fan=" + nb_fan +
                ", tracklist='" + tracklist + '\'' +
                '}';
    }
}
