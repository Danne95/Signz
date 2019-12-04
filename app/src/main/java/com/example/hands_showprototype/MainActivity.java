package com.example.hands_showprototype;


import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GoToTranslate(View view){
        Intent SignTranslate = new Intent(this,PicToTranslateActivity.class);
        startActivity(SignTranslate);
    }

    public void GoToLearn(View view){
        Intent LearnLang = new Intent(this,LearnLanguage.class);
        startActivity(LearnLang);
    }
}
