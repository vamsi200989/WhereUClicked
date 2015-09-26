/*
Final Project
Group Members: Vamsi Krishna Atukuri Belagala, Colin McKenney, Crystal Nettles
Table 14A
*/



package com.example.madproject;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.madproject.CitiesFragment.OnFragmentInteractionListener;
import com.example.madproject.tasks.GetOAuthTokenTask;
import com.example.madproject.tasks.LoadPhotostreamTask;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;








import com.parse.SaveCallback;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class SelectionActivity extends Activity implements
		PhotoListFragment.OnFragmentInteractionListener,
		OnFragmentInteractionListener, LoadPhotostreamTask.passData,
		LogoutFragment.OnFragmentInteractionListener,LoginFragment.OnFragmentInteractionListener,SignUpFragment.OnFragmentInteractionListener{

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	ProgressBar pb;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	Context mainContext;
	public ToFragmentCommunicator tofragmentcommunicator;
	OAuth oauth;
	List<String> smallImageUrls;
	ArrayList<City> gridViewList;
	City userCity;
	ListView gridView;
	public CityAdapter cityAdapter;
	OAuth tempOauth;
	int k;
	AuthorizeFragment af;
	ArrayList<String> gridUrls;
	OAuth confirmOauth;

	public DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	ParseUser currentUser;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	public static final String CALLBACK_SCHEME = "flickrj-android-sample-oauth"; //$NON-NLS-1$
	public static final String PREFS_NAME = "flickrj-android-sample-pref"; //$NON-NLS-1$
	public static final String KEY_OAUTH_TOKEN = "flickrj-android-oauthToken"; //$NON-NLS-1$
	public static final String KEY_TOKEN_SECRET = "flickrj-android-tokenSecret"; //$NON-NLS-1$
	public static final String KEY_USER_NAME = "flickrj-android-userName"; //$NON-NLS-1$
	public static final String KEY_USER_ID = "flickrj-android-userId"; //$NON-NLS-1$
	public static OAuth mOAuth;
	private static final Logger logger = LoggerFactory
			.getLogger(SelectionActivity.class);
	ProgressDialog pd;
	String titlesArray[] = { "Authorize", "Cities", "Logout" };
	private RelativeLayout mDrawerRelativeLayout;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	public SelectionActivity() {

	}

	public SelectionActivity(Context mContext) {
		this.mainContext = mContext;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);
		
		
		
		mTitle = mDrawerTitle = getTitle();
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Authorize
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Cities
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// LogOut
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		

		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
		if(!isConnected()){
			//Toast.makeText(this, "You are not connected to network! Please connect to network", Toast.LENGTH_LONG).show();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("You are not connected to network! Please connect to network")
			.setCancelable(false)
			.setPositiveButton("Connect to Network", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
			        startActivity(new Intent(Settings.ACTION_SETTINGS));
			        
			   }
			 })
			.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
			        finish();
			   }
			});
			
			
			 AlertDialog alert = builder.create();
			 alert.show();
		}
		else{
			currentUser = ParseUser.getCurrentUser();
			if(currentUser==null){
				FragmentManager fm = getFragmentManager();
				
				Fragment fragment = new LoginFragment();
				fm.beginTransaction().add(R.id.container, fragment, "loginFragment").addToBackStack("loginFragment").commit();
			}
			
			else{
				OAuth oauth = getOAuthToken();

				if (oauth == null || oauth.getUser() == null) {
					AuthorizeFragment authorizeFragment = new AuthorizeFragment();
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager
							.beginTransaction()
							.replace(R.id.container, authorizeFragment, "authorizeFragment")
							.commit();

				} else {

					Log.d("passing", "oauthyes");
					CitiesFragment citiesFragment = new CitiesFragment();
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.container, citiesFragment, "citiesFragment")
							.commit();

					load(oauth);
				}
		}
		 

		}
		
	}

	public void load(OAuth oauth) {
		if (oauth != null) {

			mOAuth = oauth;
//			pd = new ProgressDialog(SelectionActivity.this);
//			pd.setTitle("Loading");
//			pd.setMessage("Organizing Your Photos..");
//			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			pd.setIndeterminate(true);
//			//pd.setCancelable(false);
//
//			pd.show();
			new LoadPhotostreamTask(SelectionActivity.this).execute(oauth);

		}
	}

	public void displayView(int position) {
		Fragment fragment = null;
		String tagg="";
		switch (position) {

		case 0:
			fragment = new AuthorizeFragment();
			tagg="authorizeFragment";
			break;
		case 1:
			fragment = new CitiesFragment();
			tagg="citiesFragment";
			break;
		case 2:
			fragment = new LogoutFragment();
			tagg="logoutFragment";
			break;
		}

		if (position != 2 && fragment != null) {
			
			
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment,tagg).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
			//mDrawerLayout.closeDrawer(mDrawerRelativeLayout);
			// setTitle(titlesArray[position]);

		} else if (position == 2 && fragment != null) {
			
			DialogFragment logoutFragment = LogoutFragment
					.newInstance(position);
			logoutFragment.show(getFragmentManager(), "logoutFragment");
			// setTitle(titlesArray[position]);
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			//mDrawerLayout.closeDrawer(mDrawerRelativeLayout);
			mDrawerLayout.closeDrawer(mDrawerList);
			
		}

	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.authorize);
		case 2:
			mTitle = getString(R.string.cities);
			break;
		case 3:
			mTitle = getString(R.string.logout);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPhotoList(PhotoList photoList) {
		// TODO Auto-generated method stub
		String placeId = null;

		// lazyAdapter = new LazyAdapter(SelectionActivity.this,photoList);

		tofragmentcommunicator.sendPhotoList(photoList);

	}

	public interface ToFragmentCommunicator {
		public void sendData(List<Address> addressList);

		public void sendPhotoList(PhotoList photoList);

		public void sendPhotoData(CityAdapter cityAdapter);
	}

	@Override
	public void getAddressList(HashMap<String, Address> addressList) {
		// TODO Auto-generated method stub

		sortList(addressList);
		// tofragmentcommunicator.sendPhotoData(addressList);
	}

	public void sortList(HashMap<String, Address> addressList) {

		smallImageUrls = new ArrayList<String>();
		gridViewList = new ArrayList<City>();
		k = 0;

		HashMap<String, String> cityPhoto = new HashMap<String, String>();
		HashMap<String, String> countPhoto = new HashMap<String, String>();

		List<String> cityList = new ArrayList<String>();
		for (Entry<String, Address> entry : addressList.entrySet()) {
			Address tempAddress = entry.getValue();
			String city = tempAddress.getLocality();
			if(city!=null){
				cityList.add(city);
				cityPhoto.put(entry.getKey(), city);
				countPhoto.put(city, entry.getKey());

			}
			
			
		}
		
		
		Set<String> treesetList = new TreeSet<String>(cityList);
		Set<String> hashsetList = new HashSet<String>(cityList);
		Log.d("cities", "" + hashsetList.size());

		for (String cityy : treesetList) {
			userCity = new City();
			Log.d("citycount", cityy);
			int count = 0;
			for (String c : cityList) {
				if (c.equals(cityy))
					count++;
			}
			Log.d("citycount", "" + count);
			userCity.setCityName(cityy);
			userCity.setPhotosCount("" + count);
			gridViewList.add(userCity);

		}

		new GetGrid().execute(treesetList);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// this is very important, otherwise you would get a null Scheme in the
		// onResume later on.
		setIntent(intent);
	}

	// public void setUser(User user) {
	// textUserTitle.setText(user.getUsername());
	// textUserName.setText(user.getRealName());
	// textUserId.setText(user.getId());
	// }

	// public ImageView getUserIconImageView() {
	// return this.userIcon;
	// }

//	@Override
//	public void onResume() {
//		super.onResume();
//		Intent intent = getIntent();
//		String scheme = intent.getScheme();
//		OAuth savedToken = getOAuthToken();
//		if (CALLBACK_SCHEME.equals(scheme)
//				&& (savedToken == null || savedToken.getUser() == null)) {
//			Uri uri = intent.getData();
//			String query = uri.getQuery();
//			logger.debug("Returned Query: {}", query); //$NON-NLS-1$
//			String[] data = query.split("&"); //$NON-NLS-1$
//			if (data != null && data.length == 2) {
//				String oauthToken = data[0].substring(data[0].indexOf("=") + 1); //$NON-NLS-1$
//				String oauthVerifier = data[1]
//						.substring(data[1].indexOf("=") + 1); //$NON-NLS-1$
//				logger.debug(
//						"OAuth Token: {}; OAuth Verifier: {}", oauthToken, oauthVerifier); //$NON-NLS-1$
//
//				OAuth oauth = getOAuthToken();
//				if (oauth != null && oauth.getToken() != null
//						&& oauth.getToken().getOauthTokenSecret() != null) {
//					GetOAuthTokenTask task = new GetOAuthTokenTask(this);
//					task.execute(oauthToken, oauth.getToken()
//							.getOauthTokenSecret(), oauthVerifier);
//				}
//			}
//		}
//
//	}

	public void onOAuthDone(OAuth result) {
		if (result == null) {
			Toast.makeText(this, "Authorization failed", //$NON-NLS-1$
					Toast.LENGTH_LONG).show();
		} else {
			User user = result.getUser();
			OAuthToken token = result.getToken();
			if (user == null || user.getId() == null || token == null
					|| token.getOauthToken() == null
					|| token.getOauthTokenSecret() == null) {
				Toast.makeText(this, "Authorization failed", //$NON-NLS-1$
						Toast.LENGTH_LONG).show();
				return;
			}
			
			
			confirmOauth = result;
			
			
			
			String[] currentUserEmail = ParseUser.getCurrentUser().getEmail().split("@");
			String tempcurrentID = currentUserEmail[0];
				String message = String.format(Locale.US,
						"Authorization Succeed: user=%s, userId=%s", //$NON-NLS-1$
						user.getUsername(), user.getId());
				Toast.makeText(this, message, Toast.LENGTH_LONG).show();
				saveOAuthToken(user.getUsername(), user.getId(),
						token.getOauthToken(), token.getOauthTokenSecret());
				
				if(ParseUser.getCurrentUser()!=null){
					ParseUser.getCurrentUser().put("FlickrUserID", user.getId());
					ParseUser.getCurrentUser().put("FlickrUserName", user.getUsername());
					ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if(e==null){
								load(confirmOauth);
							}
						}
					});
					
				}

		}
	}

	public OAuth getOAuthToken() {
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Context.MODE_MULTI_PROCESS);
		String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
		String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);
		if (oauthTokenString == null && tokenSecret == null) {
			logger.warn("No oauth token retrieved"); //$NON-NLS-1$
			return null;
		}
		OAuth oauth = new OAuth();
		String userName = settings.getString(KEY_USER_NAME, null);
		String userId = settings.getString(KEY_USER_ID, null);
		if (userId != null) {
			User user = new User();
			user.setUsername(userName);
			user.setId(userId);
			oauth.setUser(user);
		}
		OAuthToken oauthToken = new OAuthToken();
		oauth.setToken(oauthToken);
		oauthToken.setOauthToken(oauthTokenString);
		oauthToken.setOauthTokenSecret(tokenSecret);
		logger.debug(
				"Retrieved token from preference store: oauth token={}, and token secret={}", oauthTokenString, tokenSecret); //$NON-NLS-1$
		return oauth;
	}

	public void saveOAuthToken(String userName, String userId, String token,
			String tokenSecret) {
		logger.debug(
				"Saving userName=%s, userId=%s, oauth token={}, and token secret={}", new String[] { userName, userId, token, tokenSecret }); //$NON-NLS-1$
		SharedPreferences sp = getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(KEY_OAUTH_TOKEN, token);
		editor.putString(KEY_TOKEN_SECRET, tokenSecret);
		editor.putString(KEY_USER_NAME, userName);
		editor.putString(KEY_USER_ID, userId);
		editor.commit();
	}

	public void doPositiveClick() {
		// Do stuff here.
		Log.d("LogoutF", "Positive click!");

	}

	public void unAuthorize() {
		// Do stuff here.
		Log.d("LogoutF", "Negative click!");
		SharedPreferences sp = getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
		finish();
		ParseUser.logOut();
	}
	
	public void doNegativeClick() {
		// Do stuff here.
		
	

		 ParseUser.logOut();
		 
		
		 
		FragmentManager fm = getFragmentManager();
		
		Fragment fragment = new LoginFragment();
		fm.beginTransaction().replace(R.id.container, fragment, "loginFragment").commit();
		 TextView emptyView;
		 
		Fragment cFragment = (CitiesFragment) fm.findFragmentByTag("citiesFragment");
		if(cFragment!=null){
			View view = fm.findFragmentByTag("citiesFragment").getView();
			gridView = (ListView) view.findViewById(R.id.listView1);
			emptyView = (TextView) view.findViewById(R.id.empty);
			gridView.setEmptyView(emptyView);
		}
		
		if(cityAdapter!=null){
			cityAdapter.clear();
		}
		
		Toast.makeText(this, "You have LoggedOut Successfully", Toast.LENGTH_LONG).show();
		
	}

	class GetGrid extends AsyncTask<Set<String>, Void, ArrayList<String>> {

		ArrayList<String> smallUrls;

		@Override
		protected ArrayList<String> doInBackground(Set<String>... params) {
			// TODO Auto-generated method stub
			SharedPreferences settings = getSharedPreferences(PREFS_NAME,
					Context.MODE_MULTI_PROCESS);
			String ownerName = settings.getString(KEY_USER_NAME, "");
			smallUrls = new ArrayList<String>();
			for (String p : params[0]) {
				ParseQuery<ParseObject> query = ParseQuery
						.getQuery("PhotosDetails");

				query.whereEqualTo("Locality", p);
				query.whereEqualTo("Owner",ownerName );
				query.selectKeys(Arrays.asList("GridUrl"));
				try {
					ParseObject ob = query.getFirst();

					smallUrls.add(ob.getString("GridUrl"));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			return smallUrls;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			gridUrls = result;
			Log.d("small", "" + result.size());
			for (int j = 0; j < gridUrls.size(); j++) {
				gridViewList.get(j).setThumbnailUrl(gridUrls.get(j));
			}
			
			if(LoadPhotostreamTask.FlickrPhotos.isEmpty()){
				
				
			}
			

			cityAdapter = new CityAdapter(SelectionActivity.this, gridViewList);

			LoadPhotostreamTask.pd.cancel();
			CitiesFragment cf = new CitiesFragment();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.container, cf, "citiesFragment").commit();
			setTitle(titlesArray[1]);
			
			
		}

	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	
	public boolean isConnected(){
		ConnectivityManager cm =
		        (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		
		//boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
		
		return isConnected;
	}
	
	
}
