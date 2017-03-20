package com.mydeliveries.toastit.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.activity.MainActivity;
import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.app.AppController.TrackerName;
import com.mydeliveries.toastit.model.NavItem;

import java.util.ArrayList;
import java.util.List;


public class OptionsFragment extends Fragment {


	TextView one;
	TextView two;
	TextView three;

	TextView heading;
	int selected;

	private List<FloatingActionMenu> menus = new ArrayList<>();
	private Handler mUiHandler = new Handler();

	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_options, container, false);


		Tracker t = ((AppController) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Options Page");
		t.send(new HitBuilders.ScreenViewBuilder().build());

		((MainActivity) getActivity()).loadDrawsHide();
		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

		final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");

		final FloatingActionMenu menu1 = (FloatingActionMenu) rootView.findViewById(R.id.menu1);

		final Spinner sColour = (Spinner) rootView.findViewById(R.id.addcolour);

		final Spinner sSize = (Spinner) rootView.findViewById(R.id.addsize);

		Runtime.getRuntime().gc();


		menu1.setOnMenuButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("Tcolour", sColour.getSelectedItem().toString());
				editor.putString("Tsize", sSize.getSelectedItem().toString());
				editor.commit(); // commit changes

				Runtime.getRuntime().gc();

				((MainActivity) getActivity()).displayView(2);
			}
		});

		final RadioGroup choiceRadioGroup1 = (RadioGroup) rootView
				.findViewById(R.id.choiceRadioGroup1);

		final RadioButton RadioButton1 = (RadioButton) rootView
				.findViewById(R.id.RadioButton1);
		final RadioButton RadioButton2 = (RadioButton) rootView
				.findViewById(R.id.RadioButton2);

		final RadioButton RadioButton3 = (RadioButton) rootView
				.findViewById(R.id.RadioButton3);


		NavItem nav = new NavItem();
		nav.setPage("Style");

		heading = (TextView) rootView.findViewById(R.id.heading);
		TextView clr = (TextView) rootView.findViewById(R.id.colour);
		TextView size = (TextView) rootView.findViewById(R.id.size);

		final TextView label1 = (TextView) rootView.findViewById(R.id.label1);
		final TextView label2 = (TextView) rootView.findViewById(R.id.label2);

		final TextView txtmeasure = (TextView) rootView.findViewById(R.id.txtmeasure);
		final ImageView img = (ImageView) rootView.findViewById(R.id.sizechart1);
		final ImageView img2 = (ImageView) rootView.findViewById(R.id.sizechart2);



		SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
		SharedPreferences.Editor editor = pref.edit();

		String cat = pref.getString("Selection", null);
		String msg;
		String poly;
		String cotton;
		String yester;




		switch (cat) {
			case "1":
				double tt1 = Double.parseDouble(pref.getString("TPrice_uni_round_neck", ""));

				final double tr1 = tt1 - 50;

				yester = "<font color='#f68b3c'><strong>Polyester</strong></font> <font color='#ffffff'>moisture management t-shirt that is ideal for</font> <font color='#f68b3c'>bright</font><font color='#ffffff'> colour prints.<br />-135g 100% Polyester, moisture management fabric<br />-Generous cut<br >- R"+pref.getString("TPrice_uni_round_neck", "") + "</font> ";
				cotton = "<font color='#f68b3c'><strong>100% Carded Cotton</strong></font> <font color='#ffffff'>- Prints are limited to a maximum size of A3<br />- 150g 100% Carded Cotton fabric<br />- Fashion Fit<br >- R"+String.format("%.2f",tr1) + "</font> ";
				poly =" <font color='#ffffff'>An ultra-soft, ultra-comfy unisex</font> <font color='#f68b3c'><strong>Polycotton</strong></font>  <font color='#ffffff'>slim fit T-shirt.<br />- 165g Polycotton (65% Polyester / 35% Cotton)<br />- Slim fit<br >- R"+pref.getString("TPrice_uni_round_neck","") + "</font>";
				msg= "Please Note:<br />- Measurements above are in centimeters<br />- Specifications above are subject to a 2cm tolerance<br />- Chest measurements are taken 2cm below the armhole (blue arrow)<br />- Length measurement is the longest point of the garment (red arrow)";
				txtmeasure.setText(Html.fromHtml(msg));
				label1.setText("Cotton/Polycotton");
				label2.setText("Polyester");
				img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unisex_polycotton_round_size_chart));
				img2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unisex_polyester_round_size_chart));
				RadioButton1.setText(Html.fromHtml(yester));
				RadioButton2.setText(Html.fromHtml(poly));
				RadioButton3.setText(Html.fromHtml(cotton));
				RadioButton2.setChecked(true);
				String[] stringsColour;
				String[] stringsSize;

				editor.putString("Tfabric", "Polycotton");

				editor.apply(); // apply changes

				stringsColour = new String[]{"White"};
				stringsSize = new String [] {"X-Small","Small", "Medium","Large", "X-Large","XX-Large"};

				ArrayAdapter<String>  spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
				sColour.setAdapter(spinnerArrayAdapter);

				ArrayAdapter<String>  spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
				sSize.setAdapter(spinnerArrayAdaptersize);

				choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						String[] stringsColour;
						String[] stringsSize;
						ArrayAdapter<String> spinnerArrayAdapter;
						ArrayAdapter<String> spinnerArrayAdaptersize;
						SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor editor = pref.edit();
						switch (checkedId) {
							case -1:
								//nothing
								break;
							case R.id.RadioButton1:

								editor.putString("Tfabric", "Polyester");

								editor.putString("Tprice", pref.getString("TPrice_uni_round_neck", ""));
								editor.apply(); // apply changes
								stringsColour = new String[]{"White"};
								stringsSize = new String [] {"Small", "Medium","Large", "X-Large","XX-Large"};

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;
							case R.id.RadioButton2:

								editor.putString("Tfabric", "Polycotton");
								editor.putString("Tprice", pref.getString("TPrice_uni_round_neck", ""));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White"};
								stringsSize = new String [] {"X-Small","Small", "Medium","Large", "X-Large","XX-Large"};

								img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unisex_polycotton_round_size_chart));

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;
							case R.id.RadioButton3:

								editor.putString("Tfabric", "Cotton");
								editor.putString("Tprice", String.valueOf(tr1));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White","Black","Red","Navy Melange","Charcoal Melange"};
								stringsSize = new String [] {"X-Small","Small", "Medium","Large", "X-Large","XX-Large"};

								img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unisex_cotton_round_size_chart));

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);

								break;
							default:
							//nothing
								break;
						}

					}
				});

				break;
			case "2":

				double tt2 = Double.parseDouble(pref.getString("TPrice_uni_v_veck", ""));

				final double tr2 = tt2 - 50;

				yester = "<font color='#f68b3c'><strong>Polyester</strong></font> <font color='#ffffff'>moisture management t-shirt that is ideal for</font> <font color='#f68b3c'>bright</font><font color='#ffffff'> colour prints.<br />-135g 100% Polyester, moisture management fabric<br />-Generous cut</font> ";
				cotton = "<font color='#f68b3c'><strong>100% Carded Cotton</strong></font> <font color='#ffffff'>- Prints are limited to a maximum size of A3<br />- 160g 100% Carded Cotton fabric<br />- Classic Fit<br >- R"+String.format("%.2f",tr2) + "</font> ";
				poly =" <font color='#ffffff'>An ultra-soft, ultra-comfy unisex</font> <font color='#f68b3c'><strong>Polycotton</strong></font>  <font color='#ffffff'>slim fit T-shirt.<br />- 165g Polycotton (65% Polyester / 35% Cotton)<br />- Slim fit cut <br >- R"+pref.getString("TPrice_uni_v_veck", "") + "</font>";
				msg= "Please Note:<br />- Measurements above are in centimeters<br />- Specifications above are subject to a 2cm tolerance<br />- Chest measurements are taken 2cm below the armhole (blue arrow)<br />- Length measurement is the longest point of the garment (red arrow)";


				txtmeasure.setText(Html.fromHtml(msg));
				label1.setText("Cotton/Polycotton");
				label2.setVisibility(View.GONE);
				img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.unisex_v_size_chart));
				img2.setVisibility(View.GONE);
				RadioButton1.setVisibility(View.GONE);
				RadioButton2.setText(Html.fromHtml(poly));
				RadioButton3.setText(Html.fromHtml(cotton));
				RadioButton2.setChecked(true);

				editor.putString("Tfabric", "Polycotton");
				editor.putString("Tprice", pref.getString("TPrice_uni_v_veck", ""));
				editor.apply(); // apply changes

				stringsColour = new String[]{"White"};
				stringsSize = new String [] {"X-Small","Small", "Medium","Large", "X-Large","XX-Large"};

				 spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
				sColour.setAdapter(spinnerArrayAdapter);

				spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
				sSize.setAdapter(spinnerArrayAdaptersize);

				choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						String[] stringsColour;
						String[] stringsSize;
						ArrayAdapter<String> spinnerArrayAdapter;
						ArrayAdapter<String> spinnerArrayAdaptersize;
						SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor editor = pref.edit();
						switch (checkedId) {
							case -1:
								//nothing
								break;
							case R.id.RadioButton2:

								editor.putString("Tfabric", "Polycotton");
								editor.putString("Tprice", pref.getString("TPrice_uni_v_veck", ""));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White"};
								stringsSize = new String [] {"X-Small","Small", "Medium","Large", "X-Large","XX-Large"};

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;
							case R.id.RadioButton3:

								editor.putString("Tfabric", "Cotton");

								double tt = Double.parseDouble(pref.getString("TPrice_uni_v_veck", ""));

								double tr = tt - 50;

								editor.putString("Tprice", String.valueOf(tr2));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White","Black"};
								stringsSize = new String [] {"Small", "Medium","Large", "X-Large","XX-Large"};

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);

								break;
							default:
								//nothing
								break;
						}

					}
				});
				break;
			case "3":
				double tt3 = Double.parseDouble(pref.getString("TPrice_uni_kids", ""));

				final double tr3 = tt3 - 50;

				yester = "<font color='#f68b3c'><strong>Polyester</strong></font> <font color='#ffffff'>moisture management t-shirt that is ideal for</font> <font color='#f68b3c'>all over</font><font color='#ffffff'> T-shirts prints.<br />-135g 100% Polyester, moisture management fabric<br />- Great durability & colour-fastness<br />-Generous cut<br >- R"+pref.getString("TPrice_uni_kids", "") + "</font> ";
				cotton = "<font color='#f68b3c'><strong>100% Carded Cotton</strong></font> <font color='#ffffff'>- Prints are limited to a maximum size of A3<br />- 180g 100% Carded Cotton fabric<br />- Rib crew neck with taped neckline for comfort <br />- Generous cut provides comfortable fit<br />- Durable double stitching on sleeves and bottom hem<br >- R"+String.format("%.2f",tr3) + "</font> ";
				poly =" <font color='#ffffff'>An ultra-soft, ultra-comfy unisex</font> <font color='#f68b3c'><strong>Polycotton</strong></font>  <font color='#ffffff'>slim fit T-shirt.<br />- 165g Polycotton (65% Polyester / 35% Cotton)<br />- Slim fit<br >- R"+pref.getString("TPrice_uni_kids","") + "</font>";
				msg= "Please Note:<br />- Measurements above are in centimeters<br />- Specifications above are subject to a 2cm tolerance<br />- Chest measurements are taken 2cm below the armhole (blue arrow)<br />- Length measurement is the longest point of the garment (red arrow)";
				txtmeasure.setText(Html.fromHtml(msg));
				label1.setText("Cotton");
				label2.setText("Polyester");
				img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.kids_cotton_round_size_chart));
				img2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.kids_polyester_round_size_chart));
				RadioButton1.setText(Html.fromHtml(yester));
				RadioButton2.setVisibility(View.GONE);
				RadioButton3.setText(Html.fromHtml(cotton));
				RadioButton1.setChecked(true);

				editor.putString("Tfabric", "Polyester");

				editor.apply(); // apply changes

				stringsColour = new String[]{"White"};
				stringsSize = new String [] {"3-4","5-6","7-8", "9-10","11-12", "13-14"};

				spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
				sColour.setAdapter(spinnerArrayAdapter);

				spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
				sSize.setAdapter(spinnerArrayAdaptersize);

				choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						String[] stringsColour;
						String[] stringsSize;
						ArrayAdapter<String> spinnerArrayAdapter;
						ArrayAdapter<String> spinnerArrayAdaptersize;
						SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor editor = pref.edit();
						switch (checkedId) {
							case -1:
								//nothing
								break;
							case R.id.RadioButton1:

								editor.putString("Tfabric", "Polyester");
								editor.putString("Tprice", pref.getString("TPrice_uni_kids", ""));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White"};
								stringsSize = new String [] {"3-4","5-6","7-8", "9-10","11-12", "13-14"};

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;

							case R.id.RadioButton3:

								editor.putString("Tfabric", "Cotton");
								editor.putString("Tprice", String.valueOf(tr3));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White","Black","Red","Navy Melange","Charcoal Melange"};
								stringsSize = new String [] {"2-3","4-5","5-6","7-8","9-10","11-12", "13-14"};


								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);

								break;
							default:
								//nothing
								break;
						}

					}
				});

				break;
			case "4":

				yester = "<font color='#f68b3c'><strong>Polyester</strong></font> <font color='#ffffff'>moisture management t-shirt that is ideal for</font> <font color='#f68b3c'>bright</font><font color='#ffffff'> colour prints.<br />-135g 100% Polyester, moisture management fabric<br />-Generous cut<br >- R"+pref.getString("TPrice_ladies_v", "") + "</font> ";
				cotton = "<font color='#f68b3c'><strong>100% Carded Cotton</strong></font> <font color='#ffffff'>- Prints are limited to a maximum size of A3<br />- 150g 100% Carded Cotton fabric<br />- Fashion Fit</font> ";
				poly =" <font color='#ffffff'>An ultra-soft, ultra-comfy unisex</font> <font color='#f68b3c'><strong>Polycotton</strong></font>  <font color='#ffffff'>slim fit T-shirt.<br />- 195g Polycotton (65% Polyester / 35% Cotton)<br />- Slim fit Cut<br >- Deeper V for comfort<br >- Curved Waistline<br >- R"+pref.getString("TPrice_ladies_v","") + "</font>";
				msg= "Please Note:<br />- Measurements above are in centimeters<br />- Specifications above are subject to a 2cm tolerance<br />- Chest measurements are taken 2cm below the armhole (blue arrow)<br />- Length measurement is the longest point of the garment (red arrow)";
				txtmeasure.setText(Html.fromHtml(msg));
				label1.setText("Polycotton");
				label2.setVisibility(View.GONE);
				img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ladies_v_size_chart));
				img2.setVisibility(View.GONE);
				RadioButton1.setVisibility(View.GONE);
				RadioButton2.setText(Html.fromHtml(poly));
				RadioButton3.setVisibility(View.GONE);
				RadioButton2.setChecked(true);


				editor.putString("Tfabric", "Polycotton");

				editor.apply(); // apply changes

				stringsColour = new String[]{"White","Black","Pink","Sky Blue"};
				stringsSize = new String [] {"Small", "Medium","Large", "X-Large","XX-Large"};

				spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
				sColour.setAdapter(spinnerArrayAdapter);

				spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
				sSize.setAdapter(spinnerArrayAdaptersize);

				choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						String[] stringsColour;
						String[] stringsSize;
						ArrayAdapter<String> spinnerArrayAdapter;
						ArrayAdapter<String> spinnerArrayAdaptersize;
						SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor editor = pref.edit();
						switch (checkedId) {
							case -1:
								//nothing
								break;

							case R.id.RadioButton2:

								editor.putString("Tfabric", "Polycotton");
								editor.putString("Tprice", pref.getString("TPrice_ladies_v", ""));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White","Black","Pink","Sky Blue"};
								stringsSize = new String [] {"Small", "Medium","Large", "X-Large","XX-Large"};


								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;

							default:
								//nothing
								break;
						}

					}
				});
				break;
			case "5":
				double tt5 = Double.parseDouble(pref.getString("TPrice_ladies_vest", ""));

				final double tr5 = tt5 - 50;

				yester = "<font color='#f68b3c'><strong>Polyester</strong></font> <font color='#ffffff'>moisture management t-shirt that is ideal for</font> <font color='#f68b3c'>bright</font><font color='#ffffff'> colour prints.<br />-135g 100% Polyester, moisture management fabric<br />-Generous cut</font> ";
				cotton = "<font color='#f68b3c'><strong>100% Carded Cotton</strong></font> <font color='#ffffff'>- Prints are limited to a maximum size of A3<br />- 160g 100% Carded Cotton fabric<br />- Classic Fit<br >- R"+String.format("%.2f",tr5) + "</font> ";
				poly =" <font color='#f68b3c'><strong>Polycotton interlock</strong></font>  <font color='#ffffff'>produced from the best quality yarns for coolness and strength<br />- 200g Polycotton Interlock (65% Polyester / 35% Cotton)<br />- Bound armhole and neckline <br >- Double stitched hem <br >- Curved waistline <br >- White: R"+pref.getString("TPrice_ladies_vest", "") + " Black: R" +String.format("%.2f",tr5) + "</font>";
				msg= "Please Note:<br />- Measurements above are in centimeters<br />- Specifications above are subject to a 2cm tolerance<br />- Chest measurements are taken 2cm below the armhole (blue arrow)<br />- Length measurement is the longest point of the garment (red arrow)";


				txtmeasure.setText(Html.fromHtml(msg));
				label1.setText("Polycotton");
				label2.setVisibility(View.GONE);
				img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ladies_vest_size_chart));
				img2.setVisibility(View.GONE);
				RadioButton1.setVisibility(View.GONE);
				RadioButton2.setText(Html.fromHtml(poly));
				RadioButton3.setVisibility(View.GONE);
				RadioButton2.setChecked(true);

				editor.putString("Tfabric", "Polycotton");
				editor.putString("Tprice", pref.getString("TPrice_ladies_vest", ""));
				editor.apply(); // apply changes

				stringsColour = new String[]{"White","Black"};
				stringsSize = new String [] {"Small", "Medium","Large", "X-Large","XX-Large"};

				spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
				sColour.setAdapter(spinnerArrayAdapter);

				spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
				sSize.setAdapter(spinnerArrayAdaptersize);

				choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						String[] stringsColour;
						String[] stringsSize;
						ArrayAdapter<String> spinnerArrayAdapter;
						ArrayAdapter<String> spinnerArrayAdaptersize;
						SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor editor = pref.edit();
						switch (checkedId) {
							case -1:
								//nothing
								break;
							case R.id.RadioButton2:

								editor.putString("Tfabric", "Polycotton");
								editor.putString("Tprice", pref.getString("TPrice_ladies_vest", ""));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White","Black"};
								stringsSize = new String [] {"Small", "Medium","Large", "X-Large","XX-Large"};

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;

							default:
								//nothing
								break;
						}

					}
				});
				break;
			case "6":

				double tt6 = Double.parseDouble(pref.getString("TPrice_mens_vest", ""));

				final double tr6 = tt6 - 50;

				yester = "<font color='#f68b3c'><strong>Polyester</strong></font> <font color='#ffffff'>moisture management t-shirt that is ideal for</font> <font color='#f68b3c'>bright</font><font color='#ffffff'> colour prints.<br />-135g 100% Polyester, moisture management fabric<br />-Generous cut</font> ";
				cotton = "<font color='#f68b3c'><strong>100% Carded Cotton</strong></font> <font color='#ffffff'>- Prints are limited to a maximum size of A4<br />- 160g 100% Carded Cotton fabric<br />-  Fashion Fit<br >- R"+String.format("%.2f",tr6) + "</font> ";
				poly =" <font color='#ffffff'>An ultra-soft, ultra-comfy unisex</font> <font color='#f68b3c'><strong>Polycotton</strong></font>  <font color='#ffffff'>slim fit T-shirt.<br />- 165g Polycotton (65% Polyester / 35% Cotton)<br />- Slim fit<br >- R"+pref.getString("TPrice_mens_vest", "") + "</font>";
				msg= "Please Note:<br />- Measurements above are in centimeters<br />- Specifications above are subject to a 2cm tolerance<br />- Chest measurements are taken 2cm below the armhole (blue arrow)<br />- Length measurement is the longest point of the garment (red arrow)";
				txtmeasure.setText(Html.fromHtml(msg));
				label1.setText("Cotton/Polycotton");
				label2.setVisibility(View.GONE);
				img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mens_vest_size_chart));
				img2.setVisibility(View.GONE);
				RadioButton1.setVisibility(View.GONE);
				RadioButton2.setText(Html.fromHtml(poly));
				RadioButton3.setText(Html.fromHtml(cotton));
				RadioButton3.setChecked(true);

				editor.putString("Tfabric", "Cotton");
				editor.putString("Tprice", String.valueOf(tr6));
				editor.apply(); // apply changes

				stringsColour = new String[]{"White","Black"};
				stringsSize = new String [] {"Small", "Medium","Large", "X-Large","XX-Large"};

				spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
				sColour.setAdapter(spinnerArrayAdapter);

				spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
				sSize.setAdapter(spinnerArrayAdaptersize);

				choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						String[] stringsColour;
						String[] stringsSize;
						ArrayAdapter<String> spinnerArrayAdapter;
						ArrayAdapter<String> spinnerArrayAdaptersize;
						SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor editor = pref.edit();
						switch (checkedId) {
							case -1:
								//nothing
								break;
							case R.id.RadioButton2:

								editor.putString("Tfabric", "Polycotton");
								editor.putString("Tprice", pref.getString("TPrice_mens_vest", ""));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White"};
								stringsSize = new String [] {"X-Small","Small", "Medium","Large", "X-Large","XX-Large"};

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;
							case R.id.RadioButton3:

								editor.putString("Tfabric", "Cotton");
								editor.putString("Tprice", String.valueOf(tr6));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White","Black"};
								stringsSize = new String [] {"Small", "Medium","Large", "X-Large","XX-Large"};

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);

								break;
							default:
								//nothing
								break;
						}

					}
				});
				break;
			case "7":
				yester = "<font color='#f68b3c'><strong>Polyester</strong></font> <font color='#ffffff'>moisture management t-shirt that is ideal for</font> <font color='#f68b3c'>bright</font><font color='#ffffff'> colour prints.<br />-135g 100% Polyester, moisture management fabric<br />-Generous cut<br >- R"+pref.getString("TPrice_ladies_round", "") + "</font> ";
				cotton = "<font color='#ffffff'>100%</font><font color='#f68b3c'><strong> Soft Cotton</strong></font> <font color='#ffffff'>- Prints are limited to a maximum size of A3<br />- 150g 100% Carded Cotton fabric<br />- Fashion Fit<br >- R"+pref.getString("TPrice_ladies_round","") + "</font> ";
				poly =" <font color='#ffffff'>An ultra-soft, ultra-comfy unisex</font> <font color='#f68b3c'><strong>Polycotton</strong></font>  <font color='#ffffff'>slim fit T-shirt.<br />- 195g Polycotton (65% Polyester / 35% Cotton)<br />- Slim fit Cut<br >- Deeper V for comfort<br >- Curved Waistline<br >- R"+pref.getString("TPrice_ladies_round","") + "</font>";
				msg= "Please Note:<br />- Measurements above are in centimeters<br />- Specifications above are subject to a 2cm tolerance<br />- Chest measurements are taken 2cm below the armhole (blue arrow)<br />- Length measurement is the longest point of the garment (red arrow)";
				txtmeasure.setText(Html.fromHtml(msg));
				label1.setText("Cotton");
				label2.setVisibility(View.GONE);
				img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ladies_cotton_round_size_chart));
				img2.setVisibility(View.GONE);
				RadioButton1.setVisibility(View.GONE);
				RadioButton2.setVisibility(View.GONE);
				RadioButton3.setText(Html.fromHtml(cotton));
				RadioButton3.setChecked(true);


				editor.putString("Tfabric", "Cotton");
				editor.putString("Tprice", pref.getString("TPrice_ladies_round", ""));
				editor.apply(); // apply changes

				stringsColour = new String[]{"White","Black","Red","Navy Melange","Charcoal Melange"};
				stringsSize = new String [] {"X-Small","Small", "Medium","Large", "X-Large","XX-Large"};

				spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
				sColour.setAdapter(spinnerArrayAdapter);

				spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
				sSize.setAdapter(spinnerArrayAdaptersize);

				choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						String[] stringsColour;
						String[] stringsSize;
						ArrayAdapter<String> spinnerArrayAdapter;
						ArrayAdapter<String> spinnerArrayAdaptersize;
						SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor editor = pref.edit();
						switch (checkedId) {
							case -1:
								//nothing
								break;

							case R.id.RadioButton3:

								editor.putString("Tfabric", "Cotton");
								editor.putString("Tprice", pref.getString("TPrice_ladies_round", ""));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White","Black","Red","Navy Melange","Charcoal Melange"};
								stringsSize = new String [] {"X-Small","Small", "Medium","Large", "X-Large","XX-Large"};


								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;

							default:
								//nothing
								break;
						}

					}
				});
				break;
			case "8":
				yester = "<font color='#f68b3c'><strong>Polyester</strong></font> <font color='#ffffff'>moisture management t-shirt that is ideal for</font> <font color='#f68b3c'>bright</font><font color='#ffffff'> colour prints.<br />-135g 100% Polyester, moisture management fabric<br />-Generous cut<br >- R"+pref.getString("TPrice_ladies_razor", "") + "</font> ";
				cotton = "<font color='#ffffff'>100%</font><font color='#f68b3c'><strong> Soft Cotton</strong></font> <font color='#ffffff'>- Prints are limited to a maximum size of A3<br />- 150g 100% Carded Cotton fabric<br />- Fashion Fit<br >- R"+pref.getString("TPrice_ladies_razor","") + "</font> ";
				poly ="<font color='#f68b3c'><strong>Polycotton</strong></font>  <font color='#ffffff'>- Prints are limited to a maximum size of A3<br />- 195g PolyCotton<br >- R"+pref.getString("TPrice_ladies_razor","") + "</font>";
				msg= "Please Note:<br />- Measurements above are in centimeters<br />- Specifications above are subject to a 2cm tolerance<br />- Chest measurements are taken 2cm below the armhole (blue arrow)<br />- Length measurement is the longest point of the garment (red arrow)";
				txtmeasure.setText(Html.fromHtml(msg));
				label1.setText("Polycotton");
				label2.setVisibility(View.GONE);
				img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ladies_racerback_size_chart));
				img2.setVisibility(View.GONE);
				RadioButton1.setVisibility(View.GONE);
				RadioButton2.setText(Html.fromHtml(poly));
				RadioButton3.setVisibility(View.GONE);
				RadioButton2.setChecked(true);


				editor.putString("Tfabric", "Polycotton");
				editor.putString("Tprice", pref.getString("TPrice_ladies_razor", ""));
				editor.apply(); // apply changes

				stringsColour = new String[]{"White","Black","Red","Pink"};
				stringsSize = new String [] {"Small", "Medium","Large", "X-Large","XX-Large"};

				spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
				sColour.setAdapter(spinnerArrayAdapter);

				spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
				sSize.setAdapter(spinnerArrayAdaptersize);

				choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						String[] stringsColour;
						String[] stringsSize;
						ArrayAdapter<String> spinnerArrayAdapter;
						ArrayAdapter<String> spinnerArrayAdaptersize;
						SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor editor = pref.edit();
						switch (checkedId) {
							case -1:
								//nothing
								break;

							case R.id.RadioButton2:

								editor.putString("Tfabric", "Polycotton");
								editor.putString("Tprice", pref.getString("TPrice_ladies_razor", ""));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White","Black","Red","Pink"};
								stringsSize = new String [] {"Small", "Medium","Large", "X-Large","XX-Large"};


								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;

							default:
								//nothing
								break;
						}

					}
				});
				break;
			case "12":
				yester = "<font color='#f68b3c'><strong>Polyester</strong></font> <font color='#ffffff'>moisture management t-shirt that is ideal for</font> <font color='#f68b3c'>bright</font><font color='#ffffff'> colour prints.<br />-135g 100% Polyester, moisture management fabric<br />-Generous cut</font> ";
				cotton = "<font color='#f68b3c'><strong>100% Carded Cotton</strong></font> <font color='#ffffff'>- Prints are limited to a maximum size of A3<br />- 160g 100% Carded Cotton fabric<br />- Classic Fit</font> ";
				poly =" <font color='#ffffff'>An ultra-soft, ultra-comfy unisex</font> <font color='#f68b3c'>Polycotton </font><font color='#ffffff'>Baby Grows.<br/> - 165g Polycotton (65% Polyester / 35% Cotton)<br >- R"+pref.getString("TPrice_baby_grow", "") + "</font>";
				msg= "Please Note:<br />- Measurements above are in centimeters<br />- Specifications above are subject to a 2cm tolerance<br />- Chest measurements are taken 2cm below the armhole (blue arrow)<br />- Length measurement is the longest point of the garment (red arrow)";
				txtmeasure.setVisibility(View.GONE);
				label1.setVisibility(View.GONE);
				label2.setVisibility(View.GONE);
				img.setVisibility(View.GONE);
				img2.setVisibility(View.GONE);
				RadioButton1.setVisibility(View.GONE);
				RadioButton2.setText(Html.fromHtml(poly));
				RadioButton3.setVisibility(View.GONE);
				RadioButton2.setChecked(true);

				editor.putString("Tfabric", "Polycotton");
				editor.putString("Tprice", pref.getString("TPrice_baby_grow", ""));
				editor.apply(); // apply changes

				stringsColour = new String[]{"White"};
				stringsSize = new String [] {"0/3 months","3/6 months","6/12 months","12/18 months"};

				spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
				sColour.setAdapter(spinnerArrayAdapter);

				spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
				sSize.setAdapter(spinnerArrayAdaptersize);

				choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						String[] stringsColour;
						String[] stringsSize;
						ArrayAdapter<String> spinnerArrayAdapter;
						ArrayAdapter<String> spinnerArrayAdaptersize;
						SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor editor = pref.edit();
						switch (checkedId) {
							case -1:
								//nothing
								break;
							case R.id.RadioButton2:

								editor.putString("Tfabric", "Polycotton");
								editor.putString("Tprice", pref.getString("TPrice_baby_grow", ""));
								editor.apply(); // apply changes

								stringsColour = new String[]{"White"};
								stringsSize = new String [] {"0/3 months","3/6 months","6/12 months","12/18 months"};

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;

							default:
								//nothing
								break;
						}

					}
				});
				break;
			case "11":

				double tt11 = Double.parseDouble(pref.getString("TPrice_mug", ""));

				final double tr11 = tt11 + 30;
				yester = "<font color='#f68b3c'><strong>Polyester</strong></font> <font color='#ffffff'>moisture management t-shirt that is ideal for</font> <font color='#f68b3c'>bright</font><font color='#ffffff'> colour prints.<br />-135g 100% Polyester, moisture management fabric<br />-Generous cut</font> ";
				cotton = "<font color='#f68b3c'><strong>100% Carded Cotton</strong></font> <font color='#ffffff'>- Prints are limited to a maximum size of A3<br />- 160g 100% Carded Cotton fabric<br />- Classic Fit</font> ";
				poly =" <font color='#ffffff'>Add a pop of color to your morning coffee with our</font> <font color='#f68b3c'>Two-Tone Mugs </font><font color='#ffffff'> 11oz (325ml) ! <br/>The outside of the mug features a bright white base for your photo, logo, pattern, or saying, while the inside is vividly glazed in rich color. Give this fun gift to a friend, or add some zest to your dinnerware collection<br >- Front/Half Mug: R"+pref.getString("TPrice_mug", "") +" Full Mug: R"+String.format("%.2f",tr11) +"</font>";
				msg= "Please Note:<br />- Measurements above are in centimeters<br />- Specifications above are subject to a 2cm tolerance<br />- Chest measurements are taken 2cm below the armhole (blue arrow)<br />- Length measurement is the longest point of the garment (red arrow)";
				txtmeasure.setVisibility(View.GONE);
				label1.setVisibility(View.GONE);
				label2.setVisibility(View.GONE);
				img.setVisibility(View.GONE);
				img2.setVisibility(View.GONE);
				RadioButton1.setVisibility(View.GONE);
				RadioButton2.setText(Html.fromHtml(poly));
				RadioButton3.setVisibility(View.GONE);
				RadioButton2.setChecked(true);

				editor.putString("Tfabric", "Mug");
				editor.apply(); // apply changes

				stringsColour = new String[]{"White","Black","Pink","Green","Navy Blue","Orange"};
				stringsSize = new String [] {"Full Mug","Front / Half Mug"};

				spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
				sColour.setAdapter(spinnerArrayAdapter);

				spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
				sSize.setAdapter(spinnerArrayAdaptersize);

				choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						String[] stringsColour;
						String[] stringsSize;
						ArrayAdapter<String> spinnerArrayAdapter;
						ArrayAdapter<String> spinnerArrayAdaptersize;
						SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor editor = pref.edit();
						switch (checkedId) {
							case -1:
								//nothing
								break;
							case R.id.RadioButton2:

								editor.putString("Tfabric", "Mug");
								editor.apply(); // apply changes

								stringsColour = new String[]{"White","Black","Pink","Green","Navy Blue","Orange"};
								stringsSize = new String [] {"Full Mug","Front / Half Mug"};

								spinnerArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, stringsColour);
								sColour.setAdapter(spinnerArrayAdapter);

								spinnerArrayAdaptersize = new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, stringsSize);
								sSize.setAdapter(spinnerArrayAdaptersize);
								break;

							default:
								//nothing
								break;
						}

					}
				});
				break;
			default:
//nothing
				break;
		}



		heading.setTypeface(face);
		clr.setTypeface(face);
		size.setTypeface(face);
		txtmeasure.setTypeface(face);
		label1.setTypeface(face);
		label2.setTypeface(face);
		RadioButton1.setTypeface(face);
		RadioButton2.setTypeface(face);
		RadioButton3.setTypeface(face);


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
