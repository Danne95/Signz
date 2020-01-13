package com.example.hands_showprototype;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

public class UsersStatsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Task<DocumentSnapshot> task;
    private PieChart pieChart;
    private int[] stats=new int[3];
    private String[] statsNames={"GoToTranslate","GoToLearn","GoToSignIn"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_stats);
        db=FirebaseFirestore.getInstance();
        task = db.document("userstats/statsforregular").get();//Retrieves document statsforregular from firestore.
        withdrawStats();//lister for task.
    }

    public void withdrawStats() {
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for(int i=0;i<statsNames.length;i++) {
                            try {
                                stats[i] = document.getLong(statsNames[i]).intValue();//If field exists, returns value to stats[i].
                            } catch (Exception e) {
                                stats[i]=0;//Field doesn't exists so stats[i]=0.
                            }
                        }
                        SetPieChart();//After making stats, go to function to set up PieChart.
                    }
                }
            }
        });
    }

    private void SetPieChart() {
        pieChart = (PieChart)findViewById(R.id.PieChart);//find PieChart from layout.
        Description desc = new Description();
        desc.setText("Statistics for users of access level 0(lowest)");//setting text to description.
        //Settings to PieChart
        pieChart.setDescription(desc);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(5f);
        pieChart.setTransparentCircleAlpha(0);
        //Data add to entries
        ArrayList<PieEntry> pieEntries= new ArrayList<>();
        for(int i=0;i<stats.length;i++){
            if(stats[i]!=0) {
                pieEntries.add(new PieEntry(stats[i], statsNames[i]));
            }
        }
        //Make data set
        PieDataSet dataSet = new PieDataSet(pieEntries,"Stats for regular user.");
        dataSet.setSliceSpace(3);
        dataSet.setValueTextSize(12);
        //Adding colors(if needed more colors, add here)
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.MAGENTA);
        colors.add(Color.YELLOW);
        dataSet.setColors(colors);
        //Add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        //Create PieData object
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();//Draw PieChart
    }
}