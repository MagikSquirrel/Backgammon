package com.MagikSquirrel.backgammon;

import android.app.ActionBar;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class imgBoard {

	//FIELDS
    private Context _mContext;
    private Resources _rResources;
    private DisplayMetrics _dmDisplay;
    private gameBoard _gBoard;
    private TableLayout _tlBoard;

	private TextView _tvPlayer;
	private TextView _tvJail;
    private Spinner _sSrcPoint;

    private ImageView[][] imgBoard;

	private Bitmap bmBlack;
    private Bitmap bmWhite;
    private Bitmap bmBlack2;
    private Bitmap bmWhite2;
    private Bitmap bmBlackPick;
    private Bitmap bmWhitePick;
    private Bitmap bmClear;
    private Bitmap bmClearPick;

    private backgammonApp _application;

    private String _sWhiteName = "Red";
    private String _sBlackName = "Black";
    private boolean showHints = true;

    //CONSTRUCTOR
    imgBoard(Context Context, Resources Resources, Display Display, gameBoard gBoard, TableLayout tlBoard, Spinner sSrcPoint, TextView Player, TextView Jail)  {
        _mContext = Context;
        _rResources = Resources;
        _tlBoard = tlBoard;
        _sSrcPoint = sSrcPoint;
		_tvPlayer = Player;
		_tvJail = Jail;
		_gBoard = gBoard;

        //Get the screen resolution.
        _dmDisplay = new DisplayMetrics();
        Display.getMetrics(_dmDisplay);
        imgResolution iRes = new imgResolution(_dmDisplay.widthPixels, _dmDisplay.heightPixels);

		//Initialize the bitmaps
		initBitmaps(iRes);

        //Initialize the board...
        initBoard(iRes);
    }

	//Initializes the Bitmaps that will be used repeatedly.
	private void initBitmaps(imgResolution iRes) {

        bmBlack = BitmapFactory.decodeResource(_rResources, R.drawable.black);
        bmBlack = Bitmap.createScaledBitmap(bmBlack, iRes._piece, iRes._piece, true);

        bmWhite = BitmapFactory.decodeResource(_rResources, R.drawable.red);
        bmWhite = Bitmap.createScaledBitmap(bmWhite, iRes._piece, iRes._piece, true);

        bmBlack2 = BitmapFactory.decodeResource(_rResources, R.drawable.black2);
        bmBlack2 = Bitmap.createScaledBitmap(bmBlack2, iRes._piece, iRes._piece, true);

        bmWhite2 = BitmapFactory.decodeResource(_rResources, R.drawable.red2);
        bmWhite2 = Bitmap.createScaledBitmap(bmWhite2, iRes._piece, iRes._piece, true);

        bmBlackPick = BitmapFactory.decodeResource(_rResources, R.drawable.blackpick);
        bmBlackPick = Bitmap.createScaledBitmap(bmBlackPick, iRes._piece, iRes._piece, true);

        bmWhitePick = BitmapFactory.decodeResource(_rResources, R.drawable.redpick);
        bmWhitePick = Bitmap.createScaledBitmap(bmWhitePick, iRes._piece, iRes._piece, true);

        bmClear = BitmapFactory.decodeResource(_rResources, R.drawable.empty);
        bmClear = Bitmap.createScaledBitmap(bmClear, iRes._piece, iRes._piece, true);

        bmClearPick = BitmapFactory.decodeResource(_rResources, R.drawable.emptypick);
        bmClearPick = Bitmap.createScaledBitmap(bmClearPick, iRes._piece, iRes._piece, true);
	}

    //This initializes the gameBoard, and all the game pieces
    //It spaces out the columns and rows appropriately.
    private void initBoard(imgResolution iRes) {

        //This is what will hold the 2da for image views
        imgBoard = new ImageView[24][8];

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

                            final ImageView iv = new ImageView(_mContext);

                            if (iRow == 1) {
                                setImageViewOwner(gameBoard.Player.WHITE, iv);

                                //Save the Column number and Piece number for later.
                                iv.setTag(R.id.tvWhite, iBoardCol);
                                iv.setTag(R.id.tvBlack, iPiece);

                                imgBoard[iBoardCol][iPiece] = iv;
                            } else {
                                setImageViewOwner(gameBoard.Player.BLACK, iv);

                                //Save the Column number and Piece number for later.
                                iv.setTag(R.id.tvWhite, iBoardCol);
                                iv.setTag(R.id.tvBlack, (7-iPiece));

                                imgBoard[iBoardCol][7 - iPiece] = iv;
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

	//METHODS
	//This updates the spinner with a list of columns where the current
    //player has pieces available
    public void updateSpinnerChoices() {

        List<String> lsSources;

		//Does this player have jailed pieces?
		if(_gBoard.getPiecesInJail() != 0) {
			lsSources = Arrays.asList("-1");
        }
		//Guess not, so they can move pieces.
		else {
			lsSources = _gBoard.getColumnsWithPieces();
		}

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(_mContext,
                R.layout.spinner_item, lsSources);
        _sSrcPoint.setAdapter(adapter);

    }
    public void setSpinnerChoice( int iChoice ) {

        ArrayAdapter aChoices = (ArrayAdapter) _sSrcPoint.getAdapter();
        try {
            int iPosition = aChoices.getPosition(Integer.toString(iChoice));
            _sSrcPoint.setSelection(iPosition);
        }
        //Spinner has no choices which means the array adapter was never initilized.
        catch (NullPointerException e) {
            updateSpinnerChoices();
            aChoices.getPosition(Integer.toString(iChoice));
            int iPosition = aChoices.getPosition(Integer.toString(iChoice));
            _sSrcPoint.setSelection(iPosition);

            //If we get an exception here then something is really wrong!
        }
    }

	//Updates the Current Player Text
	public void showCurrentPlayer (){
        if(_gBoard.getCurrentPlayer() == gameBoard.Player.BLACK)
		    _tvPlayer.setText("Player: (B)"+getBlackName());
        else if(_gBoard.getCurrentPlayer() == gameBoard.Player.WHITE)
            _tvPlayer.setText("Player: (R)"+getWhiteName());
	}

	//Updates the Jail Count Text
	public void showJailCount (){
        int iBlack = _gBoard.getPiecesInJail(gameBoard.Player.BLACK);
		int iWhite = _gBoard.getPiecesInJail(gameBoard.Player.WHITE);

		String sOut = "Jail - (Black): "+Integer.toString(iBlack)+" "+
                "(Red): "+Integer.toString(iWhite);
		_tvJail.setText(sOut);
	}

    //Sets the default image, and pressed image as well as creation teh listeneres.
    private void setDefaultAndPressedImages(final ImageView iv, final Bitmap bmDefault, final Bitmap bmPressed) {

        //Default is the down image.
        iv.setImageBitmap(bmDefault);

        //nulls get nothing
        if(bmDefault == null || bmPressed == null) {
            iv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return false;
                }
            });
        }
        else {

            iv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    //Get the Point and Height of this piece from Tags
                    int iPoint = (int) iv.getTag(R.id.tvWhite);
                    int iHeight = (int) iv.getTag(R.id.tvBlack);

                    //Only the current owner can touch a piece
                    if(_gBoard.getColumnsWithPieces().contains(Integer.toString(iPoint)) )
                    {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                iv.setImageBitmap(bmPressed);

                                //Set spinner to this!
                                setSpinnerChoice(iPoint);

                                //Show moves (if we're allowed)
                                if(getShowHints())
                                    showMoves(iPoint);

                                break;
                            case MotionEvent.ACTION_UP:
                                iv.setImageBitmap(bmDefault);

                                redrawBoard();

                                break;
                        }

                        return true;
                    }
                    return  false;
                }
            });
        }
    }

    //Sets an image view to be owned by a particular player (or empty if null)
	private void setImageViewOwner(gameBoard.Player player, final ImageView iv, int Count) {
        try {

            //Black player
            if (player == gameBoard.Player.BLACK) {

                //Over 8 pieces already exist
				if(Math.abs(Count) > 8) {
                    setDefaultAndPressedImages(iv, bmBlack2, bmBlackPick);
                }

                //8 or less
                else {
                    setDefaultAndPressedImages(iv, bmBlack, bmBlackPick);
                }

            //White player
			} else if (player == gameBoard.Player.WHITE) {

                //Over 8 pieces
				if(Math.abs(Count) > 8) {
                    setDefaultAndPressedImages(iv, bmWhite2, bmWhitePick);

                //8 or less
				}
                else {
                    setDefaultAndPressedImages(iv, bmWhite, bmWhitePick);
                }
            } else {
                setDefaultAndPressedImages(iv, bmClear, null);
            }
        }
        catch (Exception e){

        }
	}
    //If count isn't specified, assume 1
    private void setImageViewOwner(gameBoard.Player player, ImageView iv) {
		setImageViewOwner(player, iv, 1);
    }

    public void showMoves(int iSrc) {
        boolean[] bMoves = _gBoard.getAllowedMovesByDice(iSrc);

        for(int i=0 ; i<bMoves.length ; i++) {

            //Only worry about places we can go
            if (bMoves[i] == true) {
                //Get the first piece!
                ImageView ivDst = (ImageView) this.imgBoard[i][0];

                ivDst.setImageBitmap(bmClearPick);
            }
        }
    }

    public void redrawBoard()
    {
        for(int i=0 ; i<24 ; i++) {

            int iCount = _gBoard.getPiecesInColumn(i);

            for(int j=0 ; j<8 ; j++) {
                if(Math.abs(iCount) > j) {
                    if(iCount < 0)
                        setImageViewOwner(gameBoard.Player.BLACK, imgBoard[i][j], iCount+j);
                    else if(iCount > 0)
                        setImageViewOwner(gameBoard.Player.WHITE, imgBoard[i][j], iCount-j);
                    else
                        setImageViewOwner(null, imgBoard[i][j]);
                }
                else
                    setImageViewOwner(null, imgBoard[i][j]);
            }
        }

        //Update the player
        showCurrentPlayer();

		//Update the jail count
		showJailCount();
    }

    public backgammonApp getApplication() {
        return _application;
    }

    public void setApplication(backgammonApp application) {
        this._application = application;
    }

    private String getWhiteName() {
        if(this._application != null)
        {
            _sWhiteName = this._application.getWhiteName();
        }
        return _sWhiteName;
    }

    private String getBlackName() {
        if(this._application != null)
        {
            _sBlackName = this._application.getBlackName();
        }
        return _sBlackName;
    }

    private boolean getShowHints() {
        if(this._application != null)
        {
            showHints = this._application.getShowHints();
        }
        return showHints;
    }
}
