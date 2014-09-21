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

    private OnColorSelectedListener onColorSelectedListener;
    private float[] hsv;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        hsv = new float[3];
        hsv[2] = 1;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.color_wheel_dialog, null);
        HueSaturationWheelView wheel = (HueSaturationWheelView) layout.findViewById(R.id.hue_saturation_wheel);

        wheel.setHueSaturationChangeListener(new HueSaturationWheelView.OnHueSaturationChangeListener() {
            @Override
            public void onHueSaturationChange(double hue, double saturation) {
                hsv[0] = (float) hue;
                hsv[1] = (float) saturation;

                layout.setBackgroundColor(Color.HSVToColor(hsv));
                layout.invalidate();
            }
        });

        builder.setView(layout);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onColorSelectedListener != null) {
                    onColorSelectedListener.onColorSelected(Color.BLUE /*TODO: actually pick a color*/);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return builder.create();
    }

    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        this.onColorSelectedListener = onColorSelectedListener;
    }
}
