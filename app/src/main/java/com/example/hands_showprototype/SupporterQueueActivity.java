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

public class SupporterQueueActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private LinearLayout list;
    private Task<QuerySnapshot> task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_queue);
        db=FirebaseFirestore.getInstance();
        task = db.collection("users").whereEqualTo("pendingsupport", true).get();
        list = (LinearLayout) findViewById(R.id.insideScroll);
        querySearch();
    }

    private void querySearch() {
        task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        addALinearView(document.get("uid").toString(),document.get("email").toString());
                    }
                }
            }
        });
    }

    private void addALinearView(final String uid,final String email) {
        //Params var for all views.
        RelativeLayout.LayoutParams params;
        //New line of a pending supporter with settings.
        RelativeLayout newline = new RelativeLayout(this);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        newline.setLayoutParams(params);
        //Making view for email and settings to it.
        TextView useremail = new TextView(this);
        useremail.setText(email);
        useremail.setTextSize(20);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        newline.addView(useremail,params);
        //Making approve and decline buttons and settings to them.
        ImageButton ApproveButton = new ImageButton(this), DeclineButton = new ImageButton(this);
        //Decline button.
        DeclineButton.setId(View.generateViewId());
        DeclineButton.setImageResource(R.drawable.ic_delete);
        DeclineButton.setContentDescription(uid);
        DeclineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.collection("users").document(v.getContentDescription().toString()).update("pendingsupport",false);
                db.collection("users").document(v.getContentDescription().toString()).update("accesslevel",0);
                ((LinearLayout)v.getParent()).setVisibility(View.GONE);
            }
        });
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        newline.addView(DeclineButton,params);
        //Approve button.
        ApproveButton.setImageResource(R.drawable.ic_input_add);
        ApproveButton.setContentDescription(uid);
        ApproveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.collection("users").document(v.getContentDescription().toString()).update("pendingsupport",false);
                db.collection("users").document(v.getContentDescription().toString()).update("accesslevel",1);
                ((RelativeLayout)v.getParent()).setVisibility(View.GONE);
            }
        });
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.LEFT_OF, DeclineButton.getId());
        newline.addView(ApproveButton,params);
        //Adding RelativeLayout to our LinearLayout(son of ScrollView).
        list.addView(newline);
    }
}
