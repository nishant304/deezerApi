package com.deezerapi.streammusic.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
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
        Slide slide = new Slide(Gravity.BOTTOM);
        //slide.setStartDelay(300);
        slide.setInterpolator(AnimationUtils.loadInterpolator(this,
                android.R.interpolator.linear_out_slow_in));
        android.transition.TransitionSet transitionSet = new android.transition.TransitionSet();
        transitionSet.addTransition(slide).addTransition(new ChangeTransform());
        albumFragment.setEnterTransition(new Fade());
        ImageTransition imageTransition = new ImageTransition();
        imageTransition.addListener(albumFragment);
        //imageTransition.setStartDelay(300);
        albumFragment.setSharedElementEnterTransition(
                imageTransition);
        albumFragment.setExitTransition(new Explode());


        Bundle bundle = new Bundle();
        bundle.putString(AlbumFragment.QUERY, artistSelectedEvent.getName());
        bundle.putString(AlbumFragment.URL, artistSelectedEvent.getUrl());
        albumFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .addSharedElement(artistSelectedEvent.getImageView(), "random")
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
