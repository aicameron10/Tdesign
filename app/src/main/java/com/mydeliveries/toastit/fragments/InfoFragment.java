package com.mydeliveries.toastit.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.activity.MainActivity;
import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.app.AppController.TrackerName;
import com.mydeliveries.toastit.model.NavItem;


public class InfoFragment extends Fragment {


	TextView one;
	TextView two;
	TextView three;

	TextView heading;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_quality, container, false);


		Tracker t = ((AppController) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Info Page");
		t.send(new HitBuilders.ScreenViewBuilder().build());

		((MainActivity) getActivity()).loadDrawsHide();
		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

		final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");


		NavItem nav = new NavItem();
		nav.setPage("Home");



		heading = (TextView) rootView.findViewById(R.id.heading);
		one = (TextView) rootView.findViewById(R.id.textView1);
		two = (TextView) rootView.findViewById(R.id.textView2);
		three = (TextView) rootView.findViewById(R.id.textView3);

		heading.setTypeface(face);
		one.setTypeface(face);
		two.setTypeface(face);
		three.setTypeface(face);

		return rootView;
	}



}
