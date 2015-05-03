package com.MagikSquirrel.backgammon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class homePage extends ActionBarActivity {

    private Context mContext;
    private gameBoard gameBoard;
    private imgBoard imgBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Draw the first one
        mContext = getApplicationContext();

        //Create the internal game gameBoard
        gameBoard = new gameBoard();
        imgBoard = new imgBoard(mContext, getResources(), (TableLayout) findViewById(R.id.tlBoard));

        //Create dice options
        final NumberPicker npDie1 = (NumberPicker) findViewById(R.id.npDie1);
        npDie1.setMinValue(1); npDie1.setMaxValue(6);
        final NumberPicker npDie2 = (NumberPicker) findViewById(R.id.npDie2);
        npDie2.setMinValue(1); npDie2.setMaxValue(6);

        //NEW GAME BUTTON
        Button btnCreate = (Button) findViewById(R.id.bNewGame);
        btnCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, "New Game Started", Toast.LENGTH_SHORT).show();

                gameBoard.newGame();
                imgBoard.redrawBoard(gameBoard);

                //Give black a go
                Spinner sSpin = (Spinner) findViewById(R.id.sSource);
                imgBoard.updateSpinnerChoices(gameBoard, sSpin, android.R.layout.simple_spinner_item);

                Button bMove = (Button) findViewById(R.id.bMove);
                bMove.setEnabled(true);
            }
        });

        //MOVE ALL BUTTON
        Button btnMove = (Button) findViewById(R.id.bMove);
        btnMove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //Get the source value (clolumn selection)
                Spinner sSpin = (Spinner) findViewById(R.id.sSource);
                int iSrc = Integer.parseInt(sSpin.getSelectedItem().toString());

                //Get the states of the spinners (contain the value of our dice)
                CheckBox cbDie1 = (CheckBox) findViewById(R.id.cbDie1);
                CheckBox cbDie2 = (CheckBox) findViewById(R.id.cbDie2);
                int iCount = 0;

                if(cbDie1.isChecked())
                    iCount += npDie1.getValue();

                if(cbDie2.isChecked())
                    iCount += npDie2.getValue();

                //Move the piece
                int iVal = gameBoard.movePiece(iSrc, iCount, false);
                Toast.makeText(mContext, "Move was a :"+Integer.toString(iVal), Toast.LENGTH_SHORT).show();

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
