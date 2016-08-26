package com.magnetic.pokemonsafari.model;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.magnetic.pokemonsafari.PokemonSafariApplication;

import org.apache.commons.lang3.RandomUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dji.sdk.FlightController.DJICompass;
import dji.sdk.FlightController.DJIFlightControllerDataType;
import dji.sdk.FlightController.DJIFlightControllerDelegate;

/**
 * Created by joey.bickerstaff on 8/25/16.
 */
public class WorldTracker implements DJIFlightControllerDelegate.FlightControllerUpdateSystemStateCallback {
    public static final double MIN_HEADING = -180.0;
    public static final double MAX_HEADING = 180.0;
    public static final double FOV = 94.0;

    private final Object mutex = new Object();

    private Context context;
    private DJICompass compass;
    private List<Pokemon> allPokemon;
    private double aircraftHeading = 0.0;
    private Set<SpawnedPokemon> spawnedPokemon = new HashSet<>();
    private double previousHeading;
    private DJIFlightControllerDataType.DJILocationCoordinate3D aircraftLocation;

    private boolean dirty = true;

    public WorldTracker(Context context,  PokemonDatabaseHelper pokemonDatabaseHelper) {
        this.context = context;
        compass = PokemonSafariApplication.getCompass();
        allPokemon = pokemonDatabaseHelper.getAllPokemon();
        PokemonSafariApplication.getFlightController().setUpdateSystemStateCallback(this);
    }

    public static void validateHeading(double newHeading) {
        if (newHeading < MIN_HEADING || newHeading > MAX_HEADING) {
            throw new IllegalArgumentException("spawn heading out of bounds");
        }
    }

    private void setAircraftHeading(double newHeading) {
        validateHeading(newHeading);
        aircraftHeading = newHeading;
    }

    public void spawnNewPokemon() {
        Log.d(getClass().getName(), "SPAWNING NEW POKEMON");
        synchronized (mutex) {
            Pokemon randomPokemon = getRandomPokemon();
            SpawnedPokemon pokemon = new SpawnedPokemon(randomPokemon, aircraftHeading, context.getAssets());
            spawnedPokemon.add(pokemon);
        }
    }

    private Pokemon getRandomPokemon() {
        return allPokemon.get(RandomUtils.nextInt(0, allPokemon.size()));
    }

    public void renderSpawnedPokemon(Canvas canvas) {
        Log.d(getClass().getName(), "RENDERING SPAWNED POKEMON");
        synchronized (mutex) {
            if (!dirty) {
                Log.d(getClass().getName(), "NOT DIRRTY");
                return;
            }

            for (SpawnedPokemon pokemon : spawnedPokemon) {
                Log.d(getClass().getName(), "CHECKING " + pokemon);
                if (pokemon.isInFov(getFovLeftBound(), getFovRightBound())) {
                    Log.d(getClass().getName(), "IN FOV");
                    try {
                        pokemon.draw(context, canvas);
                    } catch (IOException e) {
                        throw new RuntimeException("failed to render spawned pokemon", e);
                    }
                }
            }
        }
    }

    private double getFovLeftBound() {
        Log.d(getClass().getName(), "left bound = " + aircraftHeading + " - (" + FOV + " / 2.0) = " + (aircraftHeading - (FOV/ 2.0)));
        return aircraftHeading - (FOV / 2.0);
    }

    private double getFovRightBound() {
        Log.d(getClass().getName(), "left bound = " + aircraftHeading + " + (" + FOV + " / 2.0) = " + (aircraftHeading + (FOV/ 2.0)));
        return aircraftHeading + (FOV / 2.0);
    }

    @Override
    public void onResult(DJIFlightControllerDataType.DJIFlightControllerCurrentState currentState) {
        Log.d(getClass().getName(), "CURRENT STATE UPDATED: " + currentState);
        synchronized (mutex) {

            double newAircraftHeading = compass.getHeading();
            DJIFlightControllerDataType.DJILocationCoordinate3D newAircraftLocation
                    = currentState.getAircraftLocation();
            if (!newAircraftLocation.equals(aircraftLocation) || newAircraftHeading != aircraftHeading) {
                Log.d(getClass().getName(), "NEW POSITION: " + newAircraftLocation);
                aircraftHeading = newAircraftHeading;
                aircraftLocation = newAircraftLocation;
                setDirty(true);
            }
        }
    }

    private void setDirty(boolean newDirty) {
        dirty = newDirty;
    }
}
