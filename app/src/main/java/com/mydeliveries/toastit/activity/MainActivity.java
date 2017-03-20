package com.mydeliveries.toastit.activity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mydeliveries.toastit.adapter.MyAdapter;
import com.mydeliveries.toastit.app.AppController;
import com.mydeliveries.toastit.app.AppController.TrackerName;

import com.mydeliveries.toastit.fragments.BulkOrderFragment;
import com.mydeliveries.toastit.fragments.ContactFragment;
import com.mydeliveries.toastit.fragments.DesignFragment;
import com.mydeliveries.toastit.fragments.DisclaimerFragment;
import com.mydeliveries.toastit.fragments.FeedbackFragment;
import com.mydeliveries.toastit.fragments.HomeFragment;
import com.mydeliveries.toastit.fragments.InfoFragment;
import com.mydeliveries.toastit.fragments.InviteFragment;
import com.mydeliveries.toastit.fragments.OptionsFragment;
import com.mydeliveries.toastit.fragments.OrderFragment;
import com.mydeliveries.toastit.fragments.QualityFragment;
import com.mydeliveries.toastit.fragments.StepsFragment;
import com.mydeliveries.toastit.fragments.StyleFragment;
import com.mydeliveries.toastit.fragments.UserDetailsFragment;
import com.mydeliveries.toastit.model.NavItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.util.PermissionUtil;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity implements ComponentCallbacks2 {

    Toolbar toolbar;

    String TITLES[] = {"Home", "My Profile", "Start Designing", "Contacts Us", "Feedback", "Buy in Bulk", "Invite Friends", "Disclaimer"};
    int ICONS[] = {R.drawable.ic_home_white_24dp, R.drawable.ic_person_white_24dp, R.drawable.ic_touch_app_white_24dp, R.drawable.ic_error_outline_white_24dp, R.drawable.ic_create_white_24dp, R.drawable.ic_mail_white_24dp, R.drawable.ic_people_white_24dp, R.drawable.ic_description_white_24dp};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view
    String NAME = "";
    String EMAIL = "";
    int PROFILE = R.drawable.success;


    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;

    private static long back_pressed;



    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_STORAGE = 101;
    private static final int PERMISSIONS_REQUEST_PHONE = 102;
    private static final int PERMISSIONS_REQUEST_CAMERA = 103;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        requestPermission1();
        requestPermission2();
        requestPermission3();
        requestPermission4();


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Tracker t = ((AppController) this.getApplication()).getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("Main Activity");
        t.send(new HitBuilders.ScreenViewBuilder().build());

        loadDraws();

        new getPrice().execute();

        displayView(0);


    }


    public void loadDraws() {

        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String User = "";
        if (pref.getString("User", null) != null && (!pref.getString("User", null).equalsIgnoreCase(""))) {
            User = pref.getString("User", null);
            try {
                JSONObject jsonObjorder = new JSONObject(User);

                NAME = "Welcome " + jsonObjorder.getString("name") + " " + jsonObjorder.getString("surname");
                EMAIL = "Design your own T-Shirts!";


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {

            NAME = "Welcome User";
            EMAIL = "Design your own T-Shirts!";


        }


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    if (recyclerView.getChildPosition(child) == 1) {
                        displayView(0);
                    } else if (recyclerView.getChildPosition(child) == 2) {
                        displayView(1);
                    } else if (recyclerView.getChildPosition(child) == 3) {

                        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();

                        if (pref.getString("gotPrice", null) != null && (!pref.getString("gotPrice", null).equalsIgnoreCase(""))) {

                            if(pref.getString("gotPrice", null).equalsIgnoreCase("true")){
                                displayView(7);
                            }else{
                                Toast toast = Toast.makeText(getApplicationContext(), "Prices are still being loaded, Please try again.", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                new getPrice().execute();
                            }

                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(), "Prices are still being loaded, Please try again.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            new getPrice().execute();
                        }


                    } else if (recyclerView.getChildPosition(child) == 4) {
                        displayView(10);
                    } else if (recyclerView.getChildPosition(child) == 5) {
                        displayView(4);

                    } else if (recyclerView.getChildPosition(child) == 6) {

                        // displayView(9);
                        String address = "toastitmobile@gmail.com";
                        String subject = "Bulk Buy";
                        String body = "";
                        String chooserTitle = "T-shirt Order";

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + address));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                        emailIntent.putExtra(Intent.EXTRA_TITLE, chooserTitle);


                        startActivity(Intent.createChooser(emailIntent, "Bulk Order"));


                    } else if (recyclerView.getChildPosition(child) == 7) {


                        displayView(3);

                    } else if (recyclerView.getChildPosition(child) == 8) {
                        displayView(11);
                    }
                    //Toast.makeText(HomeActivity.this,"The Item Clicked is: "+recyclerView.getChildPosition(child),Toast.LENGTH_SHORT).show();
                    Drawer.closeDrawers();
                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State


        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void getPrice() {

        new getPrice().execute();
    }

    public void loadDrawsDisplay() {


        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();
    }

    public void loadDrawsHide() {


        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        NavItem nav = new NavItem();
        // Handle action bar actions click
        switch (item.getItemId()) {

            case android.R.id.home:
                String navi = nav.getPage();
                switch (navi) {
                    case "Home":
                        displayView(0);
                        return true;
                    case "Style":
                        displayView(7);
                        return true;
                    case "MapView":
                        displayView(3);
                        return true;
                    case "BizPage":
                        displayView(7);
                        return true;
                    case "Profile":
                        displayView(1);
                        return true;
                    case "MyList":
                        displayView(8);
                        return true;
                    case "Info":
                        displayView(9);
                        return true;
                    case "Login":
                        displayView(10);
                        return true;

                }
                return true;

            case R.id.action_home:
                // location found
                displayView(0);
                return true;
            case R.id.action_profile:
                // location found
                displayView(1);
                return true;
            case R.id.action_feedback:
                // location found
                displayView(4);
                return true;

            case R.id.action_invite:
                // help action
                displayView(3);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }


    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    public void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new UserDetailsFragment();
                break;
            case 2:
                fragment = new DesignFragment();
                break;
            case 3:
                fragment = new InviteFragment();
                break;
            case 4:
                fragment = new FeedbackFragment();
                break;
            case 5:
                fragment = new StepsFragment();
                break;
            case 6:
                fragment = new QualityFragment();
                break;
            case 7:
                fragment = new StyleFragment();
                break;
            case 8:
                fragment = new InfoFragment();
                break;
            case 9:
                fragment = new BulkOrderFragment();
                break;
            case 10:
                fragment = new ContactFragment();
                break;
            case 11:
                fragment = new DisclaimerFragment();
                break;
            case 12:
                fragment = new OrderFragment();
                break;
            case 13:
                fragment = new OptionsFragment();
                break;


            default:
                break;
        }

        if (fragment != null) {

            try {
                InputMethodManager input = (InputMethodManager) this
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {

            }

            FragmentManager fragmentManager = getSupportFragmentManager();


            fragmentManager.beginTransaction()

                    .replace(R.id.frame_container, fragment)
                    .commit();

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    @Override
    public void onBackPressed() {

        NavItem nav = new NavItem();
        String navi = nav.getPage();

        switch (navi) {
            case "HomePage":
                if (back_pressed + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                    SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();



                } else {

                    Toast toast = Toast.makeText(getBaseContext(), "Press back once again to exit application!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    back_pressed = System.currentTimeMillis();
                }
                return;
            case "Home":
                displayView(0);
                return;
            case "Style":
                displayView(7);
                return;
            case "MapView":
                displayView(3);
                return;
            case "BizPage":
                displayView(7);
                return;
            case "Profile":
                displayView(1);
                return;
            case "MyList":
                displayView(8);
                return;
            case "Info":
                displayView(9);
                return;
            case "Login":
                displayView(10);
                return;

        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        Runtime.getRuntime().gc();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == 1111) {

                try {

                    Uri selectedImage = data.getData();


                    SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();


                    editor.putString("Image", selectedImage.toString());

                    editor.commit(); // commit changes

                } catch (Exception e) {

                }

                FragmentManager fm = getSupportFragmentManager();

                DesignFragment fragment = (DesignFragment) fm.findFragmentById(R.id.frame_container);
                fragment.addImageCamera();
                //ivImage.setImageBitmap(thumbnail);

            } else if (requestCode == 1112) {


                Uri selectedImage = data.getData();


                SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();


                editor.putString("Image", selectedImage.toString());

                editor.commit(); // commit changes

                // ivImage.setImageBitmap(bm);
                FragmentManager fm = getSupportFragmentManager();

                DesignFragment fragment = (DesignFragment) fm.findFragmentById(R.id.frame_container);
                fragment.addImage();

            }
        } else {

        }

    }


    public void imageactivate() {
        // ivImage.setImageBitmap(bm);
        FragmentManager fm = getSupportFragmentManager();

        DesignFragment fragment = (DesignFragment) fm.findFragmentById(R.id.frame_container);
        fragment.SaveImages();
    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    public class getPrice extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                String url;
                url = "http://www.toastit.co.za/api/design/price";


                // Creating volley request obj
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {

                                System.out.println("response" + response.toString());
                                //hidePDialog();
                                SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();



                                try {


                                    JSONArray results = (JSONArray) response.get("prices");

                                    editor.putString("gotPrice", "true");
                                    editor.apply(); // apply changes

                                    for (int i = 0; i < results.length(); i++) {
                                        JSONObject obj = results.getJSONObject(i);
                                        String name = obj.getString("Name");
                                        String price = obj.getString("Price");
                                        String id = obj.getString("ID");

                                        if(name.equalsIgnoreCase("Adult Unisex V-Neck")) {
                                            editor.putString("TPrice_uni_v_veck", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Adult Unisex Round Neck")) {
                                            editor.putString("TPrice_uni_round_neck", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Kids Unisex Round Neck")) {
                                            editor.putString("TPrice_uni_kids", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Ladies V-Neck")) {
                                            editor.putString("TPrice_ladies_v", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Ladies Vest")) {
                                            editor.putString("TPrice_ladies_vest", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Mens Vest")) {
                                            editor.putString("TPrice_mens_vest", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Ladies Round-Neck")) {
                                            editor.putString("TPrice_ladies_round", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Ladies Racerback Vest")) {
                                            editor.putString("TPrice_ladies_razor", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Unisex Pull-Over Hoodie")) {
                                            editor.putString("TPrice_hoodie_pull", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Unisex Zip-Up Hoodie")) {
                                            editor.putString("TPrice_hoodie_zip", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Mug")) {
                                            editor.putString("TPrice_mug", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Short Sleeve Baby Grow")) {
                                            editor.putString("TPrice_baby_grow", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Ladies Sleevless Crop Top")) {
                                            editor.putString("TPrice_crop_top", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Leggings")) {
                                            editor.putString("TPrice_leggings", price);
                                            editor.apply(); // apply changes
                                        }
                                        if(name.equalsIgnoreCase("Mens Long Sleeve T-Shirt")) {
                                            editor.putString("TPrice_long_sleeve", price);
                                            editor.apply(); // apply changes
                                        }



                                    }


                                } catch (Exception e) {

                                }


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


            } catch (Exception e) {

            }


            return result;
        }


        @Override
        protected void onPostExecute(Integer result) {
            // closing progress dialog


            super.onPostExecute(result);


        }

    }



    /**
     * Release memory when the UI becomes hidden or when system resources become low.
     * @param level the memory-related event that was raised.
     */
    public void onTrimMemory(int level) {

        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */


                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:

                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:

                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                requestPermission2();
                requestPermission3();
                requestPermission4();

            }else{

            }
        }

        if (requestCode == PERMISSIONS_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                requestPermission1();
                requestPermission3();
                requestPermission4();
            }else{

            }
        }
        if (requestCode == PERMISSIONS_REQUEST_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                requestPermission1();
                requestPermission2();
                requestPermission4();

            }else{

            }
        }
        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                requestPermission1();
                requestPermission2();
                requestPermission3();

            }else{

            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    private void requestPermission1() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);


        }
    }


    private void requestPermission2() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_STORAGE);


        }
    }

    private void requestPermission3() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE);


        }
    }
    private void requestPermission4() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);

        }
    }
}
