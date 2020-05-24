package com.yuphilip.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    //region Properties

    String body;
    String createdAt;
    long id;
    User user;
    String mediaUrl;

    //endregion

    // empty constructor needed by the Parceler library
    public Tweet() {

    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {

        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");

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

    public String getBody() {

        return body;

    }

    public String getCreatedAt() {

        return createdAt;

    }

    public long getId() {

        return id;

    }

    public String getMediaUrl() {

        return mediaUrl;

    }

    public User getUser() {

        return user;

    }

}
