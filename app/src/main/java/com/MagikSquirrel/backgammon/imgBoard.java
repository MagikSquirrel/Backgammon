package com.MagikSquirrel.backgammon;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class imgBoard {

    private Context _mContext;
    private Resources _rResources;
    private DisplayMetrics _dmDisplay;
    private TableLayout _tlBoard;
	private TextView _tvPlayer;
	private TextView _tvJail;

    private ImageView[][] imgBoard;

    private static enum Player
    {
        BLACK,
        WHITE
    }

    private Bitmap bmBlack;
    private Bitmap bmWhite;
    private Bitmap bmClear;



    //Constructor
    imgBoard(Context Context, Resources Resources, Display Display, TableLayout Board, TextView Player, TextView Jail)  {
        _mContext = Context;
        _rResources = Resources;
        _tlBoard = Board;
		_tvPlayer = Player;
		_tvJail = Jail;

        //Get the screen resolution.
        _dmDisplay = new DisplayMetrics();
        Display.getMetrics(_dmDisplay);

        //Initialize the board...
        initBoard();
    }

    //This updates the spinner with a list of columns where the current
    //player has pieces available
    public Spinner updateSpinnerChoices(gameBoard gameBoard, Spinner sSource, int id) {	
		
        List<String> lsSources;
		
		//Does this player have jailed pieces?
		if(gameBoard.getPiecesInJail() != 0) {
			lsSources = Arrays.asList("-1");
        }
		//Guess not, so they can move pieces.
		else {
			lsSources = gameBoard.getColumnsWithPieces();
		}
		
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(_mContext,
                id, lsSources);
        sSource.setAdapter(adapter);

        return sSource;
    }
	
	//Updates the Current Player Text
	public void showCurrentPlayer (gameBoard Board){
        if(Board.getCurrentPlayer() == gameBoard.Player.BLACK)
		    _tvPlayer.setText("Player: Black");
        else if(Board.getCurrentPlayer() == gameBoard.Player.WHITE)
            _tvPlayer.setText("Player Red");
	}
	
	//Updates the Jail Count Text
	public void showJailCount (gameBoard Board){
        int iBlack = Board.getPiecesInJail(gameBoard.Player.BLACK);
		int iWhite = Board.getPiecesInJail(gameBoard.Player.WHITE);
		
		String sOut = "Jail - Black: "+Integer.toString(iBlack)+" Red: "+Integer.toString(iWhite);
		_tvJail.setText(sOut);
	}	

    //Sets an image view to be owned by a particular player (or empty if null)
    private void setImageViewOwner(Player player, ImageView iv) {

        try {

            if (player == Player.BLACK) {
                iv.setImageBitmap(bmBlack);
                //iv.setVisibility(View.VISIBLE);
            } else if (player == Player.WHITE) {
                iv.setImageBitmap(bmWhite);
                //iv.setVisibility(View.VISIBLE);
            } else {
                iv.setImageBitmap(bmClear);
                //iv.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception e){

        }
    }

    //This initializes the gameBoard, and all the game pieces
    //It spaces out the columns and rows appropriately.
    private void initBoard() {

        //This is what will hold the 2da for image views
        imgBoard = new ImageView[24][8];

        //Get the screen spacing variables
        imgResolution iRes = new imgResolution(_dmDisplay.widthPixels, _dmDisplay.heightPixels);

        //Set the 3 Bitmaps which will be used heavily
        bmBlack = BitmapFactory.decodeResource(_rResources, R.drawable.black);
        bmWhite = BitmapFactory.decodeResource(_rResources, R.drawable.red);
        bmClear = BitmapFactory.decodeResource(_rResources, R.drawable.empty);

        bmBlack = Bitmap.createScaledBitmap(bmBlack, iRes._piece, iRes._piece, true);
        bmWhite = Bitmap.createScaledBitmap(bmWhite, iRes._piece, iRes._piece, true);
        bmClear = Bitmap.createScaledBitmap(bmClear, iRes._piece, iRes._piece, true);

        //Top Down Border Pixels
        Bitmap bmTD = BitmapFactory.decodeResource(_rResources, R.drawable.empty); //R.dawable.orange);
        bmTD = Bitmap.createScaledBitmap(bmTD, iRes._misc, iRes._edgetd, true);

        //Left Right Border Pixels
        Bitmap bmLR = BitmapFactory.decodeResource(_rResources, R.drawable.empty); //R.dawable.green);
        bmLR = Bitmap.createScaledBitmap(bmLR, iRes._edgelr, iRes._misc, false);

        //Middle (Between Top and Down) Border Pixels
        Bitmap bmMidTD = BitmapFactory.decodeResource(_rResources, R.drawable.empty); //R.dawable.yellow);
        bmMidTD = Bitmap.createScaledBitmap(bmMidTD, iRes._misc, iRes._midtd/*this one*/, true);

        //Middle (Between Left and Right) Border Pixels
        Bitmap bmMidLR = BitmapFactory.decodeResource(_rResources, R.drawable.empty); //R.dawable.grey);
        bmMidLR = Bitmap.createScaledBitmap(bmMidLR, iRes._midlr, iRes._misc, true);

        //Middle (Between Left and Right) Border Pixels
        Bitmap bmDivider = BitmapFactory.decodeResource(_rResources, R.drawable.empty); //R.dawable.pink);
        bmDivider = Bitmap.createScaledBitmap(bmDivider, iRes._divider, iRes._misc, true);

        //Now we build the table
        for (int iRow = 0; iRow < 5; iRow++) {
            TableRow tr = new TableRow(_mContext);

            //Border
            if (iRow == 0 || iRow == 4) {
                ImageView iv = new ImageView(_mContext);
                iv.setImageBitmap(bmTD);
                tr.addView(iv);
            }
            //Middle Break
            else if (iRow == 2) {
                ImageView iv = new ImageView(_mContext);
                iv.setImageBitmap(bmMidTD);
                tr.addView(iv);
            }
            //Innerboard
            else {

                for (int iCol = 0; iCol < 27; iCol++) {
                    TableLayout tc = new TableLayout(_mContext);

                    //Left (and Right) Border
                    if (iCol == 0 || iCol == 26) {
                        ImageView iv = new ImageView(_mContext);
                        iv.setImageBitmap(bmLR);
                        tc.addView(iv);
                    }
                    //Middle divider
                    else if (iCol == 13) {
                        ImageView iv = new ImageView(_mContext);
                        iv.setImageBitmap(bmDivider);
                        tc.addView(iv);
                    }
                    //Middle between column border
                    else if (iCol % 2 == 0) {
                        ImageView iv = new ImageView(_mContext);
                        iv.setImageBitmap(bmMidLR);
                        tr.addView(iv);
                    } else {

                        //WHich graphical column does this translate to in game gameBoard columns?
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

                            ImageView iv = new ImageView(_mContext);
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

            _tlBoard.addView(tr);
        }

        //Now we just hide those pieces.
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 8; j++) {
                setImageViewOwner(null, imgBoard[i][j]);
            }
        }
    }

    public void redrawBoard(gameBoard Board)
    {
        for(int i=0 ; i<24 ; i++) {

            int iCount = Board.getPiecesInColumn(i);

            for(int j=0 ; j<8 ; j++) {
                if(Math.abs(iCount) > j) {
                    if(iCount < 0)
                        setImageViewOwner(Player.BLACK, imgBoard[i][j]);
                    else if(iCount > 0)
                        setImageViewOwner(Player.WHITE, imgBoard[i][j]);
                    else
                        setImageViewOwner(null, imgBoard[i][j]);
                }
                else
                    setImageViewOwner(null, imgBoard[i][j]);
            }
        }

        //Update the player
        showCurrentPlayer(Board);
		
		//Update the jail count
		showJailCount(Board);
    }

}
