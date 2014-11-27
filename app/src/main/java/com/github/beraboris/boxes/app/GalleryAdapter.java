package com.github.beraboris.boxes.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class GalleryAdapter extends BaseAdapter {
    private Context context;
    private Bitmap[] bitmaps;

    private int thumbnailSize;

    public GalleryAdapter(Context context) {
        this.context = context;
        bitmaps = new Bitmap[0];
        thumbnailSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                context.getResources().getDisplayMetrics());
    }

    public void update(Bitmap[] bitmaps) {
        Bitmap[] thumbnails = new Bitmap[bitmaps.length];
        for (int i = 0; i < bitmaps.length; i++) {
            thumbnails[i] = Bitmap.createScaledBitmap(bitmaps[i], thumbnailSize, thumbnailSize, true);
        }
        this.bitmaps = thumbnails;
    }

    @Override
    public int getCount() {
        return bitmaps.length;
    }

    @Override
    public Object getItem(int i) {
        return bitmaps[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.d("boxes gallery", "requesting " + i);
        ImageView imageView;

        if (view == null) {
            Log.d("boxes gallery", "built new");

            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, thumbnailSize));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            Log.d("boxes gallery", "recycled");
            imageView = (ImageView) view;
        }

        imageView.setCropToPadding(true);
        imageView.setImageBitmap(bitmaps[i]);

        return imageView;
    }
}
