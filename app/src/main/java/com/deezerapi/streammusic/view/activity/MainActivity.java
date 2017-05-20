package com.deezerapi.streammusic.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.deezerapi.streammusic.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        artistFragment = new ArtistFragment();
        addFragment(R.id.container,artistFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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
    public void onArtistSelected(ArtistSelectedEvent  artistSelectedEvent){
        searchView.onActionViewCollapsed();
        AlbumFragment albumFragment = new AlbumFragment();
        String transitionName = artistSelectedEvent.imageView.getTransitionName();
        if(transitionName == null){
            transitionName = UUID.randomUUID().toString();
        }
        artistSelectedEvent.imageView.setTransitionName(transitionName);
        albumFragment.setEnterTransition(new Fade());
        albumFragment.setSharedElementEnterTransition(
                new ImageTransition());
        albumFragment.setSharedElementReturnTransition(new ImageTransition());

        Bundle bundle = new Bundle();
        bundle.putString("query",artistSelectedEvent.name);
        bundle.putString("url",artistSelectedEvent.url);
        bundle.putString("transitionName",transitionName);
        albumFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .addSharedElement(artistSelectedEvent.imageView,transitionName)
                .addToBackStack("album")
                .replace(R.id.container,albumFragment)
                .commit();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArtistSelected(AlbumSelectedEvent  albumSelectedEvent){
        TracksFragment tracksFragment = new TracksFragment();
        String transitionName = albumSelectedEvent.imageView.getTransitionName();
        if(transitionName == null){
            transitionName = UUID.randomUUID().toString();
        }
        albumSelectedEvent.imageView.setTransitionName(transitionName);
        tracksFragment.setEnterTransition(new Fade());
        tracksFragment.setSharedElementEnterTransition(
                new ImageTransition());

        Bundle bundle = new Bundle();
        bundle.putInt("albumID",albumSelectedEvent.id);
        bundle.putString("url",albumSelectedEvent.url);
        bundle.putString("transitionName",transitionName);
        tracksFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .addSharedElement(albumSelectedEvent.imageView,transitionName)
                .addToBackStack(null)
                .replace(R.id.container,tracksFragment)
                .commit();
    }

    public static class ArtistSelectedEvent{
        public ImageView imageView;
        public String name ;
        public String url;

        public ArtistSelectedEvent(ImageView imageView, String name,String url){
            this.imageView = imageView;
            this.name = name;
            this.url = url;
        }
    }

    public static class AlbumSelectedEvent{
        public ImageView imageView;
        public int id;
        public String url;

        public AlbumSelectedEvent(ImageView imageView, int id, String url){
            this.imageView = imageView;
            this.id = id;
            this.url = url;
        }
    }

}
