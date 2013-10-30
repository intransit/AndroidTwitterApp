package com.veer.apps.twitterclient.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.veer.apps.twitterclient.TwitterClientApp;
import com.veer.apps.twitterclient.models.Tweet;

import android.os.Bundle;

public class HomeTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TwitterClientApp.getRestClient().getHomeTimeline(0, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets){
				getAdapter().addAll(Tweet.fromJson(jsonTweets));
			}
		});
	}

}
