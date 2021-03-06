package com.MagikSquirrel.backgammon;


import android.app.Activity;
import android.app.Application;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

public class backgammonApp extends Application {

    private Map<String, Activity> _lActivities;
    private String _sBlackName = "Black";
    private String _sWhiteName = "Red";
    private boolean showHints = true;
    private int testCase;

    public void addActivity(String name, Activity activity) {
        if(!this._lActivities.containsKey(activity)) {
            _lActivities.put(name, activity);
        }
    }

    public Activity getActivity(String name) {
        return this._lActivities.get(name);
    }


    public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

    public static void main() {

        //Create a new empty game board
        gameBoard bg = new gameBoard();
        bg.print();

        //Setup the pieces
        System.out.println("Setting up a new game...");
        bg.setBoardNew();
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

		Scanner in = new Scanner(System.in);
		while(iSrc != -1) {
			iSrc = in.nextInt();
			iDie1 = in.nextInt(); //randInt(1, 6);
			iDie2 = in.nextInt(); //randInt(1, 6);

			System.out.println("Rolled a ["+Integer.toString(iDie1)+"] and ["+Integer.toString(iDie2)+"]\n");
			//bMoves = bg.getAllowedMoves(iSrc, iDie1, iDie2);

			bg.movePiece(iSrc, iDie1, false);
			bg.print();
			//bg.printMoves(bMoves);
		}
		in.close();
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

    public String getBlackName() {
        return _sBlackName;
    }

    public void setBlackName(String _sBlackName) {
        this._sBlackName = _sBlackName;
    }

    public String getWhiteName() {
        return _sWhiteName;
    }

    public void setWhiteName(String _sWhiteName) {
        this._sWhiteName = _sWhiteName;
    }

    public boolean getShowHints() {
        return showHints;
    }

    public void setShowHints(boolean showHints) {
        this.showHints = showHints;
    }

    public int getTestCase() {
        return testCase;
    }

    public void setTestCase(int testCase) {
        this.testCase = testCase;
    }
}