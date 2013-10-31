package com.veer.apps.twitterclient;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.veer.apps.twitterclient.models.Tweet;

public class TweetsAdapter extends ArrayAdapter<Tweet>{

	private Context baseContext;
	private String screenName;
	
	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		baseContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null){
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		
		Tweet tweet = getItem(position);
		
		//setup the username and twitter handle (ID)
		TextView nameView = (TextView)view.findViewById(R.id.tvName);
		String formattedName = "<b>" + tweet.getUser().getName() + "</b>" + "<small><font color='#777777'>@" + 
				tweet.getUser().getScreenName() + "</font></small>";
		nameView.setText(Html.fromHtml(formattedName));
		
		//setup the tweet/status
		TextView bodyView = (TextView)view.findViewById(R.id.tvBody);
		bodyView.setText(Html.fromHtml(tweet.getBody()));
		
		//setup the profile image
		ImageView imageView = (ImageView)view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);
		
		screenName = tweet.getUser().getScreenName();
		//imageView.setTag(screenName);
		imageView.setTag(tweet.getUser().getScreenName());
		
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(baseContext, ProfileActivity.class);
				//i.putExtra("screen_name", screenName);
				i.putExtra("screen_name", v.getTag().toString());
				baseContext.startActivity(i);
			}
		});
		
		return view;
	}

	
}
