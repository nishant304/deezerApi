package com.deezerapi.streammusic.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deezerapi.streammusic.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by nishant on 19.05.17.
 */

public class BaseFragment extends Fragment {

    private Context context;

    private ImageView imageView;

    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    private boolean isStarted;

    private RecyclerView.LayoutManager layoutManager;

    protected View findViewById(int id){
        if(getView() == null){
            throw new NullPointerException("no view attached yet");
        }
        return getView().findViewById(id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    protected Context getActivityContext(){
        return context;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    protected void makeToast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

}
