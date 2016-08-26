package com.magnetic.pokemonsafari.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import us.feras.ecogallery.EcoGallery;
import us.feras.ecogallery.EcoGalleryAdapterView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.magnetic.pokemonsafari.R;
import com.magnetic.pokemonsafari.model.ImageStats;
import com.magnetic.pokemonsafari.model.Pokemon;
import com.magnetic.pokemonsafari.model.PokemonDatabaseHelper;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PokeDetailsActivity extends AppCompatActivity {

    private static final SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");

    private Pokemon pokemon;
    private List<ImageStats> imageStats = Arrays.asList(
            new ImageStats(9001 * RandomUtils.nextInt(1, 4), new Date(1471697946000L)),
            new ImageStats(1000 * RandomUtils.nextInt(1, 4), new Date(1472216346000L)),
            new ImageStats(666 * RandomUtils.nextInt(1, 4), new Date(1471957146000L)));

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
        int number = extras.getInt("selectedIndex") + 1;

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

        pokemon.cry(this);

        EcoGallery ecoGallery = (EcoGallery) findViewById(R.id.gallery);
        ecoGallery.setAdapter(new ImageAdapter2(this, pokemon));
        ecoGallery.setOnItemSelectedListener(new EcoGalleryAdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(EcoGalleryAdapterView<?> parent, View view, int position, long id) {
                TextView scoreTextView = (TextView) findViewById(R.id.score);
                TextView dateTextView = (TextView) findViewById(R.id.date);
                scoreTextView.setText(String.valueOf(imageStats.get(position).getScore()));
                dateTextView.setText(format1.format(imageStats.get(position).getDate()));
            }

            @Override
            public void onNothingSelected(EcoGalleryAdapterView<?> parent) {

            }
        });
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

            switch (position) {
                default:
                    InputStream ims = null;
                    try {
                        ims = context.getAssets().open(pokemon.getImageFile());
                        Drawable d = Drawable.createFromStream(ims, null);
                        Drawable d2 = resize(d);
                        contentDetailsView.setImageDrawable(d2);

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


