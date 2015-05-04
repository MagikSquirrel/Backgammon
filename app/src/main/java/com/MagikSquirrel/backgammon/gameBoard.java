package com.MagikSquirrel.backgammon;

//import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class gameBoard {
    private int[] _board;
    private int _outblack; //Pieces that have been jailed
    private int _outwhite;
    private Player _current;

    public static enum Player
    {
        BLACK,
        WHITE,
        NULL
    }

    private String _log = "";

    //Constructor
    gameBoard() {
        _board = new int[24];
        _outblack = 0;
        _outwhite = 0;
        _current = Player.NULL;

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

    public Player getCurrentPlayer() {
        return _current;
    }

    public void swapCurrentPlayer() {
        if(_current == Player.BLACK){
            _current = Player.WHITE;
        }
        else if(_current == Player.WHITE){
            _current = Player.BLACK;
        }
        else
            _current = Player.BLACK;

    }

    //Setup New game by placing pieces where they should
    //Negatives are black, positives are white
    public void newGame(Player StartingPlayer){
        _current = StartingPlayer;
        newGame();
    }
    public void newGame() {

        //Clear the board.
        emptyGame();

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
        _board[18] = -5;
        _board[23] = 2;
    }
    
	private String printMoveString(boolean b) {
		if(b)
			return "X";
		return "0";
	}
	public void printMoves(boolean moves[]) {
        String sTop = "";
        String sBot = "";

        for(int i=0 ; i< 24 ; i++) {
            //This is a top row
            if(i <= 11) {
                sTop = (printMoveString(moves[i]) + "|" + sTop);
            }

            //This is a bottom row
            else {
                sBot =  (sBot + printMoveString(moves[i]) + "|");
            }
        }

        System.out.println(sTop + "\n" + sBot);
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

        System.out.println(sTop + "\n" + sBot);
        System.out.println("Out: Black ("+_outblack+") White ("+_outwhite+")\n");
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
            System.out.println("Freedom!");
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
                System.out.println(" Counterjail!");
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
                System.out.println(" Blocked. Try again.");
                return -1;
            }
        }

        return 0;
    }

    public int getPiecesInColumn(int i){
        return _board[i];
    }

    //Gets all the avaliable pieces to the current player.
    public List<String> getColumnsWithPieces(){

        List<String> list = new ArrayList<>();
        for(int i=0 ; i<_board.length ; i++){

            //Black team
            if(_current == Player.BLACK && _board[i] < 0)
                list.add(Integer.toString(i));

            //Black team
            if(_current == Player.WHITE && _board[i] > 0)
                list.add(Integer.toString(i));
        }

        return list;
    }

    public int[] getCount(){
        return _board;
    }
	
	//Returns all the destination columns allowed WITH the dice restrictions
	public boolean[] getAllowedMoves(int iSrc, int iDie1, int iDie2) {
		int iCombo = (iDie1+iDie2);		
		boolean[] bMoves = getAllowedMoves(iSrc);
		
		//If we're white we're going in reverse.
		if(getPiecesInColumn(iSrc) > 0) {
			iDie1 = -(iDie1);
			iDie2 = -(iDie2);
			iCombo = -(iCombo);
		}
		
		for(int i=0 ; i<bMoves.length ; i++) {
		
			//Are we allowed to move there currently?
			if(bMoves[i]) {
			
				//Is Die1 allowed?
				if(i == (iSrc + iDie1)) {
				}
				//Is Die2 allowed?
				else if(i == (iSrc + iDie2)) {
				}
				//Is Combo allowed?
				else if(i == (iSrc + iCombo)) {
				}
				//No allowance to move here.
				else {
					bMoves[i] = false;
				}				
			}			
		}
		
		return bMoves;
	}
	
	//Returns all the destination columns allowed
	public boolean[] getAllowedMoves(int iSrc) {
	
		boolean[] bReturn;
		bReturn = new boolean[_board.length];
	
		for(int i=0 ; i<_board.length ; i++) {
		
			int iCount = (i - iSrc);
	
			if(movePiece(iSrc, iCount, true) == 0) {
				//System.out.println("We can move from "+Integer.toString(iSrc)+" to "+Integer.toString(i));
				bReturn[i] = true;
			}
			else {
				//System.out.println("We CAN'T move from "+Integer.toString(iSrc)+" to "+Integer.toString(i));
				bReturn[i] = false;
			}
			
		}
		
		return bReturn;
	}
	
	public int movePiece(int iSrc, int iCount, boolean bTest) {

        //Where is this going?
        int iDst;                   //Black move up    //White move down
		iDst = (_board[iSrc] < -1) ? (iSrc + iCount) : (iSrc - iCount);

        //Are these in bounds?
        if(iSrc < 0 || iDst < 0 || iSrc >= 24 || iDst >= 24) {
            //System.out.println("Out of bounds move!");
            return -1;
        }
        //Do we have a piece to move?
        else if(_board[iSrc] == 0) {
            //System.out.println("No source piece!");
            return -1;
        }
		//Is the destination the SAME as the source?
		else if(iDst == iSrc){
	        //System.out.println("Can't move back into the same spot");
            return -2;
		}
        //Is the space free?
        else if(_board[iDst] == 0) {
		
			//Testing Only
			if(bTest)
				return 0;
		
            //System.out.println("No Team!");
            _move(iSrc, iDst);
        }
        //Is the dest the same team as source?
        else if (!(_board[iSrc] < 0 ^ _board[iDst] < 0)) {
            //System.out.println("Same Team!");

			//Testing Only
			if(bTest)
				return 0;
			//Actual Move
			else			
				_move(iSrc, iDst);
        }
        else {
            //System.out.print("Opposite Team - ");
            //If the enemy only has one piece we can kill it!
            if(Math.abs(_board[iDst]) <= 1) {
			
			//System.out.println(" jailed!");
			
			//Testing Only
			if(bTest)
				return 0;		
			
			if (_board[iDst] < 0)
				_outwhite++;
			else
				_outblack++;
			
			_move(iSrc, iDst);
			return 1;
            }

            //System.out.println(" blocked! Try again.");
            return -1;
        }
        return 1;
    }
}
