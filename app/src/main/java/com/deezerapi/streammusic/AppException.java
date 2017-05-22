package com.deezerapi.streammusic;

/**
 * Created by nishant on 21.05.17.
 */

public class AppException extends Throwable {

    int errorCode;

    public AppException(int errorCode){
        this.errorCode = errorCode;
    }

    public AppException(String message){
        super(message);
    }

    public boolean shouldRetry(){
        return errorCode <400 || errorCode >499;
    }

}
