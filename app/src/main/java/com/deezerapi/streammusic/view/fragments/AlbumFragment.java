package com.deezerapi.streammusic.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.deezerapi.streammusic.R;
import com.deezerapi.streammusic.controller.ArtisitController;
import com.deezerapi.streammusic.model.Album;
import com.deezerapi.streammusic.model.AlbumSearchResponse;
import com.deezerapi.streammusic.view.adapter.AlbumAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nishant on 19.05.17.
 */

public class AlbumFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private AlbumAdapter albumAdapter;

    private ProgressBar progressBar;

    private List<Album> albumList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_layout,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        albumAdapter = new AlbumAdapter(getActivityContext(),albumList);
        ImageView imageView = (ImageView)view.findViewById(R.id.img);
        imageView.setTransitionName(getArguments().getString("transitionName"));
        Glide.with(getActivityContext()).load(getArguments().getString("url")).into(imageView);
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivityContext(),2));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String query = getArguments().getString("query");
        ArtisitController.getInstance().getAlbums(query);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlbumsFetched(AlbumSearchResponse albumSearchResponse){
        albumList = albumSearchResponse.getAlbums();
        if(albumAdapter == null){
            albumAdapter = new AlbumAdapter(getActivityContext(),albumList);
        }else {
            albumAdapter.addNewAlbums(albumSearchResponse.getAlbums());
        }
    }

}
