package com.mydeliveries.toastit.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.activity.MainActivity;
import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.app.AppController.TrackerName;
import com.mydeliveries.toastit.model.NavItem;
import com.mydeliveries.toastit.util.FileDownloader;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;


public class DisclaimerFragment extends Fragment {


	private ProgressDialog pDialog;


	TextView heading;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_disclaimer, container, false);


		Tracker t = ((AppController) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Disclaimer Page");
		t.send(new HitBuilders.ScreenViewBuilder().build());

		((MainActivity) getActivity()).loadDrawsHide();
		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

		final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");


		NavItem nav = new NavItem();
		nav.setPage("Home");


		Button btn1 = (Button)rootView.findViewById(R.id.button1);

		btn1.setTypeface(face);


		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			download(v);
			}
		});




		heading = (TextView) rootView.findViewById(R.id.heading);


		heading.setTypeface(face);


		return rootView;
	}

	public void download(View v)
	{
		new DownloadFile().execute("http://www.toastit.co.za/Disclaimer/Designer_Disclaimer.pdf", "Designer_Disclaimer.pdf");
	}

	public void view(View v)
	{
		File pdfFile = new File(Environment.getExternalStorageDirectory() + "/toast.it/" + "Designer_Disclaimer.pdf");  // -> filename = maven.pdf
		Uri path = Uri.fromFile(pdfFile);
		Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
		pdfIntent.setDataAndType(path, "application/pdf");
		pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		try{
			startActivity(pdfIntent);
		}catch(ActivityNotFoundException e){
			Toast.makeText(getActivity(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
		}
	}

	private class DownloadFile extends AsyncTask<String, String, String>{



		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... strings) {
			String fileUrl = strings[0];
			String fileName = strings[1];
			String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
			File folder = new File(extStorageDirectory, "toast.it");
			folder.mkdir();

			File pdfFile = new File(folder, fileName);

			try{
				pdfFile.createNewFile();

			}catch (IOException e){
				e.printStackTrace();
			}
			FileDownloader.downloadFile(fileUrl, pdfFile);
			return null;
		}
		@Override
		protected void onPostExecute(String file) {

			if (pDialog != null) {
				pDialog.dismiss();
			}

			View v = new View(getActivity());
			// closing progress dialog
			view(v);

			super.onPostExecute(file);
		}

	}

}
