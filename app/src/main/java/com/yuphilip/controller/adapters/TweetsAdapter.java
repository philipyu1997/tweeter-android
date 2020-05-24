package com.yuphilip.controller.adapters;

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
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.yuphilip.apps.restclienttemplate.R;
import com.yuphilip.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.yuphilip.controller.activities.DetailActivity;
import com.yuphilip.model.Constant;
import com.yuphilip.model.Tweet;
import com.yuphilip.model.helper.LinkifiedTextView;

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

    // Define a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemTweetBinding binding;
        RelativeLayout container;
        ImageView ivProfileImage;
        TextView tvName;
        TextView tvScreenName;
        TextView tvTime;
        LinkifiedTextView tvBody;
        ImageView ivMediaImage;

        // Define a view holder
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            binding = DataBindingUtil.bind(itemView);

            container = binding.container;
            ivProfileImage = binding.ivProfileImage;
            tvName = binding.tvName;
            tvScreenName = binding.tvScreenName;
            tvTime = binding.tvTime;
            tvBody = binding.tvBody;
            ivMediaImage = binding.ivMediaImage;

        }

        public void bind(final Tweet tweet) {

            Glide.with(context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .transform(new CircleCrop())
                    .into(ivProfileImage);

            tvName.setText(tweet.getUser().getName());
            tvScreenName.setText("@" + tweet.getUser().getScreenName());
            tvTime.setText(Constant.getRelativeTimeAgo(tweet.getCreatedAt()));
            tvBody.setText(tweet.getBody());

            int radius = 30; // corner radius, higher value = more rounded

            if (tweet.getMediaUrl() != null) {
                ivMediaImage.setVisibility(View.VISIBLE);

                Glide.with(context)
                        .load(tweet.getMediaUrl())
                        .transform(new RoundedCorners(radius))
                        .into(ivMediaImage);
            } else {
                ivMediaImage.setVisibility(View.GONE);
            }

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
