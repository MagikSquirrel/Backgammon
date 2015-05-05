package com.MagikSquirrel.backgammon;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Random;

public class homePage extends ActionBarActivity {

    private Context mContext;
    private gameBoard gameBoard;
    private imgBoard imgBoard;
	private boolean bDoubles1;
	private boolean bDoubles2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Draw the first one
        mContext = getApplicationContext();


        //Create the internal game gameBoard
        gameBoard = new gameBoard();
        imgBoard = new imgBoard(mContext,
                            getResources(),
                            (Display) getWindowManager().getDefaultDisplay(),
                            (TableLayout) findViewById(R.id.tlBoard),
                            (TextView) findViewById(R.id.tvPlayer),
							(TextView) findViewById(R.id.tvJail)
        );

        //Create dice options
        final NumberPicker npDie1 = (NumberPicker) findViewById(R.id.npDie1);
        npDie1.setMinValue(1); npDie1.setMaxValue(6);
        final NumberPicker npDie2 = (NumberPicker) findViewById(R.id.npDie2);
        npDie2.setMinValue(1); npDie2.setMaxValue(6);

        //NEW GAME BUTTON
        Button btnCreate = (Button) findViewById(R.id.bNewGame);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, "New Game Started", Toast.LENGTH_SHORT).show();

                gameBoard.newGame(com.MagikSquirrel.backgammon.gameBoard.Player.BLACK);
                imgBoard.redrawBoard(gameBoard);

                //Give black a go
                Spinner sSpin = (Spinner) findViewById(R.id.sSource);
                imgBoard.updateSpinnerChoices(gameBoard, sSpin, android.R.layout.simple_spinner_item);

                //Enable check-boxes
                CheckBox cbDie1 = (CheckBox) findViewById(R.id.cbDie1);
                CheckBox cbDie2 = (CheckBox) findViewById(R.id.cbDie2);
                cbDie1.setEnabled(true);
                cbDie2.setEnabled(true);

                //Disable number picker
                npDie1.setEnabled(false);
                npDie2.setEnabled(false);

                //Enable Roll
                Button btnRoll = (Button) findViewById(R.id.bRoll);
                btnRoll.setEnabled(true);
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
                int iCount = 0;

                if(cbDie1.isChecked()) {
                    iCount += npDie1.getValue();
				}

                if(cbDie2.isChecked()) {
                    iCount += npDie2.getValue();
                }

                //Dealing with a jailing?
                gameBoard.MoveMsg mMsg = com.MagikSquirrel.backgammon.gameBoard.MoveMsg.INVALID;
                if(iSrc == -1){
                    mMsg = gameBoard.unjailPiece(iCount);
                    Toast.makeText(mContext, "Unjailing was a :"+mMsg.toString(), Toast.LENGTH_SHORT).show();
                }
                //Normal Movement
                else {

                    //Move the piece
                    mMsg = gameBoard.movePiece(iSrc, iCount, false);
                }
				
                //Was the move successful?
                if(mMsg == com.MagikSquirrel.backgammon.gameBoard.MoveMsg.VALID_COMPLETE) {

                    //Exhaust the use of Die1.
                    if (cbDie1.isChecked()) {
					
						//Was this a double?
						if(bDoubles1)
							bDoubles1 = false;
						else {					
							cbDie1.setChecked(false);
							cbDie1.setEnabled(false);
						}
                    }

                    //Exhaust the use of Die2.
                    if (cbDie2.isChecked()) {
					
						//Was this a double?
						if(bDoubles2)
                            bDoubles2 = false;
						else {
							cbDie2.setChecked(false);
							cbDie2.setEnabled(false);
						}
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
                imgBoard.redrawBoard(gameBoard);
                imgBoard.updateSpinnerChoices(gameBoard, sSpin, android.R.layout.simple_spinner_item);
            }
        });

        //CLEAR BUTTON
        Button btnClear = (Button) findViewById(R.id.bClear);
        btnClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                gameBoard.emptyGame();
                imgBoard.redrawBoard(gameBoard);

                Button bMove = (Button) findViewById(R.id.bMove);
                bMove.setEnabled(false);
            }
        });

        //DISPLAY ALL BUTTON
        Button btnDisplayAll = (Button) findViewById(R.id.bDisplayAll);
        btnDisplayAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                gameBoard.fullGame();
                imgBoard.redrawBoard(gameBoard);
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

                imgBoard.redrawBoard(gameBoard);

                //Give the spinner ALL choices
                List<String> lsSources = new ArrayList<String>();
                for(int i=0 ; i<=24 ; i++)
                    lsSources.add(Integer.toString(i));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, lsSources);
                sSpin.setAdapter(adapter);

            }
        });

        //DEBUG BUTTON
        final Button btnDebug = (Button) findViewById(R.id.bDebug);
        btnDebug.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                gameBoard.setTestGame(Integer.parseInt(btnDebug.getText().toString()));
                imgBoard.redrawBoard(gameBoard);

                Spinner sSpin = (Spinner) findViewById(R.id.sSource);
                imgBoard.updateSpinnerChoices(gameBoard, sSpin, android.R.layout.simple_spinner_item);

                Button bMove = (Button) findViewById(R.id.bMove);
                bMove.setEnabled(true);
            }
        });

        //ROLL DICE BUTTON
        final Button btnRoll = (Button) findViewById(R.id.bRoll);
        btnRoll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            Random r = new Random();

            //Get random in range of their default min/max vals
            int i1 = r.nextInt(npDie1.getMaxValue() - npDie1.getMinValue() + 1) + npDie1.getMinValue();
            int i2 = r.nextInt(npDie2.getMaxValue() - npDie2.getMinValue() + 1) + npDie2.getMinValue();
			
			//Doubles!
			if(i1 == i2) {
				bDoubles1 = true;
				bDoubles2 = true;
				Toast.makeText(mContext, gameBoard.getCurrentPlayer().toString()+" rolled doubles!", Toast.LENGTH_SHORT).show();
			}			

            //Set the Number Pickers to those
            npDie1.setValue(i1);
            npDie2.setValue(i2);

            //You can only roll once man.
            btnRoll.setEnabled(false);

            //Enable Mover button.
            Button btnMove = (Button) findViewById(R.id.bMove);
            btnMove.setEnabled(true);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_board) {
            setContentView(R.layout.activity_home_page);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            setContentView(R.layout.settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
