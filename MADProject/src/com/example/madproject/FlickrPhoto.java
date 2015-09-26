package com.example.madproject;

import java.io.Serializable;

public class FlickrPhoto implements Serializable {

	String largeImageUrl, views, title, flickrurl, smallUrl, SquareUrl;

	public String getSmallUrl() {
		return smallUrl;
	}

	public String getSquareUrl() {
		return SquareUrl;
	}

	public void setSquareUrl(String squareUrl) {
		SquareUrl = squareUrl;
	}

	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}

	int photoNumber;

	public String getFlickrurl() {
		return flickrurl;
	}

	public int getPhotoNumber() {
		return photoNumber;
	}

	public void setPhotoNumber(int photoNumber) {
		this.photoNumber = photoNumber;
	}

	public void setFlickrurl(String flickrurl) {
		this.flickrurl = flickrurl;
	}

	public String getLargeImageUrl() {
		return largeImageUrl;
	}

	public void setLargeImageUrl(String largeImageUrl) {
		this.largeImageUrl = largeImageUrl;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
