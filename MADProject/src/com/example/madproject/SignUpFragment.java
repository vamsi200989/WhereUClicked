package com.example.madproject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.flickrjandroid.oauth.OAuth;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link SignUpFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link SignUpFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class SignUpFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	TextView email, password, confirmPassword;
	Button signupBtn,signinBtn;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment SignUpFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SignUpFragment newInstance(String param1, String param2) {
		SignUpFragment fragment = new SignUpFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public SignUpFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		
		((SelectionActivity)getActivity()).getActionBar().setTitle("Sign Up");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_fragment_sign_out,
				container, false);
		((SelectionActivity) getActivity()).mDrawerLayout
				.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		if (!((SelectionActivity) getActivity()).isConnected()) {
			Toast.makeText(
					getActivity(),
					"You are not connected to network! Please connect to network",
					Toast.LENGTH_LONG).show();
		}
		
		signupBtn = (Button) view.findViewById(R.id.signup);
		signinBtn = (Button) view.findViewById(R.id.signin);
		
		signinBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment loginFragment  = new LoginFragment();
				FragmentManager fm= getFragmentManager();
				fm.beginTransaction().replace(R.id.container, loginFragment,"loginFragment").commit();
				
			}
		});
		
		email = (TextView) view.findViewById(R.id.userName);
		password = (TextView) view.findViewById(R.id.password);
		confirmPassword = (TextView) view.findViewById(R.id.confirmpassword);
		signupBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String emailAddress = email.getText().toString();
				String userpassword = password.getText().toString();
				String confirmpassword = confirmPassword.getText().toString();
				if (emailAddress.isEmpty() || userpassword.isEmpty()) {
					Toast.makeText(getActivity(),
							"Please enter the credentials", Toast.LENGTH_LONG)
							.show();
				}


				if (!confirmpassword.equals(userpassword)) {
					Toast.makeText(getActivity(),
							"Password and Confirm Password should be equal",
							Toast.LENGTH_LONG).show();
				}
				if (confirmpassword.equals(userpassword)&&!emailAddress.isEmpty()) {
					boolean valid = isEmailValid(emailAddress);

					if (valid) {
						ParseUser user = new ParseUser();
						user.setUsername(email.getText().toString());
						user.setEmail(email.getText().toString());
						user.setPassword(password.getText().toString());

						user.signUpInBackground(new SignUpCallback() {

							@Override
							public void done(ParseException e) {
								// TODO Auto-generated method stub
								
								if(e!=null){
									Toast.makeText(getActivity(),
											e.getMessage(),
											Toast.LENGTH_LONG).show();
									ParseErrorHandler.handleParseError(e,getActivity());
								}
								else if (e == null) {
									Toast.makeText(getActivity(),
											"Registered successfully",
											Toast.LENGTH_LONG).show();
									((SelectionActivity) getActivity())
									.getActionBar()
									.setHomeButtonEnabled(true);
									((SelectionActivity) getActivity()).mDrawerLayout
											.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
									String[] currentUserEmail = ParseUser
											.getCurrentUser().getEmail()
											.split("@");
									String tempcurrentID = currentUserEmail[0];

									OAuth oauth = ((SelectionActivity) getActivity())
											.getOAuthToken();

									AuthorizeFragment authorizeFragment = new AuthorizeFragment();
									FragmentManager fragmentManager = getFragmentManager();
									fragmentManager
											.beginTransaction()
											.add(R.id.container,
													authorizeFragment,
													"authorizeFragment")
											.commit();

								}

								
							}
						});

					} else {
						Toast.makeText(getActivity(),
								"Please enter valid email", Toast.LENGTH_LONG)
								.show();
					}
				}

			}
		});
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

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
}
