package com.MagikSquirrel.backgammon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class gameBoard {

    //FINALS
    private static final int DICE_MIN = 1;
    private static final int DICE_MAX = 6;
    private static final int PIECE_MAX = 15; //Maximum pieces a player can own.

	//FIELDS
    private int[] _board;
    private int _outblack; //Pieces that have been jailed
    private int _outwhite;
    private int _bearblack; //Pieces that have been "beared off"
    private int _bearwhite; //Pieces that have been "beared off"
    private int _die1;
    private int _die2;
    private Player _current;
	private String _log = "";

	//ENUMS
    public static enum Player
    {
        BLACK  { public String toString() { return "Black"; } }, //Negative pieces
        WHITE { public String toString() { return "White"; } }, //Positive pieces
        NULL
    }
	
	//Various states for a reason a move does or doesn't work
	public static enum MoveMsg
	{
		VALID_COMPLETE { public String toString() { return "Valid"; } },
		VALID_TEST { public String toString() { return "Valid (Test)"; } },
		OUT_OF_BOUNDS { public String toString() { return "Out of Bounds"; } },
		NO_SOURCE { public String toString() { return "No Source Piece"; } },
        SAME_SPOT { public String toString() { return "Move into Same Spot"; } },
        BLOCKED	 { public String toString() { return "Blocked by Enemy"; } },
        BOTH_DICE	 { public String toString() { return "Both die used"; } },
        INVALID	 { public String toString() { return "UNKNOWN STATE"; } }
	}    

    //CONSTRUCTOR
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
	
	//PROPERTIES
	public Player getCurrentPlayer() {
        return _current;
    }
	
    public int getPiecesInColumn(int i){
        return _board[i];
    }
	
    //Assume Current player if none passed
    public int getPiecesBearedOff(){
        return getPiecesBearedOff(_current);
    }

    //METHODS - Dice Settings
    public Integer getDie1(boolean UseIt) {
        //We are NOT consuming this dice
        if(!UseIt)
            return _die1;

        //Store the return value
        int iReturn = _die1;

        //If it's negative, it's the first use of a double.
        if(_die1 < 0)
            _die1 = -(_die1);
            //If it's positive, it's a normal die
        else
            _die1 = 0;

        return iReturn;
    }
    public Integer getDie2(boolean UseIt) {
        //We are NOT consuming this dice
        if(!UseIt)
            return _die2;

        //Store the return value
        int iReturn = _die2;

        //If it's negative, it's the first use of a double.
        if(_die2 < 0)
            _die2 = -(_die2);
            //If it's positive, it's a normal die
        else
            _die2 = 0;

        return iReturn;
    }
    public Integer getDie1() {
        return getDie1(false);
    }
    public Integer getDie2() {
        return getDie2(false);
    }

    public boolean rollDice() {
        Random r = new Random();

        //Get random in range of their default min/max vals
        _die1 = r.nextInt(DICE_MAX - DICE_MIN + 1) + DICE_MIN;
        _die2 = r.nextInt(DICE_MAX - DICE_MIN + 1) + DICE_MIN;
		
		//Doubles? If so set both to negative
		if(_die1 == _die2) {
			_die1 = -_die1;
			_die2 = -_die2;
		}

        //Is a move allowed?
        if(anyMove())
            return true;

        return false;
    }

	//METHODS - Full Game Settings
    //Checks to see if the board setup is legal and running.
    //  We need 15 pieces each (check bear offed and jailed)
    //  An active player
    public boolean isBoardLegal() {

        //An active player?
        if(_current == Player.NULL)
            return false;

        int iWhite = 0;
        int iBlack = 0;

        //Count the pieces in play.
        for(int i=0 ; i<_board.length ; i++){
            if(_board[i] < 0)
                iBlack += Math.abs(_board[i]);
            else if(_board[i] > 0)
                iWhite += _board[i];
        }

        //Count the jailed pieces
        iWhite += _outwhite;
        iBlack += _outwhite;

        //Count the beared off pieces
        iWhite += _bearwhite;
        iBlack += _bearblack;

        //Do the pieces equal the legal requirement?
        if(iWhite != PIECE_MAX || iBlack != PIECE_MAX)
            return false;

        return true;
    }


    //This fills the gameboard with pieces (not a real state)
    //This is used for testing piece graphics
    public void setBoardFull() {
        for(int i=0 ; i< _board.length ; i++){
            _board[i] = -8;
        }
    }

    //This empties the gameboard of pieces (not a real state)
    //This is used for testing piece graphics
    public void setBoardEmpty() {
	
		//Clear the board
        for(int i=0 ; i< _board.length ; i++){
            _board[i] = 0;
        }
		
		//Clear the jails
		_outblack = 0;
		_outwhite = 0;
    }

    //Setup New game by placing pieces where they should
    //Negatives are black, positives are white
    public void setBoardNew(Player StartingPlayer){
        _current = StartingPlayer;
        setBoardNew();
    }
    public void setBoardNew() {

        //Clear the board.
        setBoardEmpty();

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
	public void setBoardTest(int i) {
		//Firstly clear the game.
		setBoardEmpty();
	
		switch(i) {
			//Puts black and white next to each other to easily jail the other piece.
			case 1:

                //Black side 0-11
                for(int j=0 ; j<=11 ; j++) {
                    _board[j] = -1;
                }

                //Black side 12-23
                for(int j=12 ; j<=23 ; j++) {
                    _board[j] = 1;
                }
			
			break;

            //Setups black in their home arena, white not
            case 2:

                _board[0] = 1;
                _board[1] = 3;
                _board[10] = -1;
                _board[18] = -1;
                _board[22] = -1;

            break;

            //Setups red to win, black to loose. Testing piece adjustment on bearoff phase.
            case 3:

                _board[2] = 1;
                _board[3] = 1;
                _board[4] = 2;
                _board[5] = 3;
                _board[18] = -6;
                _board[19] = -5;
                _board[20] = -1;
                _board[21] = -1;
                _board[22] = -2;
                _board[23] = -2;

                break;
				
            //Incrementing piece counts to test pieces able to be shown.
            case 4:
			
				//White side 0-11
				for(int j=0 ; j<=11 ; j++) {
					_board[j] = j+4; //4 - 15 pieces
				}
				
				//Black side 12-23
				for(int j=12 ; j<=23 ; j++) {
					_board[j] = -(j-8); //2 - 15 pieces
				}

                break;

            //Full game
            case 5:
                setBoardFull();

                break;

            //Red's turn to take a black piece
            case 6:

                for(int j=0 ; j<6 ; j++) {
                    _board[j] = -1;
                }
                _board[6] = 5;
                _board[7] = -5;

                break;

            //Same as 6, but we have no move as Red!
            case 7:

                for(int j=0 ; j<6 ; j++) {
                    _board[j] = -2;
                }
                _board[6] = 5;
                _board[7] = -5;

                _current = Player.WHITE;

                break;

            //Red has one everywhere! (Testing highlighting)
            case 8:

                for(int j=0 ; j<_board.length ; j++){
                    _board[j] = 1;
                }

                _current = Player.WHITE;

                break;

            //Black has one everywhere! (Testing highlighting)
            case 9:

                for(int j=0 ; j<_board.length ; j++){
                    _board[j] = -1;
                }

                _current = Player.BLACK;

                break;

            //Game with stepmom that she could go anywhere.
            case 10:

                _board[2] = 1;
                _board[3] = -1;

                _board[5] = 4;
                _board[6] = -1;
                _board[7] = 2;

                _board[11] = -4;
                _board[12] = 5;

                _board[16] = -4;

                _board[18] = -5;

                _board[21] = 1;

                _board[23] = 2;

                _die1 = 6;
                _die2 = 3;

                _current = Player.WHITE;

                break;

            //Game with Grace that white was bearing off black just outside home where
            //  bearing off jacked up the pieces
            case 11:

                for(int j=0 ; j<6 ; j++){
                    _board[j] = (j % 2 == 0) ? 2 : 3;
                }

                for(int j=_board.length-8 ; j<_board.length ; j++){
                    _board[j] = (j % 2 == 0) ? -2 : -1;
                }

                _current = Player.WHITE;

                break;
		}
	}
	
	//METHODS - Printing
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
	
	//METHODS - Board Management
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

    //METHODS - Piece Movement	
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

    //How many pieces does this player have in jail?
    public int getPiecesInJail(Player p){
        if(p == Player.BLACK)
            return _outblack;
        else if(p == Player.WHITE)
            return _outwhite;

        return -1;
    }
    public int getPiecesInJail(){
        return getPiecesInJail(_current);
    }    
	
	public MoveMsg unjailPiece(int iRoll, Player player) {

        int iDst;

        //Which player?
        boolean bBlack = false;
        if(player == Player.BLACK)
            bBlack = true;
        else  if(player == Player.WHITE)
            bBlack = false;

        iDst = (bBlack ? iRoll-1 : (24-iRoll));

        //Free?             OR      Black side add              OR  White side add
        if( (_board[iDst] == 0) || (bBlack && _board[iDst] < 0) || (!bBlack && _board[iDst] > 0) ) {
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
        else {
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
                return MoveMsg.BLOCKED;
            }
        }

        return MoveMsg.VALID_COMPLETE;
    }
    public MoveMsg unjailPiece(int iRoll) {
        return unjailPiece(iRoll, _current);
    }

    //How many pieces does this player have beared off
    public int getPiecesBearedOff(Player p){
        if(p == Player.BLACK)
            return _bearblack;
        else if(p == Player.WHITE)
            return _bearwhite;

        return -1;
    }

    //Can this player "Bear off" (Where all pieces are in their home arena)
    public boolean canBearOff(Player player) {

        int iHomeSize = 6; //How many "points" constitute the home arena.

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

            for(int i = iHomeSize; i<= _board.length-1 ; i++) {

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

    //Can Any of our pieces move anywhere?
    public boolean anyMove(){
        List<String> lsMoves = getColumnsWithPieces();

        for( String s : lsMoves ) {
            int iColumn = Integer.parseInt(s);
            boolean[] bMoves = getAllowedMoves(iColumn);

            for(int i=0 ; i<bMoves.length ; i++) {

                //Return true on the first valid move!
                if(bMoves[i])
                    return true;
            }
        }

        return false;
    }

    //Given a single source, what points can we move to?
    public boolean[] getAllowedMovesByDice(int iSrc) {

        int iDie1 = Math.abs(_die1);
        int iDie2 = Math.abs(_die2);

        return getAllowedMoves(iSrc, iDie1, iDie2);
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
		
			int iCount = Math.abs(i - iSrc);
	
			if(movePiece(iSrc, iCount, true) == MoveMsg.VALID_TEST) {
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
	
	public MoveMsg movePiece(int iSrc, int iCount, boolean bTest) {

        //Where is this going?
        int iDst;                   //Black move up    //White move down
		iDst = (_board[iSrc] < 0) ? (iSrc + iCount) : (iSrc - iCount);

        //Are these "Bearing Off?"   BLACK going beyond 24 WHITE going beyond 0
        if(canBearOff() && ((_board[iSrc] < 0 && iDst >= 24) || (_board[iSrc] > 0 && iDst < 0)) ) {
            if(_board[iSrc] < 0) {

                if(bTest)
                    return MoveMsg.VALID_TEST;

                _bearblack++;
                _board[iSrc]++;
            }
            else if(_board[iSrc] > 0) {

                if(bTest)
                    return MoveMsg.VALID_TEST;

                _bearwhite++;
                _board[iSrc]--;
            }
            else
                return MoveMsg.INVALID;
        }
        //Are these reverse moves?
        else if(iSrc < 0 || iDst < 0 || iSrc >= 24 || iDst >= 24) {
            return MoveMsg.OUT_OF_BOUNDS;
        }
        //Do we have a piece to move?
        else if(_board[iSrc] == 0) {
            return MoveMsg.NO_SOURCE;
        }
		//Is the destination the SAME as the source?
		else if(iDst == iSrc){
            return MoveMsg.SAME_SPOT;
		}
        //Is the space free?
        else if(_board[iDst] == 0) {
		
			//Testing Only
			if(bTest)
				return MoveMsg.VALID_TEST;

            _move(iSrc, iDst);
        }
        //Is the dest the same team as source?
        else if (!(_board[iSrc] < 0 ^ _board[iDst] < 0)) {
            //System.out.println("Same Team!");

			//Testing Only
			if(bTest)
                return MoveMsg.VALID_TEST;
			//Actual Move
			else			
				_move(iSrc, iDst);
        }
        else {
            //System.out.print("Opposite Team - ");
            //If the enemy only has one piece we can kill it!
            if(Math.abs(_board[iDst]) <= 1) {
			
                //System.out.println(" jailed!");
                if(bTest)
                    return MoveMsg.VALID_TEST;

                if (_board[iDst] < 0)
                    _outblack++;
                else
                    _outwhite++;

                _move(iSrc, iDst);
                return MoveMsg.VALID_COMPLETE;
            }

            //System.out.println(" blocked! Try again.");
            return MoveMsg.BLOCKED;
        }
        return MoveMsg.VALID_COMPLETE;
    }
}
