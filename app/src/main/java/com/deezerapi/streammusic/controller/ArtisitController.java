package com.deezerapi.streammusic.controller;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.TagConstraint;
import com.birbit.android.jobqueue.config.Configuration;
import com.deezerapi.streammusic.App;
import com.deezerapi.streammusic.jobs.FetchAlbumJob;
import com.deezerapi.streammusic.jobs.FetchArtistJob;
import com.deezerapi.streammusic.jobs.FetchTracksJob;
import com.deezerapi.streammusic.model.ArtistSearchResponse;

import java.util.UUID;

/**
 * Created by nishant on 17.05.17.
 */

public class ArtisitController {
    private static final ArtisitController ourInstance = new ArtisitController();

    public static ArtisitController getInstance() {
        return ourInstance;
    }

    private JobManager jobManager = App.getJobManager();

    private ArtisitController() {
    }

    private String tag = "";

    private String currentQuery;

    private int currIndex;

    private int total;

    public void getArtists(String artistName){
        currIndex = 0;
        this.currentQuery = artistName;
        jobManager.cancelJobsInBackground(null, TagConstraint.ANY,tag);
        tag = UUID.randomUUID().toString();
        jobManager.addJobInBackground(new FetchArtistJob(artistName,tag,null));
    }

    public void getAlbums(String artistName){
        jobManager.addJobInBackground(new FetchAlbumJob(artistName,UUID.randomUUID().toString()));
    }

    public void loadMore(){
        if(currIndex < total){
            jobManager.addJobInBackground(new FetchArtistJob(currentQuery,tag,currIndex+""));
        }
    }

    public void onResponse(ArtistSearchResponse artistSearchResponse){
        currIndex += artistSearchResponse.getArtists().size();
        total = artistSearchResponse.getTotal();
    }

}
