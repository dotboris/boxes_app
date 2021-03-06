package com.github.beraboris.boxes.app.drawing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.github.beraboris.boxes.app.R;

/**
 * Dialog used to pick a brush size
 */
public class BrushSizeDialogFragment extends DialogFragment {

    public static final String FROM_ARG = "from";
    public static final String TO_ARG = "to";
    public static final String CURRENT_ARG = "current";

    public interface OnBrushSizeSelectedListener {
        void onBrushSizeSelected(int size);
    }
    private OnBrushSizeSelectedListener onBrushSizeSelectedListener;

    private TextView label;
    private BrushPreview preview;

    private int from;
    private int to;
    private int size;

    public static BrushSizeDialogFragment newInstance(int from, int to, int current) {
        BrushSizeDialogFragment fragment = new BrushSizeDialogFragment();

        Bundle args = new Bundle();
        args.putInt(FROM_ARG, from);
        args.putInt(TO_ARG, to);
        args.putInt(CURRENT_ARG, current);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        from = args.getInt(FROM_ARG, 1);
        to = args.getInt(TO_ARG, 100);
        size = args.getInt(CURRENT_ARG, 20);
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.brush_size_dialog, null);
        builder.setView(layout);

        label = (TextView) layout.findViewById(R.id.size_label);
        label.setText(Integer.toString(size));

        preview = (BrushPreview) layout.findViewById(R.id.size_preview);
        preview.getLayoutParams().height = to;
        preview.setSize(size);

        SeekBar bar = (SeekBar) layout.findViewById(R.id.size_slider);
        bar.setMax(to - from);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateSize(from + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // nope
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // nope
            }
        });
        bar.setProgress(size - from);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onBrushSizeSelectedListener != null) {
                    onBrushSizeSelectedListener.onBrushSizeSelected(size);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    private void updateSize(int size) {
        this.size = size;

        label.setText(Integer.toString(size));
        preview.setSize(size);
    }

    public void setOnBrushSizeSelectedListener(OnBrushSizeSelectedListener onBrushSizeSelectedListener) {
        this.onBrushSizeSelectedListener = onBrushSizeSelectedListener;
    }
}
