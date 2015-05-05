package com.MagikSquirrel.backgammon;

//import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class gameBoard {
    private int[] _board;
    private int _outblack; //Pieces that have been jailed
    private int _outwhite;
    private int _bearblack; //Pieces that have been "beared off"
    private int _bearwhite; //Pieces that have been "beared off"
    private Player _current;

    public static enum Player
    {
        BLACK {
            public String toString() {
                return "Black";
            }
        },
        WHITE{
            public String toString() {
                return "White";
            }
        },
        NULL
    }

    private String _log = "";

    //Constructor
    gameBoard() {
        _board = new int[24];
        _outblack = 0;
        _outwhite = 0;
        _bearblack = 0;
        _bearwhite = 0;
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

    //This empties the gameboard of pieces (not a real state)
    //This is used for testing piece graphics
    public void emptyGame() {
	
		//Clear the board
        for(int i=0 ; i< _board.length ; i++){
            _board[i] = 0;
        }
		
		//Clear the jails
		_outblack = 0;
		_outwhite = 0;
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
	
	//This method is for setting up very specific game scenarios for testing
	public void setTestGame(int i) {
		//Firstly clear the game.
		emptyGame();
	
		switch(i) {
			//Puts black and white next to each other to easily jail the other piece.
			case 1:
			
				_board[0] = -1;
				_board[1] = 1;
				_board[12] = -1;
				_board[14] = 1;
			
			break;

            //Setups black in their home arena, white not
            case 2:

                _board[0] = 1;
                _board[1] = 3;
                _board[10] = -1;
                _board[18] = -1;
                _board[22] = -1;

            break;
		}
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

    public int unjailPiece(int iRoll, Player player) {

        int iDst;

        //Which player?
        boolean bBlack = false;
        if(player == Player.BLACK)
            bBlack = true;
        else  if(player == Player.WHITE)
            bBlack = false;

        iDst = ( bBlack == true ? iRoll-1 : (24-iRoll));

        //Free?             OR      Same side add
        if( (_board[iDst] == 0) || (bBlack && _board[iDst] < 0) ) {
            //System.out.println("Freedom!");
            if(bBlack) {
                _board[iDst]--;
                _outblack--;
            }
            else {
                _board[iDst]++;
                _outwhite--;
            }
        }
        //Same team add
        else if(bBlack ^ _board[iDst] < 0) {
            //System.out.print("Opposite side unjail - ");
            //Jail the enemy!
            if(Math.abs(_board[iDst]) == 1) {
                //System.out.println(" Counterjail!");
                if(bBlack) {
                    _board[iDst]-=2;
                    _outwhite--;
                    _outblack++;
                }
                else {
                    _board[iDst]+=2;
                    _outwhite--;
                    _outblack++;
                }
            }
            //Can't move here, blocked
            else {
                //System.out.println(" Blocked. Try again.");
                return -1;
            }
        }

        return 1;
    }
    public int unjailPiece(int iRoll) {
        return unjailPiece(iRoll, _current);
    }

    public int getPiecesInColumn(int i){
        return _board[i];
    }

    //How many pieces does this player have in jail?
    public int getPiecesInJail(Player p){
        if(p == Player.BLACK)
            return _outblack;
        else if(p == Player.WHITE)
            return _outwhite;

        return -1;
    }

    //Assume Current player if none passed
    public int getPiecesInJail(){
        return getPiecesInJail(_current);
    }

    //How many pieces does this player have beared off
    public int getPiecesBearedOff(Player p){
        if(p == Player.BLACK)
            return _bearblack;
        else if(p == Player.WHITE)
            return _bearblack;

        return -1;
    }

    //Assume Current player if none passed
    public int getPiecesBearedOff(){
        return getPiecesBearedOff(_current);
    }

    //Can this player "Bear off" (Where all pieces are in their home arena)
    public boolean canBearOff(Player player) {

        int iHomeSize = 6; //How many "points" constitue the home arena.

        //Any jailed pieces means we can't bear Off
        if(getPiecesInJail(player) != 0)
            return false;

        //Black all pieces are 16+
        else if(player == Player.BLACK) {

            for(int i = 0; i<=_board.length-iHomeSize-1 ; i++) {

                //Piece found not in home arena!
                if(_board[i] < 0)
                    return false;
            }
        }

        //White all pieces are 6-
        else if(player == Player.WHITE) {

            for(int i = _board.length-iHomeSize-1; i<= _board.length-1 ; i++) {

                //Piece found not in home arena!
                if(_board[i] > 0)
                    return false;
            }
        }

        //No pieces found outside their arena.
        return true;
    }
    public boolean canBearOff() {
        return canBearOff(_current);
    }

    //Gets all the available pieces to the current player.
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

    //If the player has no pieces left on the board they win!
    public boolean isGameWon() {

        if(_current == Player.BLACK) {

            for(int i=0; i<=_board.length-1 ; i++) {

                //Piece found, Black didn't win yet!
                if(_board[i] < 0)
                    return false;
            }
        }

        //White all pieces are 6-
        else if(_current == Player.WHITE) {

            for (int i = 0; i <= _board.length - 1; i++) {

                //Piece found, White didn't win yet!
                if (_board[i] > 0)
                    return false;
            }
        }

        return true;
    }
	
	public int movePiece(int iSrc, int iCount, boolean bTest) {

        //Where is this going?
        int iDst;                   //Black move up    //White move down
		iDst = (_board[iSrc] < 0) ? (iSrc + iCount) : (iSrc - iCount);

        //Are these "Bearing Off?"   BLACK going beyond 24 WHITE going beyond 0
        if(canBearOff() && ((_board[iSrc] < 0 && iDst >= 24) || (_board[iSrc] > 0 && iDst < 0)) ) {
            if(_board[iSrc] < 0)
                _bearblack++;
            if(_board[iSrc] > 0)
                _bearwhite++;

            _board[iSrc]--;
        }
        //Are these reverse moves?
        else if(iSrc < 0 || iDst < 0 || iSrc >= 24 || iDst >= 24) {
            //System.out.println("Out of bounds move!");
            return -1;
        }
        //Do we have a piece to move?
        else if(_board[iSrc] == 0) {
            //System.out.println("No source piece!");
            return -2;
        }
		//Is the destination the SAME as the source?
		else if(iDst == iSrc){
	        //System.out.println("Can't move back into the same spot");
            return -3;
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
                    _outblack++;
                else
                    _outwhite++;

                _move(iSrc, iDst);
                return 1;
            }

            //System.out.println(" blocked! Try again.");
            return -4;
        }
        return 1;
    }
}
