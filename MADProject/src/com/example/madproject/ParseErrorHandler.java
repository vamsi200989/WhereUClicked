package com.example.madproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.Toast;

import com.parse.ParseException;


	public class ParseErrorHandler {
		  private static final int INVALID_SESSION_TOKEN=209;
		  static Activity mactivity;
		 
		 
		public static void handleParseError(ParseException e,Activity activity) {
		    switch (e.getCode()) {
		      case INVALID_SESSION_TOKEN:handleInvalidSessionToken( activity);
		        break;
		 
		      
		    }
		  }
		 
		public static  void handleInvalidSessionToken(Activity activity) {
			  
			 mactivity=activity;
			 Toast.makeText(activity, "Session is no longer valid, please log out and log in again.", Toast.LENGTH_LONG).show();
			 
			
				((SelectionActivity)mactivity).mDrawerLayout.openDrawer(Gravity.LEFT);
			  
			  new AlertDialog.Builder(activity)
			       .setMessage("Session is no longer valid, please log out and log in again.")
			       .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						
					}
				}).create().show();
		    //--------------------------------------
		    // Option 1: Show a message asking the user to log out and log back in.
		    //--------------------------------------
		    // If the user needs to finish what they were doing, they have the opportunity to do so.
		    //
		    // new AlertDialog.Builder(getActivity())
		    //   .setMessage("Session is no longer valid, please log out and log in again.")
		    //   .setCancelable(false).setPositiveButton("OK", ...).create().show();
		 
		    //--------------------------------------
		    // Option #2: Show login screen so user can re-authenticate.
		    //--------------------------------------
		    // You may want this if the logout button could be inaccessible in the UI.
		    //
		    // startActivityForResult(new ParseLoginBuilder(getActivity()).build(), 0);
		  }
		
		 
		}

