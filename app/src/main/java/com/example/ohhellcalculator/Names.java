package com.example.ohhellcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Names extends AppCompatActivity {
    private int playernumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            playernumber = bundle.getInt("playernum");

        for(int i = 1; i <= playernumber; i++){
            TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow, null);
            TextView tv;

            tv = (TextView) tableRow.findViewById(R.id.tableCell1);
            tv.setText(String.format(getResources().getString(R.string.pn), i));
            tableLayout.addView(tableRow);
        }
    }
}