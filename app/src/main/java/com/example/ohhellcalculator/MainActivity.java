package com.example.ohhellcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Names n = new Names();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void play(View view) {
        Intent intent = new Intent(this, n.getClass());
        startActivity(intent);
        EditText etplayernum = (EditText) findViewById(R.id. editTextPlayerNum);
        n.setPlayernumber(Integer.parseInt(etplayernum.getText().toString()));
    }
}