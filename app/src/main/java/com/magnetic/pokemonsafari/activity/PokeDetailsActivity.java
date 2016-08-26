package com.magnetic.pokemonsafari.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import us.feras.ecogallery.EcoGallery;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.magnetic.pokemonsafari.R;

public class PokeDetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        int number = extras.getInt("pokemonNumber");

        EcoGallery ecoGallery = (EcoGallery) findViewById(R.id.gallery);
        ecoGallery.setAdapter(new ImageAdapter2(this));
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//    }
}

class ImageAdapter2 extends BaseAdapter {
    private Context context;

    ImageAdapter2(Context context) {
        this.context = context;
    }

    public int getCount() {
        return 3;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Not using convertView for simplicity. You should probably use it in real application to get better performance.
        ImageView contentDetailsView;

        if (convertView == null) {
            contentDetailsView = new ImageView(context);
        } else {
            contentDetailsView = (ImageView) convertView;
        }

        int resId;
        switch (position) {
            case 0: resId = R.drawable.drone_img;
                break;
            case 1: resId = R.drawable.drone_img2;
                break;
            case 2: resId = R.drawable.big_house;
                break;
            default: resId = R.drawable.drone_img;
        }
        contentDetailsView.setImageResource(resId);
        return contentDetailsView;
    }
}


