package com.mydeliveries.toastit.adapter;

/**
 * Created by andrewcameron on 2016/01/25.
 */
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.mydeliveries.toastit.model.GridItem;
import com.mydeliveries.toastit.R;

public class GridViewAdapter extends ArrayAdapter<GridItem> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();
    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<GridItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            holder.idTextView = (TextView) row.findViewById(R.id.grid_item_id);
            holder.urlTextView = (TextView) row.findViewById(R.id.grid_item_url);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        GridItem item = mGridData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getTitle()));
        holder.idTextView.setText(item.getId());
        holder.urlTextView.setText(item.getUrl());
        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        return row;
    }
    static class ViewHolder {
        TextView titleTextView;
        TextView idTextView;
        TextView urlTextView;
        ImageView imageView;
    }
}