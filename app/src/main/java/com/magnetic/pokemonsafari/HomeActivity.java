package com.magnetic.pokemonsafari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /** Called when the user clicks the Send button */
    public void openPokeDex(View view) {
        Intent intent = new Intent(this, PokeDexActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Send button */
    public void openSafari(View view) {
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
    }
}
