package com.deezerapi.streammusic.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.deezerapi.streammusic.App;
import com.deezerapi.streammusic.AppException;
import com.deezerapi.streammusic.model.TrackResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Response;

/**
 * Created by nishant on 20.05.17.
 */

public class FetchTracksJob  extends BaseJob{

    private int alBumId;

    private String index;

    public FetchTracksJob(int albumid,String index){
        super(new Params(1).requireNetwork().persist());
        this.alBumId = albumid;
        this.index = index;
    }

    @Override
    public void onRun() throws Throwable {
        Response<TrackResponse> response = App.getApiService().getTracksForAlbum(alBumId,index).execute();
        if(response.isSuccessful()){
            EventBus.getDefault().post(response.body());
        }else{
            throw new AppException(response.code());
        }
    }

    @Override
    public void onAdded() {

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

}
