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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class PicToTranslateActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private ImageView mimageView;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private FirebaseAutoMLLocalModel localModel;
    private FirebaseVisionImage image;
    private FirebaseVisionImageLabeler labeler;
    private TextView translation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_to_translate);
        mimageView = findViewById(R.id.imageView);
        translation=findViewById(R.id.textView);
        translation.setText(null);
        this.localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("manifest.json")
                .build();
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });
    }

    public void takePicture(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            Toast txt = Toast.makeText(getApplicationContext(), "Please, grant permission to open camera.", Toast.LENGTH_LONG);
            txt.show();
            tts.speak("Please, grant permission to open camera", TextToSpeech.QUEUE_FLUSH, null);//reads
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
        }

        Intent imageTakeIntent;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (imageTakeIntent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }

        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                            .setConfidenceThreshold(0.5f)  // Evaluate your model in the Firebase console
                            // to determine an appropriate value.
                            .build();
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
        } catch (FirebaseMLException e) {
            //...
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mimageView.setImageBitmap(imageBitmap);
            this.image = FirebaseVisionImage.fromBitmap(imageBitmap); // save the image to send it to FB
        }
        else return;

        labeler.processImage(this.image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        if (labels.size() == 0)
                        {
                            ErrorMsg();
                            return;
                        }
                        String text = labels.get(0).getText();
                        float maxConfidence = labels.get(0).getConfidence();
                        for (int i = 1; i < labels.size(); i++) // find the best match
                            if (labels.get(i).getConfidence() > maxConfidence) {
                                maxConfidence = labels.get(i).getConfidence();
                                text = labels.get(i).getText();
                            }
                        if(maxConfidence<0.5) // if the process wasn't good enough (under 50%)
                        {
                            ErrorMsg();
                            return;
                        }
                        if(((Switch)findViewById(R.id.ttsState)).isChecked()) {
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);//reads letter
                        }
                        try {
                            if(text.equals("SPACE")) {
                                translation.setText(translation.getText() + " "); // update sentence translation
                                Toast txt = Toast.makeText(getApplicationContext(), "Space added.", Toast.LENGTH_SHORT);
                                txt.show();
                            }
                            else if(text.equals("DELETE")) {
                                if(translation.length()==0)
                                    Toast.makeText(getApplicationContext(), "The sentence is empty!", Toast.LENGTH_SHORT).show();
                                else {
                                    translation.setText(translation.getText().subSequence(0, translation.getText().length() - 1)); // update sentence translation
                                    Toast.makeText(getApplicationContext(), "Last letter deleted.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                                translation.setText(translation.getText() + text); // update sentence translation
                        }
                        catch(Exception e){}
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ErrorMsg();
                        return;
                    }
                });
    }
    private void ErrorMsg()
    {
        Toast txt = Toast.makeText(getApplicationContext(), "Oops, I didn't catch it :(", Toast.LENGTH_SHORT);
        txt.show();
        if(((Switch)findViewById(R.id.ttsState)).isChecked()) {
            tts.speak("Oops, I didn't catch it", TextToSpeech.QUEUE_FLUSH, null);//reads
        }
    }

    public void ClearText(View view)
    {
        this.translation.setText(""); // clear the text of translation
    }

    public void ReadSentence(View view){
        try {
            String sentence = translation.getText().toString();
            if (sentence != "") {
                tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null);
            }
            else {
                Toast.makeText(getApplicationContext(), "There's no sentence yet.", Toast.LENGTH_SHORT).show();
                tts.speak("There's no sentence yet", TextToSpeech.QUEUE_FLUSH, null);//reads
            }
        }
        catch(Exception e) {
            Toast.makeText(getApplicationContext(), "There's no sentence yet.", Toast.LENGTH_SHORT).show();
            tts.speak("There's no sentence yet", TextToSpeech.QUEUE_FLUSH, null);//reads
        }
    }

}
