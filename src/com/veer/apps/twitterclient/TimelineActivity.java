package com.veer.apps.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.veer.apps.twitterclient.fragments.HomeTimelineFragment;
import com.veer.apps.twitterclient.fragments.MentionsFragment;
import com.veer.apps.twitterclient.fragments.TweetsListFragment;
import com.veer.apps.twitterclient.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends FragmentActivity implements TabListener {

	private long max_id = 0;
	private static final int REQUEST_CODE = 1234;
	
	private String newTweet;
	private TweetsAdapter adapter;
	
	//private ListView lvTweets;
	//private PullToRefreshListView lvTweets;
	//private TweetsListFragment fragmentTweets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		//lvTweets = (ListView)findViewById(R.id.lvTweets);
		//lvTweets = (PullToRefreshListView)findViewById(R.id.lvTweets);
		//fragmentTweets = (TweetsListFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentTweets);
		
		setupNavigationTabs();
		
		//Get home timeline the first time activity loads [max_id = 0]
/*		TwitterClientApp.getRestClient().getHomeTimeline(max_id, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets){
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);				
//				adapter = new TweetsAdapter(getBaseContext(), tweets);
//				lvTweets.setAdapter(adapter);
				
				fragmentTweets.getAdapter().addAll(tweets);
				
				Log.d("DEBUG", jsonTweets.toString());
			}
		});*/
		
		//Endless scrolling listener
/*		lvTweets.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
                // Whatever code is needed to append new items to your AdapterView
                // probably sending out a network request and appending items to your adapter. 
                // Use the page or the totalItemsCount to retrieve correct data.
		        loadMoreTweets();
		    }
	    });
		
		// Pull-to-refresh: Set a listener to be invoked when the list should be refreshed.
        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
                fetchTimelineOnPullToRefresh(0);
                
                // Now we call onRefreshComplete to signify refresh has finished
                lvTweets.onRefreshComplete();
            }
        });
*/
	}

	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tabHome = actionBar.newTab().setText("Home")
				.setTag("HomeTimelineFragment").setIcon(R.drawable.ic_home).setTabListener(this);
		Tab tabMentions = actionBar.newTab().setText("Mentions")
				.setTag("MentionsTimelineFragment").setIcon(R.drawable.ic_mentions).setTabListener(this);
		
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
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
	
	public void onProfileView(MenuItem m){
		Intent i = new Intent(this, ProfileActivity.class);
   	 	startActivity(i);
	}
	
	public void onImageView(){
		Intent i = new Intent(this, ProfileActivity.class);
   	    startActivity(i);
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
					//Toast.makeText(getBaseContext(), "yi hoo..", Toast.LENGTH_SHORT).show();
					Log.d("DEBUG", jsonTweets.toString());
					
					Tweet tweets = Tweet.fromJson(jsonTweets);
					adapter.insert(tweets, 0);
				}
			});
		}
	}
	
	public void fetchTimelineOnPullToRefresh(int page) {
		TwitterClientApp.getRestClient().getHomeTimeline(0, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray jsonTweets) {
                // ...the data has come back, finish populating listview...
            	ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
            	adapter.clear();
				adapter.addAll(tweets);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		
		if(tab.getTag() == "HomeTimelineFragment"){
			//set the fragment in framelayout to home timeline
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
		} else if(tab.getTag() == "MentionsTimelineFragment"){
			//set the fragment in framelayout to mentions timeline
			fts.replace(R.id.frame_container, new MentionsFragment());
		}
		
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
	}
}
