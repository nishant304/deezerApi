package com.deezerapi.streammusic.controller;

import com.birbit.android.jobqueue.JobManager;
import com.deezerapi.streammusic.App;
import com.deezerapi.streammusic.jobs.FetchTracksJob;
import com.deezerapi.streammusic.model.TrackResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by nishant on 21.05.17.
 */

public class TracksController {

    private JobManager jobManager = App.getJobManager();

    private int albumId;

    private int total;

    private  int curr;

    public TracksController(int albumId) {
        this.albumId = albumId;
    }

    public void getTracks() {
        if (total == 0){
            jobManager.addJobInBackground(new FetchTracksJob(albumId, null));
        }else if(curr < total) {
            jobManager.addJobInBackground(new FetchTracksJob(albumId, curr+""));
        }
    }

    public void onTracksFetched(TrackResponse trackResponse){
        curr += trackResponse.getTracks().size();
        total = trackResponse.getTotal();
    }

}
