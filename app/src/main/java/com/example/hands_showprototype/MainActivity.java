package com.example.hands_showprototype;


import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.util.Map;
import java.util.HashMap;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.tasks.OnCompleteListener;
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


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void GoToSignUp(View view){
        Intent SignUp = new Intent(this,SignUpActivity.class);
        startActivity(SignUp);
    }

    public void GoToSignIn(View view){
        Intent SignIn = new Intent(this,SignInActivity.class);
        startActivity(SignIn);
    }

    public void GoToTranslate(View view) {
        Intent SignTranslate = new Intent(this, PicToTranslateActivity.class);
        UpdateStats("GoToTranslate");
        startActivity(SignTranslate);
    }

    public void GoToLearn(View view){
        Intent LearnLang = new Intent(this,LearnLanguage.class);
        UpdateStats("GoToLearn");
        startActivity(LearnLang);
    }

    public void GoToAdmin(View view){
        Intent Admin = new Intent(this,AdminActivity.class);
        UpdateStats("GoToAdmin");
        startActivity(Admin);
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            findViewById(R.id.SignIn).setVisibility(View.GONE);
            findViewById(R.id.SignUp).setVisibility(View.GONE);
            findViewById(R.id.SignOut).setVisibility(View.VISIBLE);
            db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if(document.get("accesslevel").hashCode()>=0) {
                                String accessName="Regular";
                                if (document.get("accesslevel").hashCode() >= 1) {
                                    findViewById(R.id.Supporter).setVisibility(View.VISIBLE);
                                    if (document.get("accesslevel").hashCode()==1) accessName="Supporter";
                                    else if (document.get("accesslevel").hashCode()==2){
                                        accessName="Admin";
                                        findViewById(R.id.Admin).setVisibility(View.VISIBLE);
                                    }
                                    else
                                        findViewById(R.id.Admin).setVisibility(View.GONE);
                                } else {
                                    findViewById(R.id.Supporter).setVisibility(View.GONE);
                                }
                                String name= "Hey "+document.get("name").toString()+'('+accessName+')'+',';
                                ((TextView)findViewById(R.id.Welcome)).setText(name);
                                findViewById(R.id.Welcome).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });
        } else {
            findViewById(R.id.Welcome).setVisibility(View.GONE);
            findViewById(R.id.SignIn).setVisibility(View.VISIBLE);
            findViewById(R.id.SignUp).setVisibility(View.VISIBLE);
            findViewById(R.id.SignOut).setVisibility(View.GONE);
            findViewById(R.id.Supporter).setVisibility(View.GONE);
            findViewById(R.id.Admin).setVisibility(View.GONE);
        }
    }

    public void UpdateStats(final String funcname){
        if (mAuth.getCurrentUser() != null) {
            db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    int accesslevel = 0;
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            accesslevel = document.get("accesslevel").hashCode();
                        }
                    }
                    if (accesslevel == 0) {
                        db.collection("userstats").document("statsforregular").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        try {
                                            int count = document.get(funcname).hashCode();
                                            db.collection("userstats").document("statsforregular").update(funcname, count + 1);
                                        }
                                        catch(Exception e){
                                            db.collection("userstats").document("statsforregular").update(funcname,1);
                                        }
                                    }
                                }
                            }
                        });
                    }
                    else if(accesslevel==1){
                        db.collection("userstats").document("statsforsupporter").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        try {
                                            int count = document.get(funcname).hashCode();
                                            db.collection("userstats").document("statsforsupporter").update(funcname, count + 1);
                                        }
                                        catch(Exception e){
                                            db.collection("userstats").document("statsforsupporter").update(funcname,1);
                                        }
                                    }
                                }
                            }
                        });
                    }
                    else if(accesslevel==2){
                        db.collection("userstats").document("statsforadmin").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        try {
                                            int count = document.get(funcname).hashCode();
                                            db.collection("userstats").document("statsforadmin").update(funcname, count + 1);
                                        }
                                        catch(Exception e){
                                            db.collection("userstats").document("statsforadmin").update(funcname,1);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public void SignOut(View view) {
        mAuth.signOut();
        updateUI(null);
    }
}
