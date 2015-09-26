package com.example.madproject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import com.example.madproject.FlickrPhoto;
import com.example.madproject.R;
import com.example.madproject.images.TouchImageView;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MapFullScreenAdapter extends PagerAdapter {

	private Activity _activity;
	private Context context;
	private ArrayList<Person> photosList;
	private LayoutInflater inflater;
	private int photoPosition;

	// constructor
	public MapFullScreenAdapter(Activity activity, ArrayList<Person> imagePaths) {
		this._activity = activity;
		this.context = activity;
		this.photosList = imagePaths;
	}

	@Override
	public int getCount() {
		return this.photosList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		TouchImageView imgDisplay;
		Button btnClose;
		ImageView flickrlink;
		TextView viewsText;

		inflater = (LayoutInflater) _activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = inflater.inflate(R.layout.fullscreen_imageview,
				container, false);

		imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
		btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
		flickrlink = (ImageView) viewLayout.findViewById(R.id.flickrlink);
		viewsText = (TextView) viewLayout.findViewById(R.id.views);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// if(photosList.get(position).getViews()!=null){
		// viewsText.setText(Integer.valueOf(photosList.get(position).getViews())+" "+"Views");
		// }
		// else{
		// viewsText.setText("No Views");
		// }
		viewsText.setText("No Views");
		photoPosition = position;
		// Picasso.with(context).load(photosList.get(position).getFlickrUrl()).
		// placeholder(R.drawable.android_avatar).error(R.drawable.android_avatar).into(imgDisplay);

		// close button click event
		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_activity.finish();
			}
		});

		flickrlink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		((ViewPager) container).addView(viewLayout);

		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);

	}

}
