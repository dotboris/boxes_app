package com.github.beraboris.boxes.app.drawing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.github.beraboris.boxes.app.R;
import com.github.beraboris.boxes.app.clients.DriveThroughClient;
import com.github.beraboris.boxes.app.clients.Slice;


public class DrawingActivity extends Activity {
    private class GetSliceTask extends AsyncTask<Void, Void, Slice> {
        private Throwable exception;
        private DriveThroughClient client;

        private GetSliceTask(DriveThroughClient client) {
            this.client = client;
        }

        @Override
        protected Slice doInBackground(Void... args) {
            try {
                return client.getSlice();
            } catch (RuntimeException e) {
                exception = e;
                Log.e("boxes", "failed to get slice", e);
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

    private class PushDrawingTask extends AsyncTask<Void, Void, Void> {

        private boolean failed;
        private Slice slice;
        private Bitmap drawing;
        private DriveThroughClient client;

        private PushDrawingTask(DriveThroughClient client, Slice slice, Bitmap drawing) {
            this.slice = slice;
            this.drawing = drawing;
            this.client = client;
            this.failed = false;
        }

        @Override
        protected Void doInBackground(Void... args) {
            try {
                client.putDrawing(slice.getQueue(), slice.getId(), drawing);
            } catch (RuntimeException e) {
                Log.e("boxes", "failed to put drawings", e);
                failed = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (failed) {
                Toast.makeText(DrawingActivity.this, "Failed to send drawing", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DrawingActivity.this, "Sent drawing", Toast.LENGTH_SHORT).show();
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
        new GetSliceTask(client).execute();

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

        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PushDrawingTask(client, slice, canvas.getBitmap()).execute();
                finish();
            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
