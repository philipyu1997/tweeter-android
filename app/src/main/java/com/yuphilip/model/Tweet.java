package com.yuphilip.model;

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

    //endregion

    // empty constructor needed by the Parceler libraryk
    public Tweet() {

    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {

        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");

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

    public User getUser() {

        return user;

    }

}
