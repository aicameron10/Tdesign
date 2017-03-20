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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.activity.MainActivity;
import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.model.NavItem;
import com.mydeliveries.toastit.util.AlertDialogManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserDetailsFragment extends Fragment {


    TextView uname;
    TextView usurname;
    TextView unumber;
    TextView uemail;
    TextView uaddress;

    String url = "";
    String authStringEnc = "";


    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    private List<String> Profile = new ArrayList<String>();

    private ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_user, container, false);


        Tracker t = ((AppController) getActivity().getApplication()).getTracker(AppController.TrackerName.APP_TRACKER);
        t.setScreenName("Profile Page");
        t.send(new HitBuilders.ScreenViewBuilder().build());

        NavItem nav = new NavItem();

        nav.setPage("Home");


        ((MainActivity) getActivity()).loadDrawsHide();


        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        final Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/thincool.ttf");


        TextView tx1 = (TextView) rootView.findViewById(R.id.textView1);
        TextView tx2 = (TextView) rootView.findViewById(R.id.textView2);
        TextView tx3 = (TextView) rootView.findViewById(R.id.textView3);

        TextView tx4 = (TextView) rootView.findViewById(R.id.textView4);
        TextView tx5 = (TextView) rootView.findViewById(R.id.textView5);


        final TextView heading = (TextView) rootView
                .findViewById(R.id.heading);
        heading.setTypeface(face);

        tx1.setTypeface(face);
        tx2.setTypeface(face);
        tx3.setTypeface(face);
        tx4.setTypeface(face);
        tx5.setTypeface(face);


        uname = (TextView) rootView.findViewById(R.id.rg_name);
        usurname = (TextView) rootView.findViewById(R.id.rg_lastname);
        unumber = (TextView) rootView.findViewById(R.id.rg_number);

        uemail = (TextView) rootView.findViewById(R.id.rg_email);
        uaddress = (TextView) rootView.findViewById(R.id.rg_address);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String User = "";
        if (pref.getString("User", null) != null && (!pref.getString("User", null).equalsIgnoreCase(""))) {
            User = pref.getString("User", null);
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

        Button submit = (Button) rootView.findViewById(R.id.rg_send);
        submit.setTypeface(face);


        submit.setOnClickListener(new OnClickListener() {
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
                }


            }
        });


        return rootView;
    }

    private class reg extends AsyncTask<String, String, String> {

        int res = 0;
        String name;
        String surname;
        String number;
        String email;
        String address;

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request

            name = uname.getText().toString();
            surname = usurname.getText().toString();
            number = unumber.getText().toString();
            email = uemail.getText().toString();
            address = uaddress.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {

            String jsonString;

            try {

                JSONObject jsonObject = new JSONObject();


                jsonObject.put("name", name);
                jsonObject.put("surname", surname);
                jsonObject.put("number", number);
                jsonObject.put("email", email);
                jsonObject.put("address", address);

                jsonString = jsonObject.toString();

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String user;

                user = jsonString;

                editor.putString("User", user);

                editor.apply();
                String jsonString1;
                String baseURL;


                try {

                    JSONObject jsonObject1 = new JSONObject();

                    jsonObject1.put("name", name);
                    jsonObject1.put("surname", surname);
                    jsonObject1.put("number", number);
                    jsonObject1.put("email", email);
                    jsonObject1.put("address", address);
                    jsonObject1.put("gcm_regid", "");

                    jsonString1 = jsonObject1.toString();

                    System.out.println("Serialized JSON Data ::" + jsonString1);

                    // JSON content to post
                    String contentToPost = jsonString1;
                    // Create a URLConnection

                    baseURL = "http://www.toastit.co.za/api/design/newuser";

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


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file) {
            // closing progress dialog

            Toast toast = Toast.makeText(getActivity(), "User saved Successfully", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();


            ((MainActivity) getActivity()).loadDraws();

            super.onPostExecute(file);
        }


    }


}
