package com.veer.apps.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.veer.apps.twitterclient.models.Tweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ComposeActivity extends Activity {

	private EditText etTweet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		etTweet = (EditText)findViewById(R.id.etTweet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	public void postTweet(View v){
		String tweet = etTweet.getEditableText().toString();
		
		if(tweet.isEmpty())
			Toast.makeText(this, "Please enter a proper value!", Toast.LENGTH_SHORT).show();
		else if(tweet.length() > 140)
			Toast.makeText(this, "Length of the text should be atmost 140 characters!", Toast.LENGTH_SHORT).show();
		else{
			TwitterClientApp.getRestClient().postStatus(new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONArray jsonTweets){
					ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
					
					ListView lvTweets = (ListView)findViewById(R.id.lvTweets);
					TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);
					lvTweets.setAdapter(adapter);
					
					Log.d("DEBUG", jsonTweets.toString());
				}
			});
			
			Intent data = new Intent();
			data.putExtra("tweet", tweet);
			
			//set result code and bundle data for response
			setResult(RESULT_OK, data);
			finish(); 
		}
	}
}
