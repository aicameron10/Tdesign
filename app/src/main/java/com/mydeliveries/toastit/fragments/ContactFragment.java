package com.mydeliveries.toastit.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.activity.MainActivity;
import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.app.AppController.TrackerName;
import com.mydeliveries.toastit.model.NavItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ContactFragment extends Fragment {

  String version ="";

	TextView name;

	TextView heading;
	TextView Telephone;
	TextView Email; 

	EditText comments;


	private ProgressDialog pDialog;

	// Request code for READ_CONTACTS. It can be any number > 0.
	private static final int PERMISSIONS_CALL_PHONE = 100;
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_contact, container, false);


		Tracker t = ((AppController) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Contact Us Page");
		t.send(new HitBuilders.ScreenViewBuilder().build());

		((MainActivity) getActivity()).loadDrawsHide();
		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


		NavItem nav = new NavItem();
		nav.setPage("Home");
		


		final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");



		heading = (TextView) rootView.findViewById(R.id.heading);

		TextView cus = (TextView) rootView.findViewById(R.id.txtcustomer);
		TextView hours  = (TextView) rootView.findViewById(R.id.txtopen);

		TextView txt  = (TextView) rootView.findViewById(R.id.textView1);
		TextView txt1  = (TextView) rootView.findViewById(R.id.textView2);

		TextView txtcall = (TextView) rootView.findViewById(R.id.txtcall);
		TextView txtemail  = (TextView) rootView.findViewById(R.id.txtemail);
		TextView txtdirections  = (TextView) rootView.findViewById(R.id.txtdirections);

		TextView txtweb  = (TextView) rootView.findViewById(R.id.txtweb);

		heading.setTypeface(face);
		cus.setTypeface(face);
		hours.setTypeface(face);
		txt.setTypeface(face);
		txt1.setTypeface(face);

		txtcall.setTypeface(face);
		txtemail.setTypeface(face);
		txtdirections.setTypeface(face);
		txtweb.setTypeface(face);


		String msg= "<strong>Visit our Store at:</strong><br />27Boxes Mall,<br/>75 4th Avenue, <br />Melville";


		cus.setText(Html.fromHtml(msg));


		String msg1 = "Monday - CLOSED <br />Tuesday - Friday: 10.00 - 17.00<br/>Saturday: 09.00 - 17.00<br/>Sunday: 10.00 - 16.00";


		hours.setText(Html.fromHtml(msg1));


		ImageView directions = (ImageView) rootView.findViewById(R.id.directions);
		ImageView call = (ImageView) rootView.findViewById(R.id.call);
		ImageView email = (ImageView) rootView.findViewById(R.id.email);
		ImageView web = (ImageView) rootView.findViewById(R.id.web);

		directions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if(isGoogleMapsInstalled())
				{
					try{


						//-26.1757791,28.0068387

						Intent intent = new Intent(Intent.ACTION_VIEW,
								Uri.parse("http://maps.google.com/maps?daddr=" + -26.1757791 + "," + 28.0068387));
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
						getActivity().startActivity( intent );
					}catch(Exception e){
						Intent intent = new Intent(Intent.ACTION_VIEW,
								Uri.parse("geo:0,0?q=" + -26.1757791 + "," + 28.0068387 + ""));
						getActivity().startActivity(intent);
					}
				}else{

					Intent intent = new Intent(Intent.ACTION_VIEW,
							Uri.parse("geo:0,0?q=" + -26.1757791 + "," + 28.0068387 + ""));
					getActivity().startActivity(intent);
				}

			}
		});


		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String num = "0832008869";
				if(num == null || num.equalsIgnoreCase("null"))
				{
					Toast.makeText(getActivity(), "Number not available", Toast.LENGTH_SHORT).show();
				}else
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
						requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_CALL_PHONE);

					} else {
						// Android version is lesser than 6.0 or the permission is already granted.
						try {
							Intent myIntent = new Intent(Intent.ACTION_CALL);
							String phNum = "tel:" + num;
							myIntent.setData(Uri.parse(phNum));
							getActivity().startActivity(myIntent);
						}catch (SecurityException e){
							e.printStackTrace();
						}

					}

				}
			}

		});

		email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				String address = "toastitmobile@gmail.com";
				String subject = "Toast.it Contact Us";
				String body = "";
				String chooserTitle = "Contact Us";

				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + address));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
				emailIntent.putExtra(Intent.EXTRA_TEXT, body);
				emailIntent.putExtra(Intent.EXTRA_TITLE, chooserTitle);


				getActivity().startActivity(Intent.createChooser(emailIntent, "Toast.it"));

			}

		});

		web.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				String url = "http://toastit.co.za/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);

			}

		});



		return rootView;
	}




	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
										   int[] grantResults) {
		if (requestCode == PERMISSIONS_CALL_PHONE) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


			} else {
				Toast.makeText(getActivity(), "Until you grant the permission, we cannot make the call", Toast.LENGTH_SHORT).show();
			}
		}
	}


	public boolean isGoogleMapsInstalled()
	{
		try
		{
			ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
			return true;
		}
		catch(NameNotFoundException e)
		{
			return false;
		}
	}
	


}
