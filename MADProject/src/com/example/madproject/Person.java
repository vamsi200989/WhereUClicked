package com.example.madproject;





import java.io.Serializable;




import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;



public class Person implements ClusterItem {
    public  String name;
 
    public  String picUrl,FlickrUrl;
    private  LatLng mPosition;
    public  int photoNum;
    public Bitmap imageBitmap;
    public Bitmap getImageBitmap() {
		return imageBitmap;
	}
	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}
	public Person(LatLng position){
    	this.mPosition=position;
    }
	
	public Person(){
		
	}
	@Override
	public LatLng getPosition() {
		// TODO Auto-generated method stub
		
		return mPosition;
	}
	public LatLng getmPosition() {
		return mPosition;
	}
	public void setmPosition(LatLng mPosition) {
		this.mPosition = mPosition;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getFlickrUrl() {
		return FlickrUrl;
	}
	public void setFlickrUrl(String flickrUrl) {
		FlickrUrl = flickrUrl;
	}
	
	public int getPhotoNum() {
		return photoNum;
	}
	public void setPhotoNum(int photoNum) {
		this.photoNum = photoNum;
	}

   
}

