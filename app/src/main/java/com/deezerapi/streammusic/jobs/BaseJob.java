package com.deezerapi.streammusic.jobs;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.deezerapi.streammusic.AppException;

/**
 * Created by nishant on 21.05.17.
 */

public abstract class BaseJob extends Job {

    public BaseJob(Params params){
        super(params);
    }


    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {

        if(throwable instanceof AppException){
            AppException appException = (AppException) throwable;
            if(appException.shouldRetry()){
                return RetryConstraint.createExponentialBackoff(runCount,1000);
            }
        }
        return RetryConstraint.CANCEL;
    }

    @Override
    protected int getRetryLimit() {
        return 3;
    }

}
