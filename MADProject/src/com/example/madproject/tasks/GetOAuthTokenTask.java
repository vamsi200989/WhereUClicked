package com.example.madproject.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.madproject.FlickrHelper;

import com.example.madproject.SelectionActivity;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;

import android.os.AsyncTask;


public class GetOAuthTokenTask extends AsyncTask<String, Integer, OAuth> {
	private static final Logger logger = LoggerFactory.getLogger(GetOAuthTokenTask.class);

	private SelectionActivity activity;

	public GetOAuthTokenTask(SelectionActivity context) {
		this.activity = context;
	}

	@Override
	protected OAuth doInBackground(String... params) {
		String oauthToken = params[0];
		String oauthTokenSecret = params[1];
		String verifier = params[2];

		Flickr f = FlickrHelper.getInstance().getFlickr();
		OAuthInterface oauthApi = f.getOAuthInterface();
	
		try {
			return oauthApi.getAccessToken(oauthToken, oauthTokenSecret,
					verifier);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return null;
		}

	}

	@Override
	protected void onPostExecute(OAuth result) {
		if (activity != null) {
			activity.onOAuthDone(result);
		}
	}


}
