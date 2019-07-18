package com.hbandroid.viewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hbandroid.viewdemo.view.DefineProgressView;

public class MainActivity extends AppCompatActivity {

    private DefineProgressView levelView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        levelView = (DefineProgressView) findViewById(R.id.levelView);

    }

    int i = 1;

    public void clickGo(View view) {
        i++;
        levelView.resetLevelProgress(i);
    }

    public void clickBack(View view) {
        levelView.resetLevelProgress(100);
    }

    public void clickReset(View view) {
        levelView.resetLevelProgress(50);
    }
}
