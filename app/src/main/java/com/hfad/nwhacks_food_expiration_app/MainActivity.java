package com.hfad.nwhacks_food_expiration_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
}
