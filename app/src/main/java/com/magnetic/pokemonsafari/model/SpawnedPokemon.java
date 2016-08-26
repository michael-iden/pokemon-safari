package com.magnetic.pokemonsafari.model;

import static com.magnetic.pokemonsafari.model.WorldTracker.validateHeading;

/**
 * Created by joey.bickerstaff on 8/25/16.
 */
public class SpawnedPokemon extends Pokemon {

    private double heading;

    public SpawnedPokemon(Pokemon basePmon, double heading) {
        super(basePmon);
        this.heading = heading;
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
}
