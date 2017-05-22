package com.deezerapi.streammusic.events.artist;

import com.deezerapi.streammusic.events.BaseEvent;
import com.deezerapi.streammusic.model.ArtistSearchResponse;

/**
 * Created by nishant on 22.05.17.
 */

public class FetchArtistEvent  extends BaseEvent{

    private ArtistSearchResponse artistSearchResponse;

    public FetchArtistEvent(ArtistSearchResponse artistSearchResponse,long startTime, boolean isSuccess){
        super(startTime,isSuccess);
        this.artistSearchResponse = artistSearchResponse;
    }

    public ArtistSearchResponse getArtistSearchResponse() {
        return artistSearchResponse;
    }

}
