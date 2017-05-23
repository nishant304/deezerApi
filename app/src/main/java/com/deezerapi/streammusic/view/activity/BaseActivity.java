package com.deezerapi.streammusic.view.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by nishant on 17.05.17.
 */

public class BaseActivity extends AppCompatActivity {

    protected void makeToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    protected void addFragment(int id, Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .add(id,fragment).commit();
    }




    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
