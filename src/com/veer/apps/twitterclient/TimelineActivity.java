package com.veer.apps.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.veer.apps.twitterclient.models.Tweet;

public class TimelineActivity extends Activity {

	private long max_id = 0;
	private static final int REQUEST_CODE = 1234;
	
	private String newTweet;
	private TweetsAdapter adapter;
	private ListView lvTweets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		lvTweets = (ListView)findViewById(R.id.lvTweets);
		
		TwitterClientApp.getRestClient().getHomeTimeline(max_id, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets){
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				
				adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
				
				Log.d("DEBUG", jsonTweets.toString());
			}
		});
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
                // Whatever code is needed to append new items to your AdapterView
                // probably sending out a network request and appending items to your adapter. 
                // Use the page or the totalItemsCount to retrieve correct data.
		        loadMoreTweets();
		    }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	public void loadMoreTweets(){
		int numItems = adapter.getCount();
		Tweet lastTweet = adapter.getItem(numItems-1);
		max_id = lastTweet.getId();
		
		TwitterClientApp.getRestClient().getHomeTimeline(max_id, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets){
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				adapter.addAll(tweets);
			}
		});
	}
	
	public void launchTweetComposeActivity(MenuItem m){
		Intent i = new Intent(this, ComposeActivity.class);
   	 	startActivityForResult(i, REQUEST_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			newTweet = data.getExtras().getString("tweet");
		    Toast.makeText(this, newTweet, Toast.LENGTH_SHORT).show();
		    
		    TwitterClientApp.getRestClient().postStatus(newTweet, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject jsonTweets){
					Toast.makeText(getBaseContext(), "yi hoo..", Toast.LENGTH_SHORT).show();
					Log.d("DEBUG", jsonTweets.toString());
					
					Tweet tweets = Tweet.fromJson(jsonTweets);
					adapter.insert(tweets, 0);
				}
			});
		}
	}
}
