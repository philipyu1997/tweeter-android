package com.yuphilip.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Tweet.class, User.class}, version = 2)
public abstract class MyDatabase extends RoomDatabase {

    public abstract TweetDao tweetDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";

}
