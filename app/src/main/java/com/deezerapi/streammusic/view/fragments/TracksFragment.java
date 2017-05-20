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
import com.deezerapi.streammusic.model.Track;
import com.deezerapi.streammusic.model.TrackResponse;
import com.deezerapi.streammusic.view.adapter.TrackAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by nishant on 20.05.17.
 */

public class TracksFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private TrackAdapter trackAdapter;

    private Context context;

    private ProgressBar progressBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_layout,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        trackAdapter = new TrackAdapter(context,new ArrayList<Track>());
        ImageView imageView = (ImageView)view.findViewById(R.id.img);
        imageView.setTransitionName(getArguments().getString("transitionName"));
        Glide.with(context).load(getArguments().getString("url")).into(imageView);
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ArtisitController.getInstance().getTracks(getArguments().getInt("albumID"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTracksFetched(TrackResponse trackResponse){
        trackAdapter.appendData(trackResponse.getTracks());
    }

}
