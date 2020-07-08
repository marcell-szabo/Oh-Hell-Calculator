package com.example.ohhellcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void play(View view) {
        EditText etplayernum = (EditText) findViewById(R.id. editTextPlayerNum);
        EditText etroundsnum = (EditText) findViewById(R.id.editTextRoundsNum);
        if(!etplayernum.getText().toString().matches("")) {
            Game.getInstance().setNoOfRounds(Integer.parseInt(etroundsnum.getText().toString()));
            Intent intent = new Intent(this, Names.class);
            intent.putExtra("playernum", Integer.parseInt(etplayernum.getText().toString()));
            startActivity(intent);
        }
    }
}