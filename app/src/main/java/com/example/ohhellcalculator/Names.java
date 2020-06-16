package com.example.ohhellcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Names extends AppCompatActivity {
    private int playernumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            playernumber = bundle.getInt("playernum");

    }
}