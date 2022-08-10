package com.nunmsc.bcisng.nafisa.sec;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    ImageView img;
    Animation slideUpAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img = (ImageView) findViewById(R.id.arm);

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_animation);
        img.startAnimation(slideUpAnimation);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i =new Intent(Splash.this,UserMain.class);
                startActivity(i);
                finish();
            }
        },3000);

    }
}
