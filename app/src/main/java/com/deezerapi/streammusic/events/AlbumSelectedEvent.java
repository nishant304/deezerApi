package com.deezerapi.streammusic.events;

import android.widget.ImageView;

/**
 * Created by nishant on 23.05.17.
 */
public class AlbumSelectedEvent {

    public ImageView imageView;

    public int id;

    public String url;

    public AlbumSelectedEvent(ImageView imageView, int id, String url) {
        this.imageView = imageView;
        this.id = id;
        this.url = url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

}