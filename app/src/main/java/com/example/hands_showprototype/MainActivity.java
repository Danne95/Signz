package com.example.hands_showprototype;


import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;


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

    public void GoToTranslate(View view){
        Intent SignTranslate = new Intent(this,PicToTranslateActivity.class);
        startActivity(SignTranslate);
    }

    public void GoToLearn(View view){
        Intent LearnLang = new Intent(this, LearnLanguage.class);
        startActivity(LearnLang);
    }

    public void GoToSupporter(View view){
        Intent Supporter = new Intent(this, LearnLanguage.class);
        startActivity(Supporter);
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
                                    else if (document.get("accesslevel").hashCode()==2) accessName="Admin";
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
        }
    }

    public void SignOut(View view) {
        mAuth.signOut();
        updateUI(null);
    }
}
