package com.veer.apps.twitterclient.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.veer.apps.twitterclient.R;
import com.veer.apps.twitterclient.TweetsAdapter;
import com.veer.apps.twitterclient.models.Tweet;

import eu.erikw.PullToRefreshListView;

public class TweetsListFragment extends Fragment {

	private TweetsAdapter adapter;
	private PullToRefreshListView lvTweets;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tweets_list, parent, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//to get access to the activity that owns (the handle) this fragment, use: getActivity()
		//from that method you will have access to all the functions that you had within that activity
		//eg: getActivity().findViewById(id);
		
		lvTweets = (PullToRefreshListView)getActivity().findViewById(R.id.lvTweets);
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(); //.fromJson(jsonTweets);
		
		adapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets.setAdapter(adapter);
	}
	
	public TweetsAdapter getAdapter(){
		return adapter;
	}
}
