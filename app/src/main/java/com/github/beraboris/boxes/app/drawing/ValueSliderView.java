package com.github.beraboris.boxes.app.drawing;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Slider used to pick the value in a HSV color
 */
public class ValueSliderView extends View {
    public interface OnValueChangedListener {
        void onValueChanged(float value);
    }

    private OnValueChangedListener valueChangedListener;
    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (valueChangedListener != null) {
                        valueChangedListener.onValueChanged(1 - (motionEvent.getY() / getHeight()));
                    }

                    return true;
                default:
                    return false;
            }
        }
    };

    private Paint paint;

    private float hue;
    private float saturation;

    public ValueSliderView(Context context) {
        super(context);
        init();
    }

    public ValueSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ValueSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(touchListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updatePaint();
    }

    private void updatePaint() {
        float[] fullValue = {hue, saturation, 1};
        float[] noValue = {hue, saturation, 0};

        LinearGradient gradient = new LinearGradient(0f, 0f, 0f, ((float) getHeight()),
                Color.HSVToColor(fullValue), Color.HSVToColor(noValue), Shader.TileMode.CLAMP);

        paint = new Paint();
        paint.setDither(true);
        paint.setShader(gradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPaint(paint);
    }

    /**
     * Set the hue and saturation without forcing the slider to re render
     */
    public void setHueSaturation(float hue, float saturation) {
        this.hue = hue;
        this.saturation = saturation;

    }

    /**
     * Update the hue and saturation triggering events that force the slider to re render
     */
    public void updateHueSaturation(float hue, float saturation) {
        this.hue = hue;
        this.saturation = saturation;

        updatePaint();
        invalidate();
    }

    public void setOnValueChangedListener(OnValueChangedListener valueChangedListener) {
        this.valueChangedListener = valueChangedListener;
    }
}
