package com.example.ohhellcalculator;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.material.button.MaterialButton;

public class Table extends AppCompatActivity {
    TableLayout tableLayout;
    int width;
    HorizontalScrollView hsw_names, hsw_table;
    NestedScrollView nsw_rounds, nsw_table;
    NestedScrollView.OnScrollChangeListener nswl_rounds, nswl_table, nswl_names;
    HorizontalScrollView.OnScrollChangeListener hswl_table, hswl_names;
    Game g = Game.getInstance();
    boolean endround = false;
    RoundResult lastResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        //setup names in first row
        LinearLayout namesLayout = findViewById(R.id.namesLayout);
        int playernum = g.getPlayernumber();

        TextView firsttextView = new TextView(getApplicationContext());
        firsttextView.setText(g.getPlayerName(0));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = (displayMetrics.widthPixels - Math.round(changeDpToPixel(60))) / playernum;
        firsttextView.setWidth(width);
        firsttextView.setMinWidth(300);
        firsttextView.setPadding(changeDpToPixel(15),0,0,0);
        namesLayout.addView(firsttextView);
        for(int i = 1; i < playernum; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText(g.getPlayerName(i));
            textView.setWidth(width);
            //textView.setMinWidth(300);
            namesLayout.addView(textView);
        }
        hswl_names = new HorizontalScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                hsw_table.setOnScrollChangeListener(null);
                hsw_table.scrollTo(i,i1);
                hsw_table.setOnScrollChangeListener(hswl_table);
            }
        };
        hsw_names = findViewById(R.id.horizontalScrollViewForNames);
        hsw_names.setOnScrollChangeListener(hswl_names);

        tableLayout = findViewById(R.id.calculatortable);
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

        hswl_table = new HorizontalScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                hsw_names.setOnScrollChangeListener(null);
                hsw_names.scrollTo(i, i1);
                hsw_names.setOnScrollChangeListener(hswl_names);
            }
        };
        hsw_table = findViewById(R.id.horizontalScrollViewForTable);
        hsw_table.setOnScrollChangeListener(hswl_table);
        addTableRow();
        addRoundsNum();

        for(int i = 0; i <= 1; i++)
            addGuessButton(i);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Table.this);
                builder.setTitle(R.string.dialogtitle)
                        .setMessage(R.string.dialogmessage)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Table.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
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
        int roundsno = g.getNoOfRounds();
        for(int i = 1; i <= (2 * roundsno - 1); i++) {
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
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.width = changeDpToPixel(50);
        button.setLayoutParams(params);
    }

    @SuppressLint("SetTextI18n")
    public void guessButton_click(View view) {
        if(!endround) {
            int playernum = g.getActualPlayer(), round = g.getActualRound();
            lastResult = g.manageRound((int) view.getTag());
            if( lastResult == RoundResult.ADDGUESS) {
                TextView guessText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 1);
                Integer guess = (Integer) view.getTag();
                guessText.setText(guess.toString());
            } else if(lastResult == RoundResult.ADDACTUAL || lastResult == RoundResult.ENDROUND) {
                TextView pointsText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 2);
                Integer points = g.getPlayer(playernum).getPoints();
                pointsText.setText(points.toString());
                if(lastResult == RoundResult.ENDROUND)
                    endround = true;
            }
        }
    }

    public void endRoundButton_click(View view) {
        if(endround) {
            addGuessButton(g.getActualRound());
            endround = false;
        }
    }

    public void undoButton_click(View view) {
        UndoResult result = g.undo();
        int playernum = g.getActualPlayer(), round = g.getActualRound();
        if(result == UndoResult.FIRST) {
            TextView guessText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 1);
            guessText.setText("");
        } else {
            TextView pointsText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 2);
            pointsText.setText("");
        }
        if(result == UndoResult.CHANGE_ENDROUND)
            endround = false;
    }

}