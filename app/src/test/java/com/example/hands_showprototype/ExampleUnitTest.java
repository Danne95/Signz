package com.example.hands_showprototype;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private FirebaseVisionImageLabeler labeler;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    private String CheckScanLetter_L()
    {
        Bitmap imgBMP=BitmapFactory.decodeFile("res/drawable/r.jpg");
        FirebaseVisionImage img = FirebaseVisionImage.fromBitmap(imgBMP);

        FirebaseAutoMLLocalModel localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("manifest.json")
                .build();

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

        final String[] text = new String[1];
        labeler.processImage(img)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        if (labels.size() == 0)
                            return;

                        text[0] = labels.get(0).getText();
                        float maxConfidence = labels.get(0).getConfidence();

                        for (int i = 1; i < labels.size(); i++) // find the best match
                            if (labels.get(i).getConfidence() > maxConfidence) {
                                maxConfidence = labels.get(i).getConfidence();
                                text[0] = labels.get(i).getText();
                            }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        return;
                    }
                });

        return text[0];
    }

    @Test
    public void Button_SignsTranslation_isWorking(){
        assertEquals("L",CheckScanLetter_L());
     }
}