package com.github.beraboris.boxes.app;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * A wheel used to pick a color through color and saturation.
 *
 * Hue and saturation is used to pick a HSV color. This needs to be combined with a Value slider/picker to get a full
 * HSV color.
 */
public class HueSaturationWheelView extends View {
    public void setHueSaturationChangeListener(OnHueSaturationChangeListener hueSaturationChangeListener) {
        this.hueSaturationChangeListener = hueSaturationChangeListener;
    }

    public interface OnHueSaturationChangeListener {
        void onHueSaturationChange(double hue, double saturation);
    }

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            if (hueSaturationChangeListener == null) {
                return false;
            }

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    float distx = e.getX() - midX;
                    float disty = e.getY() - midY;
                    double dist = Math.sqrt(Math.pow(distx, 2) + Math.pow(disty, 2));

                    if (dist <= diameter / 2) {
                        double angle = Math.toDegrees(Math.atan2(disty / dist, distx / dist));
                        if (angle < 0)
                            angle = 360 + angle;

                        hueSaturationChangeListener.onHueSaturationChange(angle, dist / (diameter / 2));

                        return true;
                    } else {
                        return false;
                    }

                default:
                    return false;
            }
        }
    };

    private OnHueSaturationChangeListener hueSaturationChangeListener;

    private Paint paint;
    private float diameter;
    private float midY;
    private float midX;

    private float value;

    public HueSaturationWheelView(Context context) {
        super(context);
        init();
    }

    public HueSaturationWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HueSaturationWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        value = 1;

        setOnTouchListener(touchListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        midX = w / 2;
        midY = h / 2;
        diameter = Math.min(w, h);

        updatePaint();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void updatePaint() {
        float[] saturationHsv = {0, 0, value};
        RadialGradient saturationGradiant = new RadialGradient(midX, midY, diameter / 2,
                Color.HSVToColor(255, saturationHsv), Color.HSVToColor(0, saturationHsv), Shader.TileMode.CLAMP);

        int[] keyColors = new int[13];
        for (int i = 0; i < 12; i++) {
            float[] hsv = {(360 / 12) * i, 1, 1};
            keyColors[i] = Color.HSVToColor(hsv);
        }
        keyColors[12] = keyColors[0];

        SweepGradient hueGradiant = new SweepGradient(midX, midY, keyColors, null);

        paint = new Paint();
        paint.setDither(true);
        paint.setShader(new ComposeShader(hueGradiant, saturationGradiant, PorterDuff.Mode.SRC_OVER));

        // force software rendering because composition of multiple gradients doesn't work on hardware.
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(midX, midY, diameter / 2, paint);
    }

    public void setValue(float value) {
        this.value = value;
        invalidate();
    }
}
