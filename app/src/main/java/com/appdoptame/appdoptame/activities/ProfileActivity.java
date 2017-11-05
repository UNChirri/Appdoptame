package com.appdoptame.appdoptame.activities;

import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.model.Profile;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    CarouselView carouselView;
    ArrayList <Drawable> drawableResources;
    ImageView image;
    SliderLayout sliderShow;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        drawableResources = new ArrayList<>();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            profile = (Profile)getIntent().getSerializableExtra("profile"); //Obtaining data
        }
        Log.d("Profile", profile.toString());

        for (String str: profile.getPhotos()
                ) {
            drawableResources.add(getDrawable(str));
        }

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(profile.getName());

        Log.d("Profile", profile.toString());
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        int n = profile.getPhotos().size();
        carouselView.setPageCount(n);
        carouselView.setImageListener(imageListener);

        carouselView.setOnTouchListener(new ImageMatrixTouchHandler(getApplicationContext()));
       carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("test", "Touch" + position);
                image = (ImageView) findViewById(R.id.temp_image);
                image.setBackground(drawableResources.get(position));
                image.setOnTouchListener(new ImageMatrixTouchHandler(getApplicationContext()));
                
            }
        });
////
        TextView tv = (TextView) findViewById(R.id.name);
        tv.setText(profile.getName());

        tv = (TextView) findViewById(R.id.breed);
        tv.setText(profile.getBreed());

        tv = (TextView) findViewById(R.id.age);
        tv.setText(profile.getAge().toString());

        tv = (TextView) findViewById(R.id.description);
        tv.setText(profile.getDescription());

        tv = (TextView) findViewById(R.id.location);
        tv.setText(profile.getLocation());
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(getApplicationContext())
                    .load(profile.getPhotos().get(position))
                    .centerCrop()
                    .placeholder(drawableResources.get(position))
                    .into(imageView);
        }
    };


    void showToolbar(String title, Boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);
    }

    public static Drawable getDrawable(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}
