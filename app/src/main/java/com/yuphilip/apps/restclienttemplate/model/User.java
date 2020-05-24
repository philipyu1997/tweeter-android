package com.yuphilip.apps.restclienttemplate.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    //region Properties

    String name;
    String screenName;
    String profileImageUrl;

    //endregion

    // empty constructor needed by the Parceler libraryk
    public User() {

    }

    public String getName() {

        return name;

    }

    public String getScreenName() {

        return screenName;

    }

    public String getProfileImageUrl() {

        return profileImageUrl;

    }

    public static User fromJson(JSONObject jsonObject) throws JSONException {

        User user = new User();

        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");

        return user;

    }

}
