package com.example.hands_showprototype;


import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.quickstart.auth.R;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
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
        Intent LearnLang = new Intent(this,LearnLanguage.class);
        startActivity(LearnLang);
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            findViewById(R.id.SignIn).setVisibility(View.GONE);
            findViewById(R.id.SignUp).setVisibility(View.GONE);
            findViewById(R.id.SignOut).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.SignIn).setVisibility(View.VISIBLE);
            findViewById(R.id.SignUp).setVisibility(View.VISIBLE);
            findViewById(R.id.SignOut).setVisibility(View.GONE);
            /*mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.GONE);*/
        }
    }

    public void SignOut(View view) {
        mAuth.signOut();
        updateUI(null);
    }
}
