package com.hfad.nwhacks_food_expiration_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import edmt.dev.edmtdevcognitivevision.Contract.AnalysisResult;
import edmt.dev.edmtdevcognitivevision.Contract.Caption;
import edmt.dev.edmtdevcognitivevision.Rest.VisionServiceException;
import edmt.dev.edmtdevcognitivevision.VisionServiceClient;
import edmt.dev.edmtdevcognitivevision.VisionServiceRestClient;

public class Camera_Capture extends AppCompatActivity {
    private final String API_KEY = "23dc58af58194da9a7dc03a196537d22";
    private final String API_LINK = "https://nwhacksfoodscanner.cognitiveservices.azure.com/vision/v1.0";

    //TextView txtResult;

    // Declare vision client
    VisionServiceClient visionServiceClient = new VisionServiceRestClient(API_KEY, API_LINK);

    ImageView view_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera__capture);

        view_photo = (ImageView) findViewById(R.id.imgview_photo);
        //txtResult = (TextView) findViewById(R.id.txt_result);
    }

    // Take Photo
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = createImageFile();
                System.out.println(createImageFile().toString());
            } catch (IOException ex) {
                // error occured while creating file
            }
            // continue only if file was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.hfad.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
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

            // Convert bitmap to ByteArray
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            // Request API
            AsyncTask<InputStream, String, String> visionTask = new AsyncTask<InputStream, String, String>(){
                ProgressDialog progressDialog = new ProgressDialog(Camera_Capture.this);

                @Override
                protected void onPreExecute() {
                    progressDialog.show();
                }

                @Override
                protected String doInBackground(InputStream... inputStreams){
                    try {
                        publishProgress("Recognizing... ");
                        String[] features = {"Description"};    // Get description from the API
                        String[] details = {};

                        AnalysisResult result = visionServiceClient.analyzeImage(inputStreams[0], features, details);

                        String jsonResult = new Gson().toJson(result);
                        return jsonResult;

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (VisionServiceException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String s){
                    progressDialog.dismiss();

                    AnalysisResult result = new Gson().fromJson(s, AnalysisResult.class);
                    StringBuilder resultText = new StringBuilder();
                    for (Caption caption:result.description.captions)
                        resultText.append(caption.text);
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    progressDialog.setMessage(values[0]);
                }
             };
        }
    }

    // Save photo
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,     // prefix
                ".jpg",     // suffix
                storageDir         // directory
        );

        // Save a file: path for use with 'ACTION_VIEW' intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        // Ensure that there's a camera activity to handle the intent
//
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create File where photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // error occured while creating file
//            }
//            // continue only if file was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }

}
