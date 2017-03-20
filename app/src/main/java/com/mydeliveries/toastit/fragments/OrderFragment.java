package com.mydeliveries.toastit.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
import com.mydeliveries.toastit.model.NavItem;
import com.mydeliveries.toastit.util.Mail;
import com.mydeliveries.toastit.views.LoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OrderFragment extends Fragment {




	TextView uname;
	TextView usurname;
	TextView unumber;
	TextView uemail;
	TextView uaddress;

	double total = 0;
	double base;

	double extraDelivery = 0;
	double extraBackPrint = 0;
	int printCount;


	private Mail m;

	View rootView;

	String mode="Collect";
	String msg="";

	String msg2="";
	String msg3="";

	CheckBox chkprint;

	LoadingView loading;
	FloatingActionMenu menu1;

	String Tcolour;
	String Tsize;
	String Tfabric;
	String Tprice;
	String Tmake;


	String filenameOrder;
	String filenameFlip;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {



		rootView = inflater.inflate(R.layout.fragment_order, container, false);


		Tracker t = ((AppController) getActivity().getApplication()).getTracker(AppController.TrackerName.APP_TRACKER);
		t.setScreenName("Order Page");
		t.send(new HitBuilders.ScreenViewBuilder().build());


		NavItem nav = new NavItem();

		nav.setPage("Home");

		m = new Mail("toastitmobile@gmail.com", "%m0bil3@toastit%");

		((MainActivity) getActivity()).loadDrawsHide();


		((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

		final Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");

		loading = (LoadingView) rootView.findViewById(R.id.loading_view);
		loading.setVisibility(View.GONE);


		TextView tx1 = (TextView) rootView.findViewById(R.id.user);
		TextView tx2 = (TextView) rootView.findViewById(R.id.details);
		final TextView tx3 = (TextView) rootView.findViewById(R.id.total);
		final TextView tx4 = (TextView) rootView.findViewById(R.id.quantity);

		 chkprint = (CheckBox) rootView.findViewById(R.id.chkprint);


        final TextView heading = (TextView) rootView
                .findViewById(R.id.heading);

		final RadioGroup choiceRadioGroup1 = (RadioGroup) rootView
				.findViewById(R.id.choiceRadioGroup1);

		final Spinner squan = (Spinner) rootView.findViewById(R.id.addquantity);

		final RadioButton RadioButton1 = (RadioButton) rootView
				.findViewById(R.id.RadioButton1);
		final RadioButton RadioButton2 = (RadioButton) rootView
				.findViewById(R.id.RadioButton2);


		heading.setTypeface(face);

		RadioButton1.setTypeface(face);
		RadioButton2.setTypeface(face);
		RadioButton1.isChecked();

		tx1.setTypeface(face);
		tx2.setTypeface(face);
		tx3.setTypeface(face);
		tx4.setTypeface(face);

		final SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
		SharedPreferences.Editor editor = pref.edit();


		chkprint.setVisibility(View.GONE);


		Tcolour = pref.getString("Tcolour", null);
		Tsize = pref.getString("Tsize", null);
		Tfabric = pref.getString("Tfabric", null);
		Tprice = pref.getString("Tprice", null);
		Tmake = pref.getString("Tmake", null);




		if(pref.getString("payBack", null).equalsIgnoreCase("Yes")){
			chkprint.setVisibility(View.VISIBLE);
			chkprint.setChecked(true);
			extraBackPrint = 80;
			base = Double.parseDouble( pref.getString("Tprice", null));
			double newValue = base + extraBackPrint;

			tx3.setText("Total: R" + newValue);
		}else{
			base = Double.parseDouble( pref.getString("Tprice", null));
			double newValue = base + extraBackPrint;

			tx3.setText("Total: R" + base);
		}

		chkprint.setOnClickListener(new OnClickListener() {


			@Override
			public void onClick(View v) {
				//is chkIos checked?

				if (((CheckBox) v).isChecked()) {
					extraBackPrint = 80;
					total = ((base + extraBackPrint) * printCount) + extraDelivery;


					tx3.setText("Total: R" + total);
				}else{
					extraBackPrint = 0;
					total = ((base + extraBackPrint) * printCount) + extraDelivery;


					tx3.setText("Total: R" + total);
				}

			}
		});


		msg= "<strong>Style: </strong>" + Tmake + "<br /><strong>Fabric: </strong>" + Tfabric + "<br /><strong>Size: </strong>" + Tsize + "<br /><strong>Colour: </strong>" + Tcolour + "<br /><strong>Price: R</strong>" + Tprice;


		tx2.setText(Html.fromHtml(msg));


		uname = (TextView) rootView.findViewById(R.id.rg_name);
		usurname = (TextView) rootView.findViewById(R.id.rg_lastname);
		unumber = (TextView) rootView.findViewById(R.id.rg_number);
		
		uemail = (TextView) rootView.findViewById(R.id.rg_email);
		uaddress = (TextView) rootView.findViewById(R.id.rg_address);


		String User ="";
		if( pref.getString("User", null) != null &&(!pref.getString("User", "").equalsIgnoreCase("")) ) {
			User   = pref.getString("User", null);
			try {
				JSONObject jsonObjorder = new JSONObject(User);

				uname.setText(jsonObjorder.getString("name"));
				usurname.setText(jsonObjorder.getString("surname"));
				unumber.setText(jsonObjorder.getString("number"));

				uemail.setText(jsonObjorder.getString("email"));
				uaddress.setText(jsonObjorder.getString("address"));


			} catch (JSONException e) {
				e.printStackTrace();
			}


		}


		String[] strings = null;
		strings = new String [] {"1","2","3","4","5","6","7","8","9","10"};

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, strings);
		squan.setAdapter(spinnerArrayAdapter);

		squan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				printCount = Integer.parseInt(squan.getSelectedItem().toString());

				total = ((base + extraBackPrint) * printCount) + extraDelivery;


				tx3.setText("Total: R" + total);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

		choiceRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				RadioButton rb = (RadioButton) rootView.findViewById(checkedId);
				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();

				if (rb.getText().toString().equalsIgnoreCase("Collect in store")) {
					extraDelivery = 0;
					mode = "Collect";

					total = ((base + extraBackPrint) * printCount) + extraDelivery;

					tx3.setText("Total: R" + total);
				}

				if (rb.getText().toString().equalsIgnoreCase("Delivery(R70)")) {
					extraDelivery = 70;
					mode = "Delivery";

					total = ((base + extraBackPrint) * printCount) + extraDelivery;


					tx3.setText("Total: R" + total);
				}
			}
		});


		menu1 = (FloatingActionMenu) rootView.findViewById(R.id.rg_send);
		menu1.setOnMenuButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				InputMethodManager inputManager = (InputMethodManager)
						getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				String errMsg = "";
				if (uname.getText().toString().equals("")) {
					uname.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg = errMsg + "\nPlease enter your Name";
				} else {
					uname.setBackgroundResource(R.drawable.rect_text_edit_border);
				}
				if (usurname.getText().toString().equals("")) {
					usurname.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg = errMsg + "\nPlease enter your Surname";
				} else {
					usurname.setBackgroundResource(R.drawable.rect_text_edit_border);
				}
				if (uemail.getText().toString().equals("")) {
					uemail.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg = errMsg + "\nPlease enter your Email Address";
				} else {
					uemail.setBackgroundResource(R.drawable.rect_text_edit_border);
				}

				if (uaddress.getText().toString().equals("")) {
					uaddress.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg = errMsg + "\nPlease enter your Address";
				} else {
					uaddress.setBackgroundResource(R.drawable.rect_text_edit_border);
				}


				if (unumber.getText().toString().equals("") || unumber.length() < 10) {
					unumber.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg = errMsg + "\nPlease enter a valid Telephone Number";
				} else {
					unumber.setBackgroundResource(R.drawable.rect_text_edit_border);
				}
				if (!android.util.Patterns.EMAIL_ADDRESS.matcher(uemail.getText().toString()).matches()) {
					uemail.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg = errMsg + "\nInvalid Email Address";
				}
				if (!android.util.Patterns.PHONE.matcher(unumber.getText().toString()).matches()) {
					unumber.setBackgroundResource(R.drawable.rect_text_edit_border_red);
					errMsg = errMsg + "\nInvalid Telephone Number";
				}


				if (!errMsg.equalsIgnoreCase("")) {
					Toast toast = Toast.makeText(getActivity(), errMsg + "\n", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {

					new reg().execute();
					new sendMail().execute();
				}




			}
		});




		return rootView;
	}


	public String compressImageOrder(String imageUri) {

		String filePath = getRealPathFromURI(imageUri);
		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();

		//by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
		//you try the use the bitmap here, you will get null.
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;

		//max Height and width values of the compressed image is taken as 816x612

		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		//width and height values are set maintaining the aspect ratio of the image

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}

		//setting inSampleSize value allows to load a scaled down version of the original image
		options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
		//inJustDecodeBounds set to false to load the actual bitmap
		options.inJustDecodeBounds = false;
		//this options allow android to claim the bitmap memory if it runs low on memory
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
			//load the bitmap from its path
			bmp = BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float) options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

		//check the rotation of the image and display it properly
		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);

			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
					scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
					true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream out;
		filenameOrder = getFilename();
		try {
			out = new FileOutputStream(filenameOrder);

			//write the compressed bitmap at the destination specified by filename.
			scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}





		return filenameOrder;

	}


	public String compressImageFlip(String imageUri) {

		String filePath = getRealPathFromURI(imageUri);
		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();

		//by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
		//you try the use the bitmap here, you will get null.
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;

		//max Height and width values of the compressed image is taken as 816x612

		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		//width and height values are set maintaining the aspect ratio of the image

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}

		//setting inSampleSize value allows to load a scaled down version of the original image
		options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
		//inJustDecodeBounds set to false to load the actual bitmap
		options.inJustDecodeBounds = false;
		//this options allow android to claim the bitmap memory if it runs low on memory
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
			//load the bitmap from its path
			bmp = BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float) options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

		//check the rotation of the image and display it properly
		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);

			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
					scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
					true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream out;
		filenameFlip = getFilenameFlip();
		try {
			out = new FileOutputStream(filenameFlip);

			//write the compressed bitmap at the destination specified by filename.
			scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}





		return filenameFlip;

	}

	public String getFilename() {




		String folder_main = "ToastItImages";

		File f = new File(Environment.getExternalStorageDirectory(), folder_main);
		if (!f.exists()) {
			f.mkdirs();
		}

		String uriSting = (f.getAbsolutePath() + "/" + "orderScreenShot.png");
		return uriSting;

	}

	public String getFilenameFlip() {




		String folder_main = "ToastItImages";

		File f = new File(Environment.getExternalStorageDirectory(), folder_main);
		if (!f.exists()) {
			f.mkdirs();
		}

		String uriSting = (f.getAbsolutePath() + "/" + "flipScreenShot.png");
		return uriSting;

	}

	private String getRealPathFromURI(String contentURI) {
		Uri contentUri = Uri.parse(contentURI);
		Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
		if (cursor == null) {
			return contentUri.getPath();
		} else {
			cursor.moveToFirst();
			int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(index);
		}
	}

	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		final float totalPixels = width * height;
		final float totalReqPixelsCap = reqWidth * reqHeight * 2;
		while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}

		return inSampleSize;
	}


	private class sendMail extends AsyncTask<String, String, String> {


		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request
			loading.setVisibility(View.VISIBLE);
			loading.startAnimating();

			menu1.setEnabled(false);

			msg2= "Style: " + Tmake + "\nFabric: " + Tfabric + "\nSize: " + Tsize + "\nColour: " + Tcolour + "\nPrice: R" + total + "\nDelivery/Collect: " + mode+ "\nQuantity: " + printCount;

			msg3= "\nCustomer Details\nName: " + uname.getText().toString() + "\nSurname: " + usurname.getText().toString() + "\nNumber: " + unumber.getText().toString() + "\nEmail: " + uemail.getText().toString() + "\nAddress: " + uaddress.getText().toString();

		}
		@Override
		protected String doInBackground(String... params) {

			String[] toArr = {"toastitmobile@gmail.com"}; // This is an array, you can add more emails, just separate them with a coma
			m.setTo(toArr); // load array to setTo function
			m.setFrom("toastitmobile@gmail.com"); // who is sending the email
			m.setSubject("New Order" +
					"");
			m.setBody(msg2 + msg3);

			try {
				Uri flipImage;
				Uri orderImageNew;
				Uri uri3;
				Uri uri4;

				File myFileFlip = new File(Environment.getExternalStorageDirectory().getPath() + "/toastitImages/" + "flipScreenShot.png");

				if(myFileFlip.exists()){
					compressImageFlip(Environment.getExternalStorageDirectory().getPath() + "/toastitImages/" + "flipScreenShot.png");
					flipImage = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/toastitImages/" + "flipScreenShot.png"));

					 uri3 = Uri.parse(flipImage.toString());

					m.addAttachment(uri3.getPath());  // path to file you want to attach
				}

				File myFileOrder = new File(Environment.getExternalStorageDirectory().getPath() + "/toastitImages/" + "orderScreenShot.png");

				if(myFileOrder.exists()){
					compressImageOrder(Environment.getExternalStorageDirectory().getPath() + "/toastitImages/" + "orderScreenShot.png");
					orderImageNew = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/toastitImages/" + "orderScreenShot.png"));
					 uri4 = Uri.parse(orderImageNew.toString());

					m.addAttachment(uri4.getPath());  // path to file you want to attach

				}




			Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/toastitImages/" + "frontImage.png"));
			Uri uriback = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/toastitImages/" + "backImage.png"));



			Uri uri1 = Uri.parse(uri.toString());

			Uri uri2 = Uri.parse(uriback.toString());



				m.addAttachment(uri1.getPath());  // path to file you want to attach




				if(chkprint.isChecked() == true) {
					m.addAttachment(uri2.getPath());  // path to file you want to attach
				}


				if(m.send()) {
					// success
					//Toast.makeText(getActivity(), "Email was sent successfully.", Toast.LENGTH_LONG).show();
				} else {
					// failure
					//Toast.makeText(getActivity(), "Email was not sent.", Toast.LENGTH_LONG).show();
					menu1.setEnabled(true);
				}


			} catch(Exception e) {
				// some other problem
				e.printStackTrace();
				loading.setVisibility(View.GONE);
				loading.stopAnimating();
				menu1.setEnabled(true);
				//Toast.makeText(getActivity(), "There was a problem sending the email.", Toast.LENGTH_LONG).show();
			}
				return null;
			}
		@Override
		protected void onPostExecute(String file) {
			// closing progress dialog
			new sendEmailBankDetails().execute();
			//Toast.makeText(getActivity(), "Order was sent successfully.", Toast.LENGTH_LONG).show();

			super.onPostExecute(file);
		}
	}

	private class reg extends AsyncTask<String, String, String> {


		@Override
		protected void onPreExecute() {
			// Showing progress dialog before sending http request

		}
		@Override
		protected String doInBackground(String... params) {

			String jsonString = "";

			try {	 

				JSONObject jsonObject = new JSONObject();


				jsonObject.put("name", uname.getText().toString());
				jsonObject.put("surname", usurname.getText().toString());

				jsonObject.put("number", unumber.getText().toString());
				jsonObject.put("email", uemail.getText().toString());
				jsonObject.put("address", uaddress.getText().toString());

				jsonString = jsonObject.toString();

				SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor editor = pref.edit();
				String user ="";

				user =  jsonString;

				editor.putString("User",user);

				editor.apply(); // commit changes


			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}


		@Override
		protected void onPostExecute(String file) {
			// closing progress dialog



			super.onPostExecute(file);
		}



	}

	public class sendEmailBankDetails extends AsyncTask<String, String, String> {

		int res =0;


		@Override
		protected void onPreExecute() {

		}
		@Override
		protected String doInBackground(String... params) {

			String jsonString;
			String baseURL;


			try {

				JSONArray ja = new JSONArray();

				JSONObject jo = new JSONObject();
				jo.put("email", uemail.getText().toString());
				ja.put(jo);

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("email", ja);
				jsonObject.put("price", total);
				jsonString = jsonObject.toString();

				//System.out.println("Serialized JSON Data ::" + jsonString);

				// JSON content to post
				String contentToPost = jsonString;
				// Create a URLConnection

				baseURL = "http://www.toastit.co.za/api/design/order";

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
			try {
				if (res == 201) {


					Toast toast = Toast.makeText(getActivity(), "Order was sent successfully", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					((MainActivity) getActivity()).displayView(0);

				} else {


					Toast toast = Toast.makeText(getActivity(), "Error Sending , please try again!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();


				}
			}catch (Exception e){
				e.printStackTrace();
			}

			super.onPostExecute(file);
		}



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
