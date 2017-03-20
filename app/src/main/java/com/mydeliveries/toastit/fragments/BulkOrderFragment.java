package com.mydeliveries.toastit.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class BulkOrderFragment extends Fragment {


	EditText order;

	TextView name;

	TextView heading;
	TextView Telephone;
	TextView Email;



	private ProgressDialog pDialog;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_bulk, container, false);

		// Get tracker.
		Tracker t = ((AppController) getActivity().getApplication()).getTracker(
				TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Bulk Order Page");

		// Send a screen view.
		t.send(new HitBuilders.AppViewBuilder().build());

		((MainActivity) getActivity()).loadDrawsHide();
		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


		NavItem nav = new NavItem();
		nav.setPage("Home");

		final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");



		heading = (TextView) rootView.findViewById(R.id.heading);
		order = (EditText) rootView.findViewById(R.id.bo_des);

		name = (TextView) rootView.findViewById(R.id.bo_name);
		Telephone = (TextView) rootView.findViewById(R.id.bo_tel_number);
		Email = (TextView) rootView.findViewById(R.id.bo_email);

		heading.setTypeface(face);


		TextView tx1 = (TextView) rootView.findViewById(R.id.textView1);
		TextView tx2 = (TextView) rootView.findViewById(R.id.textView2);
		TextView tx3 = (TextView) rootView.findViewById(R.id.textView3);
		TextView tx4 = (TextView) rootView.findViewById(R.id.textView4);
		TextView tx5 = (TextView) rootView.findViewById(R.id.textView);




		tx1.setTypeface(face);
		tx2.setTypeface(face);
		tx3.setTypeface(face);
		tx4.setTypeface(face);
		tx5.setTypeface(face);

		Button submit = (Button) rootView.findViewById(R.id.fb_send);
		submit.setTypeface(face);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager inputManager = (InputMethodManager)
						getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 

				inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS); 
				String errMsg = "";
				if (name.getText().toString().equals(""))
				{
					name.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg=errMsg+ "\nPlease enter your Name";
				}else{
					name.setBackgroundResource(R.drawable.rect_text_edit_border);
				}
				if (Telephone.getText().toString().equals(""))
				{
					Telephone.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg=errMsg+ "\nPlease enter your Telephone";
				}else{
					Telephone.setBackgroundResource(R.drawable.rect_text_edit_border);
				}
				if (Email.getText().toString().equals(""))
				{
					Email.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg=errMsg+ "\nPlease enter your Email Address";
				}else{
					Email.setBackgroundResource(R.drawable.rect_text_edit_border);
				}
				if (order.getText().toString().equals(""))
				{
					order.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg=errMsg+ "\nPlease fill in required information";
				}else{
					order.setBackgroundResource(R.drawable.rect_text_edit_border);
				}

				
				if(!errMsg.equalsIgnoreCase("")){
				   Toast toast =Toast.makeText(getActivity(),errMsg+"\n", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{

					new sendbulkorder().execute();
				}	
				
				
				

			}
		});


		return rootView;
	}

	private class sendbulkorder extends AsyncTask<String, String, String> {

		int res =0;


		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Sending...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {

			String jsonString = "";
			String baseURL = "";


			try {

				String nameuser = "";
				String surname = "";
				String number = "";
				String email = "";

				String address = "";

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				String User = "";
				if (pref.getString("User", null) != null && (!pref.getString("User", null).equalsIgnoreCase(""))){
					User = pref.getString("User", null);
				try {
					JSONObject jsonObjorder = new JSONObject(User);

					nameuser = jsonObjorder.getString("name");
					surname = jsonObjorder.getString("surname");
					number = jsonObjorder.getString("number");

					email = jsonObjorder.getString("email");
					address = jsonObjorder.getString("address");


				} catch (JSONException e) {
					e.printStackTrace();
				}
			}


					JSONObject jsonObject = new JSONObject();


				jsonObject.put("Name", name.getText().toString());
				jsonObject.put("Telephone", Telephone.getText().toString());

				jsonObject.put("Email", Email.getText().toString());

					jsonObject.put("Order",  order.getText().toString());

					jsonString = jsonObject.toString();

					System.out.println("Serialized JSON Data ::" + jsonString);

					// JSON content to post
					String contentToPost = jsonString;
					// Create a URLConnection

					baseURL = "http://www.toastit.co.za/api/design/bulkorder";

					URL url = null;
					HttpURLConnection urlConnection = null;

					url = new URL(baseURL);
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setDoOutput(true);
					urlConnection.setRequestMethod("POST");
					urlConnection.setRequestProperty("Authorization", "e031107c0d1ae937e3c12c30982966ff");

					urlConnection.setRequestProperty("Content-Length", "" + contentToPost.length());
					// To post JSON content, set the content type to application/json OR application/jsonrequest
					urlConnection.setRequestProperty("Content-Type", "application/json");
					urlConnection.setRequestProperty("Cache-Control", "no-cache"); // Post your content
					OutputStream stream = urlConnection.getOutputStream();
					stream.write(contentToPost.getBytes());
					stream.close();

					res = urlConnection.getResponseCode();
					System.out.println("***********" + res + "**************");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}


		@Override
		protected void onPostExecute(String file) {
			// closing progress dialog
			if (pDialog != null) {
				pDialog.dismiss();			
			}

			order.setText("");


			if (res ==201) {



				Toast toast = Toast.makeText(getActivity(), "Bulk Order sent successfully", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();


			} else {


				Toast toast = Toast.makeText(getActivity(), "Error Sending , please try again!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();



			}

			super.onPostExecute(file);
		}



	}    
	

}
