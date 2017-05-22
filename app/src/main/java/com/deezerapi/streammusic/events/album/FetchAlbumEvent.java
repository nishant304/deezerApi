package com.deezerapi.streammusic.events.album;

import com.deezerapi.streammusic.events.BaseEvent;
import com.deezerapi.streammusic.model.AlbumSearchResponse;

/**
 * Created by nishant on 22.05.17.
 */

public class FetchAlbumEvent extends BaseEvent {

    private AlbumSearchResponse albumSearchResponse;

    public FetchAlbumEvent(AlbumSearchResponse albumSearchResponse, long reqTime, boolean isSuccess){
        super(reqTime,isSuccess);
        this.albumSearchResponse = albumSearchResponse;
    }

    public AlbumSearchResponse getAlbumSearchResponse() {
        return albumSearchResponse;
    }

}
