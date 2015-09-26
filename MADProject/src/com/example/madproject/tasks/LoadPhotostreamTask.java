package com.example.madproject.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.madproject.CitiesFragment;
import com.example.madproject.FlickrHelper;
import com.example.madproject.ParseErrorHandler;
import com.example.madproject.PhotosMapActivity;
import com.example.madproject.R;
import com.example.madproject.SelectionActivity;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.GeoData;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoadPhotostreamTask extends AsyncTask<OAuth, Void, PhotoList> {

	/**
	 * 
	 */
	private ListView listView;
	private Activity activity;
	public SelectionActivity photosmaplistner;
	public SelectionActivity selectionlistner;
	public Context context;
	GridView mgridview;
	public static PhotoList FlickrPhotos;
	List<String> ParsePhotos;
	public static ProgressDialog pd;

	/**
	 * @param context
	 * @param flickrjAndroidSampleActivity
	 */
	public LoadPhotostreamTask(Activity activity) {
		this.activity = activity;
		// this.context = context;
		// this.mgridview=gridview;
		this.photosmaplistner = (SelectionActivity) activity;
		this.selectionlistner = (SelectionActivity) activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected PhotoList doInBackground(OAuth... arg0) {
		OAuthToken token = arg0[0].getToken();
		Flickr f = FlickrHelper.getInstance().getFlickrAuthed(
				token.getOauthToken(), token.getOauthTokenSecret());
		
		Set<String> extras = new HashSet<String>();
		extras.add("url_m"); //$NON-NLS-1$
		extras.add("url_l");
		extras.add("url_o");
		extras.add("url_s");
		extras.add("url_sq");
		extras.add("url_t");
		extras.add("url_sq");
		extras.add("url_sq");//$NON-NLS-1$
		extras.add("views"); //$NON-NLS-1$
		extras.add("geo");
		extras.add("date_taken");
		extras.add("date_upload");
		extras.add("owner_name");

		User user = arg0[0].getUser();
		try {
			return f.getPhotosInterface().getWithGeoData(null, null, null,
					null, 0, "date-posted-asc", extras, 0, 1);
			// return f.getPeopleInterface().getPhotos(user.getId(), extras, 10,
			// 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		 pd = new ProgressDialog(activity);
		pd.setTitle("Loading");
		pd.setMessage("Organizing Your Photos..");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setIndeterminate(true);
		//pd.setCancelable(false);

		pd.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(PhotoList result) {
		if (!result.isEmpty()) {
			// photosmaplistner.getPhotoList(result);

			FlickrPhotos = result;

			queryPhotos();
		}
		
		else {
			
			Toast.makeText(activity, "You dont have any Geo Tagged photos", Toast.LENGTH_LONG).show();
			Fragment fragment = new CitiesFragment();
			if(fragment!=null){
				FragmentManager fragmentManager = activity.getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.container, fragment,"citiesFragment").commit();
			}
			
				pd.cancel();
			
		}
	}

	public void queryPhotos() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("PhotosDetails");
		SharedPreferences sp = activity.getSharedPreferences(
				SelectionActivity.PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		String userName = sp.getString(SelectionActivity.KEY_USER_NAME, "");
		query.whereEqualTo("Owner", userName);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
				
				
				if(e==null&&objects.size()!=0){
					List<String> fp = new ArrayList<String>();
					for (Photo p : FlickrPhotos) {
						fp.add(p.getId());
					}
					List<String> pp = new ArrayList<String>();
					for (ParseObject po : objects) {
						pp.add(po.getString("PhotoID"));
					}

					fp.removeAll(pp);
					PhotoList toParse = new PhotoList();
					for (Photo p : FlickrPhotos) {
						if (fp.contains(p.getId()))
							toParse.add(p);
					}
					new GeoTask(activity).execute(toParse);
				}
				else if(e==null&&objects.size()==0){
				
					new GeoTask(activity).execute(FlickrPhotos);
				}
				else{
					Toast.makeText(activity, e.getMessage().toString(), Toast.LENGTH_LONG).show();
					ParseErrorHandler.handleParseError(e,activity);
					
				}
			}
		});
	}

	public interface passData {
		public void getPhotoList(PhotoList photoList);

		public void getAddressList(HashMap<String, Address> addressList);

	}

	class GetAddress extends
			AsyncTask<PhotoList, Void, HashMap<String, Address>> {

		PhotoList listPhotos;
		List<Address> addressList = new ArrayList<Address>();
		Photo tempPhoto;
		Context mcontext;

		ParseObject addressObject;

		Geocoder geocoder;
		HashMap<String, Address> photoMap = new HashMap<String, Address>();
		List<String> idlist;
		ArrayList<String> gridUrls;

		public GetAddress(Context mcontext) {
			super();
			this.mcontext = mcontext;
		}

		@Override
		protected HashMap<String, Address> doInBackground(PhotoList... params) {
			// TODO Auto-generated method stub
			geocoder = new Geocoder(mcontext);
			listPhotos = params[0];
			for (Photo p : listPhotos) {

				tempPhoto = p;

				try {
					GeoData latlng = tempPhoto.getGeoData();

					addressList = geocoder.getFromLocation(
							latlng.getLatitude(), latlng.getLongitude(), 1);
					Log.d("parsed", "" + ":address" + tempPhoto.getId() + ":"
							+ addressList.size());
					for (Address address : addressList) {

						photoMap.put(tempPhoto.getId(), address);

					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			return photoMap;
		}

		@Override
		protected void onPostExecute(HashMap<String, Address> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {

				selectionlistner.getAddressList(result);
				// tofragmentcommunicator.sendData(result);

			}

			else {
				Log.d("demo", "null results");
			}
		}

	}

	class GeoTask extends AsyncTask<PhotoList, Void, HashMap<String, Address>> {

		Context mcontext;
		PhotoList listPhotos;
		Photo tempPhoto;
		ParseObject addressObject;
		List<Address> addressList = null;
		Geocoder geocoder;
		HashMap<String, Address> photoMap;
		List<String> idlist;
		ArrayList<String> gridUrls;

		public GeoTask(Context mcontext) {
			super();
			this.mcontext = mcontext;
		}

		@Override
		protected HashMap<String, Address> doInBackground(PhotoList... params) {
			// TODO Auto-generated method stub

			geocoder = new Geocoder(mcontext);
			photoMap = new HashMap<String, Address>();

			List<Address> addressList = new ArrayList<Address>();

			List<List<Address>> tempAddress = new ArrayList<List<Address>>();
			ParseGeoPoint photoGeoPoint;

			listPhotos = params[0];
			listPhotos.size();
			idlist = new ArrayList<String>();
			queryPhotos();

			// ///////////////////
			for (Photo p : listPhotos) {

				addressObject = new ParseObject("PhotosDetails");
				gridUrls = new ArrayList<String>();

				tempPhoto = p;

				try {
					GeoData latlng = tempPhoto.getGeoData();
					photoGeoPoint = new ParseGeoPoint(latlng.getLatitude(),
							latlng.getLongitude());
					addressObject.put("GeoData", photoGeoPoint);
					addressObject.put("Accuracy", latlng.getAccuracy());
					addressObject.put("LargeUrl", p.getLargeUrl());
					Log.d("parse", "insideif");
					addressObject.put("Owner", p.getOwner().getUsername());
					//addressObject.put("SquareUrl", tempPhoto.getLargeSquareSize().getSource());

					addressObject.put("PhotoID", tempPhoto.getId());
					gridUrls.add(tempPhoto.getId());
					addressObject
							.put("SmallUrl", tempPhoto.getSmallSquareUrl());
					gridUrls.add(tempPhoto.getSmallSquareUrl());
					addressObject.put("MediumUrl", tempPhoto.getMediumUrl());
					addressObject.put("FlickrUrl", tempPhoto.getUrl());

					addressObject.put("GridUrl", tempPhoto.getLargeSquareUrl());
					addressObject.put("LargeUrl", tempPhoto.getLargeUrl());
					// addressObject.put("DateTaken",
					// p.getDateTaken());
					addressObject.put("Title", tempPhoto.getTitle());
					// addressObject.put("URLs", p.getUrls());
					addressObject.put("Views", tempPhoto.getViews());
					addressList = geocoder.getFromLocation(
							latlng.getLatitude(), latlng.getLongitude(), 1);
					Log.d("parsed", "" + ":address" + tempPhoto.getId() + ":"
							+ addressList.size());
					for (Address address : addressList) {
						for (int j = 0; j < address.getMaxAddressLineIndex(); j++) {

							// addressObject.put("AddressLine"+" "+i,address.getAddressLine(i));

							if (address.getFeatureName() != null) {
								addressObject.put("feature",
										address.getFeatureName());
							}
							if (address.getThoroughfare() != null) {
								addressObject.put("thoroughfare",
										address.getThoroughfare());
							}
							if (address.getAdminArea() != null) {
								addressObject.put("Admin",
										address.getAdminArea());
							}
							if (address.getPostalCode() != null) {
								addressObject.put("PostalCode",
										address.getPostalCode());
							}
							if (address.getCountryCode() != null) {
								addressObject.put("CountryCode",
										address.getCountryCode());
							}
							if (address.getCountryName() != null) {
								addressObject.put("CountryName",
										address.getCountryName());
							}

							if (address.getLocality()!= null) {
								addressObject.put("Locality",
										address.getLocality());
							}
							
//							else if(address.getLocality()==null){
//								addressObject.put("Locality",
//										address.getAdminArea());
//							}
							

						}

						photoMap.put(tempPhoto.getId(), address);
						addressList.add(address);

					}

					// tempAddress.add(addressList);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				addressObject.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {

						// TODO Auto-generated method stub

						if (e != null) {
							Log.d("parse", "" + e.toString());
							ParseErrorHandler.handleParseError(e,activity);
						} else {
							Log.d("parse", "saved successfully");
							
						}

					}
				});
			}

			return photoMap;
		}

		public void queryPhotos() {
			ParseQuery<ParseObject> query = ParseQuery
					.getQuery("PhotosDetails");
			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					// TODO Auto-generated method stub
					if(e!=null){
						ParseErrorHandler.handleParseError(e,activity);
					}
					else{
						for (ParseObject oo : objects) {
							idlist.add(oo.getString("PhotoID"));
						}	
					}
					

				}
			});
		}

		@Override
		protected void onPostExecute(HashMap<String, Address> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null) {
				new GetAddress(mcontext).execute(FlickrPhotos);
				// selectionlistner.getAddressList(result);
				// tofragmentcommunicator.sendData(result);

			}

			else {
				Log.d("demo", "null results");
			}
		}

	}

}
