package com.github.beraboris.boxes.app.drawing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class PreviewDialogFragment extends DialogFragment {

    public static final String IMAGE_ARG_KEY = "image";
    private Bitmap image;

    public static PreviewDialogFragment newInstance(Bitmap image) {
        PreviewDialogFragment self = new PreviewDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(IMAGE_ARG_KEY, image);
        self.setArguments(args);

        return self;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        image = (Bitmap) args.get(IMAGE_ARG_KEY);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ImageView imageView = new ImageView(getActivity());
        imageView.setImageBitmap(image);
        builder.setView(imageView);

        return builder.create();
    }
}
