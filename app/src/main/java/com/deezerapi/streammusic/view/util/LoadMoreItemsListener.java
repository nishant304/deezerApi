package com.deezerapi.streammusic.view.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by nishant on 21.05.17.
 */

public abstract class LoadMoreItemsListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    private boolean isloading;

    public LoadMoreItemsListener(LinearLayoutManager linearLayoutManager){
        this.layoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int visibleItems = layoutManager.getChildCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        int totalItems = layoutManager.getItemCount();
        if (!isloading && dy > 0 && visibleItems + firstVisibleItem >= totalItems) {
            isloading = true;
            loadMore();
        }
    }

    public void onLoadFinished(){
        isloading = false;
    }

    public abstract void loadMore();

}
