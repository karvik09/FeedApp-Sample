package com.feedapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.feedapp.db.entities.Like;
import com.feedapp.models.FeedModel;
import com.feedapp.repositories.FeedDetailsRepository;

import javax.inject.Inject;

public class FeedDetailsViewModel extends AndroidViewModel {

    FeedDetailsRepository mRepo;
    private MediatorLiveData<FeedModel> mFeedDetail = new MediatorLiveData<>();

    @Inject
    public FeedDetailsViewModel(@NonNull Application application,FeedDetailsRepository repo) {
        super(application);
        mRepo = repo;
        observeFeedDetails();
    }

    public LiveData<FeedModel> getFeedLiveData() {
        return mFeedDetail;
    }

    public void observeFeedDetails() {
        mFeedDetail.addSource(mRepo.getFeedDetailLiveData(),feedModel -> {
            mFeedDetail.setValue(feedModel);
        });
    }

    @Override
    protected void onCleared() {
        mRepo.dispose();
        super.onCleared();
    }

    public void loadFeedDetails(String name) {
        mRepo.loadDetails(name);
    }
    public void changeLikeState(Like like,boolean isLiked){
        mRepo.changeLikeState(like,isLiked);
    }
}
