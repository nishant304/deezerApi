package com.deezerapi.streammusic.view.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.deezerapi.streammusic.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by nishant on 19.05.17.
 */

public class BaseFragment extends Fragment {

    protected View findViewById(int id){
        if(getView() == null){
            throw new NullPointerException("no view attached yet");
        }
        return getView().findViewById(id);
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

}
