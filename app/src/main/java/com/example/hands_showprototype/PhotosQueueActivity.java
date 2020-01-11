package com.example.hands_showprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class PhotosQueueActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private LinearLayout list;
    private UploadTask uploadTask;
    private ImageView photo;
    private TextView sign;
    private TextToSpeech tts;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_queue);

        this.storageReference= FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        list = findViewById(R.id.insideScroll);
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        IterateOverPhotos();
    }

    private void IterateOverPhotos() {
        StorageReference toApprove = storageReference.child("Signs/toApprove/");
        Task<ListResult> toApproveTask = toApprove.listAll();
        // Now we get the references of these images

        // FAILS HERE! NEEDS TO CHECK HOW TO ITERATE OVER FILES INSIDE A SPECIFIC FOLDER (toApprove)
        //iterate over folders inside 'toApprove' folder (signs folders)
        toApproveTask.addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult result) {
                for (StorageReference FolderRef : result.getPrefixes()) {
                    Task<ListResult> signTask = FolderRef.listAll();
                    //now iterate over images inside a specific sign folder (inside 'toApprove' folder)
                    signTask.addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult result) {
                            for (final StorageReference fileRef : result.getItems())
                                //add the waiting to approval image to the view of the activity (for the admin to see and decide what to do with it)
                                fileRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            addALinearView(fileRef.getName(), BitmapFactory.decodeByteArray(bytes,0,bytes.length), fileRef.getPath());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception exception) {
                            // Handle any errors
                            //if directory of 'signName' doesn't exist yet
                            Toast.makeText(getApplicationContext(), "No Photos to Approve.", LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle any errors
                //if directory of 'signName' doesn't exist yet
                Toast.makeText(getApplicationContext(), "No Photos to Approve.", LENGTH_SHORT).show();
            }
        });

    }

    private void addALinearView(final String signName, final Bitmap img, final String path) {
        //Params var for all views.
        RelativeLayout.LayoutParams params;
        //New line of a pending supporter with settings.
        RelativeLayout newline = new RelativeLayout(this);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        newline.setLayoutParams(params);

        //Enter photo from toApprove
        photo=new ImageView(this);
        photo.setImageBitmap(img);
        photo.setId(View.generateViewId());
        photo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        FrameLayout frame = new FrameLayout(this);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        FrameLayout.LayoutParams fparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        frame.addView(photo, fparams);
        params = new RelativeLayout.LayoutParams(500, 500);
        newline.addView(frame, params);

        //Making view for sign name and settings to it.
        sign=new TextView(this);
        this.sign.setText(storageReference.child(path).getParent().getName());
        sign.setTextSize(50);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        newline.addView(this.sign, params);


        //Making approve and decline buttons and settings to them.
        ImageButton ApproveButton = new ImageButton(this), DeclineButton = new ImageButton(this);
        //Approve button.
        ApproveButton.setId(View.generateViewId());
        ApproveButton.setImageResource(R.drawable.ic_input_add);
        ApproveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //image view to byte[] in order to upload to FB
                photo.setDrawingCacheEnabled(true);
                photo.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data = baos.toByteArray();

                uploadTask = storageReference.child("Signs/" + storageReference.child(path).getParent().getName() + "/" + signName).putBytes(data); // copy the picture to the correct folder (by sign)
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        tts.speak("Photo Approval Failed", TextToSpeech.QUEUE_FLUSH, null);//reads
                        Toast.makeText(getApplicationContext(), "Photo Approval Failed", LENGTH_SHORT).show();
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        tts.speak("Photo Approval Succeeded", TextToSpeech.QUEUE_FLUSH, null);//reads
                        Toast.makeText(getApplicationContext(), "Photo Approval Succeeded", LENGTH_SHORT).show();
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        storageReference.child(path).delete(); // delete the photo from FB storage
                        ((RelativeLayout) v.getParent()).setVisibility(View.GONE);
                    }
                });
            }
        });
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        newline.addView(ApproveButton, params);
        //Decline button.
        DeclineButton.setId(View.generateViewId());
        DeclineButton.setImageResource(R.drawable.ic_delete);
        DeclineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    storageReference.child(path).delete(); // delete the photo from FB storage
                    tts.speak("Photo Deletion Succeeded", TextToSpeech.QUEUE_FLUSH, null);//reads
                    Toast.makeText(getApplicationContext(), "Photo Deletion Succeeded", LENGTH_SHORT).show();
                    ((RelativeLayout) v.getParent()).setVisibility(View.GONE);
                }
                catch (Exception e)
                {
                    tts.speak("Photo Deletion Failed", TextToSpeech.QUEUE_FLUSH, null);//reads
                    Toast.makeText(getApplicationContext(), "Photo Deletion Failed", LENGTH_SHORT).show();
                }
            }
        });

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.BELOW,ApproveButton.getId());
        newline.addView(DeclineButton, params);
        //Adding RelativeLayout to our LinearLayout(son of ScrollView).
        list.addView(newline);
    }
}
