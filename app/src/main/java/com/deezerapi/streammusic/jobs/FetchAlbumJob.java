package com.deezerapi.streammusic.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.deezerapi.streammusic.App;
import com.deezerapi.streammusic.AppException;
import com.deezerapi.streammusic.events.album.AlbumJobAdded;
import com.deezerapi.streammusic.events.album.FetchAlbumEvent;
import com.deezerapi.streammusic.model.AlbumSearchResponse;
import com.deezerapi.streammusic.network.AppResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Response;

/**
 * Created by nishant on 18.05.17.
 */

public class FetchAlbumJob extends BaseJob {

    private String qString;

    private long reqTime = System.currentTimeMillis();

    public FetchAlbumJob(String query,String tag){
        super(new Params(1).requireNetwork().persist().addTags(tag));
        this.qString = query;
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new AlbumJobAdded(reqTime,false));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        EventBus.getDefault().post(new FetchAlbumEvent(null,reqTime,false));
    }

    @Override
    public void onRun() throws Throwable {
          AppResponse<AlbumSearchResponse> response = App.getApiEndPoint().searchAlbum(qString);
          if(response.isSuccess()) {
              EventBus.getDefault().post(new FetchAlbumEvent(response.getSuccessResponse(),reqTime,true));
          }else{
              throw response.getErrorResposne();
          }
    }

}
