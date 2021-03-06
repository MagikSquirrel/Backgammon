package com.MagikSquirrel.backgammon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class homePage extends backgammonActionBarActivity {

    private backgammonApp app;
    private Context mContext;
    private gameBoard gameBoard;
    private imgBoard imgBoard;

    @Override
    protected void onPostResume() {
        super.onPostResume();

        //If test case has been set, then we need to modify the game board.
        if(app.getTestCase() != 0){
            gameBoard.setBoardTest(app.getTestCase());
            app.setTestCase(0);
            imgBoard.updateSpinnerChoices();
            imgBoard.redrawBoard();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Add this activity to the App list
        app = (backgammonApp) this.getApplication();
        //((backgammonApp) this.getApplication()).addActivity(this.getPackageName(), this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Draw the first one
        mContext = getApplicationContext();

        //Create the internal game gameBoard
        gameBoard = new gameBoard();
        imgBoard = new imgBoard(mContext,
                getResources(),
                (Display) getWindowManager().getDefaultDisplay(),
                gameBoard,
                (TableLayout) findViewById(R.id.tlBoard),
                (Spinner) findViewById(R.id.sSource),
                (TextView) findViewById(R.id.tvPlayer),
                (TextView) findViewById(R.id.tvJail)
        );
        imgBoard.setApplication(app);

        //Create dice options
        final NumberPicker npDie1 = (NumberPicker) findViewById(R.id.npDie1);
        final NumberPicker npDie2 = (NumberPicker) findViewById(R.id.npDie2);

        npDie1.setMinValue(0); npDie1.setMaxValue(6); npDie1.setEnabled(false);
        npDie2.setMinValue(0); npDie2.setMaxValue(6); npDie2.setEnabled(false);

        //NEW GAME BUTTON
        Button btnCreate = (Button) findViewById(R.id.bNewGame);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Is there already a legal running game?
                if(gameBoard.isBoardLegal() && !gameBoard.isGameWon()) {

                    //Create the Alert Box
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            homePage.this);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("There is a running game already. Creating a new game will make you loose your progress." +
                                    " Are you sure you want to start a new game?")
                            .setTitle("Confirm New Game")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Toast.makeText(mContext, "New Game Started", Toast.LENGTH_SHORT).show();
                                    newGame();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
                //Open to new game!
                else {
                    newGame();
                }
            }
        });

        //MOVE BUTTON
        final Button btnMove = (Button) findViewById(R.id.bMove);
        btnMove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //Get the source value (column selection)
                Spinner sSpin = (Spinner) findViewById(R.id.sSource);
                int iSrc = Integer.parseInt(sSpin.getSelectedItem().toString());

                //Get the states of the spinners (contain the value of our dice)
                CheckBox cbDie1 = (CheckBox) findViewById(R.id.cbDie1);
                CheckBox cbDie2 = (CheckBox) findViewById(R.id.cbDie2);

                gameBoard.MoveMsg mMsg = com.MagikSquirrel.backgammon.gameBoard.MoveMsg.INVALID;
                int iCount = 0;

                //As per rule: "The same checker may be moved twice, as long as the two moves
                //          can be made separately and legally: six and then three, or three and then six."
                if(cbDie1.isChecked() && cbDie2.isChecked()) {
                    mMsg = com.MagikSquirrel.backgammon.gameBoard.MoveMsg.BOTH_DICE;
                }

                else if(cbDie1.isChecked()) {
                    iCount += npDie1.getValue();
				}

                else if(cbDie2.isChecked()) {
                    iCount += npDie2.getValue();
                }

                //Dealing with a jailing?
                if(iSrc == -1){
                    mMsg = gameBoard.unjailPiece(iCount);
                    //Toast.makeText(mContext, "Unjailing was a :"+mMsg.toString(), Toast.LENGTH_SHORT).show();
                }
                //Normal Movement
                else if(iCount != 0) {

                    //Move the piece
                    mMsg = gameBoard.movePiece(iSrc, iCount, false);
                }
				
                //Was the move successful?
                if(mMsg == com.MagikSquirrel.backgammon.gameBoard.MoveMsg.VALID_COMPLETE) {

                    //Exhaust the use of Die1.
                    if( (cbDie1.isChecked()) && (gameBoard.getDie1(true) >= 0) ) {
                        cbDie1.setChecked(false);
                        cbDie1.setEnabled(false);
                    }

                    //Exhaust the use of Die2.
                    if( (cbDie2.isChecked()) && (gameBoard.getDie2(true) >= 0) ) {
                        cbDie2.setChecked(false);
                        cbDie2.setEnabled(false);
                    }

                    //Was that a winner?
                    if(gameBoard.isGameWon()) {
                        Toast.makeText(mContext, gameBoard.getCurrentPlayer().toString()+" wins!", Toast.LENGTH_LONG).show();
                        cbDie1.setEnabled(false);
                        cbDie2.setEnabled(false);
                        btnMove.setEnabled(false);
                    }

                    //If both dies are disabled, switch player and re-enable.
                    else if (!cbDie1.isEnabled() && !cbDie2.isEnabled()) {
                        cbDie1.setEnabled(true);
                        cbDie2.setEnabled(true);
                        gameBoard.swapCurrentPlayer();

                        //Enable roll, disable move
                        Button btnRoll = (Button) findViewById(R.id.bRoll);
                        btnRoll.setEnabled(true);
                        btnMove.setEnabled(false);
                    }
				}
				//Couldn't move there, let's say why.
				else {
					Toast.makeText(mContext, "Can't move there: "+mMsg.toString(), Toast.LENGTH_LONG).show();
				}

                //Redraw the board
                imgBoard.redrawBoard();
                imgBoard.updateSpinnerChoices();
            }
        });

        //CLEAR BUTTON
        Button btnClear = (Button) findViewById(R.id.bClear);
        btnClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //Create the Alert Box
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        homePage.this);

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to clear the board?")
                        .setTitle("Confirm Clearing Board")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                clearGame();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        //REFRESH BUTTON
        Button btnRefresh = (Button) findViewById(R.id.bRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //Get the source value (column selection)
                Spinner sSpin = (Spinner) findViewById(R.id.sSource);
                int iSrc = Integer.parseInt(sSpin.getSelectedItem().toString());

                int iVal = gameBoard.getPiecesInColumn(iSrc);
                Toast.makeText(mContext, "Column has this many pieces :"+Integer.toString(iVal), Toast.LENGTH_SHORT).show();

                imgBoard.redrawBoard();

                //Give the spinner ALL choices
                List<String> lsSources = new ArrayList<String>();
                for(int i=0 ; i<=24 ; i++)
                    lsSources.add(Integer.toString(i));
                
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_item, lsSources);
                sSpin.setAdapter(adapter);

            }
        });

        //ROLL DICE BUTTON
        final Button btnRoll = (Button) findViewById(R.id.bRoll);
        btnRoll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //Are we rolling for real or starting a new game?
                if (gameBoard.isBoardLegal()) {

                    //Roll and lets see if we have a move.
                    if (!gameBoard.rollDice()) {
                        Toast.makeText(mContext, gameBoard.getCurrentPlayer().toString() + " can't move with this roll.", Toast.LENGTH_SHORT).show();

                        //Swap players
                        gameBoard.swapCurrentPlayer();

                        //Update Spinner
                        imgBoard.updateSpinnerChoices();

                        //Redraw the board.
                        imgBoard.showCurrentPlayer();

                        //Enable Roll
                        btnRoll.setEnabled(true);

                        //Disable Move
                        btnMove.setEnabled(false);
                    }

                    //Regular movable
                    else {
                        //Disable Roll
                        btnRoll.setEnabled(false);

                        //Enable Move
                        btnMove.setEnabled(true);
                    }

                    //Doubles!
                    if (gameBoard.getDie1() == gameBoard.getDie2()) {
                        Toast.makeText(mContext, gameBoard.getCurrentPlayer().toString() + " rolled doubles!", Toast.LENGTH_SHORT).show();
                    }

                    //Set the Number Pickers to those
                    npDie1.setValue(Math.abs(gameBoard.getDie1()));
                    npDie2.setValue(Math.abs(gameBoard.getDie2()));

                }

                //No, this is a starting game we're rolling to see who goes first.
                else if(gameBoard.rollForFirst() == com.MagikSquirrel.backgammon.gameBoard.Player.NULL){

                    int iDie1 = gameBoard.getDie1(false);
                    int iDie2 = gameBoard.getDie2(false);

                    if(iDie1 == 0) {

                    }

                    else if (iDie2 == 0) {
                        Toast.makeText(mContext, app.getBlackName() + " rolled a " + Integer.toString(iDie1),
                                Toast.LENGTH_SHORT).show();

                        imgBoard.showCurrentPlayer(com.MagikSquirrel.backgammon.gameBoard.Player.WHITE);
                        npDie1.setValue(iDie1);
                    }
                    else {
                        Toast.makeText(mContext, app.getWhiteName() + " rolled a " + Integer.toString(iDie2),
                                Toast.LENGTH_SHORT).show();
                        npDie2.setValue(iDie2);

                        btnRoll.performClick();

                        //Are they the same, we have to start over...
                        if(iDie1 == iDie2) {

                            Toast.makeText(mContext, "Reroll to see who goes first!",
                                    Toast.LENGTH_SHORT).show();

                            //Set the Player to Black (text only)
                            imgBoard.showCurrentPlayer(com.MagikSquirrel.backgammon.gameBoard.Player.BLACK);
                            homePage.this.setUIObjects(false, true, 0, true, null);
                        }
                    }

                }
                else {
                    imgBoard.showCurrentPlayer();

                    Toast.makeText(mContext, gameBoard.getCurrentPlayer().toString() + " goes first.",
                            Toast.LENGTH_SHORT).show();

                    npDie1.setMinValue(1);
                    npDie2.setMinValue(1);

                    btnMove.setEnabled(true);
                    btnRoll.setEnabled(false);

                    imgBoard.updateSpinnerChoices();
                }
            }
        });

        //Checkbox Use Dice
        final CheckBox cbDie1 = (CheckBox) findViewById(R.id.cbDie1);
        final CheckBox cbDie2 = (CheckBox) findViewById(R.id.cbDie2);

        cbDie1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(cbDie2.isChecked())
                    cbDie2.setChecked(false);
            }
        });

        cbDie2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(cbDie1.isChecked())
                    cbDie1.setChecked(false);
            }
        });

    }

    //Set the buttons and pickers. Here we can enable/disable the Move/roll buttons
    //Set the mix number picker values, set to that, and even disable the cheating of them.
    public void setUIObjects(Boolean setMove, Boolean setRoll, Integer pickMin, Boolean setToPickMin, Boolean setCheatingPicker) {
        final NumberPicker npDie1 = (NumberPicker) findViewById(R.id.npDie1);
        final NumberPicker npDie2 = (NumberPicker) findViewById(R.id.npDie2);
        final Button btnRoll = (Button) findViewById(R.id.bRoll);
        final Button btnMove = (Button) findViewById(R.id.bMove);

        if(setMove != null)
            btnMove.setEnabled(setMove);
        if(setRoll != null)
            btnRoll.setEnabled(setRoll);
        if(pickMin != null) {
            npDie1.setMinValue(pickMin);
            npDie2.setMinValue(pickMin);
            if(setToPickMin != null) {
                npDie1.setValue(pickMin);
                npDie2.setValue(pickMin);
            }
        }
        if(setCheatingPicker != null) {
            npDie1.setEnabled(setCheatingPicker);
            npDie2.setEnabled(setCheatingPicker);
        }
    }

    //Clears the board for a new game.
    private void clearGame() {

        setUIObjects(false, false, 0, true, null);
        gameBoard.setBoardEmpty();
        imgBoard.redrawBoard();
    }

    //Sets up the board and pieces as necessary then configures the buttons and die.
    private void newGame() {

        setUIObjects(false, true, 0, true, null);

        gameBoard.setBoardNew();
        imgBoard.redrawBoard();

        //Give black a go
        Spinner sSpin = (Spinner) findViewById(R.id.sSource);
        imgBoard.updateSpinnerChoices();

        //Enable check-boxes
        CheckBox cbDie1 = (CheckBox) findViewById(R.id.cbDie1);
        CheckBox cbDie2 = (CheckBox) findViewById(R.id.cbDie2);
        cbDie1.setEnabled(true);
        cbDie2.setEnabled(true);

        //Set the Player to Black (text only)
        imgBoard.showCurrentPlayer(com.MagikSquirrel.backgammon.gameBoard.Player.BLACK);
    }

}
