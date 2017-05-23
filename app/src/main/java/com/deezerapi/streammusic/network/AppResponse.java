package com.deezerapi.streammusic.network;

import com.deezerapi.streammusic.AppException;

/**
 * Created by nishant on 22.05.17.
 */

public class AppResponse<T> {

    private boolean isSuccess;

    private T t;

    private AppException appException;

    public AppResponse(boolean isSucess, T t, AppException appException) {
        this.isSuccess = isSucess;
        this.t = t;
        this.appException = appException;
    }

    public T getSuccessResponse() {
        return t;
    }

    public AppException getErrorResposne() {
        return appException;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

}
