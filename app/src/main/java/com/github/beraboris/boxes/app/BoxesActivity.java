package com.github.beraboris.boxes.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.github.beraboris.boxes.app.clients.GalleryClient;
import com.github.beraboris.boxes.app.drawing.DrawingActivity;
import com.github.beraboris.boxes.app.settings.SettingsActivity;


public class BoxesActivity extends Activity {

    private GalleryAdapter adapter;
    private GridView grid;

    private class UpdateGridAsyncTask extends AsyncTask<Void, Void, Bitmap[]> {

        private GalleryClient client;
        private Throwable exception;
        private String[] ids;

        private UpdateGridAsyncTask(GalleryClient client) {
            this.client = client;
        }

        @Override
        protected Bitmap[] doInBackground(Void... voids) {
            try {
                ids = client.getIds();

                Bitmap[] bitmaps = new Bitmap[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    bitmaps[i] = client.getDrawing(ids[i]);
                }

                return bitmaps;
            } catch (RuntimeException e) {
                exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            if (exception == null) {
                BoxesActivity.this.ids = ids;
                adapter.update(bitmaps);
                grid.invalidateViews();
                Toast.makeText(BoxesActivity.this, "Successfully loaded drawings", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BoxesActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("boxes", exception.getMessage());
            }
        }
    }

    private GalleryClient client;
    private String[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxes);

        String url = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("pref_gallery_url", "http://localhost:8081");
        client = new GalleryClient(url);

        grid = (GridView) findViewById(R.id.drawings_grid);
        adapter = new GalleryAdapter(this);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BoxesActivity.this, ShowDrawingActivity.class);
                intent.putExtra("id", ids[i]);
                startActivity(intent);
            }
        });

        updateGrid();
    }

    private void updateGrid() {
        new UpdateGridAsyncTask(client).execute();
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
                openSettingsActivity();
                return true;
            case R.id.action_draw:
                openDrawingActivity();
                return true;
            case R.id.action_refresh_gallery:
                updateGrid();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void openDrawingActivity() {
        startActivity(new Intent(this, DrawingActivity.class));
    }
}
