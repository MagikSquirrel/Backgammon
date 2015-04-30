package com.MagikSquirrel.backgammon;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class homePage extends ActionBarActivity {

    private Context mContext;
    private board bGameBoard;
    private ImageView[][] imgBoard;

    private static enum Player
    {
        BLACK,
        WHITE
    }

    private Bitmap bmBlack;
    private Bitmap bmWhite;
    private Bitmap bmClear;


    //Sets an image view to be owned by a particular player (or empty if null)
    private void setImageViewOwner(Player player, ImageView iv) {

        try {

            if (player == Player.BLACK) {
                iv.setImageBitmap(bmBlack);
                iv.setVisibility(View.VISIBLE);
            } else if (player == Player.WHITE) {
                iv.setImageBitmap(bmWhite);
                iv.setVisibility(View.VISIBLE);
            } else {
                iv.setImageBitmap(bmClear);
                iv.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception e){

        }
    }

    private void setImageViewOwner(Player player, int id) {
        //Gotta love method overloading!
        setImageViewOwner(player, (ImageView) findViewById(id));
    }

    //This initializes the board, and all the game pieces
    //It spaces out the columns and rows appropriately.
    private void initBoard() {
        //Set the two Bitmaps which will be used heavily
        bmBlack = BitmapFactory.decodeResource(getResources(), R.drawable.black);
        bmWhite = BitmapFactory.decodeResource(getResources(), R.drawable.red);
        bmClear = BitmapFactory.decodeResource(getResources(), R.drawable.pix_empty);

        bmBlack = Bitmap.createScaledBitmap(bmBlack, 50, 50, true);
        bmWhite = Bitmap.createScaledBitmap(bmWhite, 50, 50, true);
        bmClear = Bitmap.createScaledBitmap(bmClear, 50, 50, true);

        //Left Right Border Pixels
        Bitmap bmLR = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        bmLR = Bitmap.createScaledBitmap(bmLR, 39, 5, true);

        //Top Down Border Pixels
        Bitmap bmTD = BitmapFactory.decodeResource(getResources(), R.drawable.orange);
        bmTD = Bitmap.createScaledBitmap(bmTD, 5, 30, true);

        //Middle (Between Top and Bottom) Border Pixels
        Bitmap bmMidTB = BitmapFactory.decodeResource(getResources(), R.drawable.orange);
        bmMidTB = Bitmap.createScaledBitmap(bmMidTB, 5, 12, true);

        //Middle (Between Left and Right) Border Pixels
        Bitmap bmMidLR = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        bmMidLR = Bitmap.createScaledBitmap(bmMidLR, 23, 5, true);

        //Middle (Between Left and Right) Border Pixels
        Bitmap bmDivider = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        bmDivider = Bitmap.createScaledBitmap(bmDivider, 30, 5, true);

        //This is what will hold the 2da for image views
        imgBoard = new ImageView[24][8];

        //Now we build the table
        TableLayout tlBoard = (TableLayout) findViewById(R.id.tlBoard);

        for (int iRow = 0; iRow < 5; iRow++) {
            TableRow tr = new TableRow(mContext);

            //Border
            if (iRow == 0 || iRow == 4) {
                ImageView iv = new ImageView(mContext);
                iv.setImageBitmap(bmTD);
                tr.addView(iv);
            }
            //Middle Break
            else if (iRow == 2) {
                ImageView iv = new ImageView(mContext);
                iv.setImageBitmap(bmMidTB);
                tr.addView(iv);
            }
            //Innerboard
            else {

                for (int iCol = 0; iCol < 27; iCol++) {
                    TableLayout tc = new TableLayout(mContext);

                    //Left (and Right) Border
                    if (iCol == 0 || iCol == 26) {
                        ImageView iv = new ImageView(mContext);
                        iv.setImageBitmap(bmLR);
                        tc.addView(iv);
                    }
                    //Middle divider
                    else if (iCol == 13) {
                        ImageView iv = new ImageView(mContext);
                        iv.setImageBitmap(bmDivider);
                        tc.addView(iv);
                    }
                    //Middle between column border
                    else if (iCol % 2 == 0) {
                        ImageView iv = new ImageView(mContext);
                        iv.setImageBitmap(bmMidLR);
                        tr.addView(iv);
                    } else {

                        //WHich graphical column does this translate to in game board columns?
                        int iBoardCol = -1;
                        switch(iCol){
                            case 1: iBoardCol = 11; break;
                            case 3: iBoardCol = 10; break;
                            case 5: iBoardCol = 9; break;
                            case 7: iBoardCol = 8; break;
                            case 9: iBoardCol = 7; break;
                            case 11: iBoardCol = 6; break;
                            case 15: iBoardCol = 5; break;
                            case 17: iBoardCol = 4; break;
                            case 19: iBoardCol = 3; break;
                            case 21: iBoardCol = 2; break;
                            case 23: iBoardCol = 1; break;
                            case 25: iBoardCol = 0; break;
                        }
                        //Bottom
                        if(iRow != 1){
                            iBoardCol = (11-iBoardCol);
                            iBoardCol += 12;
                        }

                        //There is a max (currently of 8 Pieces per "Cell")
                        for (int iPiece = 0; iPiece < 8; iPiece++) {

                            ImageView iv = new ImageView(mContext);
                            if (iRow == 1) {
                                setImageViewOwner(Player.WHITE, iv);
                                imgBoard[(iBoardCol)][iPiece] = iv;
                            } else {
                                setImageViewOwner(Player.BLACK, iv);
                                imgBoard[(iBoardCol)][7 - iPiece] = iv;
                            }
                            tc.addView(iv);
                        }
                    }

                    tr.addView(tc);
                }
            }

            tlBoard.addView(tr);
        }

        //Now we just hide those pieces.
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 8; j++) {
                setImageViewOwner(null, imgBoard[i][j]);
            }
        }
    }

    private void redrawBoard()
    {
        for(int i=0 ; i<24 ; i++) {
            
            int iCount = bGameBoard.getPiecesInColumn(i);
            
            for(int j=0 ; j<8 ; j++) {
                if(Math.abs(iCount) > j) {
                    if(iCount < 1)
                        setImageViewOwner(Player.BLACK, imgBoard[i][j]);
                    else if(iCount > 1)
                        setImageViewOwner(Player.WHITE, imgBoard[i][j]);
                    else if(iCount == 0)
                        setImageViewOwner(null, imgBoard[i][j]);
                }
                else if(iCount == 0)
                    setImageViewOwner(null, imgBoard[i][j]);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Create the internal game board
        bGameBoard = new board();

        //Draw the first one
        mContext = getApplicationContext();
        initBoard();

        //NEW GAME BUTTON
        Button btnCreate = (Button) findViewById(R.id.bNewGame);
        btnCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                mContext = getApplicationContext();

                Toast.makeText(mContext, "New Game Started", Toast.LENGTH_LONG).show();

                bGameBoard.newGame();
                redrawBoard();
            }
        });

        //CLEAR BUTTON
        Button btnClear = (Button) findViewById(R.id.bClear);
        btnClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final EditText etBlack = (EditText) findViewById(R.id.etBlackName);
                etBlack.setText("");

                final EditText etWhite = (EditText) findViewById(R.id.etWhiteName);
                etWhite.setText("");

                bGameBoard.emptyGame();
                redrawBoard();
            }
        });

        //DISPLAY ALL BUTTON
        Button btnDisplayAll = (Button) findViewById(R.id.bDisplayAll);
        btnDisplayAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                bGameBoard.fullGame();
                redrawBoard();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
