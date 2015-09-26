package com.example.madproject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;
import android.widget.ZoomControls;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link MadMapFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link MadMapFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class MadMapFragment extends Fragment implements ClusterManager.OnClusterClickListener<Person>, ClusterManager.OnClusterInfoWindowClickListener<Person>, ClusterManager.OnClusterItemClickListener<Person>, ClusterManager.OnClusterItemInfoWindowClickListener<Person>  {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private GoogleMap mMap;
	private ClusterManager<Person> mClusterManager;
	private Random mRandom = new Random(1984);
	private  ImageView mClusterImageView;
	public ArrayList<Person> personsList;
	private GoogleMap googleMap;

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	public Person persontemp; 
	ArrayList<FlickrPhoto> flickrPhotosList;
	private OnFragmentInteractionListener mListener;
	public PhotosMapActivity pmActivity;
	ArrayList<Bitmap> tempBitmaps;
	MapView mMapView;
	View view;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment MadMapFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MadMapFragment newInstance(String param1, String param2) {
		MadMapFragment fragment = new MadMapFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public MadMapFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		 view= inflater.inflate(R.layout.fragment_custom_map, container, false);
		//setUpMapIfNeeded();
		
		   mMapView = (MapView) view.findViewById(R.id.mapView);
		    mMapView.onCreate(savedInstanceState);

		  //  mMapView.onResume();
		    
		   
		    try {
		        MapsInitializer.initialize(getActivity().getApplicationContext());
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    
		    mMapView.getMapAsync(new OnMapReadyCallback() {
				
				@Override
				public void onMapReady(GoogleMap googleMap) {
					// TODO Auto-generated method stub
					mMap = googleMap;
					//startDemo();
				}
			});
		   // mMap = mMapView.getMap();
		    
		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
			pmActivity = (PhotosMapActivity) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
		
		setUpMapIfNeeded();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	@Override
	public void onPause() {
	    super.onPause();
	    mMapView.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    mMapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
	    super.onLowMemory();
	    mMapView.onLowMemory();
	}
	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

	@Override
	public void onClusterItemInfoWindowClick(Person item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onClusterItemClick(Person item) {
		// TODO Auto-generated method stub
		Intent fullscreenIntent = new Intent(getActivity(),FullScreenViewActivity.class);
	      fullscreenIntent.putExtra("photosList", flickrPhotosList);
		fullscreenIntent.putExtra("position", item.getPhotoNum());
		fullscreenIntent.putExtra("screen", "map");
			startActivity(fullscreenIntent);
		return true;
		
	}

	@Override
	public void onClusterInfoWindowClick(Cluster<Person> cluster) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public boolean onClusterClick(Cluster<Person> cluster) {
		// TODO Auto-generated method stub
		
		Log.d("cluster",""+cluster.getSize());
		String firstName = cluster.getItems().iterator().next().name;
		Toast.makeText(getActivity(), " opening "+cluster.getSize() +" photos" , Toast.LENGTH_SHORT).show();
		ArrayList<FlickrPhoto> mapFlickrphotosList = new ArrayList<FlickrPhoto>();
		for(Person c:cluster.getItems()){
			mapFlickrphotosList.add(flickrPhotosList.get(c.getPhotoNum()));
		}
		
        Intent fullscreenIntent = new Intent(getActivity(),FullScreenViewActivity.class);
        fullscreenIntent.putExtra("photosList", mapFlickrphotosList);
	  	fullscreenIntent.putExtra("position",0);
	  	fullscreenIntent.putExtra("screen", "map");
  		startActivity(fullscreenIntent);
    	
         
	
		return true;
	}
	protected GoogleMap getMap() {
		setUpMapIfNeeded();
		return mMap;
	}
	private void setUpMapIfNeeded() {
		if (mMap != null) {
			return;
		}
		//mMap= ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		 mMapView.getMapAsync(new OnMapReadyCallback() {
				
				@Override
				public void onMapReady(GoogleMap googleMap) {
					// TODO Auto-generated method stub
					mMap = googleMap;
					if (mMap != null) {

						
						
						startDemo();
					}
				}
			});
	   // mMap = mMapView.getMap();
		
	}
	
	/**
	 * Draws profile photos inside markers (using IconGenerator).
	 * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
	 */
	private class PersonRenderer extends DefaultClusterRenderer<Person> {
		private final IconGenerator mIconGenerator = new IconGenerator(getActivity().getApplicationContext());
		private final IconGenerator mClusterIconGenerator = new IconGenerator(getActivity().getApplicationContext());
		private final ImageView mImageView;
		private final ImageView mClusterImageView;
		private final int mDimension;

		public PersonRenderer() {
			
			super(getActivity().getApplicationContext(), getMap(), mClusterManager);

			View multiProfile = getActivity().getLayoutInflater().inflate(R.layout.multi_profile, null);
			mClusterIconGenerator.setContentView(multiProfile);
			mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

			mImageView = new ImageView(getActivity().getApplicationContext());
			mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
			mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
			int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
			mImageView.setPadding(padding, padding, padding, padding);
			mIconGenerator.setContentView(mImageView);
		}

		        @Override
		        protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
		            // Draw a single person.
		            // Set the info window to show their name.
		        	
		            mImageView.setImageBitmap(person.getImageBitmap());
		            Bitmap icon = mIconGenerator.makeIcon();
		            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
		        }

		@Override
				public void onClustersChanged(
						Set<? extends Cluster<Person>> clusters) {
					// TODO Auto-generated method stub
					super.onClustersChanged(clusters);
					
					Log.d("cluster","clusterschanged");
				}

				@Override
				protected void onClusterRendered(Cluster<Person> cluster,
						Marker marker) {
					// TODO Auto-generated method stub
					super.onClusterRendered(cluster, marker);
					Log.d("cluster","clusterren");
				}

				@Override
				protected void onClusterItemRendered(Person clusterItem,
						Marker marker) {
					// TODO Auto-generated method stub
					super.onClusterItemRendered(clusterItem, marker);
					Log.d("cluster","clusteritemrendered");
				}

		@Override
		protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
			// Draw multiple people.
			// Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
			

			List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
			int width = mDimension;
			int height = mDimension;
			

			            for (Person p : cluster.getItems()) {
			                // Draw 4 at most.
			                if (profilePhotos.size() == 4) break;
			                Drawable drawable = new BitmapDrawable(getResources(), p.getImageBitmap());
			                drawable.setBounds(0, 0, width, height);
			                profilePhotos.add(drawable);
			            }
			MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
			multiDrawable.setBounds(0, 0, width, height);

			mClusterImageView.setImageDrawable(multiDrawable);
			Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
			markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
		}

		@Override
		protected boolean shouldRenderAsCluster(Cluster cluster) {
			// Always render clusters.
			return cluster.getSize() > 1;
		}
	}
	
	protected void startDemo() {
		flickrPhotosList = new ArrayList<FlickrPhoto>();
		flickrPhotosList = pmActivity.cityPhotos;
//		tempBitmaps=(ArrayList<Bitmap>) getIntent().getExtras().getSerializable("bitmaps");
		//flickrPhotosList=(ArrayList<FlickrPhoto>) getIntent().getExtras().getSerializable("peoplePhotos");
		persontemp = new Person();
		
		new GetBitmaps(getActivity()).execute(flickrPhotosList);
	
		LatLng cityLatLng;
		float zoomlevel;
		if(pmActivity.latlng!=null){
			cityLatLng = pmActivity.latlng;
			zoomlevel = 9.5f;
			Log.d("coord","active:"+cityLatLng.toString());
		}
		else{
			cityLatLng = new LatLng(pmActivity.cityGeoPoint.getLatitude(), pmActivity.cityGeoPoint.getLongitude());
			
			zoomlevel = 9.5f;
			Log.d("coord","Parse:"+cityLatLng.toString());
		}
		getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(cityLatLng, zoomlevel));

		mClusterManager = new ClusterManager<Person>(getActivity(), getMap());
		mClusterManager.setRenderer(new PersonRenderer());
		getMap().setOnCameraChangeListener(mClusterManager);
		getMap().setOnMarkerClickListener(mClusterManager);
		getMap().setOnInfoWindowClickListener(mClusterManager);
		mClusterManager.setOnClusterClickListener(this);
		mClusterManager.setOnClusterInfoWindowClickListener(this);
		mClusterManager.setOnClusterItemClickListener(this);
		mClusterManager.setOnClusterItemInfoWindowClickListener(this);
		 mClusterManager.cluster();
	}
	
	class GetBitmaps extends AsyncTask<ArrayList<FlickrPhoto>,Void,ArrayList<Bitmap>>{
		ArrayList<FlickrPhoto> peoplePhotos;
		ArrayList<Bitmap> totalBitmaps;
		Context mcontext;
		public GetBitmaps(Context context){
			this.mcontext=context;
		}
		@Override
		protected ArrayList<Bitmap> doInBackground(
				ArrayList<FlickrPhoto>... params) {
			// TODO Auto-generated method stub
			peoplePhotos = params[0];
			totalBitmaps = new ArrayList<Bitmap>();
			
			
			for(FlickrPhoto p:peoplePhotos){
				
				try {
					Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(p.getSmallUrl()).getContent());
					totalBitmaps.add(bitmap);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return totalBitmaps;
		}

		@Override
		protected void onPostExecute(ArrayList<Bitmap> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("new",""+result.size());
			 tempBitmaps=result;
			 	SharedPreferences sp=getActivity().getSharedPreferences(SelectionActivity.PREFS_NAME, Context.MODE_PRIVATE);
				String userName = sp.getString(SelectionActivity.KEY_USER_NAME, "");
				Log.d("name",userName);

				personsList = new ArrayList<Person>();
				
				

				ParseQuery<ParseObject> query = ParseQuery.getQuery("PhotosDetails");
				query.whereEqualTo("Locality", pmActivity.cityName);
				query.whereEqualTo("Owner", userName);
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> objects, ParseException e) {
						// TODO Auto-generated method stub
						if(!objects.isEmpty()&&e==null){
							int i=0;
							for(int j=0;j<objects.size();j++){

								ParseObject p=objects.get(j);
								ParseGeoPoint geopoint = p.getParseGeoPoint("GeoData");
								persontemp=new Person(new LatLng(geopoint.getLatitude(),geopoint.getLongitude()));
								persontemp.setName(p.getString("Title"));
								persontemp.setPicUrl(p.getString("SmallUrl"));
								
								persontemp.setFlickrUrl(p.getString("FlickrUrl"));
								persontemp.setPhotoNum(i);
								persontemp.setImageBitmap(tempBitmaps.get(j));
								mClusterManager.addItem(persontemp);


								i++;

							}
							personsList.add(persontemp);
							
						}
					}
				});
			 
			
		}
		
	}
	
}
