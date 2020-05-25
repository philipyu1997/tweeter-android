package com.yuphilip.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {

    @Query("SELECT Tweet.body AS tweet_body, Tweet.createdAt AS tweet_createdAt, Tweet.id AS tweet_id, " +
            "Tweet.mediaUrl AS tweet_mediaUrl, Tweet.favorited AS tweet_favorited, Tweet.favoriteCount AS tweet_favoriteCount, " +
            "Tweet.retweeted AS tweet_retweeted, Tweet.retweetCount AS tweet_retweetCount, User.* " +
            "FROM Tweet INNER JOIN User ON Tweet.userId = User.id ORDER BY createdAt DESC LIMIT 5")
    List<TweetWithUser> recentItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet... tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);

}
