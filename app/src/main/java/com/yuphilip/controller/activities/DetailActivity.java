package com.yuphilip.controller.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.yuphilip.R;
import com.yuphilip.controller.fragments.ReplyFragment;
import com.yuphilip.databinding.ActivityDetailBinding;
import com.yuphilip.model.Constant;
import com.yuphilip.model.Tweet;
import com.yuphilip.model.net.TwitterApp;
import com.yuphilip.model.net.TwitterClient;

import org.parceler.Parcels;

import java.util.Locale;

import okhttp3.Headers;

public class DetailActivity extends AppCompatActivity {

    //region Properties

    private static final String TAG = "DetailActivity";
    private Button btnFavor;
    private TextView tvFavorCount;
    private Button btnRetweet;
    private TextView tvRetweetCount;
    private TwitterClient client;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        com.yuphilip.databinding.ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        client = TwitterApp.getRestClient(this);

        ImageView ivProfileImage = binding.ivProfileImage;
        TextView tvName = binding.tvName;
        TextView tvScreenName = binding.tvScreenName;
        TextView tvTime = binding.tvTime;
        TextView tvBody = binding.tvBody;
        ImageView ivMediaImage = binding.ivMediaImage;
        btnFavor = binding.btnFavor;
        tvFavorCount = binding.tvFavorCount;
        btnRetweet = binding.btnRetweet;
        tvRetweetCount = binding.tvRetweetCount;
        Button btnReply = binding.btnReply;

        final Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .transform(new CircleCrop())
                .into(ivProfileImage);

        tvName.setText(tweet.user.name);
        tvScreenName.setText(String.format(Locale.getDefault(), "@%s", tweet.user.screenName));
        tvTime.setText(Constant.getRelativeTimeAgo(tweet.createdAt));
        tvBody.setText(tweet.body);
        tvFavorCount.setText(String.format(Locale.getDefault(), "%d", tweet.favoriteCount));
        tvRetweetCount.setText(String.format(Locale.getDefault(), "%d", tweet.retweetCount));

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
                            tvFavorCount.setText(String.format(Locale.getDefault(), "%d", (tweet.favoriteCount + 1)));
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
                            tvFavorCount.setText(String.format(Locale.getDefault(),"%d", (tweet.favoriteCount - 1)));
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
                            tvRetweetCount.setText(String.format(Locale.getDefault(),"%d", (tweet.retweetCount + 1)));
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
                            tvRetweetCount.setText(String.format(Locale.getDefault(),"%d", (tweet.retweetCount - 1)));
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to unretweet tweet", throwable);
                        }
                    });
                }
            }
        });

        // Handle reply button
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Reply to tweet");

                showTweetDialog(tweet.id, tweet.user.screenName);
            }
        });

    }

    private void showTweetDialog(long id, String tweetOP) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putLong("tweetId", id);
        bundle.putString("tweetOP", tweetOP);

        ReplyFragment replyFragment = ReplyFragment.newInstance("Reply to Tweet");

        replyFragment.setArguments(bundle);
        replyFragment.show(fragmentManager, "reply_fragment");

    }

}
