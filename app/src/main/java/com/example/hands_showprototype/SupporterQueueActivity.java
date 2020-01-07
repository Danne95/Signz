package com.example.hands_showprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SupporterQueueActivity extends AppCompatActivity {
    private LinearLayout list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supporter_queue);
        addALinearView();
    }

    private void addALinearView() {
        list = (LinearLayout)findViewById(R.id.insideScroll);
        for(int i=0;i<30;i++) {
            LinearLayout testline = new LinearLayout(this);
            testline.setOrientation(LinearLayout.HORIZONTAL);
            testline.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ImageButton ApproveButton = new ImageButton(this),DeclineButton = new ImageButton(this);
            DeclineButton.setImageResource(R.drawable.ic_delete);
            ApproveButton.setImageResource(R.drawable.ic_input_add);
            testline.addView(DeclineButton);
            testline.addView(ApproveButton);
            list.addView(testline);
        }
    }
}
