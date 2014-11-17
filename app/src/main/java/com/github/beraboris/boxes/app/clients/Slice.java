package com.github.beraboris.boxes.app.clients;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Slice {
    private String queue;
    private int id;
    private Bitmap image;

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    @JsonSetter("image")
    public void setBitmapFromBase64(String base64) {
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
