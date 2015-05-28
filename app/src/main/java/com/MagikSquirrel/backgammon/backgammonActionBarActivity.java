package com.MagikSquirrel.backgammon;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class backgammonActionBarActivity extends ActionBarActivity {

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

        switch (id) {
            case R.id.action_board:
                startActivity(new Intent(this, homePage.class));
                return true;

            case R.id.action_settings:
                startActivity(new Intent(this, settingsPage.class));
                return true;

            case R.id.action_boardfull:
                startActivity(new Intent(this, boardPage.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
