package com.hfad.nwhacks_food_expiration_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import edmt.dev.edmtdevcognitivevision.VisionServiceClient;
import edmt.dev.edmtdevcognitivevision.VisionServiceRestClient;

public class MainActivity extends AppCompatActivity {

    private final String API_KEY = "23dc58af58194da9a7dc03a196537d22";
    private final String API_LINK = "https://nwhacksfoodscanner.cognitiveservices.azure.com/vision/v1.0";

    // Declare vision client
    VisionServiceClient visionServiceClient = new VisionServiceRestClient(API_KEY, API_LINK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void cameraActivity(View view) {
        Intent intent = new Intent(this, Camera_Capture.class);
        startActivity(intent);
    }

}
