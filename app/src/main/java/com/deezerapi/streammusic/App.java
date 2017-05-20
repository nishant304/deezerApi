package com.deezerapi.streammusic;

import android.app.Application;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.deezerapi.streammusic.api.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nishant on 17.05.17.
 */

public class App extends Application {

    private static final String BASE_URL = "http://api.deezer.com/";

    private static Retrofit retrofit;

    public static ApiService getApiService() {
        return retrofit.create(ApiService.class) ;
    }

    private static ApiService apiService;

    public static App getApp() {
        return app;
    }

    private static App app;

    public static JobManager getJobManager() {
        return jobManager;
    }

    private static JobManager jobManager;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jobManager =new JobManager(new Configuration.Builder(App.getApp())
                .consumerKeepAlive(30)
                .maxConsumerCount(3).build());

    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

}

