package com.hfad.nwhacks_food_expiration_app;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Azure_Scanner extends AppCompatActivity {

    private Set<String> validItems = new HashSet<>(Arrays.asList("banana"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azure__scanner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public FoodItem jsonToFood(JSONObject jsonObject) {
        String foregroundC = "";
        String backgroundC = "";
        try {
            JSONObject JSONDesc = jsonObject.getJSONObject("description");
            JSONArray JSONTags = JSONDesc.getJSONArray("tags");
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < JSONTags.length(); i++) {
                tags.add(JSONTags.getString(i));
            }
            JSONObject JSONColor = jsonObject.getJSONObject("colors");
            foregroundC = JSONColor.getString("dominantColorForeground");
            backgroundC = JSONColor.getString("dominantColorBackground");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new FoodItem(tags, foregroundC, backgroundC, validItems);
    }

}
