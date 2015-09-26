package com.example.madproject;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.ListView;

import com.example.madproject.FlickrHelper;
import com.example.madproject.SelectionActivity;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.PhotoList;

public class GetPhotostreamTask extends AsyncTask<OAuth, Void, PhotoList> {

	/**
	 * 
	 */
	private ListView listView;
	private Activity activity;

	public PhotosMapActivity photosmaplistner;
	public Context context;
	GridView mgridview;

	/**
	 * @param context
	 * @param flickrjAndroidSampleActivity
	 */
	public GetPhotostreamTask(Activity activity) {
		this.activity = activity;

		this.photosmaplistner = (PhotosMapActivity) activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected PhotoList doInBackground(OAuth... arg0) {
		OAuthToken token = arg0[0].getToken();
		Flickr f = FlickrHelper.getInstance().getFlickrAuthed(
				token.getOauthToken(), token.getOauthTokenSecret());
		Set<String> extras = new HashSet<String>();
		extras.add("url_sq"); //$NON-NLS-1$
		extras.add("url_l"); //$NON-NLS-1$
		extras.add("views"); //$NON-NLS-1$
		extras.add("geo");
		User user = arg0[0].getUser();
		try {
			return f.getPhotosInterface().getWithGeoData(null, null, null,
					null, 0, "date-posted-asc", extras, 0, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(PhotoList result) {
		if (result != null) {
			photosmaplistner.getPhotoList(result);

		}

	}
}
