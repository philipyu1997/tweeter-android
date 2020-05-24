package com.yuphilip.apps.restclienttemplate.controller.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yuphilip.apps.restclienttemplate.R;
import com.yuphilip.apps.restclienttemplate.controller.activities.DetailActivity;
import com.yuphilip.apps.restclienttemplate.model.Constant;
import com.yuphilip.apps.restclienttemplate.model.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    //region Properties

    private Context context;
    private List<Tweet> tweets;

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

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvName;
        TextView tvTime;

        // Define a viewholder
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            container = itemView.findViewById(R.id.container);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);

        }

        public void bind(final Tweet tweet) {

            Glide.with(context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .into(ivProfileImage);

            tvName.setText(tweet.getUser().getName());
            tvScreenName.setText("@" + tweet.getUser().getScreenName());
            tvTime.setText(Constant.getRelativeTimeAgo(tweet.getCreatedAt()));
            tvBody.setText(tweet.getBody());

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

}
