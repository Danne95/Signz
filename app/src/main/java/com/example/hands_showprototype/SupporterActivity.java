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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class SupporterActivity extends AppCompatActivity {
    //vars
    private FirebaseVisionImageLabeler labeler;
    private List<FirebaseVisionImageLabel> Labels;
    private FirebaseAutoMLLocalModel localModel;
    private TextToSpeech tts;
    private ListView letters;
    private TextView letterPicked;
    private ImageView img1, img2;
    private boolean isPicTaken;
    private boolean isPicGood;
    private boolean toApprove;
    private boolean existsInLabels;
    private UploadTask uploadTask;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private int[] numberImages = {R.drawable.h, R.drawable.e, R.drawable.l, R.drawable.o, R.drawable.w, R.drawable.r, R.drawable.d};
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("manifest.json")
                .build();
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


        this.letterPicked = findViewById(R.id.displayLetter);
        this.img1 = findViewById(R.id.signImg);
        this.img2 = findViewById(R.id.picImg);
        this.isPicTaken = false;

        this.letterPicked = findViewById(R.id.displayLetter);
        this.img1 = findViewById(R.id.signImg);
        this.img2 = findViewById(R.id.picImg);
        this.isPicTaken = false;


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
        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                            .setConfidenceThreshold(0.3f)  // Evaluate your model in the Firebase console
                            // to determine an appropriate value.
                            .build();
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
        } catch (FirebaseMLException e) {
            //...
        }
    }
    public void UploadPhaseOne(View view) {
        if (this.letterPicked.getText().equals("")) // if user didn't choose a specific letter to upload
        {
            tts.speak("Pick Sign First!", TextToSpeech.QUEUE_FLUSH, null);//reads
            Toast.makeText(getApplicationContext(), "Pick Sign First!", Toast.LENGTH_LONG).show();
        } else if (!this.isPicTaken) // if image wasnt taken yet
        {
            tts.speak("Take Picture First!", TextToSpeech.QUEUE_FLUSH, null);//reads
            Toast.makeText(getApplicationContext(), "Take Picture First!", Toast.LENGTH_LONG).show();
        } else {
            final String c = String.valueOf((letterPicked.getText().toString()).charAt(letterPicked.getText().toString().length() - 1));
            if (isPicGood) {
                if(c.equalsIgnoreCase(Labels.get(0).getText())) toApprove=false;
                else toApprove=true;
                if(toApprove) {
                    for (int i = 0; i < Labels.size(); i++) {
                        if (c.equalsIgnoreCase(Labels.get(i).getText())) {
                            existsInLabels = true;
                        } else existsInLabels = false;
                    }
                }
                else existsInLabels = true;
                if (existsInLabels) {
                    Task<DocumentSnapshot> taskdoc = db.collection("userstats").document("letterscounter").get();
                    taskdoc.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                final DocumentSnapshot document = task.getResult();
                                String path = c;
                                if (toApprove) {
                                    path= "toApprove/"+path;
                                }
                                if (document.exists()) {
                                    int count = 0;
                                    try {
                                        count = document.getLong(c).intValue();
                                    } catch (Exception e) { // if sign counter doesnt exist yet in DB
                                        count = 0;
                                    }
                                    count++;
                                    db.collection("userstats").document("letterscounter").update(c, count);
                                    UpdatePersonalCounter();
                                    UploadPicture(path, count, document);
                                }
                            }
                        }
                    });
                } else {
                    tts.speak("Your picture probably doesn't match the sign", TextToSpeech.QUEUE_FLUSH, null);//reads
                    Toast.makeText(getApplicationContext(), "Your picture probably doesn't match the sign.", Toast.LENGTH_LONG).show();
                }
            } else {
                tts.speak("Your picture isn't good enough", TextToSpeech.QUEUE_FLUSH, null);//reads
                Toast.makeText(getApplicationContext(), "Your picture isn't good enough.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void UploadPicture(final String c,final int count,final DocumentSnapshot document) {
        //ImageView -> Bitmap -> Bitmap.CompressFormat.JPEG -> byte[] data
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        img2.setDrawingCacheEnabled(true);
        img2.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img2.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();
        uploadTask = storageReference.child("Signs/" + c + "/" + count + ".jpg").putBytes(data); // name of directory to upload the image to
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                db.collection("userstats").document("letterscounter").update(c, document.getLong(c).intValue() - 1);
                tts.speak("Image Upload Failed", TextToSpeech.QUEUE_FLUSH, null);//reads
                Toast.makeText(getApplicationContext(), "Image Upload Failed", LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                tts.speak("Image Uploaded Successfully", TextToSpeech.QUEUE_FLUSH, null);//reads
                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully", LENGTH_SHORT).show();
                isPicTaken = false;
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            }
        });
    }

    private void WhereToUpload(Bitmap img){
        Task<List<FirebaseVisionImageLabel>> task = labeler.processImage(FirebaseVisionImage.fromBitmap(img));
        task.addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        Labels=labels;
                        if (labels.size() == 0)
                        {
                            isPicGood=false;
                        }
                        else isPicGood = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isPicGood=false;
                    }
                });
    }
    private void UpdatePersonalCounter(){
        final String c = String.valueOf((letterPicked.getText().toString()).charAt(letterPicked.getText().toString().length() - 1));
        Task<DocumentSnapshot> taskdoc = db.collection("users").document(mAuth.getUid()).get();
        taskdoc.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Long uploads;
                        try {
                            uploads = document.getLong("Uploads." + c);
                            if (uploads == null) uploads = (long) 0;
                        } catch (Exception e) { // if sign counter doesnt exist yet in DB
                            uploads = (long)0;
                        }
                        uploads++;
                        db.collection("users").document(mAuth.getUid()).update("Uploads."+c, uploads);
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.img2.setImageBitmap(imageBitmap); // update photo of user and show it
            this.isPicTaken = true;
            WhereToUpload(imageBitmap);
        } else return;
    }
}

