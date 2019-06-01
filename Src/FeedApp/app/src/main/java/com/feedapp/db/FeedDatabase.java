package com.feedapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.feedapp.db.dao.FeedDao;
import com.feedapp.db.entities.Feed;
import com.feedapp.db.entities.Like;

@Database(entities = {Feed.class, Like.class}, version = 1)
abstract public class FeedDatabase extends RoomDatabase {

    private static FeedDatabase sInstance;

    public static FeedDatabase getInstance(Context context) {
        if (sInstance==null){
            synchronized (FeedDatabase.class){
                sInstance = Room.databaseBuilder(context.getApplicationContext()
                        , FeedDatabase.class, "Feed.db")
                        .fallbackToDestructiveMigration()
                        .build();

            }
        }
        return sInstance;
    }

    public abstract FeedDao getFeedDao();

}
