package com.deezerapi.streammusic.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.deezerapi.streammusic.R;
import com.deezerapi.streammusic.controller.ArtisitController;
import com.deezerapi.streammusic.controller.TracksController;
import com.deezerapi.streammusic.model.Track;
import com.deezerapi.streammusic.model.TrackResponse;
import com.deezerapi.streammusic.view.adapter.TrackAdapter;
import com.deezerapi.streammusic.view.util.LoadMoreItemsListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nishant on 20.05.17.
 */

public class TracksFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private TrackAdapter trackAdapter;

    private Context context;

    private ProgressBar progressBar;

    private int cdIndex;

    private LinearLayoutManager layoutManager;

    private TracksController tracksController;

    private LoadMoreItemsListener scrollListener ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracksController = new TracksController(getArguments().getInt("albumID"));
        tracksController.getTracks();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_layout,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        trackAdapter = new TrackAdapter(getActivityContext(),new ArrayList<Track>());
        ImageView imageView = (ImageView)view.findViewById(R.id.img);
        imageView.setTransitionName(getArguments().getString("transitionName"));
        Glide.with(getActivityContext()).load(getArguments().getString("url")).into(imageView);
        recyclerView.setAdapter(trackAdapter);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        scrollListener = new LoadMoreItemsListener(layoutManager) {
            @Override
            public void loadMore() {
                tracksController.getTracks();
            }
        };
        recyclerView.setOnScrollListener(scrollListener);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onTracksFetched(TrackResponse trackResponse){
        //List<Track> list = updateListForMoreVol(trackResponse.getTracks());
        trackAdapter.appendData(trackResponse.getTracks());
        tracksController.onTracksFetched(trackResponse);
        scrollListener.onLoadFinished();
    }

    /***
     * list is small enough for linear scan as there is no guarantee of list being sorted
     * @param tracks
     */
    private List<Track> updateListForMoreVol(List<Track> tracks){
        ArrayList<Track> updatedTracks =  new ArrayList<>(trackAdapter.getTrackList());
        for(int i=0;i<tracks.size();i++){
            if(cdIndex < tracks.get(i).getDiskNumber()){
                updatedTracks.add(getDummyTrackFor(++cdIndex));
            }
            updatedTracks.add(tracks.get(i));
        }

        return updatedTracks;
    }

    private Track getDummyTrackFor(int index){
        Track track = new Track();
        track.setType("dummy");
        track.setTitle("cd "+index);
        return track;
    }

}
