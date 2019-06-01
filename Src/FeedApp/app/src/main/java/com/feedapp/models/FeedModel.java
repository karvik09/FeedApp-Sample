package com.feedapp.models;

import androidx.room.Embedded;

import com.feedapp.db.entities.Feed;
import com.feedapp.db.entities.Like;

public class FeedModel {

    @Embedded
    private Feed feed;

    @Embedded
    private Like like;

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Like getLike() {
        return like;
    }

    public void setLike(Like like) {
        this.like = like;
    }
}
