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
        if(!etplayernum.getText().toString().matches("")) {
            Intent intent = new Intent(this, Names.class);
            intent.putExtra("playernum", Integer.parseInt(etplayernum.getText().toString()));
            startActivity(intent);
        }
    }
}