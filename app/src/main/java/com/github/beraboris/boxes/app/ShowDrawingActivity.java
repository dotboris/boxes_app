package com.github.beraboris.boxes.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import com.github.beraboris.boxes.app.clients.GalleryClient;

public class ShowDrawingActivity extends Activity {

    private ImageView drawingView;

    private class GetImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        private GalleryClient client;
        private String id;

        private GetImageAsyncTask(GalleryClient client, String id) {
            this.client = client;
            this.id = id;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return client.getDrawing(id);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            drawingView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);
        setContentView(R.layout.activity_show_drawing);

        String id = getIntent().getStringExtra("id");
        drawingView = (ImageView) findViewById(R.id.drawing);

        String url = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("pref_gallery_url", "http://localhost:8081");
        GalleryClient client = new GalleryClient(url);
        new GetImageAsyncTask(client, id).execute();
    }
}
