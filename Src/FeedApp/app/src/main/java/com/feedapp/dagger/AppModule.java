package com.feedapp.dagger;

import android.app.Application;

import com.feedapp.db.FeedDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    FeedDatabase provideDatabase(Application application){
        return FeedDatabase.getInstance(application);
    }
}
