package com.example.hands_showprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DeleteUserActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout list;
    private Task<QuerySnapshot> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        db = FirebaseFirestore.getInstance();
        task = db.collection("users").whereLessThanOrEqualTo("accesslevel",1).get();
        list = (LinearLayout)findViewById(R.id.insideScroll);
        querySearch();
    }

    private void querySearch() {
        task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        addALinearView(document);
                    }
                }
            }
        });
    }

    private void addALinearView(QueryDocumentSnapshot doc) {
        //Params var for all views.
        RelativeLayout.LayoutParams params;
        //New line of a pending supporter with settings.
        RelativeLayout newline = new RelativeLayout(this);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        newline.setLayoutParams(params);
        //Making view for email and settings to it.
        TextView useremail = new TextView(this);
        useremail.setId(View.generateViewId());
        useremail.setText(doc.get("email").toString());
        useremail.setTextSize(20);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        newline.addView(useremail,params);
        //Making view for email and settings to it.
        TextView accesslevel = new TextView(this);
        long alevel = doc.getLong("accesslevel");
        String alevelString;
        if(alevel==0){alevelString="Regular(0)";}
        else{alevelString="Supporter(1)";}
        accesslevel.setText("Access level: "+alevelString);
        accesslevel.setTextSize(12);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        newline.addView(accesslevel,params);
        //Making decline button and settings to it.
        ImageButton DeclineButton = new ImageButton(this);
        DeclineButton.setImageResource(R.drawable.ic_delete);
        DeclineButton.setContentDescription(doc.get("uid").toString());
        DeclineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.collection("users").document(v.getContentDescription().toString()).delete();
                ((RelativeLayout)v.getParent()).setVisibility(View.GONE);
            }
        });
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        newline.addView(DeclineButton,params);
        //Adding RelativeLayout to our LinearLayout(son of ScrollView).
        list.addView(newline);
    }
}