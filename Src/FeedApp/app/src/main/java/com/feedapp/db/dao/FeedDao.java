package com.feedapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.feedapp.db.entities.Feed;
import com.feedapp.db.entities.Like;
import com.feedapp.models.FeedModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface FeedDao {

    public String FETCH_SINGLE_FEED_QUERY_STRING = "select feed.*, like.* from feed left join like on feed.name=like.like_to where feed.name=?";

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<long[]> insertFeeds(List<Feed> feeds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Void> likeFeed(Like like);

    @Delete
    Single<Integer> deleteLike(Like like);

    @Query("select * from feed")
    Flowable<List<Feed>> getAllFeeds();

    @RawQuery(observedEntities = {Feed.class,Like.class})
    Flowable<FeedModel> getFeedWithName(SupportSQLiteQuery query);

}
