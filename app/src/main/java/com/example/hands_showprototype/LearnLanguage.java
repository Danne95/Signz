package com.example.hands_showprototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class SupporterActivity extends AppCompatActivity {
    private GridView letters;
    private String[] lettersNames={"H","E","L","O","W","R","D","Delete","Space"};
    private int[] numberImages={R.drawable.h,R.drawable.e,R.drawable.l,R.drawable.o,R.drawable.w,R.drawable.r,R.drawable.d};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter);

        letters = findViewById(R.id.gridView);

        MainAdapter adapter = new MainAdapter(SupporterActivity.this,lettersNames,numberImages);
        letters.setAdapter(adapter);

        letters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast toast= Toast.makeText(getApplicationContext(),"That's the letter "+lettersNames[i],Toast.LENGTH_SHORT);
                toast.show();

                Intent showPictureIntent = new Intent(SupporterActivity.this, ShowPicture.class);
                showPictureIntent.putExtra("imgIndex", i);
                startActivity(showPictureIntent);
            }
        });
    }
}
