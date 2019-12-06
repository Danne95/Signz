package com.example.hands_showprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
    }

    public void SignUp(View view) {
        final String email=((EditText)findViewById(R.id.emailInput)).getText().toString();
        String password=((EditText)findViewById(R.id.passInput)).getText().toString();
        String confpassword=((EditText)findViewById(R.id.confPassInput)).getText().toString();
        final String name=((EditText)findViewById(R.id.nameInput)).getText().toString();

        // Check that fields are not empty
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confpassword) || TextUtils.isEmpty(name)) {

            Toast.makeText(getApplicationContext(), "All fields must be filled.", Toast.LENGTH_LONG).show();
        } else if (!password.equals(confpassword)) {

            Toast.makeText(getApplicationContext(), "Password and confirm password should match", Toast.LENGTH_LONG).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    if(mAuth.getCurrentUser()!=null){
                        db= FirebaseFirestore.getInstance();
                        if(db!=null){
                            // Create a new user with a first, middle, and last name
                            Map<String, Object> nuser = new HashMap<>();
                            nuser.put("uid",mAuth.getCurrentUser().getUid());
                            nuser.put("email",email);
                            nuser.put("name", name);
                            nuser.put("accesslevel", 0);
                            nuser.put("pendingsupport", ((CheckBox)findViewById(R.id.supporterCheckBox)).isChecked());
                            // Add a new document with a generated ID
                            db.collection("users")
                                    .add(nuser)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "DocumentSnapshot added with ID: " + documentReference.getId(),Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error adding document"+e, Toast.LENGTH_LONG).show();
                                            mAuth.getCurrentUser().delete();
                                        }
                                    });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
