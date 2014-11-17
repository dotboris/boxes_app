package com.github.beraboris.boxes.app.drawing;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.github.beraboris.boxes.app.R;
import com.github.beraboris.boxes.app.clients.DriveThroughClient;
import com.github.beraboris.boxes.app.clients.Slice;


public class DrawingActivity extends Activity {
    private class GetSliceTask extends AsyncTask<DriveThroughClient, Void, Slice> {
        private Throwable exception;

        @Override
        protected Slice doInBackground(DriveThroughClient... clients) {
            try {
                return clients[0].getSlice();
            } catch (RuntimeException e) {
                exception = e;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Slice s) {
            if (exception == null) {
                slice = s;
                resizeCanvas();
                Toast.makeText(DrawingActivity.this,
                        "Loaded slice", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DrawingActivity.this,
                        "Failed to load slice " + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private CanvasView canvas;

    private DriveThroughClient client;
    private Slice slice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        client = createDriveThroughClient();
        new GetSliceTask().execute(client);

        canvas = (CanvasView) findViewById(R.id.canvas);

        findViewById(R.id.pick_color_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorWheelDialog();
            }
        });

        findViewById(R.id.pick_brush_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBrushSizeDialog();
            }
        });

        findViewById(R.id.other_picture_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviewDialog();
            }
        });
    }

    private void resizeCanvas() {
        canvas.resize(slice.getImage().getWidth(), slice.getImage().getHeight());
    }

    private DriveThroughClient createDriveThroughClient() {
        String url = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("pref_drivethrough_url", "http://localhost:8080");

        return new DriveThroughClient(url);
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
        BrushSizeDialogFragment dialog = BrushSizeDialogFragment.newInstance(2, 100, (int) canvas.getBrushSize());
        dialog.setOnBrushSizeSelectedListener(new BrushSizeDialogFragment.OnBrushSizeSelectedListener() {
            @Override
            public void onBrushSizeSelected(int size) {
                canvas.setBrushSize(size);
            }
        });
        dialog.show(getFragmentManager(), "BrushSizeDialogFragment");
    }

    private void showPreviewDialog() {
        PreviewDialogFragment dialog = PreviewDialogFragment.newInstance(slice.getImage());
        dialog.show(getFragmentManager(), "PreviewDialogFragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // no menu
        return false;
    }
}
