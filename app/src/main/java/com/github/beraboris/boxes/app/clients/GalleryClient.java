package com.github.beraboris.boxes.app.clients;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class GalleryClient {
    private RestTemplate template;
    private Uri base;

    public GalleryClient(String baseUrl) {
        this.base = Uri.parse(baseUrl);
        this.template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public String[] getIds() {
        return template.getForObject(base.toString(), String[].class);
    }

    public Bitmap getDrawing(String id) {
        byte[] bytes = template.getForObject(base.buildUpon().appendPath(id).toString(), byte[].class);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
