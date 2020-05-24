package com.yuphilip.apps.restclienttemplate.controller.activities;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yuphilip.apps.restclienttemplate.R;
import com.yuphilip.apps.restclienttemplate.model.Constant;
import com.yuphilip.apps.restclienttemplate.model.Tweet;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvScreenName;
    private TextView tvTime;
    private TextView tvBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvName);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvTime = findViewById(R.id.tvTime);
        tvBody = findViewById(R.id.tvBody);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        Glide.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .into(ivProfileImage);
        tvName.setText(tweet.getUser().getName());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvTime.setText(Constant.getRelativeTimeAgo(tweet.getCreatedAt()));
        tvBody.setText(tweet.getBody());

    }

}
