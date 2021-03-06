/*
 * This is a utility class with the purpose
 * of helping me separate this location stuff
 * while allowing easy access to Location services.*/
package com.electricflurry;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class GPSUtilities {
	LocationManager locationManager;
	LocationListener locationListener;
	Location efLocation;// this location is simply a fake location to test checkin with
	Location lastKnownLocation;
	final float MAX_DISTANCE = 1000f;
	
	
	public GPSUtilities(LocationManager manager, LocationListener listener) {
		locationManager = manager;
		locationListener = listener;

		
		//get last known location but only for GPS as this one is the one that takes really long to load
		lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
	}//private constructor, also known as a singleton
	
	
	
	
	public void removeUpdates() {
		
		locationManager.removeUpdates(locationListener);
		
	}//end of removeUpdates
	
	
	public void requestLocationUpdates(String provider, long minTime, float minDistance) {
		
		locationManager.requestLocationUpdates(provider, minTime, minDistance, locationListener);
		
	}//end of requestLocationUpdates
	
	
	/*This method is just going to be here incase anyone needs to access both network and GPS for inprecise stuff*/
	public void requestLocationUpdates(String provider1, long minTime1, float minDistance1, String provider2, long minTime2, float minDistance2) {
		/*this one is like the one before this except you can request both location providers to run for quicker Location */
		locationManager.requestLocationUpdates(provider1, minTime1, minDistance1, locationListener);
		locationManager.requestLocationUpdates(provider2, minTime2, minDistance2, locationListener);
	}
	
	
	public boolean isWithinRange(Location newLocation) {
		/*If newLocation is null I can just use current location
		 * */
		if(newLocation != null) {
			
			if(efLocation.distanceTo(newLocation) < MAX_DISTANCE)
				return true;
				
		} else {
			//if null just use last known location
			if(lastKnownLocation != null) {
				if(efLocation.distanceTo(lastKnownLocation) < MAX_DISTANCE)
					return true;
			}
			
		}
		
		return false;
	}//end of isWithinRange
	
	
	
	
	
	
	public void setShowLocation(double lat, double lng){
		efLocation = new Location(LocationManager.GPS_PROVIDER);
		efLocation.setLatitude(lat);
		efLocation.setLongitude(lng);
	}//end of setShowLocation
	
	
	
	
	
	
	

}//end of class
