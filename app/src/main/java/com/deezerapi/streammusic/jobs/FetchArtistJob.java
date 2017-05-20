package com.deezerapi.streammusic.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.deezerapi.streammusic.App;
import com.deezerapi.streammusic.api.ApiService;
import com.deezerapi.streammusic.model.ArtistSearchResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Response;

/**
 * Created by nishant on 17.05.17.
 */

public class FetchArtistJob extends Job {

    private ApiService apiService;

    private String qString;

    public FetchArtistJob(String query,String tag){
        super(new Params(1).requireNetwork().persist().addTags(tag));
        this.qString = query;
    }

    @Override
    public void onRun() throws Throwable {
        Response<ArtistSearchResponse> response = App.getApiService().searchArtist(qString).execute();
        if(response.isSuccessful()){
            EventBus.getDefault().post(response.body());
        }

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Log.d("nishant","job cancelled");
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new OnJobAdded());
    }

    public static class OnJobAdded{

    }

}
