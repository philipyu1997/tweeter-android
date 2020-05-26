package com.yuphilip.controller.fragments;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.yuphilip.R;
import com.yuphilip.model.net.TwitterApp;
import com.yuphilip.model.net.TwitterClient;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReplyFragment extends DialogFragment {

    //region Properties

    public static final String TAG = "ReplyFragment";
    public static final int MAX_TWEET_LENGTH = 140;

    private EditText etCompose;
    private Button btnTweet;
    private TextView tvReplyTo;
    private TwitterClient client;
    private long tweetId;
    private String tweetOP;

    //endregion

    public ReplyFragment() {

        // Required empty public constructor

    }

    public static ReplyFragment newInstance(String title) {

        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();

        args.putString("title", title);
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        tweetId = getArguments().getLong("tweetId");
        tweetOP = getArguments().getString("tweetOP");
        return inflater.inflate(R.layout.fragment_reply, container, false);

    }

    // This event is triggered soon after onCreateView()
    // Any view setup should occur here. E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        client = TwitterApp.getRestClient(view.getContext());

        etCompose = view.findViewById(R.id.etReply);
        btnTweet = view.findViewById(R.id.btnReply);
        tvReplyTo = view.findViewById(R.id.tvReplyTo);

        String title = getArguments().getString("title", "Enter name");
        getDialog().setTitle(title);

        tvReplyTo.setText(Html.fromHtml("Replying to <b><font color='#1DA1F2'>@" + tweetOP + "</font></b>"));
        etCompose.setText(Html.fromHtml("<b><font color='#1DA1F2'>@" + tweetOP + "</b> "));
        etCompose.requestFocus();

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tweetContent = etCompose.getText().toString();

                if (tweetContent.isEmpty()) {
                    Toast.makeText(view.getContext(), "Sorry, your reply cannot be empty...", Toast.LENGTH_LONG).show();
                    return;
                }

                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(view.getContext(), "Sorry, your reply is too long...", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(view.getContext(), tweetContent, Toast.LENGTH_LONG).show();

                // Make an API call to Twitter to reply to tweet
                client.replyToTweet(tweetId, tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to reply to tweet");
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to reply to tweet", throwable);
                    }
                });
            }
        });

    }

}
