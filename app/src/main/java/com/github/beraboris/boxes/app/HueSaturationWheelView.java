package com.github.beraboris.boxes.app;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/**
 * A wheel used to pick a color through color and saturation.
 *
 * Hue and saturation is used to pick a HSV color. This needs to be combined with a Value slider/picker to get a full
 * HSV color.
 */
public class HueSaturationWheelView extends View {
    private Paint paint;
    private float smallestDim;
    private float midY;
    private float midX;

    public HueSaturationWheelView(Context context) {
        super(context);
    }

    public HueSaturationWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HueSaturationWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        midX = w / 2;
        midY = h / 2;
        smallestDim = Math.min(w, h);

        updatePaint();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void updatePaint() {
        RadialGradient saturationGradiant = new RadialGradient(midX, midY, smallestDim / 2,
                0xFFFFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP);

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

        canvas.drawCircle(midX, midY, smallestDim / 2, paint);
    }
}
