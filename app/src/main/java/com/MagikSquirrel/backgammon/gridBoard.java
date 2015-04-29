package com.MagikSquirrel.backgammon;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class gridBoard extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_board);

        //BACK BUTTON
        Button btnBack = (Button) findViewById(R.id.bBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                setContentView(R.layout.activity_home_page);
            }
        });


    }

}
