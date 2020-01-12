package com.hfad.nwhacks_food_expiration_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera_Capture extends AppCompatActivity {
    ImageView view_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera__capture);

        view_photo = (ImageView) findViewById(R.id.imgview_photo);
    }

    // Take Photo
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
           startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//             File photoFile = null;
//             try {
//                 photoFile = createImageFile();
//                 System.out.println(createImageFile().toString());
//             } catch (IOException ex) {
//                 // error occured while creating file
//             }
//             // continue only if file was successfully created
//             if (photoFile != null) {
//                 Uri photoURI = FileProvider.getUriForFile(this,
//                         "com.hfad.android.fileprovider",
//                         photoFile);
//                 takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                 startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//             }
//         }
    }

    /** Called when user clicks the 'Take Photo' Button **/
    public void takePhoto(View view) {
        dispatchTakePictureIntent();
    }

    // Get Image Thumbnail
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            view_photo.setImageBitmap(imageBitmap);
        }
    }

//     // Save photo
//     String currentPhotoPath;

//     private File createImageFile() throws IOException {
//         // Create image file name
//         String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//         String imageFileName = "JPEG_" + timeStamp + "_";
//         File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//         File image = File.createTempFile(
//                 imageFileName,     // prefix
//                 ".jpg",     // suffix
//                 storageDir         // directory
//         );

//         // Save a file: path for use with 'ACTION_VIEW' intents
//         currentPhotoPath = image.getAbsolutePath();
//         return image;
//     }
    }
}
