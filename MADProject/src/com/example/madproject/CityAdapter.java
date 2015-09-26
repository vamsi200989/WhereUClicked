package com.example.madproject;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CityAdapter extends ArrayAdapter<City>{
	Context context;
	ArrayList<City> titlesList = new ArrayList<City>();
	public CityAdapter(Context context,ArrayList<City> objects) {
		super(context, R.layout.single_photo, R.id.city, objects);
		this.context=context;
		this.titlesList=objects;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		ViewHolder holder;
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 convertView =inflater.inflate(R.layout.single_city, parent, false);
			  holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.thumbnail);
			holder.nameView = (TextView) convertView.findViewById(R.id.city);
			holder.photosView = (TextView) convertView.findViewById(R.id.photosCount);
		
			convertView.setTag(holder);
		}
		
		holder = (ViewHolder) convertView.getTag();
		
		holder.nameView.setText(titlesList.get(position).getCityName());
		
	
		holder.photosView.setText((titlesList.get(position).getPhotosCount())+" Photos");
		
		Log.d("count",""+(titlesList.get(position).getPhotosCount()));
		
		if(titlesList.get(position).getThumbnailUrl()!=""){
		
			
			Picasso.with(context).load(titlesList.get(position).getThumbnailUrl()).
			error(R.drawable.android_avatar).into(holder.iv);
		}
		else{
			
			holder.iv.setImageBitmap(null);
		}
		
		
		
		return convertView;
		
		
	}
	
	static class ViewHolder{
		ImageView iv;
		TextView nameView,photosView;
	}
	
	
	
	
}
