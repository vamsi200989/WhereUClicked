package com.example.madproject.tasks;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.madproject.FlickrHelper;
import com.example.madproject.R;
import com.example.madproject.SelectionActivity;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.auth.Permission;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;




public class OAuthTask extends AsyncTask<Void, Integer, String> {

	private static final Logger logger = LoggerFactory
			.getLogger(OAuthTask.class);
	private static final Uri OAUTH_CALLBACK_URI = Uri.parse(SelectionActivity.CALLBACK_SCHEME
			+ "://oauth"); //$NON-NLS-1$

	/**
	 * The context.
	 */
	private Context mContext;
	Dialog auth_dialog;
	WebView web;
	OAuthToken oauthToken;
	public SelectionActivity sa;
	public boolean clearCookies=false;
	String username;
	String password;
	/**
	 * The progress dialog before going to the browser.
	 */
	private ProgressDialog mProgressDialog;
	SelectionActivity act;

	/**
	 * Constructor.
	 * 
	 * @param context
	 */
	public OAuthTask(Context context) {
		super();
		this.mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = ProgressDialog.show(mContext,
				"", "Generating the authorization request..."); //$NON-NLS-1$ //$NON-NLS-2$
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dlg) {
				OAuthTask.this.cancel(true);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Void... params) {
		try {
			Flickr f = FlickrHelper.getInstance().getFlickr();
			 oauthToken = f.getOAuthInterface().getRequestToken(
					OAUTH_CALLBACK_URI.toString());
			saveTokenSecrent(oauthToken.getOauthTokenSecret());
			URL oauthUrl = f.getOAuthInterface().buildAuthenticationUrl(
					Permission.READ, oauthToken);
			return oauthUrl.toString();
		} catch (Exception e) {
			logger.error("Error to oauth", e); //$NON-NLS-1$
			return "error:" + e.getMessage(); //$NON-NLS-1$
		}
	}

	/**
	 * Saves the oauth token secrent.
	 * 
	 * @param tokenSecret
	 */
	private void saveTokenSecrent(String tokenSecret) {
		logger.debug("request token: " + tokenSecret); //$NON-NLS-1$
		 act = (SelectionActivity) mContext;
		act.saveOAuthToken(null, null, null, tokenSecret);
		logger.debug("oauth token secret saved: {}", tokenSecret); //$NON-NLS-1$
	}

	@Override
	protected void onPostExecute(String result) {
		 username="vamsi201089@yahoo.com";
		 password = "geetanjaliappu";
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
//		if (result != null && !result.startsWith("error") ) { //$NON-NLS-1$
//		mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
//					.parse(result)));
//		} else {
//			Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
//			
//		}
		if (result != null) {

			 auth_dialog = new Dialog(mContext);
			auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			auth_dialog.setContentView(R.layout.auth_dialog);
		 web = (WebView) auth_dialog.findViewById(R.id.webv);
			web.getSettings().setJavaScriptEnabled(true);
			
			
			web.loadUrl(result);
			
			web.setWebViewClient(new WebViewClient() {
				boolean authComplete = false;

				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
					
					
				}

				


				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					
					view.clearHistory();
					view.clearCache(true);
					view.clearFormData();
					//web.loadUrl("javascript:document.getElementById('login-username').value = '"+username+"';document.getElementById('login-passwd').value='"+password+"';"
							//+ "document.getElementById('login-signin').click();");
					if (url.contains("oauth_verifier")
							&& authComplete == false) {
						authComplete = true;
						Log.d("twitter", "url" + url);
						OAuth savedToken = ((SelectionActivity)mContext).getOAuthToken();
						if ((savedToken == null || savedToken.getUser() == null)) {
							
							Uri uri = Uri.parse(url);
							String oauth_verifier = uri
									.getQueryParameter("oauth_verifier");
						auth_dialog.dismiss();
								
							

								OAuth oauth = ((SelectionActivity)mContext).getOAuthToken();
								if (oauth != null && oauth.getToken() != null
										&& oauth.getToken().getOauthTokenSecret() != null) {
									GetOAuthTokenTask task = new GetOAuthTokenTask((SelectionActivity) mContext);
									task.execute(oauthToken.getOauthToken(), oauth.getToken()
											.getOauthTokenSecret(), oauth_verifier);
									
									CookieSyncManager.createInstance(mContext);         
									 CookieManager cookieManager = CookieManager.getInstance();        
									 cookieManager.removeAllCookie();
								}
							
						}
//						Uri uri = Uri.parse(url);
//						String oauth_verifier = uri
//								.getQueryParameter("oauth_token");
//					auth_dialog.dismiss();
//						new GetOAuthTokenTask((SelectionActivity) mContext).execute(oauthToken.getOauthToken(),oauthToken.getOauthTokenSecret(),oauth_verifier);
						
					} else if (url.contains("denied")) {
						auth_dialog.dismiss();
						Toast.makeText(mContext, " Permission Denied",
								Toast.LENGTH_SHORT).show();
					}
					
					
				}
			});
			auth_dialog.show();
			auth_dialog.setCancelable(true);
		} else {
			Toast.makeText(mContext,
					"Network Error or Invalid Credentials",
					Toast.LENGTH_SHORT).show();
		}
		
		
		
		
	}
		
		
	}



