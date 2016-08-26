package com.magnetic.pokemonsafari.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class Pokemon {

    private String number;
    private String name;
    private String imageFile;
    private String cryFile;

    public Pokemon(String number, String name, String imageFile, String cryFile) {
        this.number = number;
        this.name = name;
        this.imageFile = imageFile;
        this.cryFile = cryFile;
    }

    public Pokemon(Pokemon pokemon) {
        this(pokemon.getNumber(), pokemon.getName(), pokemon.getImageFile(), pokemon.getCryFile());
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getImageFile() {
        return imageFile;
    }

    public String getCryFile() {
        return cryFile;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "cryFile='" + cryFile + '\'' +
                ", imageFile='" + imageFile + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    public void draw(Context context, Canvas canvas) throws IOException {
        Log.d(getClass().getName(), "RENDERING TO " + canvas);
        Drawable drawable = getImageDrawable(context);
        drawable.setBounds(100, 100, 500, 500);
        drawable.draw(canvas);
    }
}
