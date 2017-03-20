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


public class StepsFragment extends Fragment {



	TextView heading;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_steps, container, false);


		Tracker t = ((AppController) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Design Steps Page");
		t.send(new HitBuilders.ScreenViewBuilder().build());

		((MainActivity) getActivity()).loadDrawsHide();
		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


		final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");


		NavItem nav = new NavItem();
		nav.setPage("Home");


		Runtime.getRuntime().gc();

		heading = (TextView) rootView.findViewById(R.id.heading);


		heading.setTypeface(face);



		return rootView;
	}


	@Override
	public void onDestroy() {
		//android.os.Process.killProcess(android.os.Process.myPid());

		super.onDestroy();
		Runtime.getRuntime().gc();

	}

	@Override
	public void onStop() {
		super.onStop();

		Runtime.getRuntime().gc();
	}

}
