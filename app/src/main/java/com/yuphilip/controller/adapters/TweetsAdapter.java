package com.yuphilip.controller.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.yuphilip.R;
import com.yuphilip.controller.activities.DetailActivity;
import com.yuphilip.controller.fragments.ReplyFragment;
import com.yuphilip.databinding.ItemTweetBinding;
import com.yuphilip.model.Constant;
import com.yuphilip.model.Tweet;
import com.yuphilip.model.helper.LinkifiedTextView;
import com.yuphilip.model.net.TwitterApp;
import com.yuphilip.model.net.TwitterClient;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    //region Properties

    private static final String TAG = "TweetsAdapter";
    private final Context context;
    private final List<Tweet> tweets;
    private TwitterClient client;

    //endregion

    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {

        this.context = context;
        this.tweets = tweets;

    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);

        return new ViewHolder(view);

    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Get the data at position
        Tweet tweet = tweets.get(position);

        // Bind the tweet with view holder
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {

        tweets.clear();
        notifyDataSetChanged();

    }

    // Add a list of items
    public void addAll(List<Tweet> tweetList) {

        tweets.addAll(tweetList);
        notifyDataSetChanged();

    }

    // Define a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        final ItemTweetBinding binding;
        final RelativeLayout container;
        final ImageView ivProfileImage;
        final TextView tvName;
        final TextView tvScreenName;
        final TextView tvTime;
        final LinkifiedTextView tvBody;
        final ImageView ivMediaImage;
        final Button btnFavor;
        final TextView tvFavorCount;
        final Button btnRetweet;
        final TextView tvRetweetCount;
        final Button btnReply;

        // Define a view holder
        ViewHolder(@NonNull View itemView) {

            super(itemView);

            binding = DataBindingUtil.bind(itemView);
            client = TwitterApp.getRestClient(context);

            container = binding.container;
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
            btnReply = binding.btnReply;

        }

        void bind(final Tweet tweet) {

            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .transform(new CircleCrop())
                    .into(ivProfileImage);

            tvName.setText(tweet.user.name);
            tvScreenName.setText(String.format(Locale.getDefault(),"@%s", tweet.user.screenName));
            tvTime.setText(Constant.getRelativeTimeAgo(tweet.createdAt));
            tvBody.setText(tweet.body);
            tvFavorCount.setText(String.format(Locale.getDefault(),"%d", tweet.favoriteCount));
            tvRetweetCount.setText(String.format(Locale.getDefault(),"%d", tweet.retweetCount));

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

                Glide.with(context)
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
                                tvFavorCount.setText(String.format(Locale.getDefault(),"%d", (tweet.favoriteCount + 1)));
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

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Segue to tweet details.", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivity(i);
                }
            });

        }

    }

    private void showTweetDialog(long id, String tweetOP) {

        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putLong("tweetId", id);
        bundle.putString("tweetOP", tweetOP);

        ReplyFragment replyFragment = ReplyFragment.newInstance("Reply to Tweet");

        replyFragment.setArguments(bundle);
        replyFragment.show(fragmentManager, "reply_fragment");

    }

}
