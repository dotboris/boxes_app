package com.github.beraboris.boxes.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class BoxesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxes);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.boxes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                // TODO: start the settings
                return true;
            case R.id.action_draw:
                openDrawingActivity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDrawingActivity() {
        startActivity(new Intent(this, DrawingActivity.class));
    }
}
