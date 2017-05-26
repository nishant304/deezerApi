package com.deezerapi.streammusic.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.deezerapi.streammusic.App;
import com.deezerapi.streammusic.AppException;
import com.deezerapi.streammusic.events.tracks.FetchTracksEvent;
import com.deezerapi.streammusic.model.TrackResponse;
import com.deezerapi.streammusic.network.AppResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Response;

/**
 * Created by nishant on 20.05.17.
 */

public class FetchTracksJob extends BaseJob {

    private int alBumId;

    private String index;

    private long startTime = System.currentTimeMillis();

    public FetchTracksJob(int albumid, String index) {
        super(new Params(1).requireNetwork().persist());
        this.alBumId = albumid;
        this.index = index;
    }

    @Override
    public void onRun() throws Throwable {
        AppResponse<TrackResponse> response = App.getApiEndPoint().getTracksForAlbum(alBumId, index);
        if (response.isSuccess()) {
            EventBus.getDefault().postSticky(new FetchTracksEvent(response.getSuccessResponse(), startTime, true));
        } else {
            throw response.getErrorResposne();
        }
    }

    @Override
    public void onAdded() {

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        EventBus.getDefault().postSticky(new FetchTracksEvent(null, startTime, false));
    }

}
