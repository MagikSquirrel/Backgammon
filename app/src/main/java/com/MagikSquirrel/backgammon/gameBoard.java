package com.MagikSquirrel.backgammon;

import android.util.Log;

public class gameBoard {
    private int[] _board;
    private int _outblack; //Pieces that have been jailed
    private int _outwhite;

    private String _log = "";

    //Constructor
    gameBoard() {
        _board = new int[24];
        _outblack = 0;
        _outwhite = 0;

        for(int i=0 ; i<24 ; i++) {
            _board[i] = 0;
        }
    }

    //This fills the gameboard with pieces (not a real state)
    //This is used for testing piece graphics
    public void fullGame() {
        for(int i=0 ; i< _board.length ; i++){
            _board[i] = -8;
        }
    }


    //This emptys the gameboard of pieces (not a real state)
    //This is used for testing piece graphics
    public void emptyGame() {
        for(int i=0 ; i< _board.length ; i++){
            _board[i] = 0;
        }
    }

    //Setup New game by placing pieces where they should
    //Negatives are black, positives are white
    public void newGame() {
        //black home
        _board[0] = -2;
        _board[5] = 5;

        //black outer
        _board[7] = 3;
        _board[11] = -5;

        //white outer
        _board[12] = 5;
        _board[16] = -3;

        //white home
        _board[18] = -2;
        _board[23] = 2;
    }

    public void print() {

        String sTop = "";
        String sBot = "";

        for(int i=0 ; i< 24 ; i++) {
            //This is a top row
            if(i <= 11) {
                sTop = (_board[i] + "|" + sTop);
            }

            //This is a bottom row
            else {
                sBot =  (sBot + _board[i] + "|");
            }
        }

        Log.i(_log, sTop + "\n" + sBot);
        Log.i(_log, "Out: Black ("+_outblack+") White ("+_outwhite+")\n");
    }

    //Internal code to move pieces easily.
    private void _move(int iSrc, int iDst) {

        //Get the side of the source
        int iSrcSide = _board[iSrc];

        _board[iSrc] = ( iSrcSide < 0 ? _board[iSrc]+1 : _board[iSrc]-1);
        _board[iDst] = ( iSrcSide < 0 ? _board[iDst]-1 : _board[iDst]+1);

        //Did it clear out the position?
        if(_board[iDst] == 0) {
            _board[iDst] = ( iSrcSide < 0 ? _board[iDst]-1 : _board[iDst]+1);
        }
    }

    public int unjailPiece(int iRoll, boolean bWhite) {

        int iDst;
        iDst = ( bWhite == true ? iRoll-1 : (24-iRoll));

        //Free?             OR      Same side add
        if( (_board[iDst] == 0) || (bWhite && _board[iDst] < 0) ) {
            Log.i(_log, "Freedom!");
            if(bWhite) {
                _board[iDst]--;
                _outwhite--;
            }
            else {
                _board[iDst]++;
                _outblack--;
            }
        }
        //Same team add
        else if(bWhite ^ _board[iDst] < 0) {
            System.out.print("Opposite side unjail - ");
            //Jail the enemy!
            if(Math.abs(_board[iDst]) == 1) {
                Log.i(_log, " Counterjail!");
                if(bWhite) {
                    _board[iDst]-=2;
                    _outwhite--;
                    _outblack++;
                }
                else {
                    _board[iDst]+=2;
                    _outblack--;
                    _outwhite++;
                }
            }
            //Can't move here, blocked
            else {
                Log.i(_log, " Blocked. Try again.");
                return -1;
            }
        }

        return 0;
    }

    public int getPiecesInColumn(int i){
        return _board[i];
    }

    public int[] getCount(){
        return _board;
    }

    public int movePiece(int iSrc, int iCount) {

        //Where is this going?
        int iDst = (iSrc + iCount);

        //Are these in bounds?
        if(iSrc < 0 || iDst < 0 || iSrc >= 24 || iDst >= 24) {
            Log.i(_log, "Out of bounds move!");
            return -1;
        }
        //Do we have a piece to move?
        else if(_board[iSrc] == 0) {
            Log.i(_log, "No source piece!");
            return -1;
        }
        //Is the space free?
        else if(_board[iDst] == 0) {
            Log.i(_log, "No Team!");
            _move(iSrc, iDst);
        }
        //Is the dest the same team as source?
        else if (!(_board[iSrc] < 0 ^ _board[iDst] < 0)) {
            Log.i(_log, "Same Team!");
            _move(iSrc, iDst);
        }
        else {
            System.out.print("Opposite Team - ");
            //If the enemy only has one piece we can kill it!
            if(Math.abs(_board[iDst]) <= 1) {
                if (_board[iDst] < 0)
                    _outwhite++;
                else
                    _outblack++;

                Log.i(_log, " jailed!");
                _move(iSrc, iDst);
                return 1;
            }

            Log.i(_log, " blocked! Try again.");
            return -1;
        }
        return 0;
    }
}
