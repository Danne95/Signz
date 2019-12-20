package com.example.hands_showprototype.AdminPlus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hands_showprototype.MainActivity;
import com.example.hands_showprototype.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.MapValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Query MyQuery;
    private QuerySnapshot MyQuerySnapshot;
    public static final String TAG = "TAG";

    ListView listViewSupporters;
    List<SupportersClass> supportersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sq);
        db = FirebaseFirestore.getInstance();

        /*ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        //sv.addView(ll);
        TextView tv = new TextView(this);
        tv.setText("Supporter Queue!");
        ll.addView(tv);*/
        listViewSupporters = (ListView) findViewById(R.id.listViewSupporters);
        supportersList = new ArrayList<>();
        /*MyQuery = db.collection("users")
                .whereEqualTo("pendingsupport", true);
        Query[] QueryArr = new Query[20];
        QueryArr=MyQueryuery.get();
        //MyQuery.get();*/
    }

    @Override
    protected void onStart(){
        super.onStart();

        db.collection("users").whereEqualTo("pendingsupport",true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            supportersList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String,Object> DocMap = document.getData();
                                SupportersClass Supporters =new SupportersClass(DocMap.get("uid").toString(),DocMap.get("name").toString());

                                supportersList.add(Supporters);
                            }
                            SQnodeClass adapter = new SQnodeClass(SQActivity.this,supportersList);
                            listViewSupporters.setAdapter(adapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        /*for(int i = 0; i <4; i++) {
                TextView nm = new TextView(this);
               nm.setText("@@@ JEFF @@@");
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
        }*/
        //this.setContentView(sv);
    }
}


