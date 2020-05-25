package com.yuphilip.controller.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.yuphilip.R;
import com.yuphilip.databinding.ActivityComposeBinding;
import com.yuphilip.databinding.ActivityDetailBinding;
import com.yuphilip.model.Constant;
import com.yuphilip.model.Tweet;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    //region Properties

    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvScreenName;
    private TextView tvTime;
    private TextView tvBody;
    private ImageView ivMediaImage;
    private ActivityDetailBinding binding;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        ivProfileImage = binding.ivProfileImage;
        tvName = binding.tvName;
        tvScreenName = binding.tvScreenName;
        tvTime = binding.tvTime;
        tvBody = binding.tvBody;
        ivMediaImage = binding.ivMediaImage;

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .transform(new CircleCrop())
                .into(ivProfileImage);

        tvName.setText(tweet.user.name);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvTime.setText(Constant.getRelativeTimeAgo(tweet.createdAt));
        tvBody.setText(tweet.body);

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

    }

}
