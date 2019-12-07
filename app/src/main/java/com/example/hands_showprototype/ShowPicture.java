package com.example.hands_showprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowPicture extends AppCompatActivity {
    private String[] lettersNames={"H","E","L","O","W","R","D","Delete","Space"};
    private int[] numberImages={R.drawable.h,R.drawable.e,R.drawable.l,R.drawable.o,R.drawable.w,R.drawable.r,R.drawable.d};
    private ImageView letterImg;
    private TextView letterText;
    private int imgIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

        Intent mIntent = getIntent();
        this.imgIndex = mIntent.getIntExtra("imgIndex", 0);

        this.letterImg=findViewById(R.id.LetterPicture);
        this.letterText=findViewById(R.id.LetterName);

        this.letterImg.setImageResource(this.numberImages[this.imgIndex]);
        this.letterText.setText(this.lettersNames[this.imgIndex]);
    }
}
