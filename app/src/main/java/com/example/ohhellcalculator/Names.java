package com.example.ohhellcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Names extends AppCompatActivity {
    private int playernumber;
    private List<EditText> editTextArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            playernumber = bundle.getInt("playernum");
        for(int i = 1; i <= playernumber; i++){
            TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_names, null);
            TextView tv = (TextView) tableRow.findViewById(R.id.tableCell1);
            tv.setText(String.format(getResources().getString(R.string.pn), i));
            editTextArrayList.add((EditText) tableRow.findViewById(R.id.tableCell2));
            tableLayout.addView(tableRow);
        }
        Button b  = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constructPlayers();
                Intent intent = new Intent(view.getContext(), Table.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    private void constructPlayers() {
        for(int i = 0; i < playernumber; i++) {
            Game.getInstance().addPlayer(new Player(editTextArrayList.get(i).getText().toString()));
        }
    }
}