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

public class MainActivity extends AppCompatActivity {
//    ImageView view_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        view_photo = (ImageView) findViewById(R.id.imgview_photo2);
    }


    public void cameraActivity(View view) {
        Intent intent = new Intent(this, Camera_Capture.class);
        startActivity(intent);
    }

//    // Take Photo
//    static final int REQUEST_IMAGE_CAPTURE = 1;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }
//
//    /** Called when user clicks the 'Take Photo' Button **/
//    public void takePhoto(View view) {
//        dispatchTakePictureIntent();
//    }
//
//    // Get Image Thumbnail
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            view_photo.setImageBitmap(imageBitmap);
//        }
//    }


}
