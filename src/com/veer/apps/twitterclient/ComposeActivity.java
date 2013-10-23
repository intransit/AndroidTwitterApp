package com.veer.apps.twitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.veer.apps.twitterclient.models.Tweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeActivity extends Activity {

	private EditText etTweet;
	private TextView tvCharCount;
	private int charLeft = 140;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		etTweet = (EditText)findViewById(R.id.etTweet);
		tvCharCount = (TextView)findViewById(R.id.tvCharCount);
		
		etTweet.addTextChangedListener(new TextWatcher() {
			
			// Fires right as the text is being changed (even supplies the range of text)
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(before == 0)
					charLeft = charLeft-count;
				else
					charLeft = charLeft+1;
				
				CharSequence update = Integer.toString(charLeft) + " characters left!"; 
				tvCharCount.setText(update);
			}
			
			// Fires right before text is changing
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			// Fires right after the text has changed
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
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
//			TwitterClientApp.getRestClient().postStatus(tweet, new JsonHttpResponseHandler(){
//				@Override
//				public void onSuccess(JSONObject jsonTweets){
//					Toast.makeText(getBaseContext(), "yi hoo..", Toast.LENGTH_SHORT).show();
//					Log.d("DEBUG", jsonTweets.toString());
//					
//					Tweet tweets = Tweet.fromJson(jsonTweets);
//				
////					ListView lvTweets = (ListView)findViewById(R.id.lvTweets);
////					TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);
////					lvTweets.setAdapter(adapter);
//				}
//			});
			
			Intent data = new Intent();
			data.putExtra("tweet", tweet);
			
			//set result code and bundle data for response
			setResult(RESULT_OK, data);
			finish(); 
		}
	}
}
