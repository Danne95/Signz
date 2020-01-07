package com.example.hands_showprototype;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class SupporterActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private ListView letters;
    private TextView letterPicked;
    private ImageView img1, img2;
    private boolean isPicTaken;
    private int[] numberImages={R.drawable.h,R.drawable.e,R.drawable.l,R.drawable.o,R.drawable.w,R.drawable.r,R.drawable.d};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        //create object of listview
        letters=(ListView)findViewById(R.id.lettersList);

//create ArrayList of String
        final ArrayList<String> arrayList=new ArrayList<>();

//Add elements to arraylist
        arrayList.add("Letter H");
        arrayList.add("Letter E");
        arrayList.add("Letter L");
        arrayList.add("Letter O");
        arrayList.add("Letter W");
        arrayList.add("Letter R");
        arrayList.add("Letter D");


//Create Adapter
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

//assign adapter to listview
        letters.setAdapter(arrayAdapter);

        this.letterPicked=findViewById(R.id.textView14);
        this.img1=findViewById(R.id.imageView2);
        this.img2=findViewById(R.id.imageView3);
        this.isPicTaken=false;

//add listener to listview
        letters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,long l) {
                letterPicked.setText(arrayList.get(i).toString()); // write which letter you picked to upload
                img1.setImageResource(numberImages[i]); // change the picture as picked by user
                tts.speak("You Picked: "+arrayList.get(i).toString(), TextToSpeech.QUEUE_FLUSH, null);//reads
                Toast.makeText(SupporterActivity.this,"You Picked: "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void takePicture(View view) {
        // take new picture of user sign
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            tts.speak("Please, grant permission to open camera", TextToSpeech.QUEUE_FLUSH, null);//reads
            Toast txt = Toast.makeText(getApplicationContext(), "Please, grant permission to open camera.", Toast.LENGTH_LONG);
            txt.show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
        Intent imageTakeIntent;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (imageTakeIntent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(imageTakeIntent, 101);
        }
    }
    public void UploadPicture(View view) {
        if(this.letterPicked.getText().equals("")) // if user didnt choose a specific letter to upload
        {
            tts.speak("Pick Sign First!", TextToSpeech.QUEUE_FLUSH, null);//reads
            Toast.makeText(getApplicationContext(), "Pick Sign First!", Toast.LENGTH_LONG).show();
        }
        else if(!this.isPicTaken) // if image wasnt taken yet
        {
            tts.speak("Take Picture First!", TextToSpeech.QUEUE_FLUSH, null);//reads
            Toast.makeText(getApplicationContext(), "Take Picture First!", Toast.LENGTH_LONG).show();
        }
        else
        {
            //upload image taken to google drive



            tts.speak("Picture Uploaded Successfully!", TextToSpeech.QUEUE_FLUSH, null);//reads
            Toast.makeText(getApplicationContext(), "Picture Uploaded Successfully!", Toast.LENGTH_LONG).show();
        }
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 101 && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                this.img2.setImageBitmap(imageBitmap); // update photo of user and show it
                this.isPicTaken=true;
            }
            else return;
    }
}
