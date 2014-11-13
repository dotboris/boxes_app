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

        ImageButton brushSizeButton = (ImageButton) findViewById(R.id.pick_brush_btn);
        brushSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBrushSizeDialog();
            }
        });
    }

    private void showColorWheelDialog() {
        ColorWheelDialogFragment dialog = ColorWheelDialogFragment.newInstance(canvas.getBrushColor());
        dialog.setOnColorSelectedListener(new ColorWheelDialogFragment.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                canvas.setBrushColor(color);
            }
        });

        dialog.show(getFragmentManager(), "ColorWheelDialogFragment");
    }

    private void showBrushSizeDialog() {
        BrushSizeDialogFragment dialog = new BrushSizeDialogFragment(2, 100, (int) canvas.getBrushSize());
        dialog.setOnBrushSizeSelectedListener(new BrushSizeDialogFragment.OnBrushSizeSelectedListener() {
            @Override
            public void onBrushSizeSelected(int size) {
                canvas.setBrushSize(size);
            }
        });
        dialog.show(getFragmentManager(), "BrushSizeDialogFragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // no menu
        return false;
    }
}
