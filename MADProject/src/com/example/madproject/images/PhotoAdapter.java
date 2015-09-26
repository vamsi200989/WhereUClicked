package com.example.madproject.images;



import java.util.ArrayList;

import com.example.madproject.FlickrPhoto;
import com.example.madproject.R;
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


public class PhotoAdapter extends ArrayAdapter<FlickrPhoto>{
	Context context;
	ArrayList<FlickrPhoto> photosList = new ArrayList<FlickrPhoto>();
	public PhotoAdapter(Context context,ArrayList<FlickrPhoto> objects) {
		super(context, R.layout.row, R.id.imageTitle, objects);
		this.context=context;
		this.photosList=objects;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		ViewHolder holder;
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 convertView =inflater.inflate(R.layout.single_photo, parent, false);
			  holder = new ViewHolder();
			holder.imageIcon = (ImageView) convertView.findViewById(R.id.imageIcon);
			holder.viewsIcon= (ImageView) convertView.findViewById(R.id.viewIcon);
			holder.titleView = (TextView) convertView.findViewById(R.id.title);
			holder.viewsView = (TextView) convertView.findViewById(R.id.viewsText);
		
			convertView.setTag(holder);
		}
		
		holder = (ViewHolder) convertView.getTag();
		
		holder.titleView.setText(photosList.get(position).getTitle());
//		
//		if((photosList.get(position).getViews())!="null"){
//			holder.viewsView.setText((photosList.get(position).getViews()));
//		}
		
		
		if(photosList.get(position).getLargeImageUrl()!=""){
			
			
			Picasso.with(context).load(photosList.get(position).getSquareUrl()).resize(500, 500)
			.error(R.drawable.android_avatar).into(holder.imageIcon);
		}
		else{
			
			holder.imageIcon.setImageBitmap(null);
		}
		
		//iv.setImageBitmap(null);
		
		return convertView;
		
		
	}
	
	static class ViewHolder{
		ImageView imageIcon,viewsIcon;
		TextView titleView,viewsView;
	}
	
	
	
	
}

