package com.example.ohhellcalculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Table extends AppCompatActivity {
    TableLayout tableLayout;
    int width, height;
    NestedScrollView nsw_rounds, nsw_table;
    NestedScrollView.OnScrollChangeListener nswl_rounds, nswl_table;
    Game g = Game.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        //setup names in first row
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_table, null);
        int playernum = g.getPlayernumber();
        for(int i = 0; i < playernum; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText(g.getPlayerName(i));
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float dp_to_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
            width = (displayMetrics.widthPixels - Math.round(dp_to_px)) / playernum;
            textView.setWidth(width);
            textView.setMinWidth(300);
            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.span = 2;
            tableRow.addView(textView, params);
        }
        tableLayout = findViewById(R.id.calculatortable);

        tableLayout.addView(tableRow);

        nswl_table = new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                nsw_rounds.setOnScrollChangeListener((View.OnScrollChangeListener) null);
                nsw_rounds.scrollTo(scrollX, scrollY);
                nsw_rounds.setOnScrollChangeListener(nswl_rounds); //SET SCROLL LISTENER AGAIN
            }
        };
        nsw_table = findViewById(R.id.nestedScrollViewForTable);
        nsw_table.setOnScrollChangeListener(nswl_table);

        addTableRow();
        addRoundsNum();

        for(int i = 0; i <= 1; i++)
            addGuessButton(i);

    }
    private int changeDpToPixel(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @SuppressLint("ResourceAsColor")
    private void addTableRow() {
        int roundsno = g.getNoOfRounds(), playernum = g.getPlayernumber();
        roundsno += roundsno - 1;
        for(int i = 1; i <= roundsno; i++) {
            TableRow pointsRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_table, null);
            for(int j = 1; j <= playernum; j++) {
                TextView guessText = new EditText(getApplicationContext());
                guessText.setTag(i * 100 + j * 10 + 1);
                guessText.setMinWidth(100);
                guessText.setWidth(width / 3);
                guessText.setFocusable(false);
                guessText.setClickable(false);
                guessText.setEnabled(false);
                guessText.setBackgroundColor(android.R.color.transparent);
                pointsRow.addView(guessText);
                TextView pointText = new EditText(getApplicationContext());
                pointText.setTag(i * 100 + j * 10 + 2);
                pointText.setMinWidth(200);
                pointText.setWidth(2 * width / 3);
                pointText.setEnabled(false);
                pointText.setFocusable(false);
                pointText.setClickable(false);
                pointText.setBackgroundColor(android.R.color.transparent);
                pointsRow.addView(pointText);
            }
            tableLayout.addView(pointsRow);
        }

    }
    private void addRoundsNum() {
        TableLayout roundtable = findViewById(R.id.roundtable);
        TableRow firstroundtableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_table, null);
        TextView firsttextView = new TextView(getApplicationContext());
        firsttextView.setText(getString(R.string.guessbutton, 1));
        firsttextView.setPadding(0,changeDpToPixel(35),0,0);
        firstroundtableRow.addView(firsttextView);
        roundtable.addView(firstroundtableRow);

        int roundsno = g.getNoOfRounds();
        for(int i = 2; i <= (2 * roundsno - 1); i++) {
            TableRow roundtableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_table, null);
            TextView textView = new TextView(getApplicationContext());
            textView.setPadding(0, changeDpToPixel(27), 0,0);
            if(i > roundsno)
                textView.setText(getString(R.string.guessbutton, roundsno - (i - roundsno)));
            else
                textView.setText(getString(R.string.guessbutton, i));
            roundtableRow.addView(textView);
            roundtable.addView(roundtableRow);
        }
        nswl_rounds = new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                nsw_table.setOnScrollChangeListener((View.OnScrollChangeListener) null);
                nsw_table.scrollTo(scrollX, scrollY);
                nsw_table.setOnScrollChangeListener(nswl_table); //SET SCROLL LISTENER AGAIN
            }
        };
        nsw_rounds = findViewById(R.id.nestedScrollViewForRounds);
        nsw_rounds.setOnScrollChangeListener(nswl_rounds);
    }
    private void addGuessButton(int i) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.guess_button, null);
        Button button = v.findViewById(R.id.guessbutton);
        button.setTag(i);
        button.setText(getString(R.string.guessbutton, i));
        LinearLayout buttonlayout = findViewById(R.id.buttonlayout);
        buttonlayout.addView(button);
    }
    @SuppressLint("SetTextI18n")
    public void guessButton_click(View view) {
        int playernum = g.getActualPlayer(), round = g.getActualRound();
        if( !g.manageRound((int) view.getTag())) {
            TextView guessText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 1);
            Integer guess = (Integer) view.getTag();
            guessText.setText(guess.toString());
        } else {
            TextView pointsText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 2);
            Integer points = g.getPlayer(playernum).getPoints();
            pointsText.setText(points.toString());
        }
    }
    public void endRoundButton_click(View view) {
        addGuessButton(g.getActualRound());
    }
}