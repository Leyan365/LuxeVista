package com.example.luxevistaapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactUsActivity extends AppCompatActivity {

    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyDw_iuOi2CwOgx73vOSV8AoaA3aEQwhwu8";
    private static final String STATIC_MAP_URL =
            "https://maps.googleapis.com/maps/api/staticmap?" +
                    "center=6.0321,80.2167" +
                    "&zoom=15" +
                    "&size=600x300" +
                    "&maptype=roadmap" +
                    "&markers=color:red|label:G|6.0321,80.2167" +
                    "&key=" + GOOGLE_MAPS_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ImageView mapImageView = findViewById(R.id.mapImageView);

        // Fetch and display the static map
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Bitmap mapBitmap = fetchStaticMap(STATIC_MAP_URL);
            runOnUiThread(() -> {
                if (mapBitmap != null) {
                    mapImageView.setImageBitmap(mapBitmap);
                } else {
                    Toast.makeText(this, "Failed to load map", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private Bitmap fetchStaticMap(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
