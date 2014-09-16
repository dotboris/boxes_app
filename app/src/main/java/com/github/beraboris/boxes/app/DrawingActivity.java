package com.github.beraboris.boxes.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


public class DrawingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // no menu
        return false;
    }
}
