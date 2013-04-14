package com.electricflurry;

import com.electricflurry.R;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileFragment extends Fragment {
	EditText edit_name, edit_phone, edit_facebook, edit_twitter, edit_google;
	String name, phone,facebookURL, twitterURL, googleURL;
	TextView disp_name, disp_phone;
	
	//created the myprofile fragment by just copying the mingle fragment -sean

	public static MyProfileFragment newInstance() {

		MyProfileFragment f = new MyProfileFragment();
		
		return f;
	}//end of static constructor
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		final ElectricFlurryDatabase db = new ElectricFlurryDatabase(getActivity());
			
		View view = inflater.inflate(R.layout.myprofile_fragment, container, false);
		
		

		edit_name = (EditText) view.findViewById(R.id.edit_name);
		edit_phone = (EditText) view.findViewById(R.id.edit_phone);
		edit_facebook = (EditText) view.findViewById(R.id.edit_facebook);
		edit_twitter = (EditText) view.findViewById(R.id.edit_twitter);
		edit_google = (EditText) view.findViewById(R.id.edit_google);
		
		Button save = (Button) view.findViewById(R.id.save);
		
		edit_name.setHint("My Name");
		edit_phone.setHint("My Phone Number");
		edit_facebook.setHint("My Facebook URL");
		edit_twitter.setHint("My Twitter URL");
		edit_google.setHint("My Google+ URL");
		
		save.setOnClickListener(new View.OnClickListener() {
			//save button -sean
			@Override
			public void onClick(View v) {
				Profile profile = new Profile();
				name = edit_name.getText().toString();
				phone = edit_phone.getText().toString();
				facebookURL = edit_facebook.getText().toString();
				twitterURL = edit_twitter.getText().toString();
				googleURL = edit_google.getText().toString();
				if (name != null) {
					profile.setName(name);
				}
				if (phone != null) {
					profile.setPhoneNumber(phone);
				}
				if (facebookURL != null) {
					profile.setFacebookURL(facebookURL);
				}
				if (twitterURL != null) {
					profile.setTwitterURL(twitterURL);
				}
				if (googleURL != null) {
					profile.setGoogleURL(googleURL);
				}
				
				Toast.makeText(getActivity(), db.checkUser(), Toast.LENGTH_SHORT).show();
				//db.submitUser(profile);
			}
		});
		return view;
	}//end of onCreateView
	
}
