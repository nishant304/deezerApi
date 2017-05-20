package com.deezerapi.streammusic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nishant on 20.05.17.
 */

public class TrackResponse {

    @SerializedName("data")
    @Expose
    private List<Track> tracks = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
