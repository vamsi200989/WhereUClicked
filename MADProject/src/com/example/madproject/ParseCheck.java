package com.example.madproject;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import android.app.Application;

public class ParseCheck extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		Parse.initialize(this, "k4Yy1jfwN93FNyaVedkDIs6jpH4nuGQfOguglOxD",
				"Tt2vMxnQc9qw70IDMyugMqAVjuBVWieYuftP4ZeU");
//		 ParseInstallation.getCurrentInstallation().saveInBackground();
//		 ParseUser.enableRevocableSessionInBackground();
		super.onCreate();
	}

	
	
}
