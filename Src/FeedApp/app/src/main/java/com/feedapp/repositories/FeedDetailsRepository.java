package com.feedapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.feedapp.db.FeedDatabase;
import com.feedapp.db.dao.FeedDao;
import com.feedapp.db.entities.Like;
import com.feedapp.models.FeedModel;
import com.feedapp.network.ApiService;
import com.feedapp.utils.VLog;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FeedDetailsRepository extends BaseRepository{

    private ApiService mApiService;
    private MutableLiveData<FeedModel> mFeedDetails = new MutableLiveData<>();
    private FeedDatabase mDb;

    @Inject
    FeedDetailsRepository(ApiService apiService,FeedDatabase database){
        mApiService = apiService;
        mDb = database;
    }

    public LiveData<FeedModel> getFeedDetailLiveData(){
        return mFeedDetails;
    }

    public void loadDetails(String name){
        add(mDb.getFeedDao().getFeedWithName(new SimpleSQLiteQuery(FeedDao.FETCH_SINGLE_FEED_QUERY_STRING,
                new Object[]{name}))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(feedModel -> {
            mFeedDetails.setValue(feedModel);
        },t->{
            VLog.e("Error", t.getMessage());
        }));
    }

    public void changeLikeState(Like l,boolean like){
        if (like){
            markLike(l);
        }else {
            deleteLike(l);
        }
    }

    private void deleteLike(Like like) {
        add(mDb.getFeedDao().deleteLike(like)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v->{

                },t->{
                    VLog.e("Error", t.getMessage());
                }));
    }

    private void markLike(Like like) {
        add(mDb.getFeedDao().likeFeed(like)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(v->{

        },t->{

            VLog.e("Error", t.getMessage());
        }));
    }
}
