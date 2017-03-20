package com.mydeliveries.toastit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.activity.MainActivity;
import com.mydeliveries.toastit.adapter.GridViewAdapter;
import com.mydeliveries.toastit.app.AppController;

import com.mydeliveries.toastit.imageview.PhotoSortrView;
import com.mydeliveries.toastit.model.GridItem;
import com.mydeliveries.toastit.model.NavItem;
import com.mydeliveries.toastit.util.MultiColorPicker;
import com.mydeliveries.toastit.views.LoadingView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class DesignFragment extends Fragment {

    Toolbar toolbar;

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    ProgressBar progressBar;
    ProgressBar progressBarshow;
    //private String FEED_URL = "http://www.toastit.co.za/api/design/backgroundcategories";


    private PhotoSortrView photoSorterFront;

    private PhotoSortrView photoSorterBack;

    private final static String TAG = "TESTESTESTEST";

    // to take a picture
    private static final int CAMERA_PIC_REQUEST = 1111;
    private static final int GALLERY_PIC_REQUEST = 1112;

    String fontface = "";

    LoadingView loading;
    TextView userDesign;
    ImageView userText;

    ImageView userBackImage;
    String flipSide = "";

    Bitmap saveBmfront;
    Bitmap saveBmback;

    Bitmap processedBitmap;

    Bitmap returnedImage;
    Bitmap returnedImage1;
    Bitmap returnedImageCam;

    Bitmap rotatedBitmap;

    String ImageToBeSentfront;
    String ImageToBeSentback;

    EditText txttype;

    String fontt = "";

    RelativeLayout mainLayout;

    RelativeLayout mainLayoutBack;

    ImageView fade;

    boolean payBack = false;

    private MultiColorPicker multiColorPicker;

    View rootView;

    View formElementsView;

    View formElementsView1;
    View formElementsView2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_design, container, false);


        Tracker t = ((AppController) getActivity().getApplication()).getTracker(AppController.TrackerName.APP_TRACKER);
        t.setScreenName("Design Page");
        t.send(new HitBuilders.ScreenViewBuilder().build());


        NavItem nav = new NavItem();

        nav.setPage("Style");

        if(saveBmfront != null){
            saveBmfront.recycle();
        }
        if(saveBmback != null){
            saveBmback.recycle();
        }


        ((MainActivity) getActivity()).loadDrawsHide();


        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        //iv=(TouchImageView) rootView.findViewById(R.id.imageView);

        mainLayout = (RelativeLayout) rootView.findViewById(R.id.ImageSetFront);
        mainLayoutBack = (RelativeLayout) rootView.findViewById(R.id.ImageSetBack);
        progressBarshow = (ProgressBar) rootView.findViewById(R.id.progressBarshow);
        mainLayoutBack.setVisibility(View.GONE);
        progressBarshow.setVisibility(View.GONE);

        fade = (ImageView) rootView.findViewById(R.id.fade);
        fade.setVisibility(View.GONE);

        photoSorterFront = (PhotoSortrView) rootView.findViewById(R.id.photosortrFront);

        photoSorterBack = (PhotoSortrView) rootView.findViewById(R.id.photosortrBack);

        File myFileFlip = new File(Environment.getExternalStorageDirectory().getPath() + "/toastitImages/" + "flipScreenShot.png");

        if(myFileFlip.exists()){

            myFileFlip.delete();
        }


        final ImageView layout = (ImageView) rootView.findViewById(R.id.layout);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("payBack", "No");
        editor.apply(); // apply changes


        String cat = pref.getString("Selection", null);
        String colour = pref.getString("Tcolour", null);
        String size = pref.getString("Tsize", null);
        String fabric = pref.getString("Tfabric", null);
        switch (cat) {
            case "1":
                if (fabric.equalsIgnoreCase("Polyester")) {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_polyester);
                        fade.setVisibility(View.GONE);
                    }

                } else if (fabric.equalsIgnoreCase("Polycotton")) {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_polycotton);
                        fade.setVisibility(View.GONE);
                    }

                } else {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_white);
                        fade.setVisibility(View.GONE);
                    } else if (colour.equalsIgnoreCase("Black")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_black);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.black_fade);
                    } else if (colour.equalsIgnoreCase("Red")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_red);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.red_fade);
                    } else if (colour.equalsIgnoreCase("Navy Melange")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_navy);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.navy_fade);
                    } else if (colour.equalsIgnoreCase("Charcoal Melange")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_grey);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.grey_fade);
                    }
                }
                break;
            case "2":
                if (fabric.equalsIgnoreCase("Polycotton")) {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_v_polycotton);
                        fade.setVisibility(View.GONE);
                    }

                } else {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_v_cotton_white);
                        fade.setVisibility(View.GONE);
                    } else {
                        layout.setBackgroundResource(R.drawable.front_unisex_v_cotton_black);
                        fade.setVisibility(View.VISIBLE);
                    }

                }
                break;
            case "3":
                if (fabric.equalsIgnoreCase("Polyester")) {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_polyester);
                        fade.setVisibility(View.GONE);
                    }

                } else if (fabric.equalsIgnoreCase("Polycotton")) {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_polycotton);
                        fade.setVisibility(View.GONE);
                    }

                } else {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_white);
                        fade.setVisibility(View.GONE);
                    } else if (colour.equalsIgnoreCase("Black")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_black);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.black_fade);
                    } else if (colour.equalsIgnoreCase("Red")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_red);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.red_fade);
                    } else if (colour.equalsIgnoreCase("Navy Melange")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_navy);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.navy_fade);
                    } else if (colour.equalsIgnoreCase("Charcoal Melange")) {
                        layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_grey);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.grey_fade);
                    }
                }
                break;
            case "4":
                if (fabric.equalsIgnoreCase("Polycotton")) {

                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_v_neck_white);
                        fade.setVisibility(View.GONE);
                    } else if (colour.equalsIgnoreCase("Black")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_v_neck_black);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.black_fade);
                    } else if (colour.equalsIgnoreCase("Pink")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_v_neck_pink);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.pink_fade);
                    } else if (colour.equalsIgnoreCase("Sky Blue")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_v_neck_blue);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.blue_fade);
                    }

                }
                break;
            case "5":
                if (fabric.equalsIgnoreCase("Polycotton")) {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_vest_polycotton);
                        fade.setVisibility(View.GONE);

                        editor.putString("Tprice", pref.getString("TPrice_ladies_vest", ""));
                        editor.apply(); // apply changes
                    } else {

                        layout.setBackgroundResource(R.drawable.front_ladies_vest_polycotton_black);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.black_fade);

                        double tt5 = Double.parseDouble(pref.getString("TPrice_ladies_vest", ""));

                        final double tr5 = tt5 - 50;

                        editor.putString("Tprice", String.valueOf(tr5));
                        editor.apply(); // apply changes
                    }

                }
                break;
            case "6":
                if (fabric.equalsIgnoreCase("Polycotton")) {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_mens_vest_polycotton);
                        fade.setVisibility(View.GONE);
                    }

                } else {
                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_mens_vest_cotton_white);
                        fade.setVisibility(View.GONE);
                    } else {
                        layout.setBackgroundResource(R.drawable.front_mens_vest_cotton_black);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.black_fade);
                    }

                }
                break;
            case "7":
                if (fabric.equalsIgnoreCase("Cotton")) {

                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_crewneck_cotton_white);
                        fade.setVisibility(View.GONE);
                    } else if (colour.equalsIgnoreCase("Black")) {
                        layout.setBackgroundResource(R.drawable.ladies_template_crew_neck_black);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.black_fade);
                    } else if (colour.equalsIgnoreCase("Red")) {
                        layout.setBackgroundResource(R.drawable.ladies_template_crew_neck_red);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.red_fade);
                    } else if (colour.equalsIgnoreCase("Navy Melange")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_crewneck_cotton_navy);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.navy_fade);
                    } else if (colour.equalsIgnoreCase("Charcoal Melange")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_crewneck_cotton_grey);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.grey_fade);
                    }
                }
                break;
            case "8":
                if (fabric.equalsIgnoreCase("Polycotton")) {

                    if (colour.equalsIgnoreCase("White")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_racerback_white);
                        fade.setVisibility(View.GONE);
                    } else if (colour.equalsIgnoreCase("Black")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_racerback_black);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.black_fade);
                    } else if (colour.equalsIgnoreCase("Pink")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_racerback_pink);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.pink_fade);
                    } else if (colour.equalsIgnoreCase("Red")) {
                        layout.setBackgroundResource(R.drawable.front_ladies_racerback_red);
                        fade.setVisibility(View.VISIBLE);
                        fade.setBackgroundResource(R.drawable.red_fade);
                    }

                }
                break;
            case "12":
                if (fabric.equalsIgnoreCase("Polycotton")) {
                    layout.setBackgroundResource(R.drawable.front_baby_grow_poly_cotton);

                }
                break;
            case "11":
                if (fabric.equalsIgnoreCase("Mug")) {
                    if (size.equalsIgnoreCase("Full Mug")) {
                        layout.setBackgroundResource(R.drawable.full_mug_template);

                        double tt11 = Double.parseDouble(pref.getString("TPrice_mug", ""));

                        final double tr11 = tt11 + 30;

                        editor.putString("Tprice", String.valueOf(tr11));
                        editor.apply(); // apply changes

                    } else {
                        layout.setBackgroundResource(R.drawable.front_half_mug_template);
                        editor.putString("Tprice", pref.getString("TPrice_mug", ""));
                        editor.apply(); // apply changes
                    }

                }
                break;
            default:
                //nothing
                break;
        }


        final ImageView addtext = (ImageView) rootView.findViewById(R.id.addtext);
        final ImageView delete = (ImageView) rootView.findViewById(R.id.delete);
        final ImageView reset = (ImageView) rootView.findViewById(R.id.reset);
        final ImageView duplicate = (ImageView) rootView.findViewById(R.id.duplicate);

        final ImageView order = (ImageView) rootView.findViewById(R.id.order);


        flipSide = "Front";

        final ImageView flip = (ImageView) rootView.findViewById(R.id.flip);


        final ImageView background = (ImageView) rootView.findViewById(R.id.background);


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    takeScreenshotOrder();

                    RelativeLayout.LayoutParams layoutParams;

                    layoutParams = (RelativeLayout.LayoutParams) mainLayout
                            .getLayoutParams();
                    layoutParams.addRule(RelativeLayout.ABOVE, 0);

                    RelativeLayout.LayoutParams layoutParams1;

                    layoutParams1 = (RelativeLayout.LayoutParams) mainLayoutBack
                            .getLayoutParams();
                    layoutParams1.addRule(RelativeLayout.ABOVE, 0);

                    ImageToBeSentfront = "frontImage.png";
                    ImageToBeSentback = "backImage.png";

                    final ImageView img = new ImageView(getActivity());
                    final ImageView img1 = new ImageView(getActivity());


                    final ImageView layout = (ImageView) rootView.findViewById(R.id.layout);
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    String cat = pref.getString("Selection", null);
                    String colour = pref.getString("Tcolour", null);
                    String size = pref.getString("Tsize", null);
                    String fabric = pref.getString("Tfabric", null);
                    switch (cat) {
                        case "1":
                            if (fabric.equalsIgnoreCase("Polyester")) {
                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_unisex_crewneck_polyester));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_unisex_crewneck_polyester));

                            } else if (fabric.equalsIgnoreCase("Polycotton")) {
                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_unisex_crewneck_polycotton));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_unisex_crewneck_polycotton));

                            } else {
                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_crewneck_cotton));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_crewneck_cotton));
                            }
                            break;
                        case "2":
                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                if (colour.equalsIgnoreCase("White")) {
                                    img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_unisex_v_polycotton));
                                    img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_unisex_v_polycotton));
                                }

                            } else {
                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_v_cotton));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_v_cotton));

                            }
                            break;
                        case "3":
                            if (fabric.equalsIgnoreCase("Polyester")) {
                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_unisex_crewneck_polyester));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_unisex_crewneck_polyester));

                            } else if (fabric.equalsIgnoreCase("Polycotton")) {
                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_unisex_crewneck_polycotton));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_unisex_crewneck_polycotton));

                            } else {
                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_crewneck_cotton));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_crewneck_cotton));
                            }
                            break;
                        case "4":
                            if (fabric.equalsIgnoreCase("Polycotton")) {

                                if (colour.equalsIgnoreCase("White")) {
                                    img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_ladies_v_neck_white));
                                    img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_ladies_v_neck_white));
                                } else {
                                    img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_ladies_v_neck));
                                    img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_ladies_v_neck));
                                }


                            }
                            break;
                        case "5":
                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                if (colour.equalsIgnoreCase("White")) {
                                    img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_polycotton));
                                    img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_polycotton));
                                } else {

                                    img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_polycotton_black));
                                    img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_polycotton_black));
                                }

                            }
                            break;
                        case "6":
                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_mens_vest_polycotton));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_mens_vest_polycotton));

                            } else {
                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_mens_vest_cotton));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_mens_vest_cotton));

                            }
                            break;
                        case "7":
                            if (fabric.equalsIgnoreCase("Cotton")) {
                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_ladies_crewneck_cotton));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_ladies_crewneck_cotton));
                            }
                            break;
                        case "8":
                            if (fabric.equalsIgnoreCase("Polycotton")) {

                                img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_ladies_racerback));
                                img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_ladies_racerback));

                            }
                            break;
                        case "12":
                            img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_baby_grow_poly_cotton));
                            img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.mask_baby_grow_poly_cotton));
                            break;
                        case "11":
                            if (fabric.equalsIgnoreCase("Mug")) {
                                if (size.equalsIgnoreCase("Full Mug")) {
                                    img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.full_mug_mask));
                                    img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.full_mug_mask));

                                } else {
                                    img1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.front_half_mug_mask));
                                    img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.front_half_mug_mask));
                                }

                            }
                            break;
                        default:
                            //nothing
                            break;
                    }


                    mainLayout.setVisibility(View.VISIBLE);
                    mainLayoutBack.setVisibility(View.VISIBLE);

                    mainLayout.addView(img);
                    mainLayoutBack.addView(img1);


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());

                    // set title
                    alertDialogBuilder.setTitle("Order");
                    // Use an EditText view to get user input.
                    alertDialogBuilder.setMessage("Finished Designing? All designs are reset once you place order!");

                    // set dialog message
                    alertDialogBuilder

                            .setCancelable(true)
                            .setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    saveImage();


                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing

                                    mainLayout.removeView(img);
                                    mainLayoutBack.removeView(img1);
                                    mainLayoutBack.setVisibility(View.GONE);

                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                } catch (Exception e) {

                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i(TAG,"Trash clicked");
                //Log.i(TAG, "Array size is: " + mViewsArray.size());
                try {

                    if (flipSide.equalsIgnoreCase("Front")) {
                        photoSorterFront.removeImage();
                    } else if (flipSide.equalsIgnoreCase("Back")) {
                        photoSorterBack.removeImage();
                    }
                } catch (Exception e) {

                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set title
                alertDialogBuilder.setTitle("Clear design");
                // Use an EditText view to get user input.


                // set dialog message
                alertDialogBuilder

                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                photoSorterFront.removeAllImages();
                                photoSorterBack.removeAllImages();


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing

                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });


        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Select An Image:");
                final CharSequence[] chars = {"Collection", "Camera", "Album"};
                builder.setItems(chars, new android.content.DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 0) {

                                    showCollections();

                                    //Intent intent = new Intent(getActivity(), DetailsActivity.class);
                                    //Start details activity
                                    //startActivity(intent);
                                } else if (which == 1) {
                                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    getActivity().startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                                } else if (which == 2) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                    getActivity().startActivityForResult(intent, GALLERY_PIC_REQUEST);
                                }
                                dialog.dismiss();
                            }

                        }
                );
                builder.show();

            }
        });


        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String fabric = pref.getString("Tfabric", null);

                takeScreenshotFlip();

                if (flipSide.equalsIgnoreCase("Front") && !fabric.equalsIgnoreCase("Mug")) {


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());

                    // set title
                    alertDialogBuilder.setTitle("Extra");
                    // Use an EditText view to get user input.


                    // set dialog message
                    alertDialogBuilder

                            .setCancelable(true)
                            .setMessage("Please note that an additional charge of R80 is applicable for the back print to be added, Press OK to accept and add the cost to the order , You can remove the option on the final order checkout screen if you change your mind.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    flipSide = "Back";

                                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();

                                    editor.putString("payBack", "Yes");
                                    editor.apply(); // apply changes

                                    String cat = pref.getString("Selection", null);
                                    String colour = pref.getString("Tcolour", null);
                                    String size = pref.getString("Tsize", null);
                                    String fabric = pref.getString("Tfabric", null);
                                    switch (cat) {
                                        case "1":
                                            if (fabric.equalsIgnoreCase("Polyester")) {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_polyester);
                                                    fade.setVisibility(View.GONE);
                                                }

                                            } else if (fabric.equalsIgnoreCase("Polycotton")) {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_v_polycotton);
                                                    fade.setVisibility(View.GONE);
                                                }

                                            } else {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_cotton_white);
                                                    fade.setVisibility(View.GONE);
                                                } else if (colour.equalsIgnoreCase("Black")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_cotton_black);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.black_fade);
                                                } else if (colour.equalsIgnoreCase("Red")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_cotton_red);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.red_fade);
                                                } else if (colour.equalsIgnoreCase("Navy Melange")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_cotton_navy);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.navy_fade);
                                                } else if (colour.equalsIgnoreCase("Charcoal Melange")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_cotton_grey);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.grey_fade);
                                                }
                                            }
                                            break;
                                        case "2":
                                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_v_polycotton);
                                                    fade.setVisibility(View.GONE);
                                                }

                                            } else {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_v_cotton_white);
                                                    fade.setVisibility(View.GONE);
                                                } else {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_v_cotton_black);
                                                    fade.setVisibility(View.VISIBLE);
                                                }

                                            }
                                            break;
                                        case "3":
                                            if (fabric.equalsIgnoreCase("Polyester")) {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_polyester);
                                                    fade.setVisibility(View.GONE);
                                                }

                                            } else if (fabric.equalsIgnoreCase("Polycotton")) {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_v_polycotton);
                                                    fade.setVisibility(View.GONE);
                                                }

                                            } else {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_cotton_white);
                                                    fade.setVisibility(View.GONE);
                                                } else if (colour.equalsIgnoreCase("Black")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_cotton_black);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.black_fade);
                                                } else if (colour.equalsIgnoreCase("Red")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_cotton_red);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.red_fade);
                                                } else if (colour.equalsIgnoreCase("Navy Melange")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_cotton_navy);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.navy_fade);
                                                } else if (colour.equalsIgnoreCase("Charcoal Melange")) {
                                                    layout.setBackgroundResource(R.drawable.back_unisex_crewneck_cotton_grey);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.grey_fade);
                                                }
                                            }
                                            break;
                                        case "4":
                                            if (fabric.equalsIgnoreCase("Polycotton")) {

                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_v_neck_white);
                                                    fade.setVisibility(View.GONE);
                                                } else if (colour.equalsIgnoreCase("Black")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_v_neck_black);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.black_fade);
                                                } else if (colour.equalsIgnoreCase("Pink")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_v_neck_pink);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.pink_fade);
                                                } else if (colour.equalsIgnoreCase("Sky Blue")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_v_neck_blue);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.blue_fade);
                                                }

                                            }
                                            break;
                                        case "5":
                                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_vest_polycotton);
                                                    fade.setVisibility(View.GONE);
                                                } else {

                                                    layout.setBackgroundResource(R.drawable.back_ladies_vest_polycotton_black);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.black_fade);
                                                }

                                            }
                                            break;
                                        case "6":
                                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_mens_vest_polycotton);
                                                    fade.setVisibility(View.GONE);
                                                }

                                            } else {
                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_mens_vest_cotton_white);
                                                    fade.setVisibility(View.GONE);
                                                } else {
                                                    layout.setBackgroundResource(R.drawable.back_mens_vest_cotton_black);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.black_fade);
                                                }

                                            }
                                            break;
                                        case "7":
                                            if (fabric.equalsIgnoreCase("Cotton")) {

                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_crewneck_cotton_white);
                                                    fade.setVisibility(View.GONE);
                                                } else if (colour.equalsIgnoreCase("Black")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_crewneck_cotton_black);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.black_fade);
                                                } else if (colour.equalsIgnoreCase("Red")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_crewneck_cotton_red);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.red_fade);
                                                } else if (colour.equalsIgnoreCase("Navy Melange")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_crewneck_cotton_navy);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.navy_fade);
                                                } else if (colour.equalsIgnoreCase("Charcoal Melange")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_crewneck_cotton_grey);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.grey_fade);
                                                }
                                            }
                                            break;
                                        case "8":
                                            if (fabric.equalsIgnoreCase("Polycotton")) {

                                                if (colour.equalsIgnoreCase("White")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_racerback_white);
                                                    fade.setVisibility(View.GONE);
                                                } else if (colour.equalsIgnoreCase("Black")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_racerback_black);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.black_fade);
                                                } else if (colour.equalsIgnoreCase("Pink")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_racerback_pink);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.pink_fade);
                                                } else if (colour.equalsIgnoreCase("Red")) {
                                                    layout.setBackgroundResource(R.drawable.back_ladies_racerback_red);
                                                    fade.setVisibility(View.VISIBLE);
                                                    fade.setBackgroundResource(R.drawable.red_fade);
                                                }

                                            }
                                            break;
                                        case "12":
                                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                                layout.setBackgroundResource(R.drawable.back_baby_grow_poly_cotton);

                                            }
                                            break;
                                        case "11":
                                            Toast toast = Toast.makeText(getActivity(), "Full Mug to print all away around", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            break;
                                        default:
                                            //nothing
                                            break;
                                    }

                                    mainLayout.setVisibility(View.GONE);
                                    mainLayoutBack.setVisibility(View.VISIBLE);


                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing

                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                } else if (flipSide.equalsIgnoreCase("Back")) {

                    flipSide = "Front";


                    String cat = pref.getString("Selection", null);
                    String colour = pref.getString("Tcolour", null);
                    String size = pref.getString("Tsize", null);

                    switch (cat) {
                        case "1":
                            if (fabric.equalsIgnoreCase("Polyester")) {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_polyester);
                                    fade.setVisibility(View.GONE);
                                }

                            } else if (fabric.equalsIgnoreCase("Polycotton")) {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_polycotton);
                                    fade.setVisibility(View.GONE);
                                }

                            } else {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_white);
                                    fade.setVisibility(View.GONE);
                                } else if (colour.equalsIgnoreCase("Black")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_black);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.black_fade);
                                } else if (colour.equalsIgnoreCase("Red")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_red);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.red_fade);
                                } else if (colour.equalsIgnoreCase("Navy Melange")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_navy);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.navy_fade);

                                } else if (colour.equalsIgnoreCase("Charcoal Melange")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_grey);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.grey_fade);
                                }
                            }
                            break;
                        case "2":
                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_v_polycotton);
                                    fade.setVisibility(View.GONE);
                                }

                            } else {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_v_cotton_white);
                                    fade.setVisibility(View.GONE);
                                } else {
                                    layout.setBackgroundResource(R.drawable.front_unisex_v_cotton_black);
                                    fade.setVisibility(View.VISIBLE);
                                }

                            }
                            break;
                        case "3":
                            if (fabric.equalsIgnoreCase("Polyester")) {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_polyester);
                                    fade.setVisibility(View.GONE);
                                }

                            } else if (fabric.equalsIgnoreCase("Polycotton")) {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_polycotton);
                                    fade.setVisibility(View.GONE);
                                }

                            } else {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_white);
                                    fade.setVisibility(View.GONE);
                                } else if (colour.equalsIgnoreCase("Black")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_black);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.black_fade);
                                } else if (colour.equalsIgnoreCase("Red")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_red);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.red_fade);
                                } else if (colour.equalsIgnoreCase("Navy Melange")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_navy);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.navy_fade);

                                } else if (colour.equalsIgnoreCase("Charcoal Melange")) {
                                    layout.setBackgroundResource(R.drawable.front_unisex_crewneck_cotton_grey);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.grey_fade);
                                }
                            }
                            break;
                        case "4":
                            if (fabric.equalsIgnoreCase("Polycotton")) {

                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_v_neck_white);
                                    fade.setVisibility(View.GONE);
                                } else if (colour.equalsIgnoreCase("Black")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_v_neck_black);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.black_fade);
                                } else if (colour.equalsIgnoreCase("Pink")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_v_neck_pink);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.pink_fade);
                                } else if (colour.equalsIgnoreCase("Sky Blue")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_v_neck_blue);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.blue_fade);
                                }

                            }
                            break;
                        case "5":
                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_vest_polycotton);
                                    fade.setVisibility(View.GONE);
                                } else {

                                    layout.setBackgroundResource(R.drawable.front_ladies_vest_polycotton_black);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.black_fade);
                                }

                            }
                            break;
                        case "6":
                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_mens_vest_polycotton);
                                    fade.setVisibility(View.GONE);
                                }

                            } else {
                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_mens_vest_cotton_white);
                                    fade.setVisibility(View.GONE);
                                } else {
                                    layout.setBackgroundResource(R.drawable.front_mens_vest_cotton_black);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.black_fade);
                                }

                            }
                            break;
                        case "7":
                            if (fabric.equalsIgnoreCase("Cotton")) {

                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_crewneck_cotton_white);
                                    fade.setVisibility(View.GONE);
                                } else if (colour.equalsIgnoreCase("Black")) {
                                    layout.setBackgroundResource(R.drawable.ladies_template_crew_neck_black);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.black_fade);
                                } else if (colour.equalsIgnoreCase("Red")) {
                                    layout.setBackgroundResource(R.drawable.ladies_template_crew_neck_red);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.red_fade);
                                } else if (colour.equalsIgnoreCase("Navy Melange")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_crewneck_cotton_navy);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.navy_fade);
                                } else if (colour.equalsIgnoreCase("Charcoal Melange")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_crewneck_cotton_grey);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.grey_fade);
                                }
                            }
                            break;
                        case "8":
                            if (fabric.equalsIgnoreCase("Polycotton")) {

                                if (colour.equalsIgnoreCase("White")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_racerback_white);
                                    fade.setVisibility(View.GONE);
                                } else if (colour.equalsIgnoreCase("Black")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_racerback_black);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.black_fade);
                                } else if (colour.equalsIgnoreCase("Pink")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_racerback_pink);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.pink_fade);
                                } else if (colour.equalsIgnoreCase("Red")) {
                                    layout.setBackgroundResource(R.drawable.front_ladies_racerback_red);
                                    fade.setVisibility(View.VISIBLE);
                                    fade.setBackgroundResource(R.drawable.red_fade);
                                }

                            }
                            break;
                        case "12":
                            if (fabric.equalsIgnoreCase("Polycotton")) {
                                layout.setBackgroundResource(R.drawable.front_baby_grow_poly_cotton);

                            }
                            break;
                        case "11":
                            if (fabric.equalsIgnoreCase("Mug")) {
                                if (size.equalsIgnoreCase("Full Mug")) {
                                    layout.setBackgroundResource(R.drawable.full_mug_template);

                                } else {
                                    layout.setBackgroundResource(R.drawable.front_half_mug_template);
                                }

                            }
                            break;
                        default:
                            //nothing
                            break;
                    }
                    mainLayout.setVisibility(View.VISIBLE);
                    mainLayoutBack.setVisibility(View.GONE);
                } else {
                    Toast toast = Toast.makeText(getActivity(), "This option not available for this item", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });

        duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flipSide.equalsIgnoreCase("Front")) {
                    photoSorterFront.DuplicateImage(getActivity());
                } else if (flipSide.equalsIgnoreCase("Back")) {
                    photoSorterBack.DuplicateImage(getActivity());
                }

            }
        });


        addtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                formElementsView = inflater.inflate(R.layout.form_elements_add_text,
                        null, false);

                txttype = (EditText) formElementsView.findViewById(R.id.texttype);
                multiColorPicker = (MultiColorPicker) formElementsView.findViewById(R.id.multiColorPicker);


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity()).setView(formElementsView);
                LayoutInflater inflater1 = getActivity().getLayoutInflater();

                new addFonts().execute();

                alertDialogBuilder

                        .setCancelable(true)

                        .setPositiveButton("Add Text", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                if (txttype.getText().toString().equals("")) {
                                    Toast toast = Toast.makeText(getActivity(), "Please enter some text", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                } else if (fontt.equalsIgnoreCase(" ") || fontt == null || fontt.equalsIgnoreCase("") || fontt.equalsIgnoreCase("null")) {
                                    Toast toast = Toast.makeText(getActivity(), "Please choose a font", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                } else {

                                    int color = multiColorPicker.getColor();
                                    //String rgbString = "R: " + Color.red(color) + " B: " + Color.blue(color) + " G: " + Color.green(color);


                                    processedBitmap = ProcessingBitmap(txttype.getText().toString(), color, fontface);

                                    Drawable draw = new BitmapDrawable(getResources(), processedBitmap);

                                    if (flipSide.equalsIgnoreCase("Front")) {
                                        photoSorterFront.addImages(getActivity(), draw);
                                    } else if (flipSide.equalsIgnoreCase("Back")) {
                                        photoSorterBack.addImages(getActivity(), draw);
                                    }


                                    dialog.dismiss();
                                    dialog.cancel();

                                }


                            }
                        })
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.dismiss();
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });


        return rootView;
    }


    public void saveImage() {

        mainLayout.setDrawingCacheEnabled(true);

        mainLayout.buildDrawingCache(true);

        saveBmfront = Bitmap.createBitmap(mainLayout.getDrawingCache());

        mainLayout.setDrawingCacheEnabled(false);


        mainLayoutBack.setDrawingCacheEnabled(true);

        mainLayoutBack.buildDrawingCache(true);

        saveBmback = Bitmap.createBitmap(mainLayoutBack.getDrawingCache());

        mainLayoutBack.setDrawingCacheEnabled(false);


        String folder_main = "ToastItImages";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ToastItImages/" + ImageToBeSentfront);
        File file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/ToastItImages/" + ImageToBeSentback);
        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            saveBmfront.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.close();


            file1.createNewFile();
            FileOutputStream ostream1 = new FileOutputStream(file1);
            saveBmback.compress(Bitmap.CompressFormat.PNG, 100, ostream1);
            ostream1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();

        if(saveBmfront != null){
            saveBmfront.recycle();
        }
        if(saveBmback != null){
            saveBmback.recycle();
        }
        if(processedBitmap != null){
            processedBitmap.recycle();
        }
        if(returnedImage != null){
            returnedImage.recycle();
        }
        if(rotatedBitmap != null){
            rotatedBitmap.recycle();
        }
        if(returnedImage1 != null){
            returnedImage1.recycle();
        }
        if(returnedImageCam != null){
            returnedImageCam.recycle();
        }
        ((MainActivity) getActivity()).displayView(12);

    }

    public void addCollection() {
        //SaveImages();

        try {

            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            String cat = pref.getString("FILENAME", null);

            //Bitmap bb = StringToBitMap(cat);
            Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/ToastItImages/" + cat));


            InputStream is;
            is = getActivity().getContentResolver().openInputStream(uri);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            //BitmapFactory.decodeStream(bis,null,opts);
            BitmapFactory.decodeStream(is, null, opts);

            //The new size we want to scale to

            //The new size we want to scale to
            final int REQUIRED_SIZE = 5000;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (opts.outWidth / scale / 2 >= REQUIRED_SIZE || opts.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            Log.i(TAG, "Scale is: " + scale);
            opts.inSampleSize = scale;
            opts.inJustDecodeBounds = false;
            is = null;
            System.gc();
            InputStream is2 = getActivity().getContentResolver().openInputStream(uri);


           returnedImage = BitmapFactory.decodeStream(is2, null, opts);
            Drawable draw = new BitmapDrawable(getResources(), returnedImage);

            if (flipSide.equalsIgnoreCase("Front")) {
                photoSorterFront.addImages(getActivity(), draw);
            } else if (flipSide.equalsIgnoreCase("Back")) {
                photoSorterBack.addImages(getActivity(), draw);
            }

            progressBarshow.setVisibility(View.GONE);


        } catch (Exception e) {

        }

    }

    String fileName = null;

    public void SaveImages() {

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        String cat = pref.getString("URL", null);
        String folder_main = "ToastItImages";

        String[] parts = cat.split("/");
        String part1 = parts[3];
        String part2 = parts[4];

        String namefile = part1 + "_" + part2;

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        Picasso.with(getActivity()).load(cat).into(target);
        fileName = namefile + ".png";

        editor.putString("FILENAME", fileName);

        editor.commit(); // commit changes


    }

    Target target = new Target() {

        @Override
        public void onPrepareLoad(Drawable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {

            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ToastItImages/" + fileName);
            try {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                ostream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            addCollection();
        }

        @Override
        public void onBitmapFailed(Drawable arg0) {
            // TODO Auto-generated method stub

        }

    };


    public void addImageCamera() {

        try {

            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            String cat = pref.getString("Image", null);

            //Bitmap bb = StringToBitMap(cat);

            System.out.println("This is add ing image" + cat);

            InputStream is;
            is = getActivity().getContentResolver().openInputStream(Uri.parse(cat));
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            //BitmapFactory.decodeStream(bis,null,opts);
            BitmapFactory.decodeStream(is, null, opts);

            //The new size we want to scale to

            //The new size we want to scale to
            final int REQUIRED_SIZE = 5000;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (opts.outWidth / scale / 2 >= REQUIRED_SIZE || opts.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            Log.i(TAG, "Scale is: " + scale);
            opts.inSampleSize = scale;
            opts.inJustDecodeBounds = false;
            is = null;
            System.gc();
            InputStream is2 = getActivity().getContentResolver().openInputStream(Uri.parse(cat));

            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            returnedImageCam = BitmapFactory.decodeStream(is2, null, opts);
            rotatedBitmap = Bitmap.createBitmap(returnedImageCam, 0, 0, returnedImageCam.getWidth(), returnedImageCam.getHeight(), matrix, true);
            Drawable draw = new BitmapDrawable(getResources(), rotatedBitmap);

            if (flipSide.equalsIgnoreCase("Front")) {
                photoSorterFront.addImages(getActivity(), draw);
            } else if (flipSide.equalsIgnoreCase("Back")) {
                photoSorterBack.addImages(getActivity(), draw);
            }


        } catch (Exception e) {

        }

    }




    private void takeScreenshotOrder() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        String folder_main = "ToastItImages";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().getPath() + "/ToastItImages/" + "orderScreenShot.png";



            // create bitmap screen capture
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }


    private void takeScreenshotFlip() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        String folder_main = "ToastItImages";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().getPath() + "/ToastItImages/" + "flipScreenShot.png";



            // create bitmap screen capture
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    public void addImage() {

        try {

            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            String cat = pref.getString("Image", null);

            //Bitmap bb = StringToBitMap(cat);

            System.out.println("This is add ing image" + cat);

            InputStream is;
            is = getActivity().getContentResolver().openInputStream(Uri.parse(cat));
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            //BitmapFactory.decodeStream(bis,null,opts);
            BitmapFactory.decodeStream(is, null, opts);

            //The new size we want to scale to

            //The new size we want to scale to
            final int REQUIRED_SIZE = 5000;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (opts.outWidth / scale / 2 >= REQUIRED_SIZE || opts.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            Log.i(TAG, "Scale is: " + scale);
            opts.inSampleSize = scale;
            opts.inJustDecodeBounds = false;
            is = null;
            System.gc();
            InputStream is2 = getActivity().getContentResolver().openInputStream(Uri.parse(cat));


            returnedImage1 = BitmapFactory.decodeStream(is2, null, opts);
            Drawable draw = new BitmapDrawable(getResources(), returnedImage1);

            if (flipSide.equalsIgnoreCase("Front")) {
                photoSorterFront.addImages(getActivity(), draw);
            } else if (flipSide.equalsIgnoreCase("Back")) {
                photoSorterBack.addImages(getActivity(), draw);
            }



        } catch (Exception e) {

        }

    }


    class addFonts extends AsyncTask<String, String, String> {

        LinearLayout mainLayout1 = (LinearLayout) formElementsView.findViewById(R.id.fonts);
        LinearLayout linearlayout1 = new LinearLayout(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            linearlayout1.setOrientation(LinearLayout.HORIZONTAL);

            try {

                String[] elements = {"5thgrader", "42", "1942", "a_song_for_jennifer", "A_Stitch_Plus_Nine", "absender1", "Age_To_Age", "AlphaFridgeMagnetsAllCaps", "Ampad_Brush", "ARDESTINE", "armalite", "BasicScratch", "Butter_Finger", "buttons", "buttonsi", "Call_me_maybe", "cherrycoke", "CollegeMovie", "collegiateHeavyOutline Medium", "Comic_Strip", "Cookies", "Eyeliner_Tattoo", "FaktosContour", "garden_fresh_tomatoes", "heart", "Hollywood_Capital", "Hollywood_Capital_Hills_(Final)", "mara2v2", "Monkey_Snake", "offshore", "old_stamper", "Quirky_Messy", "RAGE", "Sketch_College", "Skinny", "Tatida_versal"};


                for (String s : elements) {

                    final TextView font = new TextView(getActivity());

                    final float scale = getResources().getDisplayMetrics().density;
                    int dpWidthInPx = (int) (60 * scale);
                    int dpHeightInPx = (int) (60 * scale);

                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(dpWidthInPx, dpHeightInPx);
                    parms.setMargins(0, 0, 10, 0);
                    font.setLayoutParams(parms);

                    Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/" + s + ".ttf");

                    String msg = "ABC<br/>DEF";


                    font.setText(Html.fromHtml(msg));
                    font.setTypeface(face);
                    font.setTextSize(18);
                    font.setTextColor(getActivity().getResources().getColor(R.color.white));
                    font.setTag(s);
                    font.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            TextView iv = (TextView) v;
                            fontt = iv.getTag().toString();

                            Typeface face1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/" + fontt + ".ttf");
                            txttype.setTypeface(face1);
                            fontface = iv.getTag().toString();
                            //Toast toast = Toast.makeText(getActivity(), iv.getTag().toString(), Toast.LENGTH_SHORT);
                            //toast.setGravity(Gravity.CENTER, 0, 0);
                            //toast.show();
                        }
                    });


                    linearlayout1.addView(font);

                }
            } catch (Exception e) {

            }


        }

        /**
         * getting Places JSON
         */
        protected String doInBackground(String... args) {
            // creating Places class object


            return null;
        }


        protected void onPostExecute(String file_url) {


            mainLayout1.addView(linearlayout1);


            super.onPostExecute(file_url);
        }


    }


    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    private Bitmap ProcessingBitmap(String txt, int clr, String s) {
        Bitmap image = null;

        try {
            Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/" + s + ".ttf");

            Paint paint = new Paint();
            paint.setTextSize(200);
            paint.setColor(clr);
            paint.setTypeface(face);
            paint.setTextAlign(Paint.Align.LEFT);
            float baseline = -paint.ascent(); // ascent() is negative
            int width = (int) (paint.measureText(txt) + 0.5f); // round
            int height = (int) (baseline + paint.descent() + 0.5f);
            image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(image);
            canvas.drawText(txt, 0, baseline, paint);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return image;

    }

    public void showCollectionsDetail() {


        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        formElementsView2 = inflater.inflate(R.layout.activity_graphics_view,
                null, false);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity(), R.style.DialogTheme).setView(formElementsView2);
        LayoutInflater inflater1 = getActivity().getLayoutInflater();
        View view = inflater1.inflate(R.layout.tool_bar_collect, null);


        ImageView backarrow = (ImageView) view
                .findViewById(R.id.backarrow);
        alertDialogBuilder.setCustomTitle(view);

        final Dialog dialog;

        dialog = alertDialogBuilder.create();


        //dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        dialog.show();

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        mGridView = (GridView) formElementsView2.findViewById(R.id.gridView);

        loading = (LoadingView) formElementsView2.findViewById(R.id.loading_view);
        loading.setVisibility(View.VISIBLE);
        loading.startAnimating();

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();


                editor.putString("URL", item.getUrl());

                editor.commit(); // commit changes
                progressBarshow.setVisibility(View.VISIBLE);
                SaveImages();
                dialog.dismiss();


            }
        });

        new getCollectionsDetail().execute();

    }

    public void showCollections() {


        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        formElementsView1 = inflater.inflate(R.layout.activity_details_view,
                null, false);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity(), R.style.DialogTheme).setView(formElementsView1);
        LayoutInflater inflater1 = getActivity().getLayoutInflater();
        View view = inflater1.inflate(R.layout.tool_bar_collect, null);

        ImageView backarrow = (ImageView) view
                .findViewById(R.id.backarrow);
        alertDialogBuilder.setCustomTitle(view);

        final AlertDialog dialog;

        dialog = alertDialogBuilder.create();


        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        dialog.show();

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        mGridView = (GridView) formElementsView1.findViewById(R.id.gridView);
        loading = (LoadingView) formElementsView1.findViewById(R.id.loading_view);
        loading.setVisibility(View.VISIBLE);
        loading.startAnimating();
        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();


                editor.putString("ImageTitle", item.getId());

                editor.commit(); // commit changes


                showCollectionsDetail();
                dialog.dismiss();
                //Intent intent = new Intent(DetailsActivity.this, GraphicsActivity.class);
                //Start details activity
                //startActivityForResult(intent, Activity.RESULT_CANCELED);
                //startActivity(intent);


            }
        });

        new getCollections().execute();


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
                url = "http://www.toastit.co.za/api/design/backgroundcategories";


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
                                        String url = obj.getString("URL").replaceAll(" ", "%20");
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


            } catch (Exception e) {

            }


            return result;
        }


        @Override
        protected void onPostExecute(Integer result) {
            // closing progress dialog

            loading.setVisibility(View.GONE);
            loading.stopAnimating();
            super.onPostExecute(result);


        }


    }


    public class getCollectionsDetail extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {


                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String User = "";
                String cat = pref.getString("ImageTitle", null);

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
                                        String ThumbnailURL = obj.getString("ThumbnailURL").replaceAll(" ", "%20");

                                        if (title == null || title.equalsIgnoreCase("NULL")) {
                                            title = "";
                                        }

                                        String url = obj.getString("URL").replaceAll(" ", "%20");
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


            } catch (Exception e) {

            }


            return result;
        }


        @Override
        protected void onPostExecute(Integer result) {
            // closing progress dialog

            loading.setVisibility(View.GONE);
            loading.stopAnimating();
            super.onPostExecute(result);


        }


    }

    @Override
    public void onDestroy() {
        //android.os.Process.killProcess(android.os.Process.myPid());

        super.onDestroy();
        Runtime.getRuntime().gc();
        if(saveBmfront != null){
            saveBmfront.recycle();
        }
        if(saveBmback != null){
            saveBmback.recycle();
        }


    }

    @Override
    public void onStop() {
        super.onStop();

        Runtime.getRuntime().gc();
        if(saveBmfront != null){
            saveBmfront.recycle();
        }
        if(saveBmback != null){
            saveBmback.recycle();
        }


    }

}
