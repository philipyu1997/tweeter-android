package com.yuphilip.model;

import android.widget.Button;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.yuphilip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId"))
public class Tweet {

    //region Properties

    @ColumnInfo
    @PrimaryKey
    public long id;

    @Ignore
    public User user;
    @ColumnInfo
    public long userId;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public String body;
    @ColumnInfo
    public String mediaUrl;
    @ColumnInfo
    public boolean favorited;
    @ColumnInfo
    public long favoriteCount;
    @ColumnInfo
    public boolean retweeted;
    @ColumnInfo
    public long retweetCount;

    //endregion

    // empty constructor needed by the Parceler library
    public Tweet() {

    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {

        Tweet tweet = new Tweet();

        tweet.id = jsonObject.getLong("id");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.userId = tweet.user.id;
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.body = jsonObject.getString("text");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.favoriteCount = jsonObject.getLong("favorite_count");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.retweetCount = jsonObject.getLong("retweet_count");

        // Fetch embedded media files
        JSONObject entities = jsonObject.getJSONObject("entities");

        if (entities.has("media")) {
            JSONArray media = entities.getJSONArray("media");
            JSONObject mediaObject = (JSONObject) media.get(0);
            tweet.mediaUrl = mediaObject.getString("media_url");
        }

        return tweet;

    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {

        List<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); ++i) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return tweets;

    }

}
