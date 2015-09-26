/*
Final Project
Group Members: Vamsi Krishna Atukuri Belagala, Colin McKenney, Crystal Nettles
Table 14A
 */

package com.example.madproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.example.madproject.SelectionActivity.ToFragmentCommunicator;
import com.example.madproject.tasks.LoadPhotostreamTask;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.photos.GeoData;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.FragmentManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link CitiesFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link CitiesFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class CitiesFragment extends Fragment implements
		SelectionActivity.ToFragmentCommunicator {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	List<String> citiesList;
	OAuth oauth;
	ListView gridView;
	private Context context;
	public ToFragmentCommunicator tofragmentcommunicator;
	private static final String ARG_SECTION_NUMBER = "section_number";
	PhotoList cPhotoList = null;
	HashMap<String, Address> cAddressList;
	List<String> smallImageUrls;
	ArrayList<City> gridViewList;
	City userCity;
	CityAdapter cityadapter;
	TextView emptyText;

	/**
	 * PhotoL Use this factory method to create a new instance of this fragment
	 * using the provided parameters.
	 *
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment CitiesFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static CitiesFragment newInstance(int sectionNumber) {
		CitiesFragment fragment = new CitiesFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;

	}

	//

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		this.oauth = SelectionActivity.mOAuth;
		Log.d("passing", "inactivitycreated");

	}

	public CitiesFragment() {
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

		View view = inflater
				.inflate(R.layout.fragment_cities, container, false);
		gridView = (ListView) view.findViewById(R.id.listView1);
		emptyText = (TextView) view.findViewById(R.id.empty);
		
		gridView.setEmptyView(emptyText);
		
		Log.d("passing", "increateview");

		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		citiesList = new ArrayList<String>();
	
		gridView.setAdapter(((SelectionActivity) context).cityAdapter);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView cityNameView = (TextView) view.findViewById(R.id.city);
				String cityName = (String) cityNameView.getText();
				Intent intent = new Intent(getActivity(),
						PhotosMapActivity.class);
				intent.putExtra("city_name", cityName);
				startActivity(intent);
			}
		});

		super.onResume();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
			context = getActivity();
			((SelectionActivity) context).tofragmentcommunicator = this;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
		Log.d("passing", "indetach");
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
	public void sendPhotoData(CityAdapter cityAdapter) {
		// TODO Auto-generated method stub
		// this.cAddressList=addressList;

		Log.d("passing", "inadapter");
		this.cityadapter = cityAdapter;
	}

	@Override
	public void sendData(List<Address> addressList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendPhotoList(PhotoList photoList) {
		// TODO Auto-generated method stub
		this.cPhotoList = photoList;

	}

}
