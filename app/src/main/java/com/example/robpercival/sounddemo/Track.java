package com.example.robpercival.sounddemo;

public class Track {

    private String title;
    private long id;
    private String artist;

    public Track(long id, String artist, String title) {
        this.title = title;
        this.id = id;
        this.artist = artist;
    }

    public Track() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Track{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", artist='" + artist + '\'' +
                '}';
    }
}
