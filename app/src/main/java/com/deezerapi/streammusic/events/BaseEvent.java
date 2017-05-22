package com.deezerapi.streammusic.events;

/**
 * Created by nishant on 22.05.17.
 */

public class BaseEvent {

    private long reqTime;

    private boolean isSuccess;

    public BaseEvent(long reqTime, boolean isSuccess){
        this.reqTime = reqTime;
        this.isSuccess = isSuccess;
    }

    public long getReqTime() {
        return reqTime;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

}
