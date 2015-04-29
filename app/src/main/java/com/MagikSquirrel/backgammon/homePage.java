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
import android.widget.TableRow;
import android.widget.Toast;

public class homePage extends ActionBarActivity {

    private Context mContext;
    private board bGameBoard;

    private static enum Player
    {
        BLACK,
        WHITE
    }



    //Sets an image view to be owned by a particular player (or empty if null)
    private void setImageViewOwnerById(Player player, ImageView iv) {
        if(player == Player.BLACK){
            iv.setImageResource(R.drawable.pieceblack);
            iv.setVisibility(View.VISIBLE);
        }
        else if(player == Player.WHITE){
            iv.setImageResource(R.drawable.piecered);
            iv.setVisibility(View.VISIBLE);
        }
        else {
            iv.setVisibility(View.INVISIBLE);
        }
    }

    private void setImageViewOwnerById(Player player, int id) {
        //Gotta love method overloading!
        setImageViewOwnerById(player, (ImageView) findViewById(id));
    }

    //
    private void initBoard()
    {
        TableRow trTop = (TableRow) findViewById(R.id.trBoardTop);


        for(int i=0 ; i<= 10 ; i++){
            ImageView iv = new ImageView(mContext);
            iv.layout(i,i,i+1,i+1);

            trTop.addView(iv);
        }



        for(int i = 0 ; i < trTop.getChildCount() ; i++){
            ImageView v = (ImageView) trTop.getChildAt(i);
            setImageViewOwnerById(Player.BLACK, v);
        }

        TableRow trBot = (TableRow) findViewById(R.id.trBoardBot);
        for(int i = 0 ; i < trBot.getChildCount() ; i++){
            ImageView v = (ImageView) trBot.getChildAt(i);
            setImageViewOwnerById(Player.BLACK, v);
        }
    }

    private void redrawBoard()
    {
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
