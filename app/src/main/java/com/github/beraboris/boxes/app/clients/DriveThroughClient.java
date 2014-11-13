package com.github.beraboris.boxes.app.clients;

import android.graphics.Bitmap;
import android.net.Uri;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;

public class DriveThroughClient {
    private RestTemplate template;
    private Uri baseUrl;

    public DriveThroughClient(String baseUrl) {
        this.baseUrl = Uri.parse(baseUrl);
        template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public Slice getSlice() {
        return template.getForObject(baseUrl.buildUpon().appendPath("slice").toString(), Slice.class);
    }

    public void putDrawing(String queue, String id, Bitmap drawing) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        drawing.compress(Bitmap.CompressFormat.PNG, 10, out);
        template.put(baseUrl.buildUpon().appendPath("drawings").appendPath(queue).appendPath(id).toString(),
                out.toByteArray());
    }
}
