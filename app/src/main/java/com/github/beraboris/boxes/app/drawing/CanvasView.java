package com.github.beraboris.boxes.app.drawing;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.github.beraboris.boxes.app.R;


/**
 * A view the user can draw on. Allows for saving of the drawing at a later time.
 */
public class CanvasView extends View {
    private Paint paint;
    private Path path;
    private Bitmap bitmap;
    private Canvas canvas;

    private float canvasX;
    private float canvasY;
    private float canvasWidth;
    private float canvasHeight;

    private RectF canvasBox;

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(event.getX() - canvasX, event.getY() - canvasY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(event.getX() - canvasX, event.getY() - canvasY);
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
    private int brushColor;
    private float brushWidth;

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
        canvasWidth = 640;
        canvasHeight = 480;
        brushColor = Color.BLACK;
        brushWidth = 20;

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CanvasView, style, 0);

            try {
                brushWidth = a.getDimension(R.styleable.CanvasView_brushWidth, brushWidth);
                brushColor = a.getColor(R.styleable.CanvasView_brushColor, brushColor);

                canvasHeight = a.getDimension(R.styleable.CanvasView_canvasHeight, canvasHeight);
                canvasWidth = a.getDimension(R.styleable.CanvasView_canvasWidth, canvasWidth);
            } finally {
                a.recycle();
            }
        }

        createDrawingComponents();

        this.setOnTouchListener(touchListener);
    }

    private void createDrawingComponents() {
        bitmap = Bitmap.createBitmap(
                (int) Math.ceil(canvasWidth),
                (int) Math.ceil(canvasHeight),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        path = new Path();
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(brushColor);
        paint.setStrokeWidth(brushWidth);
    }

    public void resize(int width, int height) {
        canvasWidth = width;
        canvasHeight = height;
        createDrawingComponents();
        calculateCanvasOrigin(getWidth(), getHeight());
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        calculateCanvasOrigin(w, h);

        canvasBox = new RectF(canvasX, canvasY, canvasX + canvasWidth, canvasY + canvasHeight);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void calculateCanvasOrigin(int w, int h) {
        canvasX = (w / 2) - canvasWidth / 2;
        canvasY = (h / 2) - canvasHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.clipRect(canvasBox);

        canvas.translate(canvasX, canvasY);
        canvas.drawBitmap(this.bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
    }

    public int getBrushColor() {
        return paint.getColor();
    }

    public void setBrushColor(int color) {
        paint.setColor(color);
    }

    public float getBrushSize() {
        return paint.getStrokeWidth();
    }

    public void setBrushSize(float size) {
        paint.setStrokeWidth(size);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
