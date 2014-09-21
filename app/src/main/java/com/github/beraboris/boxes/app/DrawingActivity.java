package com.github.beraboris.boxes.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;


public class DrawingActivity extends Activity {
    private CanvasView canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        canvas = (CanvasView) findViewById(R.id.canvas);

        ImageButton colorWheelButton = (ImageButton) findViewById(R.id.pick_color_btn);
        colorWheelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorWheelDialog();
            }
        });
    }

    private void showColorWheelDialog() {
        ColorWheelDialogFragment dialog = new ColorWheelDialogFragment(canvas.getBrushColor());
        dialog.setOnColorSelectedListener(new ColorWheelDialogFragment.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                canvas.setBrushColor(color);
            }
        });

        dialog.show(getFragmentManager(), "ColorWheelDialogFragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // no menu
        return false;
    }
}
