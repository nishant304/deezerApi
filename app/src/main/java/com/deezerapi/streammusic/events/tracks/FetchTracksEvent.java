package com.deezerapi.streammusic.events.tracks;

import com.deezerapi.streammusic.events.BaseEvent;
import com.deezerapi.streammusic.model.TrackResponse;

/**
 * Created by nishant on 22.05.17.
 */

public class FetchTracksEvent extends BaseEvent {

    private TrackResponse trackResponse;

    public FetchTracksEvent(TrackResponse trackResponse, long startTime, boolean isSucess){
        super(startTime,isSucess);
        this.trackResponse = trackResponse;
    }

    public TrackResponse getTrackResponse() {
        return trackResponse;
    }

}
