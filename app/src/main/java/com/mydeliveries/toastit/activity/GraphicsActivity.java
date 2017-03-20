package com.mydeliveries.toastit.activity;

/**
 * Created by andrewcameron on 2016/01/25.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.adapter.GridViewAdapter;
import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.model.GridItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphicsActivity extends ActionBarActivity  {


    Toolbar toolbar;

    ProgressBar progressBar;

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting details screen layout
        setContentView(R.layout.activity_graphics_view);



        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);


        mGridView = (GridView) findViewById(R.id.gridView);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();


                editor.putString("URL",item.getUrl());

                editor.commit(); // commit changes

                //MainActivity mActivity= new MainActivity();
               // mActivity.imageactivate();


                finish();


            }
        });

        new getCollections().execute();
        mProgressBar.setVisibility(View.VISIBLE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }


    public class getCollections extends AsyncTask<String, Void, Integer> {



        @Override
        protected void onPreExecute() {


        }
        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {


                SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String User ="";
                String cat =  pref.getString("ImageTitle", null) ;

                String url = "http://www.toastit.co.za/api/design/backgroundimages/" + cat;

                System.out.println(url);

                // Creating volley request obj
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {
                                //Log.d("response", response.toString());
                                //hidePDialog();


                                try {


                                    JSONArray results = (JSONArray) response.get("images");
                                    GridItem item;
                                    for (int i = 0; i < results.length(); i++) {
                                        JSONObject obj = results.getJSONObject(i);
                                        String title = obj.getString("Name");
                                        String BackgroundCategories_ID = obj.getString("BackgroundCategories_ID");
                                        String ThumbnailURL = obj.getString("ThumbnailURL").replaceAll(" ","%20");

                                        if(title == null || title.equalsIgnoreCase("NULL")){
                                            title ="";
                                        }

                                        String url = obj.getString("URL").replaceAll(" ","%20");
                                        item = new GridItem();
                                        item.setTitle(title);
                                        item.setUrl("http://www.toastit.co.za/" + url);
                                        item.setImage("http://www.toastit.co.za/" + ThumbnailURL);

                                        mGridData.add(item);
                                    }


                                } catch (Exception e) {

                                }

                                mGridAdapter.setGridData(mGridData);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }
                }) {

                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "e031107c0d1ae937e3c12c30982966ff");
                        return headers;
                    }

                };

                int socketTimeout = 15000;//15 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                getRequest.setRetryPolicy(policy);

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(getRequest);



            }catch(Exception e){

            }



            return result;
        }


        @Override
        protected void onPostExecute(Integer result) {
            // closing progress dialog

            progressBar.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            super.onPostExecute(result);


        }



    }



    @Override
    public void onBackPressed() {

        finish();

    }
}