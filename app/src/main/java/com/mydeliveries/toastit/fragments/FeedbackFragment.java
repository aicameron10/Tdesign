package com.mydeliveries.toastit.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.app.AppController.TrackerName;
import com.mydeliveries.toastit.model.NavItem;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.toastit.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class FeedbackFragment extends Fragment {

  String version ="";

	TextView name;

	TextView heading;
	TextView Telephone;
	TextView Email; 

	EditText comments;


	private ProgressDialog pDialog;

	
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);


		Tracker t = ((AppController) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Feedback Page");
		t.send(new HitBuilders.ScreenViewBuilder().build());

		((MainActivity) getActivity()).loadDrawsHide();
		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


		NavItem nav = new NavItem();
		nav.setPage("Home");
		


		final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");

		TextView tx1 = (TextView) rootView.findViewById(R.id.textView1);
		TextView tx2 = (TextView) rootView.findViewById(R.id.textView2);
		TextView tx3 = (TextView) rootView.findViewById(R.id.textView3);

		TextView tx4 = (TextView) rootView.findViewById(R.id.textView4);




		tx1.setTypeface(face);
		tx2.setTypeface(face);
		tx3.setTypeface(face);
		tx4.setTypeface(face);




		heading = (TextView) rootView.findViewById(R.id.heading);


		heading.setTypeface(face);


		name = (TextView) rootView.findViewById(R.id.fb_name);
		Telephone = (TextView) rootView.findViewById(R.id.fb_tel_number);
		Email = (TextView) rootView.findViewById(R.id.fb_email);
		comments = (EditText) rootView.findViewById(R.id.fb_comments);

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
				 if (comments.getText().toString().equals(""))
				{
					comments.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg=errMsg+ "\nPlease enter your comment";
				}else{
					comments.setBackgroundResource(R.drawable.rect_text_edit_border);
				}
				 if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches())
				{
					Email.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg=errMsg+ "\nInvalid Email Address";
				}if (!android.util.Patterns.PHONE.matcher(Telephone.getText().toString()).matches() || Telephone.length() <10)
				{
					Telephone.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg=errMsg+ "\nInvalid Telephone Number";
				}
				
				if(!errMsg.equalsIgnoreCase("")){
				   Toast toast =Toast.makeText(getActivity(),errMsg+"\n", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{

					new sendfeedback().execute();
				}	
				
				
				

			}
		});


		return rootView;
	}

	private class sendfeedback extends AsyncTask<String, String, String> {

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

				JSONObject jsonObject = new JSONObject();


				jsonObject.put("Name", name.getText().toString());
				jsonObject.put("Telephone", Telephone.getText().toString());

				jsonObject.put("Email", Email.getText().toString());
				jsonObject.put("Comments", comments.getText().toString());

				jsonString = jsonObject.toString();

				System.out.println("Serialized JSON Data ::" + jsonString);

				// JSON content to post
				String contentToPost = jsonString;
				// Create a URLConnection

				baseURL = "http://www.toastit.co.za/api/design/feedback";

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

			name.setText("");
			Telephone.setText("");
			Email.setText("");
			comments.setText("");

			if (res ==201) {



				Toast toast = Toast.makeText(getActivity(), "Feedback sent successfully", Toast.LENGTH_SHORT);
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
