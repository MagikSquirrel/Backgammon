package com.MagikSquirrel.backgammon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

public class settingsPage extends backgammonActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //Add this activity to the App list
        ((backgammonApp) this.getApplication()).addActivity(this.getPackageName(), this);

        //Add events to when the text changes
        final EditText whitePlayer = (EditText)findViewById(R.id.etWhite);
        whitePlayer.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                ((backgammonApp) getApplication()).setWhiteName(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        final EditText blackPlayer = (EditText)findViewById(R.id.etBlack);
        blackPlayer.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                ((backgammonApp) getApplication()).setBlackName(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        //Add events for the toggle button
        ToggleButton toggle = (ToggleButton) findViewById(R.id.tbMoveHint);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((backgammonApp) getApplication()).setShowHints(isChecked);
            }
        });

        //DEBUG BUTTON
        final Button btnDebug = (Button) findViewById(R.id.bDebug);
        btnDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText etDebug = (EditText)findViewById(R.id.etDebug);
                try {
                    ((backgammonApp) getApplication()).setTestCase(Integer.parseInt(etDebug.getText().toString()));
                }
                catch (Exception e) {
                    ((backgammonApp) getApplication()).setTestCase(0);
                }
            }
        });
    }





}
