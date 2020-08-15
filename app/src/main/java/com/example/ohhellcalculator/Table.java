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

/*
* Activity of Calculator
* */
public class Table extends AppCompatActivity {
    private TableLayout tableLayout, roundtable;
    private LinearLayout buttonLayout;
    //width of cells
    private int width;
    private HorizontalScrollView hsw_names, hsw_table;
    private HorizontalScrollView.OnScrollChangeListener hswl_table, hswl_names;
    private NestedScrollView nsw_rounds, nsw_table;
    private NestedScrollView.OnScrollChangeListener nswl_rounds, nswl_table, nswl_names;
    //Singleton Game instance
    Game g = Game.getInstance();
    //endround flag, it is false when a round is happening and true if it is completed
    private boolean endround = false;
    //Result of the round of guessing and scoring
    private RoundResult lastResult;

    /*
    * Callback function, it is executed when the activity is created
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        addNames();
        addTableRow();
        addRoundsNum();

        //Adds guessbuttons to layout
        buttonLayout = findViewById(R.id.buttonlayout);
        if(savedInstanceState == null) {
            for (int i = 0; i <= 1; i++)
                addGuessButton(i);
        } else
            for(int i = 0; i <= g.getRealActualRound(); i++)
                addGuessButton(i);
        /*
        *when back is pressed, the app does not go back on screen (Names activity), but asks if you
        * would like to quit the current game, if yes it goes back to MainActivity
        * */
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //builds Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Table.this);
                builder.setTitle(R.string.dialogtitle)
                        .setMessage(R.string.dialogmessage)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                g.clear();
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

    /*
    * Helper function, the received number which is dp-s is converted to px
    * @param dp - dp to be converted
    * */
    private int changeDpToPixel(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    /*
    * Add names to the corresponding layout
    * */
    private void addNames() {
        LinearLayout namesLayout = findViewById(R.id.namesLayout);
        int playernum = g.getPlayerNumber();
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
        // setup of synced scrolling between Table and Names
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

    /*
    * Adds rows to the Table, which contains the data of all players.
    * */
    @SuppressLint("ResourceAsColor")
    private void addTableRow() {
        tableLayout = findViewById(R.id.calculatortable);

        int roundsno = g.getNoOfRounds(), playernum = g.getPlayerNumber();
        roundsno += roundsno - 1;
        for(int i = 1; i <= roundsno; i++) {
            TableRow pointsRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_table, null);
            for(int j = 1; j <= playernum; j++) {
                for(int k = 1; k <= 2; k++) {
                    TextView textView = new EditText(getApplicationContext());
                    textView.setId(i * 100 + j * 10 + k);
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
        //setup of synced scrolling of Table and Rounds
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

        //setup of synced scrolling of Table and Names
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

    /*
    * Adds rounds to the corresponding layout
    * */
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
        //setup of synced scrolling of Rounds and Table
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

    /*
    * Adds a guessbutton to the corresponding layout
    * @param i - number of the button (equals guess)
    * */
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

    /*
    * Deletes a guessbutton from the corresponding layout
    * @param i - number (tag) of the button
    * */
    private void removeGuessButton(int i) {
        Button button = buttonLayout.findViewWithTag(i);
        if(buttonLayout != null)
            buttonLayout.removeView(button);
    }

    /*
    * OnClick listener for guessbuttons
    * */
    @SuppressLint("SetTextI18n")
    public void guessButton_click(View view) {
        if(!endround) {
            int playernum = g.getActualPlayer(), round = g.getActualRound();
            lastResult = g.manageRound((int) view.getTag());
            //if round is at the guessing phase the first cell gets updated
            if( lastResult == RoundResult.ADDGUESS || lastResult == RoundResult.LASTADDEDGUESS) {
                TextView guessText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 1);
                Integer guess = (Integer) view.getTag();
                guessText.setText(guess.toString());

                //coloring of roundnumber based on the sum of the guesses
                if(lastResult == RoundResult.LASTADDEDGUESS) {
                    View v = roundtable.findViewWithTag(g.getActualRound());
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
            }
            //if round is in the actual scoring phase the second cell gets updated
            else if(lastResult == RoundResult.ADDACTUAL || lastResult == RoundResult.ENDROUND) {
                TextView pointsText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 2);
                Integer points = g.getPlayer(playernum).getPoints();
                pointsText.setText(points.toString());
                if(lastResult == RoundResult.ENDROUND)
                    endround = true;
            }
        }
    }

    /*
    * OnClick listener for endround button(tick symbol)
    * */
    public void endRoundButton_click(View view) {
        int roundsno = g.getNoOfRounds(), actualround = g.getActualRound();
        if(endround && roundsno >= actualround) {
            addGuessButton(g.getActualRound());
        } else if(endround && g.getRealActualRound() != 0) {
            removeGuessButton(roundsno - (actualround -1 - roundsno));
        }
        if(2 * roundsno - 1 >= actualround)
            endround = false;
    }

    /*
    * OnClick listener for undo button (backward arrow symbol)
    * */
    public void undoButton_click(View view) {
        UndoResult result = g.undo();
        int playernum = g.getActualPlayer(), round = g.getActualRound();
        if(result == UndoResult.FIRST) {
            TextView guessText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 1);
            guessText.setText("");
            View v = roundtable.findViewWithTag(g.getActualRound());
            v.setBackground(null);
        } else {
            TextView pointsText = tableLayout.findViewWithTag(round * 100 + (playernum + 1) * 10 + 2);
            pointsText.setText("");
        }
        if(result == UndoResult.CHANGE_ENDROUND) {
            int roundsno = g.getNoOfRounds(), actualround = g.getActualRound();
            if(roundsno > actualround)
                removeGuessButton(actualround + 1);
            else {
                if(!endround)
                    addGuessButton(roundsno - (actualround - roundsno));
            }
            endround = false;
        }
    }

}