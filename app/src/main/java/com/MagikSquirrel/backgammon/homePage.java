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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class homePage extends ActionBarActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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

                ImageView ivBlack = new ImageView(homePage.this);
                ivBlack.setLayoutParams(new GridView.LayoutParams(85, 85));
                ivBlack.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivBlack.setPadding(8, 8, 8, 8);
                ivBlack.setImageResource(R.drawable.pieceblack);
                ivBlack.setVisibility(View.VISIBLE);

                GridView gBoard = (GridView) findViewById(R.id.gBoard);
                gBoard.addView(ivBlack);
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
