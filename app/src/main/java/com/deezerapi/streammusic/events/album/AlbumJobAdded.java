package com.deezerapi.streammusic.events.album;

import com.deezerapi.streammusic.events.BaseEvent;

/**
 * Created by nishant on 22.05.17.
 */

public class AlbumJobAdded extends BaseEvent {

    public AlbumJobAdded(long reqTime,boolean isSucess){
        super(reqTime,isSucess);
    }
}
