package com.mydeliveries.toastit.activity;

/**
 * Created by andrewcameron on 2016/01/25.
 */
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mydeliveries.toastit.R;
import com.squareup.picasso.Picasso;
import com.mydeliveries.toastit.adapter.GridViewAdapter;
import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.model.GridItem;
import com.mydeliveries.toastit.model.NavItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends ActionBarActivity {
    private static final int ANIM_DURATION = 600;
    private TextView titleTextView;
    private ImageView imageView;

    Toolbar toolbar;

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    ProgressBar progressBar;
    private String FEED_URL = "http://www.toastit.co.za/api/design/backgroundcategories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting details screen layout
        setContentView(R.layout.activity_details_view);


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


                editor.putString("ImageTitle",item.getId());

                editor.commit(); // commit changes

                Intent intent = new Intent(DetailsActivity.this, GraphicsActivity.class);
                //Start details activity
                startActivityForResult(intent, Activity.RESULT_CANCELED);
                //startActivity(intent);
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
                String url = "";
                url = FEED_URL;


                // Creating volley request obj
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {
                                //Log.d("response", response.toString());
                                //hidePDialog();


                                try {


                                    JSONArray results = (JSONArray) response.get("bk_categories");
                                    GridItem item;
                                    for (int i = 0; i < results.length(); i++) {
                                        JSONObject obj = results.getJSONObject(i);
                                        String title = obj.getString("Name");
                                        String id = obj.getString("ID");
                                        String url = obj.getString("URL").replaceAll(" ","%20");
                                        item = new GridItem();
                                        item.setTitle(title);
                                        item.setId(id);

                                        item.setImage("http://www.toastit.co.za/" + url);

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