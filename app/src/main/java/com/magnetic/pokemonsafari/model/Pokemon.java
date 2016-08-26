package com.magnetic.pokemonsafari.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.magnetic.pokemonsafari.activity.PokeDetailsActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class Pokemon {
    private static final MediaPlayer.OnCompletionListener CRY_COMPLETED_LISTENER = (mediaPlayer) -> {
        Log.d(Pokemon.class.getName(), "STOPPING PLAYBACK");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    };

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
        if (!cryFile.matches("^pokemon/cries.*$")) {
            cryFile = "pokemon/cries/" + cryFile;
        }

        if (!cryFile.matches("^.*\\.wav$")) {
            cryFile += ".wav";
        }

        return cryFile;
    }

    public Drawable getImageDrawable(Context context) throws IOException {
        InputStream ims = context.getAssets().open(imageFile);
        return Drawable.createFromStream(ims, null);
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "cryFile='" + getCryFile() + '\'' +
                ", imageFile='" + getImageFile() + '\'' +
                ", name='" + getName() + '\'' +
                ", number='" + getNumber() + '\'' +
                '}';
    }

    public void cry(Context context) {
        AssetManager assetManager = context.getAssets();
        Log.d(getClass().getName(), "Loading pokemon:\n" + toString());
        try {
            AssetFileDescriptor f = assetManager.openFd(getCryFile());
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(f.getFileDescriptor(), f.getStartOffset(), f.getLength());
            mediaPlayer.setOnPreparedListener((mediaPlayer1 -> {
                Log.d(getClass().getName(), "STARTING PLAYBACK");
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(CRY_COMPLETED_LISTENER);
            }));
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            throw new RuntimeException("failed to load pokecry", e);
        }

    }
}
