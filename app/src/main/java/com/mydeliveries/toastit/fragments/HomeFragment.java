package com.mydeliveries.toastit.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mydeliveries.toastit.activity.MainActivity;
import com.mydeliveries.toastit.adapter.CardAdapter;

import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.app.AppController.TrackerName;
import com.mydeliveries.toastit.model.NavItem;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.toastit.util.AlertDialogManager;
import com.mydeliveries.toastit.util.ConnectionDetector;

import com.mydeliveries.toastit.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemClickListener {

	RecyclerView mRecyclerView;
	RecyclerView.LayoutManager mLayoutManager;
	RecyclerView.Adapter mAdapter;

	protected static final int RESULT_SPEECH = 1;

	private FloatingActionButton fab1;
	private FloatingActionButton fab2;
	private FloatingActionButton fab3;



	private List<FloatingActionMenu> menus = new ArrayList<>();
	private Handler mUiHandler = new Handler();


	protected static final String TAG = null;


	double latitude;
	double longitude;



	// flag for Internet connection status
    Boolean isInternetPresent = false;
	 // Connection detector class
    ConnectionDetector cd;
    
 // Alert Dialog Manager
 	AlertDialogManager alert = new AlertDialogManager();

	SharedPreferences sharedpreferences;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();


	// Init the view variables
	HorizontalScrollView mHorizontalScroll;

	List<String> brandedList = new ArrayList<String>();
	Intent intent;

	View rootView;



	boolean pressloc = false;

	NetworkImageView brand;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {



		rootView = inflater.inflate(R.layout.fragment_home, container, false);



		Tracker t = ((AppController) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Home Page");
		t.send(new HitBuilders.ScreenViewBuilder().build());


		NavItem nav = new NavItem();
		nav.setPage("HomePage");

		Runtime.getRuntime().gc();


		//webviewSpinner.setVisibility(View.GONE);

		
		try{ 
			cd = new ConnectionDetector(getActivity().getApplicationContext());
			
	        // Check if Internet present
	        isInternetPresent = cd.isConnectingToInternet();
	        System.out.println("connection - " + isInternetPresent );
	       
	       if (!isInternetPresent) {
	            // Internet Connection is not present
	            alert.showAlertDialog(getActivity(), "Internet Connection Error",
	                    "No Internet connection found. Please connect and retry.", false);
	            // stop executing code by return
	           // return;
	      
	        }
	        
	        }catch(Exception e){
	        	
	        }


		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		((MainActivity) getActivity()).loadDrawsHide();
		((MainActivity) getActivity()).loadDrawsDisplay();

		SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
		SharedPreferences.Editor editor = pref.edit();






		mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
		mRecyclerView.setHasFixedSize(true);

		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);

		mAdapter = new CardAdapter();
		mRecyclerView.setAdapter(mAdapter);




		final FloatingActionMenu menu1 = (FloatingActionMenu) rootView.findViewById(R.id.menu1);


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
		fab3 = (FloatingActionButton)  rootView.findViewById(R.id.fab3);



		//fab1.setEnabled(false);

		fab1.setOnClickListener(clickListener);
		fab2.setOnClickListener(clickListener);
		fab3.setOnClickListener(clickListener);


		return rootView;
	}





	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String text = "";

			switch (v.getId()) {
				case R.id.fab1:
					((MainActivity) getActivity()).displayView(4);
					break;
				case R.id.fab2:
					((MainActivity) getActivity()).displayView(1);
					break;
				case R.id.fab3:

						((MainActivity) getActivity()).displayView(10);


					break;

			}

			//Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
		}
	};




	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		@SuppressWarnings("unused")
		String str = (String) adapterView.getItemAtPosition(position);

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
