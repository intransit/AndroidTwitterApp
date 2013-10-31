package com.veer.apps.twitterclient;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.veer.apps.twitterclient.fragments.TweetsListFragment;
import com.veer.apps.twitterclient.models.Tweet;
import com.veer.apps.twitterclient.models.User;

public class ProfileActivity extends FragmentActivity {

	public static String _screenName;
	private TweetsListFragment fragmentTweets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		_screenName = getIntent().getStringExtra("screen_name");
		
		fragmentTweets = (TweetsListFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentUserTimeline);
		
		loadProfileInfo();
		loadTimeline();
	}

	private void loadTimeline() {
		TwitterClientApp.getRestClient().getUserTimeline(_screenName, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets){
				fragmentTweets.getAdapter().clear();
				fragmentTweets.getAdapter().addAll(Tweet.fromJson(jsonTweets));
			}
		});
	}

	private void loadProfileInfo() {
		TwitterClientApp.getRestClient().getMyInfo(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json){
				User u = User.fromJson(json);
				getActionBar().setTitle("@ " + u.getScreenName());
				populateProfileHeader(u);
			}
		});
	}

	protected void populateProfileHeader(User user) {
		TextView tvName = (TextView)findViewById(R.id.tvName);
		TextView tvTagline = (TextView)findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
		
		tvName.setText(user.getName());
		tvTagline.setText(user.getTagline());
		tvFollowers.setText(user.getFollowersCount() + " Followers");
		tvFollowing.setText(user.getFriendsCount() + " Following");
		
		//loading image: using UniversalImageLoader lib
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
