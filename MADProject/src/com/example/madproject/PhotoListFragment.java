package com.example.madproject;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.madproject.images.PhotoAdapter;
import com.example.madproject.tasks.LoadPhotostreamTask;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link PhotoListFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link PhotoListFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class PhotoListFragment extends Fragment  {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final Logger logger = LoggerFactory.getLogger(SelectionActivity.class);
	GridView gridView;
	OAuth oauth;
	private Context context;
	ArrayList<FlickrPhoto> cityPhotos;
	FlickrPhoto fp;
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	SharedPreferences sp;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment PhotoListFragment.
	 */
	
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static PhotoListFragment newInstance(int sectionNumber) {
		PhotoListFragment fragment = new PhotoListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	

	public PhotoListFragment() {
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
		
		View view = inflater.inflate(R.layout.fragment_photo_list, container, false);
		gridView = (GridView) view.findViewById(R.id.gridView1);
	
		//OAuth oauth=getOAuthToken();

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		this.oauth=MainActivity.mOAuth;
//		new LoadPhotostreamTask(getActivity(), listview).execute(oauth);
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
			context=getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
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
	public void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		
//		if(!(((PhotosMapActivity)context).cityPhotos).isEmpty()){
//			
//			ArrayList<FlickrPhoto> photos = ((PhotosMapActivity)context).cityPhotos;
//			Log.d("new",""+photos.size());
//			  photoAdapter = new PhotoAdapter(getActivity(), photos);
//		}
		
		sp=getActivity().getSharedPreferences(SelectionActivity.PREFS_NAME, getActivity().MODE_PRIVATE);
		String userName = sp.getString(SelectionActivity.KEY_USER_NAME, "");
		Log.d("name",userName);
		cityPhotos= new ArrayList<FlickrPhoto>();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("PhotosDetails");
		query.whereEqualTo("Locality", ((PhotosMapActivity)context).cityName);
		query.whereEqualTo("Owner", userName);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
				if(e==null){
					
					for(ParseObject o:objects){
						fp=new FlickrPhoto();
						fp.setLargeImageUrl(o.getString("LargeUrl"));
						fp.setSmallUrl(o.getString("SmallUrl"));
						fp.setTitle(o.getString("Title"));
						fp.setViews(Integer.toString(o.getInt("Views")));
						fp.setFlickrurl(o.getString("FlickrUrl"));
						fp.setSquareUrl(o.getString("MediumUrl"));
						
						cityPhotos.add(fp);
						
					}
					
					if(!cityPhotos.isEmpty()){
						Log.d("new",""+cityPhotos.size());
						PhotoAdapter photoAdapter=new PhotoAdapter(getActivity(), cityPhotos) ;
						gridView.setAdapter(photoAdapter);
						
						gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								Intent fullscreenIntent = new Intent(getActivity(),FullScreenViewActivity.class);
								fullscreenIntent.putExtra("screen", "list");
								fullscreenIntent.putExtra("PhotosList", cityPhotos);
								fullscreenIntent.putExtra("position", position);
								startActivity(fullscreenIntent);
							}
						});
					}
				}
				else{
					Log.d("new",e.toString());
				}
			}
		});
		
		
//		Log.d("new","onResumephoto");
//		LazyAdapter lazyAdapter=((PhotosMapActivity)context).lazyAdapter ;
//		gridView.setAdapter(lazyAdapter);
		
		//gridView.setAdapter(sa.lazyAdapter);
		
		
	}

	

	
	
	
}
