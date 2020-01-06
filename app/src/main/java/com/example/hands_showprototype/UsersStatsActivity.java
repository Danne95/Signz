package com.example.hands_showprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UsersStatsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private PieChart pieChart;
    int[] stats={20,50};
    private String[] statsNames={"GoToTranslate","GoToLearn"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_stats);
        db=FirebaseFirestore.getInstance();
        withdrawStats();
        SetPieChart();
    }

    private void SetPieChart() {
        pieChart = (PieChart)findViewById(R.id.PieChart1);
        Description desc = new Description();
        desc.setText("Statistics for users of access level 0(lowest)");
        pieChart.setDescription(desc);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        //pieChart.setDrawEntryLabels(true);
        //Data add to entries
        ArrayList<PieEntry> pieEntries= new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        for(int i=0;i<stats.length;i++){
            pieEntries.add(new PieEntry(stats[i],statsNames[i]));
        }
        //Make data set
        PieDataSet dataSet = new PieDataSet(pieEntries,"Stats for regular user.");
        dataSet.setSliceSpace(2);
        dataSet.setValueTextSize(12);
        //Adding colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.MAGENTA);
        dataSet.setColors(colors);
        //Add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        //Create piedata object
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void withdrawStats() {
        db.collection("userstats").document("statsforregular").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        for(int i=0;i<statsNames.length;i++) {
                            try {
                                stats[i] = document.getLong(statsNames[i]).intValue();
                            } catch (Exception e) {
                                stats[i]=0;
                            }
                        }
                    }
                }
            }
        });
    }
}
