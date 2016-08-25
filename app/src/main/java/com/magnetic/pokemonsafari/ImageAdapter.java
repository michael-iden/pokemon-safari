package com.magnetic.pokemonsafari;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by michael.iden on 8/24/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Pokemon> pokemonList;

    public ImageAdapter(Context c) throws IOException {
        mContext = c;

        PokemonDatabaseHelper pokemonDatabaseHelper = new PokemonDatabaseHelper(mContext);
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
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = inflater.inflate(R.layout.pokedex_element, null);

        } else {
            gridViewAndroid = (View) convertView;
        }

        TextView nameTextView = (TextView) gridViewAndroid.findViewById(R.id.name);
        TextView numberTextView = (TextView) gridViewAndroid.findViewById(R.id.number);
        ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.image);

        Pokemon pokemon = pokemonList.get(position);
        nameTextView.setText(WordUtils.capitalize(pokemon.getName()));
        numberTextView.setText(pokemon.getNumber());

        try {
            InputStream ims = mContext.getAssets().open(pokemon.getImageFile());
            Drawable d = Drawable.createFromStream(ims, null);
            imageViewAndroid.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gridViewAndroid;
    }
}
