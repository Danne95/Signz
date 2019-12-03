package com.example.hands_showprototype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.util.List;

public class PicToTranslateActivity extends AppCompatActivity {


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

        this.localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("manifest.json")
                .build();
    }

    public void takePicture(View view) {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (imageTakeIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);

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

                        translation.setText(translation.getText()+text); // update sentence translation

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
    public void BackToMain(View view){
        Intent Ret = new Intent(this,MainActivity.class);
        startActivity(Ret);
    }
    private void ErrorMsg()
    {
        Toast txt = Toast.makeText(getApplicationContext(), "Opps, I didn't catch it :(", Toast.LENGTH_SHORT);
        txt.show();
    }

}
