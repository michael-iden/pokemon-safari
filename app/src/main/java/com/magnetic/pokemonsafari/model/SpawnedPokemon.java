package com.magnetic.pokemonsafari.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;

import com.google.android.gms.common.api.Releasable;

import java.io.IOException;
import java.io.InputStream;

import static com.magnetic.pokemonsafari.model.WorldTracker.validateHeading;

/**
 * Created by joey.bickerstaff on 8/25/16.
 */
public class SpawnedPokemon extends Pokemon {
    private static final Paint WHITE_FILTER_PAINT;

    private double heading;
    private Bitmap bitmap;

    static {
        WHITE_FILTER_PAINT = new Paint();
        WHITE_FILTER_PAINT.setColorFilter(new PorterDuffColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY));
    }

    public SpawnedPokemon(Pokemon basePmon, double heading, AssetManager assetManager) {
        super(basePmon);
        this.heading = heading;
        bitmap = getBitmapFromAsset(assetManager, getImageFile());
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double newSpawnHeading) {
        validateHeading(newSpawnHeading);
        heading = newSpawnHeading;
    }

    public boolean isInFov(double leftBound, double rightBound) {
        return heading > leftBound && heading < rightBound;
    }

    public int getRawWidth(Context context) {
//        getImageDrawable(context).
        return -1;
    }

    public void draw(Context context, Canvas canvas) throws IOException {
        Log.d(getClass().getName(), "RENDERING TO " + canvas);

        canvas.drawBitmap(bitmap, 100f, 100f, WHITE_FILTER_PAINT);
    }


    public static Bitmap getBitmapFromAsset(AssetManager assetManager, String filePath) {
        Bitmap bitmap;
        try (InputStream istr = assetManager.open(filePath)) {
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            throw new RuntimeException("failed to load pokebitmap", e);
        }
        return bitmap;
    }

    public void release() {
        bitmap.recycle();
        bitmap = null;
    }
}
