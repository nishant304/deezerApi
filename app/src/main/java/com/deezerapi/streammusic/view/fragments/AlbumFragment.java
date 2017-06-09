package com.deezerapi.streammusic.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import com.deezerapi.streammusic.events.album.AlbumJobAdded;
import com.deezerapi.streammusic.events.album.FetchAlbumEvent;
import com.deezerapi.streammusic.model.Album;
import com.deezerapi.streammusic.model.AlbumSearchResponse;
import com.deezerapi.streammusic.view.adapter.AlbumAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nishant on 19.05.17.
 */

public class AlbumFragment extends BaseFragment implements Transition.TransitionListener {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.img)
    public ImageView imageView;

    private AlbumAdapter albumAdapter;

    private GridLayoutManager gridLayoutManager;

    private int COUMN_COUNT = 2;

    private List<Album> albumList = new ArrayList<>();

    private boolean isStarted;

    public static final String QUERY = "query";

    public static final String URL = "url";

    public static final String TRANSITION_NAME = "transitionName";

    /***
     * keep data intact while views are created multiple times
     * and feed data to views when they are recreated
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumAdapter = new AlbumAdapter(getActivityContext(), albumList);
        albumAdapter.setHasStableIds(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_layout, container, false);
        ButterKnife.bind(this, view);
        Glide.with(getActivityContext()).load(getArguments().getString(URL)).into(imageView);
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivityContext(), COUMN_COUNT));
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onAlbumsFetched(FetchAlbumEvent fetchAlbumEvent) {
        showProgressBar(false);
        if (fetchAlbumEvent.isSuccess()) {
            AlbumSearchResponse albumSearchResponse = fetchAlbumEvent.getAlbumSearchResponse();
            albumAdapter.addNewAlbums(albumSearchResponse.getAlbums());
        } else {
            //snack bar
        }

        FetchAlbumEvent sticky = EventBus.getDefault().getStickyEvent(FetchAlbumEvent.class);
        if(sticky != null){
            EventBus.getDefault().removeStickyEvent(sticky);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onRequestAdded(AlbumJobAdded albumJobAdded) {
        showProgressBar(true);
        AlbumJobAdded sticky = EventBus.getDefault().getStickyEvent(AlbumJobAdded.class);
        if(sticky != null){
            EventBus.getDefault().removeStickyEvent(sticky);
        }
    }

    private void showProgressBar(boolean showProgress) {
        recyclerView.setVisibility(showProgress ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(!showProgress ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }

    /***
     * we dont want to fetch data when on leave anim and hence the check
     * @param transition
     */
    @Override
    public void onTransitionEnd(Transition transition) {
        if (!isStarted) {
            isStarted = true;
            ArtisitController.getInstance().getAlbums(getArguments().getString(QUERY));
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

}
