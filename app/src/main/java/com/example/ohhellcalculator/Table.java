package com.example.ohhellcalculator;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Table extends AppCompatActivity {
    private TableLayout tableLayout, roundtable;
    private LinearLayout buttonLayout;
    private int width;
    private HorizontalScrollView hsw_names, hsw_table;
    private NestedScrollView nsw_rounds, nsw_table;
    private NestedScrollView.OnScrollChangeListener nswl_rounds, nswl_table, nswl_names;
    private HorizontalScrollView.OnScrollChangeListener hswl_table, hswl_names;
    Game g = Game.getInstance();
    private boolean endround = false;
    private RoundResult lastResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        addNames();
        addTableRow();
        addRoundsNum();

        buttonLayout = findViewById(R.id.buttonlayout);
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

    private void addNames() {
        LinearLayout namesLayout = findViewById(R.id.namesLayout);
        int playernum = g.getPlayernumber();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = (displayMetrics.widthPixels - Math.round(changeDpToPixel(60))) / playernum;
        for(int i = 0; i < playernum; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText(g.getPlayerName(i));
            textView.setWidth(width);
            if(i == 0) {
                textView.setPadding(changeDpToPixel(40), 0,0,0);
                textView.setMinWidth(changeDpToPixel(85));
            } else {
                textView.setPadding(changeDpToPixel(25),0,0,0);
                textView.setMinWidth(changeDpToPixel(70));
            }
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
    }

    @SuppressLint("ResourceAsColor")
    private void addTableRow() {
        tableLayout = findViewById(R.id.calculatortable);

        int roundsno = g.getNoOfRounds(), playernum = g.getPlayernumber();
        roundsno += roundsno - 1;
        for(int i = 1; i <= roundsno; i++) {
            TableRow pointsRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_table, null);
            for(int j = 1; j <= playernum; j++) {
                for(int k = 1; k <= 2; k++) {
                    TextView textView = new EditText(getApplicationContext());
                    textView.setTag(i * 100 + j * 10 + k);
                    //guessText.setMinWidth(300);
                    textView.setWidth(changeDpToPixel(35));
                    if(k == 1) {
                        textView.setGravity(Gravity.RIGHT);
                        textView.setPadding(0,changeDpToPixel(15),changeDpToPixel(5),0);
                    } else {
                        textView.setGravity(Gravity.LEFT);
                        textView.setPadding(changeDpToPixel(5),changeDpToPixel(15),0,0);
                    }
                    textView.setFocusable(false);
                    textView.setClickable(false);
                    textView.setEnabled(false);
                    textView.setBackgroundColor(android.R.color.transparent);
                    pointsRow.addView(textView);
                }
            }
            tableLayout.addView(pointsRow);
        }
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
    }

    private void addRoundsNum() {
        roundtable = findViewById(R.id.roundtable);
        int roundsno = g.getNoOfRounds();
        for(int i = 1; i <= (2 * roundsno - 1); i++) {
            TableRow roundtableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_table, null);
            TextView textView = new TextView(getApplicationContext());
            textView.setTag(i);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setPadding(changeDpToPixel(5), changeDpToPixel(10), changeDpToPixel(5), changeDpToPixel(10));
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
        buttonLayout.addView(button);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.width = changeDpToPixel(50);
        button.setLayoutParams(params);
    }

    private void removeGuessButton(int i) {
        Button button = buttonLayout.findViewWithTag(i);
        if(buttonLayout != null)
            buttonLayout.removeView(button);
    }

    @SuppressLint("SetTextI18n")
    public void guessButton_click(View view) {
        if(!endround) {
            int playernum = g.getActualPlayer(), round = g.getActualRound();
            lastResult = g.manageRound((int) view.getTag());
            if( lastResult == RoundResult.ADDGUESS || lastResult == RoundResult.LASTADDEDGUESS) {
                TextView guessText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 1);
                Integer guess = (Integer) view.getTag();
                guessText.setText(guess.toString());
                if(lastResult == RoundResult.LASTADDEDGUESS) {
                    View v = roundtable.findViewWithTag(g.getRealActualRound());
                    GuessesNo guessResult = g.resultOfGuessing();
                    v.setBackgroundResource(R.drawable.border_textview);
                    GradientDrawable mygrad = mygrad = (GradientDrawable)v.getBackground();
                    if(guessResult == GuessesNo.FIGHT)
                        mygrad.setStroke(changeDpToPixel(3), getColor(R.color.fightRed));
                    else if(guessResult == GuessesNo.SCATTER)
                        mygrad.setStroke(changeDpToPixel(3),getColor(R.color.scatterYellow));
                    else
                        mygrad.setStroke(changeDpToPixel(3), getColor(R.color.equalsGreen));
                }
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
        int roundsno = g.getNoOfRounds(), actualround = g.getActualRound();
        if(endround && roundsno >= actualround) {
            addGuessButton(g.getActualRound());
        } else if(endround) {
            removeGuessButton(roundsno - (actualround -1 - roundsno));
        }
        if(2 * roundsno - 1 >= actualround)
            endround = false;
    }

    public void undoButton_click(View view) {
        UndoResult result = g.undo();
        int playernum = g.getActualPlayer(), round = g.getActualRound();
        if(result == UndoResult.FIRST) {
            TextView guessText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 1);
            guessText.setText("");
            View v = roundtable.findViewWithTag(g.getRealActualRound());
            v.setBackground(null);
        } else {
            TextView pointsText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 2);
            pointsText.setText("");
        }
        if(result == UndoResult.CHANGE_ENDROUND) {
            endround = false;
            int roundsno = g.getNoOfRounds(), actualround = g.getActualRound();
            if(roundsno > actualround)
                removeGuessButton(actualround + 1);
            else
                addGuessButton(roundsno - (actualround - roundsno));
        }
    }

}