package com.yuphilip.apps.restclienttemplate.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SampleModel.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    // Database name to be used
    public static final String NAME = "MyDataBase";

    public abstract SampleModelDao sampleModelDao();
}
