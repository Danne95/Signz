package com.example.hands_showprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class AdminActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
    }

    public void GoToSupportQ(View view){
        Intent SupportQ = new Intent(this, SupporterQueueActivity.class);
        UpdateStats("GoToSupportQ");
        startActivity(SupportQ);
    }

    public void GoToDeleteUser(View view){
        Intent DeleteU = new Intent(this, DeleteUserActivity.class);
        UpdateStats("GoToDeleteUser");
        startActivity(DeleteU);
    }

    public void GoToDemote(View view){
        Intent Demote = new Intent(this, DemoteActivity.class);
        UpdateStats("GoToDemote");
        startActivity(Demote);
    }

    public void GoToUsersStats(View view){
        Intent UsersStats = new Intent(this, UsersStatsActivity.class);
        UpdateStats("GoToUsersStats");
        startActivity(UsersStats);
    }

    public void GoToSupportersStats(View view){
        Intent SupportersStats = new Intent(this, SupportersStatsActivity.class);
        UpdateStats("GoToSupportersStats");
        startActivity(SupportersStats);
    }

    public void GoToAdminsStats(View view){
        Intent AdminsStats = new Intent(this, AdminStatsActivity.class);
        UpdateStats("GoToAdminsStats");
        startActivity(AdminsStats);
    }

    public void GoToUploadStats(View view){
        Intent UploadStats = new Intent(this, UploadStatsActivity.class);
        UpdateStats("GoToUploadStats");
        startActivity(UploadStats);
    }

    public void GoToPhotosQueue(View view){
        Intent imgQ = new Intent(this, PhotosQueueActivity.class);
        UpdateStats("GoToPhotosQueue");
        startActivity(imgQ);
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
}