package com.mydeliveries.toastit.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydeliveries.toastit.activity.MainActivity;
import com.mydeliveries.toastit.adapter.ListAdapterInvite;
import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.model.Invites;
import com.mydeliveries.toastit.model.NavItem;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.toastit.util.FastSearchListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mydeliveries.toastit.R;


public class InviteFragment extends Fragment {



	private ProgressDialog pDialog;
	FastSearchListView listView;
	List<Invites> emails = new ArrayList<Invites>();

	ListAdapterInvite adapter;
	ArrayList names;
	EditText emailfriend;
	FloatingActionMenu menu1;

	private FloatingActionButton fab1;
	private FloatingActionButton fab2;

	final List<String> SelectedBox = new ArrayList<String>();


	private List<FloatingActionMenu> menus = new ArrayList<>();
	private Handler mUiHandler = new Handler();


	// Request code for READ_CONTACTS. It can be any number > 0.
	private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_invite, container, false);

		listView = (FastSearchListView) rootView.findViewById(R.id.listview);



		Tracker t = ((AppController) getActivity().getApplication()).getTracker(AppController.TrackerName.APP_TRACKER);
		t.setScreenName("Invite Page");
		t.send(new HitBuilders.ScreenViewBuilder().build());


		((MainActivity) getActivity()).loadDrawsHide();
		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

		final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");


		final TextView heading = (TextView) rootView
				.findViewById(R.id.heading);

		heading.setTypeface(face);

		NavItem nav = new NavItem();
		nav.setPage("Home");


		new checkNames().execute();


		menu1 = (FloatingActionMenu) rootView.findViewById(R.id.menu_down);

		menu1.setOnMenuButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (menu1.isOpened()) {
					//Toast.makeText(getActivity(), menu1.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
				}

				menu1.toggle(true);
			}
		});

		menus.add(menu1);
		menu1.hideMenuButton(false);


		int delay = 400;
		for (final FloatingActionMenu menu : menus) {
			mUiHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					menu.showMenuButton(true);
				}
			}, delay);
			delay += 150;
		}

		menu1.setClosedOnTouchOutside(true);

		fab1 = (FloatingActionButton)  rootView.findViewById(R.id.fab1);
		fab2 = (FloatingActionButton)  rootView.findViewById(R.id.fab2);

		fab1.setOnClickListener(clickListener);
		fab2.setOnClickListener(clickListener);


		return rootView;
	}




	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String text = "";

			switch (v.getId()) {
				case R.id.fab1:

					for (int i = 0; i < adapter.getCount(); i++)
					{
						Invites invites = (Invites) adapter.getItem(i);
						if (invites.isChecked())
						{

							SelectedBox.add(invites.getEmail());
							//Toast.makeText(getActivity(),
							//	invites.getEmail() + " is Checked!!",
							//Toast.LENGTH_SHORT).show();
						}
					}
					sendselected();
					//((ListAdapterInvite) adapter).sendselected();
					menu1.close(true);

					break;
				case R.id.fab2:
					Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");
					LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View formElementsView = inflater.inflate(R.layout.friends,
							null, false);

					TextView heading = (TextView) formElementsView.findViewById(R.id.heading);
					emailfriend = (EditText) formElementsView.findViewById(R.id.email);

					heading.setTypeface(face);


					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity()).setView(formElementsView);
					LayoutInflater inflater1 = getActivity().getLayoutInflater();
					View view=inflater1.inflate(R.layout.tool_bar_order, null);
					TextView toolbar_title = (TextView) view.findViewById(R.id.toolbartitle);
					toolbar_title.setText("New Friend");
					toolbar_title.setTypeface(face);
					alertDialogBuilder.setCustomTitle(view);


					// set dialog message
					alertDialogBuilder
							.setCancelable(true)
							.setPositiveButton("Send", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

									new sendInvites().execute();
									menu1.close(true);
									dialog.cancel();
								}
							})
							.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();




					break;


			}

			//	Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
		}
	};

	class checkNames extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(getActivity());
			// Showing progress dialog before making http request
			pDialog.setMessage("Loading Friends...");
			pDialog.setCancelable(false);
			pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					((MainActivity) getActivity()).displayView(0);
				}
			});

			pDialog.show();


			super.onPreExecute();

		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			showContacts();
			//Collections.sort(emails);

			ObjectComparator comparator = new ObjectComparator();
			Collections.sort(emails, comparator);
			adapter  = new ListAdapterInvite(getActivity(),emails,InviteFragment.this);



			return null;
		}


		protected void onPostExecute(String file_url) {

			listView.setAdapter(adapter);


			//listener for nothing but it allow OnItemClickListener to work



			pDialog.hide();
			// Code to Add an item with default animation
			//((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

			// Code to remove an item with default animation
			//((MyRecyclerViewAdapter) mAdapter).deleteItem(index);

			super.onPostExecute(file_url);
		}


	}


	/**
	 * Show the contacts in the ListView.
	 */
	private void showContacts() {
		// Check the SDK version and whether the permission is already granted or not.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
			//After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
		} else {
			// Android version is lesser than 6.0 or the permission is already granted.
			getDataSet();

		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
										   int[] grantResults) {
		if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission is granted
				showContacts();
			} else {
				Toast.makeText(getActivity(), "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
			}
		}
	}



	@Override
	public void onResume() {
		super.onResume();

	}



	public ArrayList<Invites> getDataSet(){
		try {
			names = new ArrayList<Invites>();
			ContentResolver cr = getActivity().getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
					Cursor cur1 = cr.query(
							ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
							new String[]{id}, null);
					while (cur1.moveToNext()) {
						for (int index = 0; index < 1; index++) {
							String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
							//Log.e("Name :", name);
							String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
							//Log.e("Email", email);
							Invites obj = new Invites(name, email);

							//to get the contact names

							if (email != null) {

								emails.add(obj);
							}
						}
					}
					cur1.close();
				}
			}
		}catch (Exception e){

		}
		return names;
	}


	public class ObjectComparator implements Comparator<Invites> {

		public int compare(Invites obj1, Invites obj2) {
			return obj1.getName().compareTo(obj2.getName());
		}

	}



	public void sendselected(){

		if(!SelectedBox.isEmpty()){
			new sendInvitegroup().execute();
		}else{
			Toast toast = Toast.makeText(getActivity(), "Please select at least one friend", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

	}


	public class sendInvitegroup extends AsyncTask<String, String, String> {

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

				JSONArray ja = new JSONArray();
				for (String s : SelectedBox) {
					JSONObject jo = new JSONObject();
					jo.put("email", s);
					ja.put(jo);
				}



				JSONObject jsonObject = new JSONObject();
				jsonObject.put("email", ja);

				jsonString = jsonObject.toString();

				// JSON content to post
				String contentToPost = jsonString;
				// Create a URLConnection

				baseURL = "http://www.toastit.co.za/api/design/invite";

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
				//System.out.println("***********" + res + "**************");


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



			if (res ==201) {



				Toast toast = Toast.makeText(getActivity(), "Invites sent successfully", Toast.LENGTH_SHORT);
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


	public class sendInvites extends AsyncTask<String, String, String> {

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

				JSONArray ja = new JSONArray();

				JSONObject jo = new JSONObject();
				jo.put("email", emailfriend.getText().toString());
				ja.put(jo);




				JSONObject jsonObject = new JSONObject();
				jsonObject.put("email", ja);

				jsonString = jsonObject.toString();



				//System.out.println("Serialized JSON Data ::" + jsonString);

				// JSON content to post
				String contentToPost = jsonString;
				// Create a URLConnection

				baseURL = "http://www.toastit.co.za/api/design/invite";

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
				//System.out.println("***********" + res + "**************");







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



			if (res ==201) {



				Toast toast = Toast.makeText(getActivity(), "Invite sent successfully", Toast.LENGTH_SHORT);
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
