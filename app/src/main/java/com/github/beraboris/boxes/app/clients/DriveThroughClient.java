package com.github.beraboris.boxes.app.clients;

import android.graphics.Bitmap;
import android.net.Uri;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

    public void putDrawing(String queue, int id, Bitmap drawing) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        drawing.compress(Bitmap.CompressFormat.PNG, 10, out);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("image", "png"));
        HttpEntity<byte[]> entity = new HttpEntity<byte[]>(out.toByteArray(), headers);

        template.exchange(baseUrl.buildUpon().appendPath("drawings")
                        .appendPath(queue).appendPath(Integer.toString(id)).toString(),
                HttpMethod.PUT, entity, String.class);
    }
}
