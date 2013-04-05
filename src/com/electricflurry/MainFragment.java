package com.electricflurry;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment{
	
	String PREF_NAME = "MySocialSettings"; //preferences for oAuth stuff to store
	String TAG = "MainFragment";
	public static final String LE_FRAGMENT = "fragment_holder";
	
	TextView text;
	TextView venuesList;
	TextView checkin, vote, social, mingle;
	
	GPSUtilities gps;
	//LocationManager locationManager;
	//LocationListener locationListener;
	SharedPreferences settings;
	
	String oAuth;
	
	public static MainFragment newInstance() {
		MainFragment f = new MainFragment();
		
		/*
		 * Don't really need to put any arguments right now so won't bother*/
		//Bundle args = new Bundle();
		//args.putString("test", "Text I put when creating myself");
		//f.setArguments(args);
		
		return f;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		/*Seems Fragment has automatic access to some values here like getArguments()*/
		super.onCreate(savedInstanceState);
		
		//locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		settings = getActivity().getSharedPreferences(PREF_NAME, 0);
		oAuth = settings.getString("foursquare_oauth_token", null);
		
		gps = new GPSUtilities((LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE),
				new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				
				if(oAuth != null) {
					final Foursquare foursquare = new Foursquare( oAuth,location.getLatitude()+","+location.getLongitude());
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							foursquare.queryFoursquareData();//this should be done on separate thread!
							
							venuesList.post(new Runnable(){
								@Override
								public void run() {
									venuesList.setVisibility(View.VISIBLE);
									venuesList.setText(foursquare.returnLeString());
									
									if(text.getVisibility() == View.VISIBLE)
										text.setVisibility(View.GONE);
								}
							});
							
						}
					}).start();
					
				}
				//text.setText("Lon="+location.getLongitude()+" Lat="+location.getLatitude());
				
				gps.removeUpdates();
				
			}//end of onLocationChanged

			@Override
			public void onProviderDisabled(String provider) {}

			@Override
			public void onProviderEnabled(String provider) {}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {}
			
		});
		
		
	}//end of onCreate
	
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment, container, false);
		
		/*
		 * Where yo shit will go that will populate 
		 * the Fragments Views dynamically
		 * */
		
		TextView checkin = (TextView)view.findViewById(R.id.check_in);
		TextView vote = (TextView)view.findViewById(R.id.vote);
		TextView social = (TextView)view.findViewById(R.id.social_networking);
		TextView mingle = (TextView)view.findViewById(R.id.mingle);
		TextView profile = (TextView)view.findViewById(R.id.profile);
		TextView myProfile = (TextView)view.findViewById(R.id.my_profile);
		
		venuesList = (TextView)view.findViewById(R.id.venues_list);
		
		text = (TextView)view.findViewById(R.id.name);
		
		
		checkin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).addFragment(R.id.fragment_holder, CheckInFragment.newInstance());
				Toast.makeText(getActivity(), "clicked on checkin", Toast.LENGTH_SHORT).show();
			}
		});
		vote.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).addFragment(R.id.fragment_holder, VoteFragment.newInstance());
				Toast.makeText(getActivity(), "clicked on vote", Toast.LENGTH_SHORT).show();
			}
		});
		social.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).addFragment(R.id.fragment_holder, SocialNetworkFragment.newInstance());
				Toast.makeText(getActivity(), "clicked on social", Toast.LENGTH_SHORT).show();
			}
		});
		mingle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).addFragment(R.id.fragment_holder, MingleFragment.newInstance());
				Toast.makeText(getActivity(), "clicked on mingle", Toast.LENGTH_SHORT).show();
			}
		});
		
		profile.setOnClickListener(new View.OnClickListener() {
			//added interactivity to the profile button -sean
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).addFragment(R.id.fragment_holder, ProfileFragment.newInstance());
				Toast.makeText(getActivity(), "clicked on Profile", Toast.LENGTH_SHORT).show();
			}
		});
		
		myProfile.setOnClickListener(new View.OnClickListener() {
			//added interactivity to the myprofile button -sean
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).addFragment(R.id.fragment_holder, MyProfileFragment.newInstance());
				Toast.makeText(getActivity(), "clicked on My Profile", Toast.LENGTH_SHORT).show();
			}
		});

		
		
		if(oAuth == null) {
			text.setText("Login to Foursquare!");
			
			text.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Here I will see if the fragment stuff is launching correctly
					((MainActivity)getActivity()).addFragment(
							R.id.fragment_holder, FoursquareAuthFragment.newInstance(getActivity().getSharedPreferences(PREF_NAME, 0)));
					
				}
			});
		
		} else {
			gps.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0);
			text.setVisibility(View.GONE);
		}
		
		
		return view;
		
	}//end of onCreateView
	
	public void loginDisappear() {
		
	}//end of loginDisappear
	
	
	
	
	
	public void onResume() {
		super.onResume();
		//This is here just to 
		if(settings.getString("foursquare_oauth_token", null) != null && text.getVisibility() == View.VISIBLE) {
			text.setVisibility(View.GONE);
			oAuth = settings.getString("foursquare_oauth_token", null);
			gps.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0);
		}
	}
	
	public void onPause() {
		super.onPause();
		/*For now will keep this here since I want 
		 * the stop collecting anything here*/
		
		gps.removeUpdates();
		//locationManager.removeUpdates(locationListener);
		
	}//end of onPause
	
	public void onDestroy() {
		super.onDestroy();
		//Log.d(TAG, "onDestory is running!");
	}
	

	
	
	
	
	
	
	
	
	
	
	

}//end of class
