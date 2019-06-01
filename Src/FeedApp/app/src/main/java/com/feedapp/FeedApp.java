package com.feedapp;

import android.app.Application;

import com.feedapp.dagger.AppModule;
import com.feedapp.dagger.DaggerInjector;
import com.feedapp.dagger.Injector;
import com.feedapp.dagger.RestModule;

public class FeedApp extends Application {

    private static FeedApp sInstance;
    private Injector mInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mInjector = DaggerInjector.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static FeedApp getInstance() {
        return sInstance;
    }

    public Injector getInjector(){
        return mInjector;
    }
}
