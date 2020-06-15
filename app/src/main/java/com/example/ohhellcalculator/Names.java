package com.example.ohhellcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Names extends AppCompatActivity {
    private int playernumber;

    public void setPlayernumber(int playernumber) {
        this.playernumber = playernumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);
    }
}