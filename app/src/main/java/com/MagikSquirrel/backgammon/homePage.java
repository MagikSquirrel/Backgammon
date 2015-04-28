package com.MagikSquirrel.backgammon;

import android.app.ActionBar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;


public class homePage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_home_page);

        final LinearLayout llPiece = new LinearLayout(this);
        llPiece.layout(0,0,100,100);
        llPiece.setLayoutParams(new ActionBar.LayoutParams(100, 100));
        llPiece.setOrientation(LinearLayout.HORIZONTAL);

        Button btnCreate = (Button) findViewById(R.id.bNewGame);
        btnCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final EditText etBlack = (EditText) findViewById(R.id.etBlackName);
                etBlack.setText("Shit kinda worked!");



                Drawable drawable = getResources().getDrawable(R.drawable.pieceblack);
                //ivPic.setImageDrawable(drawable); // set the image to the ImageView
                //llPiece.addView(ivPic);
                //setContentView(llPiece);



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
