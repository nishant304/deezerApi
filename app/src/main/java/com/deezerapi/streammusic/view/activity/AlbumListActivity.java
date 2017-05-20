package com.deezerapi.streammusic.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deezerapi.streammusic.R;
import com.deezerapi.streammusic.controller.ArtisitController;
import com.deezerapi.streammusic.model.Album;
import com.deezerapi.streammusic.model.AlbumSearchResponse;
import com.deezerapi.streammusic.view.adapter.AlbumAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by nishant on 18.05.17.
 */

public class AlbumListActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private AlbumAdapter albumAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_artist_layout);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        albumAdapter = new AlbumAdapter(this,new ArrayList<Album>());
        recyclerView.setAdapter(albumAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        String query = getIntent().getStringExtra("query");
        ArtisitController.getInstance().getAlbums(query);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
