package com.magnetic.pokemonsafari.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import us.feras.ecogallery.EcoGallery;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.magnetic.pokemonsafari.R;
import com.magnetic.pokemonsafari.model.Pokemon;
import com.magnetic.pokemonsafari.model.PokemonDatabaseHelper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.io.InputStream;

public class PokeDetailsActivity extends AppCompatActivity {

    private Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        pokemon = getSelectedPokemon();

        toolbar.setTitle(WordUtils.capitalize(pokemon.getName()));
        setSupportActionBar(toolbar);

    }

    private Pokemon getSelectedPokemon() {
        Bundle extras = getIntent().getExtras();
        int number = extras.getInt("selectedIndex")+1;

        String pokemonNumber = String.format("%03d", number);

        Pokemon pokemon = null;
        try {
            PokemonDatabaseHelper pokemonDatabaseHelper = new PokemonDatabaseHelper(this);
            pokemon = pokemonDatabaseHelper.getPokemon(pokemonNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pokemon;
    }

    @Override
    protected void onResume() {
        super.onResume();

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.a001);
        mediaPlayer.start();

        EcoGallery ecoGallery = (EcoGallery) findViewById(R.id.gallery);
        ecoGallery.setAdapter(new ImageAdapter2(this, pokemon));
    }
}

class ImageAdapter2 extends BaseAdapter {
    private Context context;
    private Pokemon pokemon;

    ImageAdapter2(Context context, Pokemon pokemon) {
        this.context = context;
        this.pokemon = pokemon;
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
        ImageView contentDetailsView;

        if (convertView == null) {
            contentDetailsView = new ImageView(context);
//            contentDetailsView.setLayoutParams(new ViewGroup.LayoutParams(200,
//                    ViewGroup.LayoutParams.MATCH_PARENT));

            switch (position) {
                default:
                    InputStream ims = null;
                    try {
                        ims = context.getAssets().open(pokemon.getImageFile());
                        Drawable d = Drawable.createFromStream(ims, null);
                        Drawable d2 = resize(d);
                        contentDetailsView.setImageDrawable(d2);
//                        contentDetailsView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } else {
            contentDetailsView = (ImageView) convertView;
        }

        return contentDetailsView;
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 1000, 1000, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }
}


