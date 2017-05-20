package com.deezerapi.streammusic.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.deezerapi.streammusic.App;
import com.deezerapi.streammusic.model.TrackResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Response;

/**
 * Created by nishant on 20.05.17.
 */

public class FetchTracksJob  extends Job{

    private int alBumId;

    public FetchTracksJob(int albumid){
        super(new Params(1).requireNetwork().persist());
        this.alBumId = albumid;
    }

    @Override
    public void onRun() throws Throwable {
        Response<TrackResponse> response = App.getApiService().getTracksForAlbum(alBumId).execute();
        if(response.isSuccessful()){
            EventBus.getDefault().post(response.body());
        }
    }

    @Override
    public void onAdded() {

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }

}
