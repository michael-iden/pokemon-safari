package com.magnetic.pokemonsafari.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.util.Log;

import com.google.android.gms.common.api.Releasable;

import java.io.IOException;
import java.io.InputStream;

import static com.magnetic.pokemonsafari.model.WorldTracker.validateHeading;

/**
 * Created by joey.bickerstaff on 8/25/16.
 */
public class SpawnedPokemon extends Pokemon {
    private static final float BASE_SCALE_X = 2.0f;
    private static final float BASE_SCALE_Y = 2.0f;

    private double heading;
    private Bitmap bitmap;

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
        if (leftBound < rightBound) {
            return heading > leftBound && heading < rightBound;
        } else {
            return heading < leftBound && heading > rightBound;
        }
    }

    public int getRawWidth() {
        return bitmap.getWidth();
    }

    public int getRawHeight() {
        return bitmap.getHeight();
    }

    public Point getPosition(Canvas canvas) {
        return new Point((canvas.getWidth() - getRawWidth()) / 2, (canvas.getHeight() - getRawHeight()) / 2);
    }

    public void draw(Context context, Canvas canvas) throws IOException {
        Log.d(getClass().getName(), "RENDERING TO " + canvas);
        Point pos = getPosition(canvas);
        canvas.drawBitmap(bitmap, (float)pos.x, (float)pos.y, new Paint());
    }


    public static Bitmap getBitmapFromAsset(AssetManager assetManager, String filePath) {
        Bitmap bitmap;
        try (InputStream istr = assetManager.open(filePath)) {
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            throw new RuntimeException("failed to load pokebitmap", e);
        }
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * (int)BASE_SCALE_X, bitmap.getHeight() * (int)BASE_SCALE_Y, false);
        bitmap.recycle();

        return scaled;
    }

    public void release() {
        bitmap.recycle();
        bitmap = null;
    }
}
