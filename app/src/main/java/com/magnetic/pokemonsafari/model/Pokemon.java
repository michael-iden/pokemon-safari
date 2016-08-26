package com.magnetic.pokemonsafari.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

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

}
