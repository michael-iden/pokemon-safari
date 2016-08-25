package com.magnetic.pokemonsafari.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.magnetic.pokemonsafari.R;
import com.magnetic.pokemonsafari.model.ImageAdapter;

import java.io.IOException;

public class PokeDexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_dex);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        try {
            gridview.setAdapter(new ImageAdapter(this));
        } catch (IOException e) {
            e.printStackTrace();
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Log.e("ASS", String.valueOf(position));

                Intent intent = new Intent(getApplicationContext(), PokeDetailsActivity.class);
                intent.putExtra("pokemonNumber", position);
                startActivity(intent);
            }
        });



    }

}
