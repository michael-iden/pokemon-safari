package com.magnetic.pokemonsafari;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by michael.iden on 8/24/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return labels.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

//            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.pokedex_element, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.title);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.image);
            textViewAndroid.setText(labels[position]);
            imageViewAndroid.setImageResource(R.drawable.questionmark);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }

    // references to our images
    private String[] labels = {
            "No. 001 - ???",
            "No. 002 - ???",
            "No. 003 - ???",
            "No. 004 - ???",
            "No. 005 - ???",
            "No. 006 - ???",
            "No. 007 - ???",
            "No. 008 - ???",
            "No. 009 - ???",
            "No. 010 - ???",
            "No. 011 - ???",
            "No. 012 - ???",
            "No. 013 - ???"
    };
}
