package com.github.beraboris.boxes.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * View that previews the size of a brush
 */
public class BrushPreview extends View {
    private Paint paint;

    private int size;

    public BrushPreview(Context context) {
        super(context);
        init();
    }

    public BrushPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BrushPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, size / 2, paint);
    }

    public void setSize(int size) {
        this.size = size;
        invalidate();
    }
}
