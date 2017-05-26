package com.deezerapi.streammusic.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.deezerapi.streammusic.R;
import com.deezerapi.streammusic.controller.ArtisitController;
import com.deezerapi.streammusic.controller.TracksController;
import com.deezerapi.streammusic.events.album.FetchAlbumEvent;
import com.deezerapi.streammusic.events.tracks.FetchTracksEvent;
import com.deezerapi.streammusic.model.Track;
import com.deezerapi.streammusic.model.TrackResponse;
import com.deezerapi.streammusic.view.adapter.TrackAdapter;
import com.deezerapi.streammusic.view.util.LoadMoreItemsListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nishant on 20.05.17.
 */

public class TracksFragment extends BaseFragment implements Transition.TransitionListener {

    private TrackAdapter trackAdapter;

    private int cdIndex;

    private LinearLayoutManager layoutManager;

    private TracksController tracksController;

    private LoadMoreItemsListener scrollListener;

    private boolean isStarted;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.img)
    public ImageView imageView;

    public static final String ALBUM_ID = "albumID";

    public static final String URL = "url";

    public static final String TRANSITION_NAME = "transitionName";

    public static final String DUMMY_STRING = "dummy";

    public static final String CD_STRING = "cd";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracksController = new TracksController(getArguments().getInt("albumID"));
        trackAdapter = new TrackAdapter(getActivityContext(), new ArrayList<Track>());
        layoutManager = new LinearLayoutManager(getActivityContext());
        scrollListener = new ScrollListener(layoutManager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_layout, container, false);
        ButterKnife.bind(this, view);
        imageView.setTransitionName(getArguments().getString("transitionName"));

        Glide.with(getActivityContext()).load(getArguments().getString("url")).into(imageView);
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(scrollListener);
        return view;
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }

    /***
     * we dont want to fetch data leave transition and hence the check
     * @param transition
     */
    @Override
    public void onTransitionEnd(Transition transition) {
        if (!isStarted) {
            isStarted = true;
            tracksController.getTracks();
        }
    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onTracksFetched(FetchTracksEvent tracksEvent) {
        if (tracksEvent.isSuccess()) {
            TrackResponse trackResponse = tracksEvent.getTrackResponse();
            int prevSzie = trackAdapter.getItemCount();
            List<Track> list = updateListForMoreVol(trackResponse.getTracks());
            trackAdapter.notifyItemRangeInserted(prevSzie, list.size());
            tracksController.onTracksFetched(trackResponse);
            scrollListener.onLoadFinished();
        } else {
            scrollListener.onLoadFinished();
            //snack bar
        }

        FetchTracksEvent sticky = EventBus.getDefault().getStickyEvent(FetchTracksEvent.class);
        if(sticky != null){
            EventBus.getDefault().removeStickyEvent(sticky);
        }
    }

    private class ScrollListener extends LoadMoreItemsListener {

        public ScrollListener(LinearLayoutManager linearLayoutManager) {
            super(linearLayoutManager);
        }

        @Override
        public void loadMore() {
            tracksController.getTracks();
        }
    };

    /***
     * Adds cd volume or disk infromation based on disk number info in each track
     * assumes the list is sorted ....
     * @param tracks
     */
    private List<Track> updateListForMoreVol(List<Track> tracks) {
        List<Track> updatedTracks = trackAdapter.getTrackList();
        int intialSize = updatedTracks.size();
        for (int i = 0; i < tracks.size(); i++) {
            if (cdIndex < tracks.get(i).getDiskNumber()) {
                updatedTracks.add(getDummyTrackFor(++cdIndex));
            }
            updatedTracks.add(tracks.get(i));
        }

        if (intialSize == 0 && cdIndex == 1) {
            updatedTracks.remove(0);  //expensive in huge list, which is unexpected
        }

        return updatedTracks;
    }

    private Track getDummyTrackFor(int index) {
        Track track = new Track();
        track.setType(DUMMY_STRING);
        track.setTitle(CD_STRING + index);
        return track;
    }

}
