package com.example.hands_showprototype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
<<<<<<< HEAD
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
=======
>>>>>>> ecae795d538ecbfe812171798f18e448adde58f3
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class SupporterActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private ListView letters;
    private TextView letterPicked;
    private ImageView img1, img2;
    private boolean isPicTaken;
    private UploadTask task;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private int[] numberImages = {R.drawable.h, R.drawable.e, R.drawable.l, R.drawable.o, R.drawable.w, R.drawable.r, R.drawable.d};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        //create object of listview
        letters = (ListView) findViewById(R.id.lettersList);

//create ArrayList of String
        final ArrayList<String> arrayList = new ArrayList<>();

//Add elements to arraylist
        arrayList.add("Letter H");
        arrayList.add("Letter E");
        arrayList.add("Letter L");
        arrayList.add("Letter O");
        arrayList.add("Letter W");
        arrayList.add("Letter R");
        arrayList.add("Letter D");


//Create Adapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

//assign adapter to listview
        letters.setAdapter(arrayAdapter);

<<<<<<< HEAD
        this.letterPicked = findViewById(R.id.textView14);
        this.img1 = findViewById(R.id.imageView2);
        this.img2 = findViewById(R.id.imageView3);
        this.isPicTaken = false;
=======
        this.letterPicked=findViewById(R.id.displayLetter);
        this.img1=findViewById(R.id.signImg);
        this.img2=findViewById(R.id.picImg);
        this.isPicTaken=false;
>>>>>>> ecae795d538ecbfe812171798f18e448adde58f3

//add listener to listview
        letters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                letterPicked.setText(arrayList.get(i).toString()); // write which letter you picked to upload
                img1.setImageResource(numberImages[i]); // change the picture as picked by user
                tts.speak("You Picked: " + arrayList.get(i).toString(), TextToSpeech.QUEUE_FLUSH, null);//reads
                Toast.makeText(SupporterActivity.this, "You Picked: " + arrayList.get(i).toString(), LENGTH_SHORT).show();
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
        if (this.letterPicked.getText().equals("")) // if user didnt choose a specific letter to upload
        {
            tts.speak("Pick Sign First!", TextToSpeech.QUEUE_FLUSH, null);//reads
            Toast.makeText(getApplicationContext(), "Pick Sign First!", Toast.LENGTH_LONG).show();
        } else if (!this.isPicTaken) // if image wasnt taken yet
        {
            tts.speak("Take Picture First!", TextToSpeech.QUEUE_FLUSH, null);//reads
            Toast.makeText(getApplicationContext(), "Take Picture First!", Toast.LENGTH_LONG).show();
        } else {//upload image taken to google drive

            //ImageView -> Bitmap -> Bitmap.CompressFormat.JPEG -> byte[] data
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            this.img2.setDrawingCacheEnabled(true);
            img2.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) img2.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            final String c = String.valueOf((letterPicked.getText().toString()).charAt(letterPicked.getText().toString().length() - 1));


            if (mAuth.getCurrentUser() != null) {
                db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        db.collection("userstats").document("statsforregular").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        try {
                                            int count = document.getLong("letterCount").intValue();
                                            db.collection("userstats").document("statsforregular").update("letterCount", count + 1);
                                        } catch (Exception e) {
                                            db.collection("userstats").document("statsforregular").update("letterCount", 1);
                                        }
                                    }
                                }
                            }
                        });
                    }

                    task =storageReference.child("Signs/"+c+"/"+"0.jpg").

                    putBytes(data); // name of directory to upload the image to
            task.addOnFailureListener(new

                    OnFailureListener() {
                        @Override
                        public void onFailure (@NonNull Exception exception){
                            Toast.makeText(getApplicationContext(), "Image Upload Failed", LENGTH_SHORT).show();
                            // Handle unsuccessful uploads
                        }
                    }).

                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess (UploadTask.TaskSnapshot taskSnapshot){
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully", LENGTH_SHORT).show();
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        }
                    });
                }
            }
            @Override
            protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data)
            {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 101 && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    this.img2.setImageBitmap(imageBitmap); // update photo of user and show it
                    this.isPicTaken = true;
                } else return;
            }
        }
