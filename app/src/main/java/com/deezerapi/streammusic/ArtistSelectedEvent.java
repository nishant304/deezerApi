package com.deezerapi.streammusic;

import android.widget.ImageView;

/**
 * Created by nishant on 23.05.17.
 */
public  class ArtistSelectedEvent{

    public ImageView imageView;

    public String name ;

    public String url;

    public ArtistSelectedEvent(ImageView imageView, String name,String url){
        this.imageView = imageView;
        this.name = name;
        this.url = url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

