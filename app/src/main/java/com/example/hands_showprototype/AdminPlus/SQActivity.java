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
import android.graphics.Color;

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
        tv.setText("Supporter Queue!");
        ll.addView(tv);


        for(int i = 0; i < 20; i++) {
            TextView nm = new TextView(this);
            nm.setText("@@@Jeff@@@");
            ll.addView(nm);

            Button a = new Button(this);
            a.setText("Accept");
            a.setBackgroundColor(Color.parseColor("#00ff00"));
            ll.addView(a);

            Button d = new Button (this);
            d.setText("Deny");
            d.setBackgroundColor(Color.parseColor("#ff4044"));
            ll.addView(d);


            //CheckBox cb = new CheckBox(this);
            //cb.setText("I'm dynamic!");
            //ll.addView(cb);


        }
        this.setContentView(sv);
    }

}

