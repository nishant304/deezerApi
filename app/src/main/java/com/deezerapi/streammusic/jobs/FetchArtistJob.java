package com.deezerapi.streammusic.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobHolder;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.deezerapi.streammusic.App;
import com.deezerapi.streammusic.AppException;
import com.deezerapi.streammusic.api.ApiService;
import com.deezerapi.streammusic.events.artist.FetchArtistEvent;
import com.deezerapi.streammusic.model.ArtistSearchResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Response;

/**
 * Created by nishant on 17.05.17.
 */

public class FetchArtistJob extends BaseJob {

    private ApiService apiService;

    private String qString;

    private String index;

    private long reqTime = System.currentTimeMillis();

    public FetchArtistJob(String query,String tag, String index){
        super(new Params(1).requireNetwork().persist().addTags(tag));
        this.qString = query;
        this.index = index;
    }

    @Override
    public void onRun() throws Throwable {
        Response<ArtistSearchResponse> response = App.getApiService().searchArtist(qString,index).execute();
        if(response.isSuccessful()){
            EventBus.getDefault().post(new FetchArtistEvent(response.body(),reqTime,true));
        }else {
            throw new AppException(response.code());
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        if(cancelReason != CancelReason.CANCELLED_WHILE_RUNNING) {
            EventBus.getDefault().post(new FetchArtistEvent(null, reqTime, false));
        }
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new OnJobAdded());
    }

    public static class OnJobAdded{

    }

}
