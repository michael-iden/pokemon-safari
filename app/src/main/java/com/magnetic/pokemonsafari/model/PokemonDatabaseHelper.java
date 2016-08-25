package com.magnetic.pokemonsafari.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PokemonDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_PATH = "/data/data/com.magnetic.pokemonsafari/databases/";

    private static final int DATABASE_VERSION = 1;
    private static final String DICTIONARY_TABLE_NAME = "pokemon";
    private static final String DATABASE_NAME = "pokemonsafari.db";
    private static final String POKEMON_DATABASE_LOCATION = DB_PATH + DATABASE_NAME;

    private Context context;

    public PokemonDatabaseHelper(Context context) throws IOException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        createDatabase();
    }

    public void createDatabase() throws IOException {

        if(databaseExists()){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean databaseExists() {

        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(POKEMON_DATABASE_LOCATION, null, SQLiteDatabase.OPEN_READONLY);
        } catch(SQLiteException e){
            //database does't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    private void copyDatabase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(POKEMON_DATABASE_LOCATION);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public List<Pokemon> getAllPokemon() {
        SQLiteDatabase pokemonDatabase = this.getReadableDatabase();
        Cursor pokemonDatabaseCursor = pokemonDatabase.rawQuery("SELECT * FROM POKEMON", null);

        List<Pokemon> pokemonList = new ArrayList<>(151);
        while(pokemonDatabaseCursor.moveToNext()) {

            String number = pokemonDatabaseCursor.getString(0);
            String name = pokemonDatabaseCursor.getString(1);
            String imageFile = pokemonDatabaseCursor.getString(2);
            String cryFile = pokemonDatabaseCursor.getString(3);

            pokemonList.add(new Pokemon(number, name, imageFile, cryFile));
        }

        return pokemonList;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
