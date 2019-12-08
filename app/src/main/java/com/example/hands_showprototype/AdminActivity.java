package com.example.hands_showprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void GoToSupportQ(View view){
        Intent SupportQ = new Intent(this, SQActivity.class);
        startActivity(SupportQ);
    }



}



