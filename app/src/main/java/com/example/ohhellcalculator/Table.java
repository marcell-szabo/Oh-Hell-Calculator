package com.example.ohhellcalculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Table extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_table, null);
        int playernum = Game.getInstance().getPlayernumber();
        for(int i = 0; i < playernum; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText(Game.getInstance().getPlayerName(i));
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float dp_to_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
            textView.setWidth((displayMetrics.widthPixels - Math.round(dp_to_px)) / playernum);
            textView.setMinWidth(300);
            tableRow.addView(textView);
        }
        TableLayout tableLayout = findViewById(R.id.calculatortable);
        tableLayout.addView(tableRow);

    }
}