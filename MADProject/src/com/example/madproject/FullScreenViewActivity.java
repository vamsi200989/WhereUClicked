package com.example.madproject;

import java.util.ArrayList;

import com.example.madproject.images.FullScreenImageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class FullScreenViewActivity extends Activity {

	private FullScreenImageAdapter adapter;
	private MapFullScreenAdapter madapter;
	private ViewPager viewPager;
	ArrayList<FlickrPhoto> cityPhotos;
	ArrayList<FlickrPhoto> personPhotos;
	int position;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);

		viewPager = (ViewPager) findViewById(R.id.pager);

		cityPhotos = new ArrayList<FlickrPhoto>();

		if (getIntent().getExtras() != null) {

			if (getIntent().getExtras().getString("screen")
					.equalsIgnoreCase("map")) {
				position = getIntent().getExtras().getInt("position");
				personPhotos = (ArrayList<FlickrPhoto>) getIntent().getExtras()
						.getSerializable("photosList");
				Log.d("fullscreen", "" + personPhotos.size());
				adapter = new FullScreenImageAdapter(
						FullScreenViewActivity.this, personPhotos);
				// displaying selected image first
				viewPager.setAdapter(adapter);
				viewPager.setCurrentItem(position);
			} else {
				position = getIntent().getExtras().getInt("position");
				cityPhotos = (ArrayList<FlickrPhoto>) getIntent().getExtras()
						.getSerializable("PhotosList");
				Log.d("fullscreen", "" + cityPhotos.size());
				adapter = new FullScreenImageAdapter(
						FullScreenViewActivity.this, cityPhotos);
				viewPager.setAdapter(adapter);
				// displaying selected image first
				viewPager.setCurrentItem(position);
			}

		}

	}
}
