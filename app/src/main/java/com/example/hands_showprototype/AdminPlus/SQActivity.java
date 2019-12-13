package com.example.hands_showprototype.AdminPlus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;

import com.example.hands_showprototype.R;

public class SQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sq);

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        TextView tv = new TextView(this);
        tv.setText("Dynamic layouts ftw!");
        ll.addView(tv);

        EditText et = new EditText(this);
        et.setText("weeeeeeeeeee~!");
        ll.addView(et);

        Button b = new Button(this);
        b.setText("I don't do anything, but I was added dynamically. :)");
        ll.addView(b);

        for(int i = 0; i < 20; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText("I'm dynamic!");
            ll.addView(cb);
        }
        this.setContentView(sv);
    }

}

