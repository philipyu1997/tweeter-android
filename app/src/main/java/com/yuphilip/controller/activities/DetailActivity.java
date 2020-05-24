package com.yuphilip.controller.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.yuphilip.apps.restclienttemplate.R;
import com.yuphilip.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.yuphilip.apps.restclienttemplate.databinding.ActivityDetailBinding;
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
