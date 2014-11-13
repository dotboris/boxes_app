package com.github.beraboris.boxes.app.drawing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.github.beraboris.boxes.app.R;

public class ColorWheelDialogFragment extends DialogFragment {
    public static final String START_COLOR_ARG_KEY = "startColor";

    private HueSaturationWheelView wheel;
    private ValueSliderView slider;

    public interface OnColorSelectedListener {
        public void onColorSelected(int color);
    }

    private View layout;
    private OnColorSelectedListener onColorSelectedListener;
    private float[] hsv;

    public static ColorWheelDialogFragment newInstance(int startColor) {
        ColorWheelDialogFragment fragment = new ColorWheelDialogFragment();
        Bundle args = new Bundle();
        args.putInt(START_COLOR_ARG_KEY, startColor);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        int startColor = args.getInt(START_COLOR_ARG_KEY);
        hsv = new float[3];
        Color.colorToHSV(startColor, hsv);
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.color_wheel_dialog, null);
        layout.setBackgroundColor(Color.HSVToColor(hsv));

        wheel = (HueSaturationWheelView) layout.findViewById(R.id.hue_saturation_wheel);
        wheel.setHueSaturationChangeListener(new HueSaturationWheelView.OnHueSaturationChangeListener() {
            @Override
            public void onHueSaturationChange(double hue, double saturation) {
                hsv[0] = (float) hue;
                hsv[1] = (float) saturation;

                updateHueSaturation();
                updateBackground();
            }
        });
        wheel.setValue(hsv[2]);

        slider = (ValueSliderView) layout.findViewById(R.id.value_slider);
        slider.setOnValueChangedListener(new ValueSliderView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                hsv[2] = value;

                updateValue();
                updateBackground();
            }
        });
        slider.setHueSaturation(hsv[0], hsv[1]);

        builder.setView(layout);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onColorSelectedListener != null) {
                    onColorSelectedListener.onColorSelected(Color.HSVToColor(hsv));
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    private void updateValue() {
        wheel.uddateValue(hsv[2]);
    }

    private void updateHueSaturation() {
        slider.updateHueSaturation(hsv[0], hsv[1]);
    }

    private void updateBackground() {
        layout.setBackgroundColor(Color.HSVToColor(hsv));
        layout.invalidate();
    }

    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        this.onColorSelectedListener = onColorSelectedListener;
    }
}
