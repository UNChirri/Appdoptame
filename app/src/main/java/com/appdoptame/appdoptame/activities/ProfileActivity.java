package com.appdoptame.appdoptame.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.model.Profile;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

public class ProfileActivity extends AppCompatActivity {

    SliderLayout sliderShow;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            profile = (Profile)getIntent().getSerializableExtra("profile"); //Obtaining data
        }

        sliderShow = (SliderLayout) findViewById(R.id.slider);

        for(String url:profile.getPhotos())
        {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(profile.getName())
                    .image(url);

            sliderShow.addSlider(textSliderView);
        }

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

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }
}
