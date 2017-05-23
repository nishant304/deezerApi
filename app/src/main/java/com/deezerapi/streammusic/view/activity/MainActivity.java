package com.deezerapi.streammusic.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.deezerapi.streammusic.ArtistSelectedEvent;
import com.deezerapi.streammusic.R;
import com.deezerapi.streammusic.events.AlbumSelectedEvent;
import com.deezerapi.streammusic.view.fragments.AlbumFragment;
import com.deezerapi.streammusic.view.fragments.ArtistFragment;
import com.deezerapi.streammusic.view.fragments.TracksFragment;
import com.deezerapi.streammusic.view.transition.ImageTransition;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;

public class MainActivity extends BaseActivity {

    private ArtistFragment artistFragment;

    private SearchView searchView;

    private static final String ALBUM_TAG = "album";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        artistFragment = new ArtistFragment();
        addFragment(R.id.container, artistFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if (!(fragment instanceof ArtistFragment)) {
                    getSupportFragmentManager().popBackStackImmediate(ALBUM_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                artistFragment.onNewQuery(newText);
                return true;
            }
        });

        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArtistSelected(ArtistSelectedEvent artistSelectedEvent) {
        searchView.onActionViewCollapsed();

        AlbumFragment albumFragment = new AlbumFragment();
        String transitionName = artistSelectedEvent.getImageView().getTransitionName();
        if (transitionName == null) {
            transitionName = UUID.randomUUID().toString();
        }
        artistSelectedEvent.imageView.setTransitionName(transitionName);
        albumFragment.setEnterTransition(new Fade());
        ImageTransition imageTransition = new ImageTransition();
        imageTransition.addListener(albumFragment);
        albumFragment.setSharedElementEnterTransition(
                imageTransition);

        Bundle bundle = new Bundle();
        bundle.putString(AlbumFragment.QUERY, artistSelectedEvent.getName());
        bundle.putString(AlbumFragment.URL, artistSelectedEvent.getUrl());
        bundle.putString(AlbumFragment.TRANSITION_NAME, transitionName);
        albumFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .addSharedElement(artistSelectedEvent.getImageView(), transitionName)
                .addToBackStack(ALBUM_TAG)
                .replace(R.id.container, albumFragment)
                .commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlbumSelected(AlbumSelectedEvent albumSelectedEvent) {
        TracksFragment tracksFragment = new TracksFragment();

        String transitionName = albumSelectedEvent.getImageView().getTransitionName();
        if (transitionName == null) {
            transitionName = UUID.randomUUID().toString();
        }
        albumSelectedEvent.getImageView().setTransitionName(transitionName);
        tracksFragment.setEnterTransition(new Fade());
        ImageTransition imageTransition = new ImageTransition();
        imageTransition.addListener(tracksFragment);
        tracksFragment.setSharedElementEnterTransition(
                imageTransition);

        Bundle bundle = new Bundle();
        bundle.putInt(TracksFragment.ALBUM_ID, albumSelectedEvent.getId());
        bundle.putString(TracksFragment.URL, albumSelectedEvent.getUrl());
        bundle.putString(TracksFragment.TRANSITION_NAME, transitionName);
        tracksFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .addSharedElement(albumSelectedEvent.imageView, transitionName)
                .addToBackStack(null)
                .replace(R.id.container, tracksFragment)
                .commit();
    }

}
