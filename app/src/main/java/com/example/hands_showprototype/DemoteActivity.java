package com.example.hands_showprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class DemoteActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private FirebaseFirestore db;
    private LinearLayout list;
    private Task<QuerySnapshot> task;
    private String[] statsNames={"H","E","L","O","W","R","D","SPACE","DELETE"};
    private int[] stats=new int[9];
    private int Uploads;
    private Dialog myDialog;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demote);
        //Popup init
        myDialog = new Dialog(this);
        //PieChart find
        View layout = getLayoutInflater().inflate(R.layout.popup_pie_chart,null);
        //myDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.setContentView(layout);
        pieChart = (PieChart)layout.findViewById(R.id.PieChartPopUp);
        //db init
        db=FirebaseFirestore.getInstance();
        task = db.collection("users").whereEqualTo("accesslevel", 1).get();
        //Text to speech init
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });
        list = (LinearLayout) findViewById(R.id.insideScroll);
        querySearch();
    }
    private void querySearch() {
        task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        makeStatsPerUser(doc.get("uid").toString(),false);
                    }
                }
            }
        });
    }
    private void personalPieChart(View view){
        final String uid=((ImageButton)view).getContentDescription().toString();
        makeStatsPerUser(uid,true);
    }
    private void makeStatsPerUser(final String uid, final boolean drawChart){
       final Task<DocumentSnapshot> doctask=db.collection("users").document(uid).get();
        doctask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Uploads=0;
                        for(int i=0;i<9;i++){
                            try{
                                stats[i]=document.getLong("Uploads."+statsNames[i]).intValue();
                            }catch ( Exception e){
                                stats[i]=0;
                            }
                            Uploads+=stats[i];
                        }
                        if(!drawChart) addALinearView(document.get("uid").toString(),document.get("email").toString());
                        else{
                            //Create chart here.
                            Description desc = new Description();
                            desc.setText("Statistics of uploads for "+document.get("email").toString()+".");//setting text to description.
                            //Settings to PieChart
                            pieChart.setDescription(desc);
                            pieChart.setRotationEnabled(true);
                            pieChart.setHoleRadius(5f);
                            pieChart.setTransparentCircleAlpha(0);
                            pieChart.setBackgroundColor(Color.LTGRAY-500);
                            //Data add to entries
                            ArrayList<PieEntry> pieEntries= new ArrayList<>();
                            for(int i=0;i<stats.length;i++)
                                if(stats[i]!=0)
                                    pieEntries.add(new PieEntry(stats[i],statsNames[i]));
                            //Make data set
                            PieDataSet dataSet = new PieDataSet(pieEntries,"Letters "+document.get("email").toString()+" uploaded.");
                            dataSet.setSliceSpace(3);
                            dataSet.setValueTextSize(12);
                            //Adding colors(if needed more colors, add here)
                            ArrayList<Integer> colors = new ArrayList<>();
                            colors.add(Color.BLUE);
                            colors.add(Color.MAGENTA);
                            colors.add(Color.GRAY);
                            colors.add(Color.GREEN);
                            colors.add(Color.BLACK);
                            colors.add(Color.CYAN);
                            colors.add(Color.LTGRAY);
                            colors.add(Color.YELLOW);
                            colors.add(Color.RED);
                            dataSet.setColors(colors);
                            //Add legend to chart
                            Legend legend = pieChart.getLegend();
                            legend.setForm(Legend.LegendForm.CIRCLE);
                            //Create PieData object
                            PieData pieData = new PieData(dataSet);
                            pieChart.setData(pieData);
                            pieChart.invalidate();//Draw PieChart
                            //Creating PopUp
                            myDialog.show();
                        }
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
        useremail.setId(View.generateViewId());
        useremail.setText(email);
        useremail.setTextSize(20);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        newline.addView(useremail,params);
        //Making decline button and settings to it.
        ImageButton DeclineButton = new ImageButton(this);
        DeclineButton.setId(View.generateViewId());
        DeclineButton.setImageResource(R.drawable.ic_delete);
        DeclineButton.setContentDescription(uid);
        DeclineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.collection("users").document(v.getContentDescription().toString()).update("accesslevel",0);
                ((RelativeLayout)v.getParent()).setVisibility(View.GONE);
            }
        });
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        newline.addView(DeclineButton,params);
        //Making uploads counter and settings to it.
        TextView uploads = new TextView(this);
        uploads.setId(View.generateViewId());
        uploads.setText("Uploads: "+Uploads);
        uploads.setTextSize(12);
        uploads.setContentDescription(uid);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        newline.addView(uploads,params);
        //Making FrameLayout for PieChart button
        FrameLayout frame = new FrameLayout(this);
        if(Uploads>0) {
            //Making piechart button and settings to it.
            ImageButton pieChart = new ImageButton(this);
            pieChart.setImageResource(R.drawable.piechart);
            pieChart.setContentDescription(uid);
            pieChart.setAdjustViewBounds(true);
            pieChart.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            pieChart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    personalPieChart(v);
                }
            });
            FrameLayout.LayoutParams fparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            frame.addView(pieChart, fparams);
            params = new RelativeLayout.LayoutParams(50, 50);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMarginStart(10);
            newline.addView(frame, params);
        }
        //Adding RelativeLayout to our LinearLayout(son of ScrollView).
        list.addView(newline);
    }
}
