package com.veer.apps.twitterclient.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.veer.apps.twitterclient.ProfileActivity;
import com.veer.apps.twitterclient.R;
import com.veer.apps.twitterclient.TwitterClientApp;
import com.veer.apps.twitterclient.models.Tweet;

public class UserTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		String uName = ProfileActivity._screenName;
//		System.out.println("Screen nameeeee: " + uName);
//		
//		ImageView imageView = (ImageView)getActivity().findViewById(R.id.ivProfile);
//		if(imageView.getTag() != null)
//			System.out.println("Screen nameeeee 0000000: " + imageView.getTag().toString());
		
//		TwitterClientApp.getRestClient().getUserTimeline(null, new JsonHttpResponseHandler(){
//			@Override
//			public void onSuccess(JSONArray jsonTweets){
//				getAdapter().addAll(Tweet.fromJson(jsonTweets));
//			}
//		});
	}
	
	public void setUser(String sName){
		TwitterClientApp.getRestClient().getUserTimeline(sName, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets){
				getAdapter().addAll(Tweet.fromJson(jsonTweets));
			}
		});
	}
}
