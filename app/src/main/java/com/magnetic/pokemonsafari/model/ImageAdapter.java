package com.magnetic.pokemonsafari.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magnetic.pokemonsafari.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by michael.iden on 8/24/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<Pokemon> pokemonList;

    public ImageAdapter(Context c) throws IOException {
        context = c;

        PokemonDatabaseHelper pokemonDatabaseHelper = new PokemonDatabaseHelper(context);
        pokemonList = pokemonDatabaseHelper.getAllPokemon();

    }

    public int getCount() {
        return pokemonList.size();
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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = inflater.inflate(R.layout.pokedex_element, null);

        } else {
            gridViewAndroid = (View) convertView;
        }

        TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.title);
        ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.image);

        Pokemon pokemon = pokemonList.get(position);
        textViewAndroid.setText(pokemon.getNumber());

        try {
            InputStream ims = context.getAssets().open(pokemon.getImageFile());
            Drawable d = Drawable.createFromStream(ims, null);
            imageViewAndroid.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gridViewAndroid;
    }
}
