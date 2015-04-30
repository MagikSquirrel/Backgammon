public class app {
    public static void main(String[] args) {
     
        //Create a new empty game board
        gameBoard bg = new gameBoard();    
        bg.print();
         
        //Setup the pieces
        System.out.println("Setting up a new game...");
        bg.newGame();
        bg.print();
		
		int iSrc = 11;
		boolean[] bMoves;
		
		bg.print();		
		bMoves = bg.getAllowedMoves(iSrc);
		bg.printMoves(bMoves);
		
		int iDie1 = 4;
		int iDie2 = 3;
		
		System.out.println("Rolled a ["+Integer.toString(iDie1)+"] and ["+Integer.toString(iDie2)+"]\n");
		bMoves = bg.getAllowedMoves(iSrc, iDie1, iDie2);
		bg.printMoves(bMoves);
		/*
         
        //Move a piece
        System.out.println("Moving black from home top left one");
        bg.movePiece(0, 1);
        bg.print();
         
        //Move a piece
        System.out.println("Moving white from home bottom left five");
        bg.movePiece(23, -5);
        bg.print();
         
        //Move a piece
        System.out.println("Moving white from home bottom left eleven");
        bg.movePiece(23, -11);
        bg.print();
 
        //Move a piece
        System.out.println("Moving black to take alone white");
        bg.movePiece(5, -5);
        bg.print();
         
        //Unjail a piece
        System.out.println("White rolls a 3 to unjail!");
        bg.unjailPiece(3, true);
        bg.print();
         
        //Move a piece
        System.out.println("Moving black to take alone white");
        bg.movePiece(5, -3);
        bg.print();    
 
        //Unjail a piece
        System.out.println("White rolls a X to unjail!");
        bg.unjailPiece(3, true);
        bg.print();
		
		*/
    }
}