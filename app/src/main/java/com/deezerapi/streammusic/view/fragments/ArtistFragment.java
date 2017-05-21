package com.deezerapi.streammusic.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import com.deezerapi.streammusic.R;
import com.deezerapi.streammusic.controller.ArtisitController;
import com.deezerapi.streammusic.jobs.FetchArtistJob;
import com.deezerapi.streammusic.model.Artist;
import com.deezerapi.streammusic.model.ArtistSearchResponse;
import com.deezerapi.streammusic.view.adapter.ArtistAdapter;
import com.deezerapi.streammusic.view.util.LoadMoreItemsListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nishant on 19.05.17.
 */

public class ArtistFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private ArtistAdapter artistAdapter;

    private ProgressBar progressBar;

    private List<Artist> list = new ArrayList<>();

    private ScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_layout,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        artistAdapter = new ArtistAdapter(getActivityContext(), list);
        recyclerView.setAdapter(artistAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivityContext());
        scrollListener = new ScrollListener(linearLayoutManager);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(scrollListener);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onSearchResult(ArtistSearchResponse response){
        showProgressBar(false);
        list.addAll(response.getArtists());
        artistAdapter.appendData(response.getArtists());
        ArtisitController.getInstance().onResponse(response);
        scrollListener.onLoadFinished();
    }

    private class ScrollListener extends LoadMoreItemsListener{

        public ScrollListener(LinearLayoutManager linearLayoutManager){
            super(linearLayoutManager);
        }

        @Override
        public void loadMore() {
            ArtisitController.getInstance().loadMore();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJobAdded(FetchArtistJob.OnJobAdded onJobAdded){
        //showProgressBar(true);
    }

    private void showProgressBar(boolean showProgress){
        recyclerView.setVisibility(showProgress? View.GONE:View.VISIBLE);
        progressBar.setVisibility(!showProgress? View.GONE:View.VISIBLE);
    }

    public void onNewQuery(String query){
        artistAdapter.clear();
        if(!query.isEmpty()) {
            ArtisitController.getInstance().getArtists(query);
        }
    }

}
