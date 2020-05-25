package com.yuphilip.controller.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.yuphilip.R;
import com.yuphilip.databinding.ActivityDetailBinding;
import com.yuphilip.model.Constant;
import com.yuphilip.model.Tweet;
import com.yuphilip.model.net.TwitterApp;
import com.yuphilip.model.net.TwitterClient;

import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends AppCompatActivity {

    //region Properties

    private static final String TAG = "DetailActivity";
    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvScreenName;
    private TextView tvTime;
    private TextView tvBody;
    private ImageView ivMediaImage;
    private Button btnFavor;
    private TextView tvFavorCount;
    private Button btnRetweet;
    private TextView tvRetweetCount;
    private ActivityDetailBinding binding;
    private TwitterClient client;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        client = TwitterApp.getRestClient(this);

        ivProfileImage = binding.ivProfileImage;
        tvName = binding.tvName;
        tvScreenName = binding.tvScreenName;
        tvTime = binding.tvTime;
        tvBody = binding.tvBody;
        ivMediaImage = binding.ivMediaImage;
        btnFavor = binding.btnFavor;
        tvFavorCount = binding.tvFavorCount;
        btnRetweet = binding.btnRetweet;
        tvRetweetCount = binding.tvRetweetCount;

        final Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .transform(new CircleCrop())
                .into(ivProfileImage);

        tvName.setText(tweet.user.name);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvTime.setText(Constant.getRelativeTimeAgo(tweet.createdAt));
        tvBody.setText(tweet.body);
        tvFavorCount.setText(String.format("%d", tweet.favoriteCount));
        tvRetweetCount.setText(String.format("%d", tweet.retweetCount));

        if (tweet.favorited) {
            btnFavor.setBackgroundResource(R.drawable.ic_favor_red);
        } else {
            btnFavor.setBackgroundResource(R.drawable.ic_favor_grey);
        }

        if (tweet.retweeted) {
            btnRetweet.setBackgroundResource(R.drawable.ic_retweet_green);
        } else {
            btnRetweet.setBackgroundResource(R.drawable.ic_retweet_grey);
        }

        int radius = 30; // corner radius, higher value = more rounded

        if (tweet.mediaUrl != null) {
            ivMediaImage.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(tweet.mediaUrl)
                    .transform(new RoundedCorners(radius))
                    .into(ivMediaImage);
        } else {
            ivMediaImage.setVisibility(View.GONE);
        }

        // Handle favorite button
        btnFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tweet.favorited) {
                    // Tweet is not favorited. Favorite tweet...
                    client.favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Successfully favorited tweet...");

                            btnFavor.setBackgroundResource(R.drawable.ic_favor_red);
                            tvFavorCount.setText(String.format("%d", (tweet.favoriteCount + 1)));
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to favorite tweet", throwable);
                        }
                    });
                } else {
                    // Tweet already favorited. Unfavorite tweet...
                    client.unfavoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Successfully unfavorited tweet...");

                            btnFavor.setBackgroundResource(R.drawable.ic_favor_grey);
                            tvFavorCount.setText(String.format("%d", (tweet.favoriteCount - 1)));
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to unfavorite tweet", throwable);
                        }
                    });
                }
            }
        });

        // Handle retweet button
        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tweet.retweeted) {
                    // Tweet is not retweeted. Retweet tweet...
                    client.retweetTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Successfully retweeted tweet...");

                            btnRetweet.setBackgroundResource(R.drawable.ic_retweet_green);
                            tvRetweetCount.setText(String.format("%d", (tweet.retweetCount + 1)));
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to retweet tweet", throwable);
                        }
                    });
                } else {
                    // Tweet already retweeted. Unretweet tweet...
                    client.unretweetTweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Successfully unretweet tweet");

                            btnRetweet.setBackgroundResource(R.drawable.ic_retweet_grey);
                            tvRetweetCount.setText(String.format("%d", (tweet.retweetCount - 1)));
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to unretweet tweet", throwable);
                        }
                    });
                }
            }
        });

    }

}
