package com.magnetic.pokemonsafari.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import us.feras.ecogallery.EcoGallery;

import com.magnetic.pokemonsafari.R;

public class PokeDetailsActivity extends AppCompatActivity {

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
        ImageView imageView = new ImageView(context);
        int resId;
        switch (position) {
            case 0: resId = R.drawable.one;
                break;
            case 1: resId = R.drawable.two;
                break;
            case 2: resId = R.drawable.three;
                break;
            default: resId = R.drawable.one;
        }
        imageView.setImageResource(resId);
        return imageView;
    }
}


