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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class homePage extends ActionBarActivity {

    private Context mContext;
    private board bGameBoard;

    private void redrawBoard()
    {
        int[] counts = bGameBoard.getCount();
        for(int i=0 ; i<counts.length ; i++) {
            ImageView iv = null;

            if (i == 1) {
                iv = (ImageView) findViewById(R.id.iv11);
            }
            else if (i == 2) {
                iv = (ImageView) findViewById(R.id.ivb1);
            }
            else if (i == 3) {
                iv = (ImageView) findViewById(R.id.ivb2);
            }
            else if (i == 4) {
                iv = (ImageView) findViewById(R.id.ivb3);
            }

            //Vis or invis
            if(iv == null){
                //Do nothing since no Image View was found
            }
            else if (counts[i] != 0) {
                //Black or White?
                if (counts[i] > 0) {
                    iv.setImageResource(R.drawable.piecered);
                } else {
                    iv.setImageResource(R.drawable.pieceblack);
                }

                iv.setVisibility(View.VISIBLE);
            }
            else {
                iv.setVisibility(View.INVISIBLE);
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
        redrawBoard();

        final LinearLayout llPiece = new LinearLayout(this);
        llPiece.layout(0,0,100,100);
        llPiece.setLayoutParams(new ActionBar.LayoutParams(100, 100));
        llPiece.setOrientation(LinearLayout.HORIZONTAL);

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
