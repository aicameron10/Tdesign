package com.mydeliveries.toastit.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class StyleFragment extends Fragment {


	TextView heading;
	TextView heading1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_style, container, false);


		Tracker t = ((AppController) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Choose Style Page");
		t.send(new HitBuilders.ScreenViewBuilder().build());

		((MainActivity) getActivity()).loadDrawsHide();
		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

		final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");


		NavItem nav = new NavItem();
		nav.setPage("Home");

		Runtime.getRuntime().gc();


		heading = (TextView) rootView.findViewById(R.id.heading);
		heading1 = (TextView) rootView.findViewById(R.id.heading1);


		ImageView im1 = (ImageView) rootView.findViewById(R.id.adunisexneck);
		ImageView im2 = (ImageView) rootView.findViewById(R.id.adunisexvneck);
		ImageView im3 = (ImageView) rootView.findViewById(R.id.kidsunisexneck);
		ImageView im4 = (ImageView) rootView.findViewById(R.id.ladiesvneck);
		ImageView im5 = (ImageView) rootView.findViewById(R.id.ladiesvest);
		ImageView im6 = (ImageView) rootView.findViewById(R.id.mensvest);
		ImageView im7 = (ImageView) rootView.findViewById(R.id.ladyround);
		ImageView im8 = (ImageView) rootView.findViewById(R.id.ladyrazor);
		ImageView im9 = (ImageView) rootView.findViewById(R.id.hoodie);
		ImageView im10 = (ImageView) rootView.findViewById(R.id.hoodieZip);
		ImageView im11 = (ImageView) rootView.findViewById(R.id.mug);
		ImageView im12 = (ImageView) rootView.findViewById(R.id.babygrow);
		ImageView im13 = (ImageView) rootView.findViewById(R.id.sleevelessCropTop);
		ImageView im14 = (ImageView) rootView.findViewById(R.id.leggings);
		ImageView im15 = (ImageView) rootView.findViewById(R.id.longSleeve);





		TextView txt1 = (TextView) rootView.findViewById(R.id.adtuniround);
		TextView txt2 = (TextView) rootView.findViewById(R.id.adtuniv);
		TextView txt3 = (TextView) rootView.findViewById(R.id.kidsuniround);
		TextView txt4 = (TextView) rootView.findViewById(R.id.ladvneck);
		TextView txt5 = (TextView) rootView.findViewById(R.id.ladvest);
		TextView txt6 = (TextView) rootView.findViewById(R.id.menvest);
		TextView txt7 = (TextView) rootView.findViewById(R.id.ladyroundtxt);
		TextView txt8 = (TextView) rootView.findViewById(R.id.ladyrazortxt);
		TextView txt9 = (TextView) rootView.findViewById(R.id.hood);
		TextView txt10 = (TextView) rootView.findViewById(R.id.hoodieZiptxt);
		TextView txt11 = (TextView) rootView.findViewById(R.id.mugtxt);
		TextView txt12 = (TextView) rootView.findViewById(R.id.babytxt);
		TextView txt13 = (TextView) rootView.findViewById(R.id.sleevelessCropToptxt);
		TextView txt14 = (TextView) rootView.findViewById(R.id.leggingstxt);
		TextView txt15 = (TextView) rootView.findViewById(R.id.longSleevetxt);


		heading.setTypeface(face);
		heading1.setTypeface(face);

		txt1.setTypeface(face);
		txt2.setTypeface(face);
		txt3.setTypeface(face);
		txt4.setTypeface(face);
		txt5.setTypeface(face);
		txt6.setTypeface(face);
		txt7.setTypeface(face);
		txt8.setTypeface(face);
		txt9.setTypeface(face);
		txt10.setTypeface(face);
		txt11.setTypeface(face);
		txt12.setTypeface(face);
		txt13.setTypeface(face);
		txt14.setTypeface(face);
		txt15.setTypeface(face);

		SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
		SharedPreferences.Editor editor = pref.edit();



		txt1.setText("Adult Unisex Round Neck R" + pref.getString("TPrice_uni_round_neck", ""));
		txt2.setText("Adult Unisex V-Neck R" + pref.getString("TPrice_uni_v_veck", ""));
		txt3.setText("Kids Unisex Round Neck R" + pref.getString("TPrice_uni_kids", ""));
		txt4.setText("Ladies V-Neck R" + pref.getString("TPrice_ladies_v", ""));
		txt5.setText("Ladies Vest R"+ pref.getString("TPrice_ladies_vest", ""));
		txt6.setText("Men's Vest R"+ pref.getString("TPrice_mens_vest", ""));
		txt7.setText("Ladies Round Neck R"+ pref.getString("TPrice_ladies_round", ""));
		txt8.setText("Ladies Racerback Vest R"+ pref.getString("TPrice_ladies_razor", ""));
		txt9.setText("Pull-Over Hoodie R"+ pref.getString("TPrice_hoodie_pull", ""));
		txt10.setText("Zip-Up Hoodie R"+ pref.getString("TPrice_hoodie_zip", ""));
		txt11.setText("Mug R"+ pref.getString("TPrice_mug", ""));
		txt12.setText("Short Sleeve Baby Grow R"+ pref.getString("TPrice_baby_grow", ""));
		txt13.setText("Ladies Crop Top R"+ pref.getString("TPrice_crop_top", ""));
		txt14.setText("Leggings R"+ pref.getString("TPrice_leggings", ""));
		txt15.setText("Long Sleeve R"+ pref.getString("TPrice_long_sleeve", ""));




		im1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Selection", "1");
				editor.putString("Tprice", pref.getString("TPrice_uni_round_neck", ""));
				editor.putString("Tmake", "Adult Unisex Round Neck");
				editor.commit(); // commit changes

				submitChoose();
			}
		});

		im2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Selection","2");
				editor.putString("Tprice", pref.getString("TPrice_uni_v_veck", ""));
				editor.putString("Tmake", "Adult Unisex V-Neck");
				editor.commit(); // commit changes

				submitChoose();
			}
		});

		im3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Selection","3");
				editor.putString("Tprice", pref.getString("TPrice_uni_kids", ""));
				editor.putString("Tmake", "Kids Unisex Round Neck");
				editor.commit(); // commit changes

				submitChoose();
			}
		});

		im4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Selection","4");
				editor.putString("Tprice", pref.getString("TPrice_ladies_v", ""));
				editor.putString("Tmake", "Ladies V-Neck");
				editor.commit(); // commit changes

				submitChoose();
			}
		});
		im5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Selection","5");
				editor.putString("Tprice", pref.getString("TPrice_ladies_vest", ""));
				editor.putString("Tmake", "Ladies Vest");
				editor.commit(); // commit changes

				submitChoose();
			}
		});

		im6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Selection","6");
				editor.putString("Tprice", pref.getString("TPrice_mens_vest", ""));
				editor.putString("Tmake", "Mens Vest");
				editor.commit(); // commit changes

				submitChoose();
			}
		});

		im7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Selection","7");
				editor.putString("Tprice", pref.getString("TPrice_ladies_round", ""));
				editor.putString("Tmake", "Ladies Round Neck");
				editor.commit(); // commit changes

				submitChoose();
			}
		});

		im8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Selection","8");
				editor.putString("Tprice", pref.getString("TPrice_ladies_razor", ""));
				editor.putString("Tmake", "Ladies Racerback Vest");
				editor.commit(); // commit changes

				submitChoose();
			}
		});

		im9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Toast toast = Toast.makeText(getActivity().getBaseContext(), "Contact us for details", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});

		im10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Toast toast = Toast.makeText(getActivity().getBaseContext(), "Contact us for details", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});


		im11.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Selection","11");
				editor.putString("Tprice", pref.getString("TPrice_mug", ""));
				editor.putString("Tmake", "Mug");
				editor.commit(); // commit changes

				submitChoose();
			}
		});

		im12.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Selection","12");
				editor.putString("Tprice", pref.getString("TPrice_baby_grow", ""));
				editor.putString("Tmake", "Short Sleeve Baby Grow");
				editor.commit(); // commit changes

				submitChoose();
			}
		});


		im13.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Toast toast = Toast.makeText(getActivity().getBaseContext(), "Contact us for details", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});

		im14.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Toast toast = Toast.makeText(getActivity().getBaseContext(), "Contact us for details", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});


		im15.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Toast toast = Toast.makeText(getActivity().getBaseContext(), "Contact us for details", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});

		return rootView;
	}

	public void submitChoose(){
		Runtime.getRuntime().gc();
		((MainActivity) getActivity()).displayView(13);
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
