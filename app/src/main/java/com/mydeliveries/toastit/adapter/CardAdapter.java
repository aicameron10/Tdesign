package com.mydeliveries.toastit.adapter;

/**
 * Created by andrewcameron on 15/06/10.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydeliveries.toastit.activity.MainActivity;
import com.mydeliveries.toastit.R;
import com.mydeliveries.toastit.model.DocItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    List<DocItem> mItems;
    Context context = null;



    //final Typeface face= Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/thincool.ttf");

    public CardAdapter() {
        super();
        mItems = new ArrayList<DocItem>();
        DocItem category = new DocItem();
        category.setName("Start Designing");
        category.setThumbnail(R.drawable.start);
        mItems.add(category);

        category = new DocItem();
        category.setName("Design Steps");
        category.setThumbnail(R.drawable.steps2);
        mItems.add(category);

        category = new DocItem();
        category.setName("Our Quality");
        category.setThumbnail(R.drawable.qua);
        mItems.add(category);



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        context = v.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


       final DocItem category = mItems.get(position);
        viewHolder.tvText.setText(category.getName());
        viewHolder.imgThumbnail.setImageResource(category.getThumbnail());
      
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(category.getName().equalsIgnoreCase("Start Designing")){

                    //hidePDialog();
                    SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    if (pref.getString("gotPrice", null) != null && (!pref.getString("gotPrice", null).equalsIgnoreCase(""))) {

                        if(pref.getString("gotPrice", null).equalsIgnoreCase("true")){
                            ((MainActivity) context).displayView(7);
                        }else{
                            Toast toast = Toast.makeText(context.getApplicationContext(), "Prices are still being loaded, Please try again.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            ((MainActivity) context).getPrice();
                        }

                    }else{
                        Toast toast = Toast.makeText(context.getApplicationContext(), "Prices are still being loaded, Please try again.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        ((MainActivity) context).getPrice();
                    }

                }

                if(category.getName().equalsIgnoreCase("Design Steps")){
                    ((MainActivity) context).displayView(5);
                }

                if(category.getName().equalsIgnoreCase("Our Quality")){
                    ((MainActivity) context).displayView(6);
                }


            }
        });


    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgThumbnail;
        public TextView tvText;

        public ViewHolder(View itemView) {
            super(itemView);

            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            tvText = (TextView)itemView.findViewById(R.id.tv_text);

            Typeface face = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/thincool.ttf");
            tvText.setTypeface(face);


        }
    }
}