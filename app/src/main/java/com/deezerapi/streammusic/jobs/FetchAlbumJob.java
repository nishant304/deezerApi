package com.deezerapi.streammusic.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.deezerapi.streammusic.App;
import com.deezerapi.streammusic.AppException;
import com.deezerapi.streammusic.model.AlbumSearchResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Response;

/**
 * Created by nishant on 18.05.17.
 */

public class FetchAlbumJob extends BaseJob {

    private String qString;

    public FetchAlbumJob(String query,String tag){
        super(new Params(1).requireNetwork().persist().addTags(tag));
        this.qString = query;
    }

    @Override
    public void onAdded() {

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    public void onRun() throws Throwable {
          Response<AlbumSearchResponse> response = App.getApiService().searchAlbum(qString).execute();
          if(response.isSuccessful()) {
              EventBus.getDefault().post(response.body());
          }else{
              throw new AppException(response.code());
          }
    }

}
