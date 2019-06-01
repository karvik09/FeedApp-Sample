package com.feedapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.feedapp.FeedApp;
import com.feedapp.R;
import com.feedapp.baseComponent.BaseViewModelFactory;
import com.feedapp.databinding.ActivityFeedDetailsBinding;
import com.feedapp.db.entities.Feed;
import com.feedapp.db.entities.Like;
import com.feedapp.models.FeedModel;
import com.feedapp.utils.Utility;
import com.feedapp.viewModels.FeedDetailsViewModel;

import javax.inject.Inject;

public class FeedDetailsActivity extends BaseActivity implements View.OnClickListener {


    FeedDetailsViewModel mViewModel;
    ActivityFeedDetailsBinding mBinder;
    @Inject
    BaseViewModelFactory mViewModelFactory;

    private FeedModel mFeedDetails;

    private TextView headerTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_feed_details);
        initViews();
        FeedApp.getInstance().getInjector().inject(this);

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(FeedDetailsViewModel.class);

        String name = getIntent().getStringExtra("name");
        mViewModel.getFeedLiveData().observe(this,feedModel -> {
            mFeedDetails = feedModel;
            refreshView();
        });
        mViewModel.loadFeedDetails(name);
    }

    @Override
    protected void onStart() {
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    private void refreshView() {
        Feed feed = mFeedDetails.getFeed();
        Like like = mFeedDetails.getLike();
        if (feed==null){
            return;
        }
        headerTitle.setText(feed.getTitle());
        mBinder.likeText.setSelected(like != null);

        Glide.with(this)
                .load(feed.getImageUrl())
                .placeholder(R.drawable.image_loading_placeholder)
                .into(mBinder.feedImage);
        mBinder.feedName.setText(feed.getName());
        mBinder.feedText.setText(feed.getText());
        mBinder.feedDescription.setText(feed.getDescription());
    }

    private void initViews() {
        headerTitle = mBinder.getRoot().findViewById(R.id.title);
        headerTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        mBinder.likeText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.likeText:
                changeLikeState();
                break;
        }
    }

    private void changeLikeState() {
        Like like = mFeedDetails.getLike() == null ? new Like(mFeedDetails.getFeed().getName())
                : mFeedDetails.getLike();
        mViewModel.changeLikeState(like,!mBinder.likeText.isSelected());
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mBinder.offlineTag.setVisibility(Utility.isNetworkAvailable(context) ?
                    View.GONE : View.VISIBLE);
        }
    };
}
