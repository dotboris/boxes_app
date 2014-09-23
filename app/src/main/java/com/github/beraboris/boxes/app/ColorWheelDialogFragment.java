package com.github.beraboris.boxes.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class ColorWheelDialogFragment extends DialogFragment {
    public interface OnColorSelectedListener {
        public void onColorSelected(int color);
    }

    private View layout;
    private OnColorSelectedListener onColorSelectedListener;
    private float[] hsv;

    public ColorWheelDialogFragment(int startColor) {
        hsv = new float[3];
        Color.colorToHSV(startColor, hsv);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        layout = inflater.inflate(R.layout.color_wheel_dialog, null);
        layout.setBackgroundColor(Color.HSVToColor(hsv));

        HueSaturationWheelView wheel = (HueSaturationWheelView) layout.findViewById(R.id.hue_saturation_wheel);
        wheel.setValue(hsv[2]);
        wheel.setHueSaturationChangeListener(new HueSaturationWheelView.OnHueSaturationChangeListener() {
            @Override
            public void onHueSaturationChange(double hue, double saturation) {
                hsv[0] = (float) hue;
                hsv[1] = (float) saturation;

                updateBackground();
            }
        });

        builder.setView(layout);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onColorSelectedListener != null) {
                    onColorSelectedListener.onColorSelected(Color.HSVToColor(hsv));
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return builder.create();
    }

    private void updateBackground() {
        layout.setBackgroundColor(Color.HSVToColor(hsv));
        layout.invalidate();
    }

    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        this.onColorSelectedListener = onColorSelectedListener;
    }
}
