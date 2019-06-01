package com.feedapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.feedapp.FeedApp;
import com.feedapp.db.FeedDatabase;
import com.feedapp.db.entities.Feed;
import com.feedapp.network.ApiService;
import com.feedapp.network.Resource;
import com.feedapp.utils.Utility;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FeedRepository extends BaseRepository{

    private FeedDatabase mDatabase;
    private Application mApplication;
    private ApiService mApiService;

    private MutableLiveData<Resource<List<Feed>>> mFeedLiveData = new MutableLiveData<>();

    @Inject
    FeedRepository(FeedDatabase database, Application application, ApiService apiService) {
        mDatabase = database;
        mApplication = application;
        mApiService = apiService;
    }

    public void loadFeeds(){

        if (Utility.isNetworkAvailable(mApplication)) {
            loadFeedFromNetwork();
        }else {
            loadFeedFromDb();
        }
    }

    private void loadFeedFromDb() {
        mFeedLiveData.setValue(Resource.loading(null, Resource.Source.DB));

        add(mDatabase.getFeedDao().getAllFeeds()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(feeds -> {
            mFeedLiveData.setValue(Resource.success(feeds, Resource.Source.DB));

        },throwable -> {
            mFeedLiveData.setValue(Resource.error(throwable.getMessage(),null, Resource.Source.DB));
        }));
    }

    private void loadFeedFromNetwork() {

        mFeedLiveData.setValue(Resource.loading(null, Resource.Source.NETWORK));
        add(mApiService.fetchFeedFromApi()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(feeds -> {
            mFeedLiveData.setValue(Resource.success(feeds, Resource.Source.NETWORK));
            saveFeedInDb(feeds);
        },throwable -> {
            mFeedLiveData.setValue(Resource.error(throwable.getMessage(), null,Resource.Source.NETWORK));
        }));
    }

    private void saveFeedInDb(List<Feed> feeds) {
        if (feeds != null && !feeds.isEmpty()) {

            add(mDatabase.getFeedDao().insertFeeds(feeds)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(longs -> {

            },throwable -> {}));
        }
    }

    public LiveData<Resource<List<Feed>>> getFeedLiveData(){
        return mFeedLiveData;
    }
}
