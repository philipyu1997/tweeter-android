package com.yuphilip.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.yuphilip.model.Tweet;
import com.yuphilip.model.TweetDao;
import com.yuphilip.model.User;

@Database(entities = {Tweet.class, User.class}, version = 2)
public abstract class MyDatabase extends RoomDatabase {

    public abstract TweetDao tweetDao();

    // Database name to be used
    public static final String NAME = "TweetDatabase";

}
