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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
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
        setContentView(R.layout.activity_home__page);

        view_photo = (ImageView) findViewById(R.id.imgview_photo);
        //txtResult = (TextView) findViewById(R.id.txt_result);
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
        }
    }

    /** Called when user clicks the 'Take Photo' Button **/
    public void takePhoto(View view) {
        dispatchTakePictureIntent();
    }

    // Get Image Thumbnail
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        FoodList history = null;
        loadFromDisk(history);

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
                        String[] features = {"ImageType", "Colour", "Description"};    // Get description from the API
                        String[] details = {};

                        AnalysisResult result = visionServiceClient.analyzeImage(inputStreams[0], features, details);

                        String jsonResultstr = new Json().toJson(result);
                        JsonObject jsonObjResult = new JsonParser().parse(jsonResultstr).getAsJsonObject();
                        Azure_Scanner scanner = new Azure_Scanner();
                        FoodItem resultFood = scanner.jsonToFood(jsonObjResult);
                        history.addFoodItem(resultFood);
                        saveToDisk(history);


                        return jsonResultstr;

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (VisionServiceException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String s){
                    if (TextUtils.isEmpty(s)) {
                        Toast.makeText(Camera_Capture.this, "API returned empty result", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.dismiss();

                        AnalysisResult result = new Json().fromJson(s, AnalysisResult.class);
                        StringBuilder resultText = new StringBuilder();
                        for (Caption caption : result.description.captions)
                            resultText.append(caption.text);
                    }
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    progressDialog.setMessage(values[0]);
                }
             };

            // Run the task
            visionTask.execute(inputStream);
        }
    }

    public void saveToDisk(FoodList history) {
        Gson gson = new Gson();
        String toSave = gson.toJson(history);
        try {
            FileWriter file = new FileWriter("local/saveHistory.json");
            file.write(toSave);
            file.flush();
            file.close();
        }
        catch (Exception e) {

        }
    }

    public void loadFromDisk(FoodList history) {
        Gson gson = new Gson();

        try {
            Type listType = new TypeToken<FoodList>() { }.getType();
            history = gson.fromJson(new FileReader(new File("local/saveHistory.json")), listType);
        }
        catch (Exception e) {
            history = new FoodList();
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
