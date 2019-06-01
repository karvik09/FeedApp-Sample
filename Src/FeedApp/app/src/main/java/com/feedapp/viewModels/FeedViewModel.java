package com.feedapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.feedapp.db.entities.Feed;
import com.feedapp.network.Resource;
import com.feedapp.repositories.FeedRepository;

import java.util.List;

import javax.inject.Inject;

public class FeedViewModel extends AndroidViewModel {

    FeedRepository mFeedRepo;
    private MediatorLiveData<Resource<List<Feed>>> mFeedsLiveData = new MediatorLiveData<>();

    @Inject
    FeedViewModel(@NonNull Application application, FeedRepository feedRepository) {
        super(application);
        this.mFeedRepo = feedRepository;
        observeFeedsData();
    }

    private void observeFeedsData() {
        mFeedsLiveData.addSource(mFeedRepo.getFeedLiveData(),
                listResource -> mFeedsLiveData.setValue(listResource));
    }

    public LiveData<Resource<List<Feed>>> getFeedLiveData(){
        return mFeedsLiveData;
    }
    public void loadFeeds(){
        mFeedRepo.loadFeeds();
    }

    @Override
    protected void onCleared() {
        if(mFeedRepo!=null){
            mFeedRepo.dispose();
        }
        super.onCleared();
    }
}
