package com.github.beraboris.boxes.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * A view the user can draw on. Allows for saving of the drawing at a later time.
 */
public class CanvasView extends View {
    private Paint paint;
    private Path path;
    private Bitmap bitmap;
    private Canvas canvas;

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_UP:
                    canvas.drawPath(path, paint);
                    path.reset();
                    break;
                default:
                    return false;
            }

            invalidate();
            return true;
        }
    };

    public CanvasView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CanvasView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context, attrs, style);
    }

    private void init(Context context, AttributeSet attrs, int style) {
        path = new Path();
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CanvasView, style, 0);

        try {
            paint.setColor(a.getColor(R.styleable.CanvasView_brushColor, Color.BLACK));
            paint.setStrokeWidth(a.getDimension(R.styleable.CanvasView_brushWidth, 20));
        } finally {
            a.recycle();
        }

        this.setOnTouchListener(touchListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(this.bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
    }

}
