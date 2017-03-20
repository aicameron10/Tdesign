package com.mydeliveries.toastit.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.fragments.InviteFragment;
import com.mydeliveries.toastit.model.Invites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 04/09/2015.
 */

public class ListAdapterInvite extends BaseAdapter implements SectionIndexer {


    private final InviteFragment fragment;
    private List<Invites> itemList;
    private Activity activity;
    private static String sections = "abcdefghijklmnopqrstuvwxyz";

    boolean checkAll_flag = false;
    boolean checkItem_flag = false;

    ViewHolder viewHolder;

    final List<String> SelectedBox = new ArrayList<String>();

    private ProgressDialog pDialog;
    CheckBox invite;

    public ListAdapterInvite(Activity activity, List<Invites> itemList, InviteFragment fragment) {

        this.itemList = itemList;
        this.activity = activity;
        this.fragment = fragment;

    }

    public int getCount() {
        return itemList.size();
    }

    public Object getItem(int position) {
        return itemList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    // our ViewHolder.

    /** Holds child views for one row. */
    public static class ViewHolder
    {
        private CheckBox checkBox;
        private TextView textView;



        public ViewHolder()
        {
        }

        public ViewHolder(TextView textView, CheckBox checkBox)
        {
            this.checkBox = checkBox;
            this.textView = textView;

        }

        public CheckBox getCheckBox()
        {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox)
        {
            this.checkBox = checkBox;
        }

        public TextView getTextView()
        {
            return textView;
        }

        public void setTextView(TextView textView)
        {
            this.textView = textView;
        }

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //final Holder holder;
        Invites invite = (Invites) this.getItem(position);
        final CheckBox checkBox;
        TextView textView;
        TextView textView1;
        TextView textView2;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_invite, null);
            // well set up the ViewHolder

            textView = (TextView) convertView.findViewById(R.id.inviteEmail);

            checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxInvite);
            checkBox.setFocusable(false);
            checkBox.setFocusableInTouchMode(false);

            convertView.setTag(new ViewHolder(textView, checkBox));

            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Invites invite = (Invites) cb.getTag();
                    invite.setIsChecked(cb.isChecked());
                }
            });
        } else {
            // Because we use a ViewHolder, we avoid having to call
            // findViewById().
            ViewHolder viewHolder = (ViewHolder) convertView
                    .getTag();
            checkBox = viewHolder.getCheckBox();
            textView = viewHolder.getTextView();

        }

        checkBox.setTag(invite);


        checkBox.setChecked(invite.isChecked());
        checkBox.setText(invite.getName());
        textView.setText(invite.getEmail());

        return convertView;

    }



    @Override
    public int getPositionForSection(int section) {
        Log.d("ListView", "Get position for section");
        for (int i=0; i < this.getCount(); i++) {
            String item = itemList.get(i).getName().toLowerCase();
            if (item.charAt(0) == sections.charAt(section))
                return i;
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int arg0) {
        Log.d("ListView", "Get section");
        return 0;
    }

    @Override
    public Object[] getSections() {
        Log.d("ListView", "Get sections");
        String[] sectionsArr = new String[sections.length()];
        for (int i=0; i < sections.length(); i++)
            sectionsArr[i] = "" + sections.charAt(i);

        return sectionsArr;

    }

    public void sendselected(){

        if(!SelectedBox.isEmpty()){
            new sendInvites().execute();
        }else{
            Toast toast = Toast.makeText(activity, "Please select at least one friend", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    public class sendInvites extends AsyncTask<String, String, String> {

        int res =0;


        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(activity);
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



                Toast toast = Toast.makeText(activity, "Invites sent successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();






            } else {


                Toast toast = Toast.makeText(activity, "Error Sending , please try again!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();



            }

            super.onPostExecute(file);
        }



    }


}