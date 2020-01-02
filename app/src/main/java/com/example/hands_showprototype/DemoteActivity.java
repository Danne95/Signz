package com.example.hands_showprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class DemoteActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demote);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }
    public void DemoteUser(View view){
        String Email = ((EditText) findViewById(R.id.EmailInput)).getText().toString();

        // Check that fields are not empty
        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(getApplicationContext(), "All fields must be filled.", Toast.LENGTH_LONG).show();
        }else {
            if (db != null) {
                //FirebaseFirestore rootRef = FirebaseFirestore.getInstance(); == db
                final CollectionReference usersRef = db.collection("users");
                usersRef.whereEqualTo("email", Email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<Object, Number> map = new HashMap<>();
                                map.put("accesslevel", 0);
                                usersRef.document(document.getId()).set(map, SetOptions.merge());
                            }
                        }
                        //                    }else Toast.makeText(getApplicationContext(), "All fields must be filled.", Toast.LENGTH_LONG).show();
                    }

                });
            }
        }
    }

}
