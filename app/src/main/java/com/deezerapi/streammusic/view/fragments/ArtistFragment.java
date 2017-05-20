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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nishant on 19.05.17.
 */

public class ArtistFragment extends BaseFragment implements ViewTreeObserver.OnGlobalLayoutListener {

    private RecyclerView recyclerView;

    private ArtistAdapter artistAdapter;

    private Context context;

    private ProgressBar progressBar;

    private List<Artist> list = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_layout,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        artistAdapter = new ArtistAdapter(context, list);
        recyclerView.setAdapter(artistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        return view;
    }

    @Override
    public void onGlobalLayout() {
        getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        startPostponedEnterTransition();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchResult(ArtistSearchResponse response){
        list = response.getArtists();
        showProgressBar(false);
        artistAdapter.appendData(response.getArtists());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJobAdded(FetchArtistJob.OnJobAdded onJobAdded){
        showProgressBar(true);
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
