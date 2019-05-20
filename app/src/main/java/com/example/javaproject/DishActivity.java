package com.example.javaproject;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class DishActivity extends AppCompatActivity {

    public static final String DISH_NAME = "dish_name";
    public static final String DISH_IMAGE_ID = "dish_image_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        Intent intent = getIntent();
        String dishName = intent.getStringExtra(DISH_NAME);
        int dishImageId = intent.getIntExtra(DISH_IMAGE_ID, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.card_toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.card_collapsing_toolbar);
        ImageView dishImageView = (ImageView) findViewById(R.id.dish_image_view);
        TextView dishContentText = (TextView) findViewById(R.id.dish_content_text);

        //display the toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //set the back button
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //display the title, image and text
        collapsingToolbar.setTitle(dishName);
        Glide.with(this).load(dishImageId).into(dishImageView);
        String dishContent = generateDishContent(dishName);
        dishContentText.setText(dishContent);
    }

    //generate the text
    private String generateDishContent(String dishName)
    {
        StringBuilder dishContent = new StringBuilder();
        for(int i = 0; i< 500; i++)
            dishContent.append(dishName);
        return dishContent.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
