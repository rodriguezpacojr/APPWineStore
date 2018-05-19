package com.rodriguezpacojr.winestore;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity {
    @BindView(R.id.pbwelcome)
    ProgressBar pbwelcome;


    private   Activity activity  = this;
    private Threads objT;

    @Override
    protected void onPause() {
        super.onPause();
        objT.cancel(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);
        objT = new Threads(pbwelcome,this, activity);
        objT.execute();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}