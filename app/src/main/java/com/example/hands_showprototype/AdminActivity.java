package com.example.hands_showprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hands_showprototype.AdminPlus.DeleteUserActivity;
import com.example.hands_showprototype.AdminPlus.SQActivity;
import com.example.hands_showprototype.AdminPlus.DemoteActivity;
import com.example.hands_showprototype.AdminPlus.SupportersStatsActivity;
import com.example.hands_showprototype.AdminPlus.UsersStatsActivity;


public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void GoToSupportQ(View view){
        Intent SupportQ = new Intent(this, SQActivity.class);
        startActivity(SupportQ);
    }

    public void GoToDeleteUser(View view){
        Intent DeleteU = new Intent(this, DeleteUserActivity.class);
        startActivity(DeleteU);
    }

    public void GoToDemote(View view){
        Intent Demote = new Intent(this, DemoteActivity.class);
        startActivity(Demote);
    }

    public void GoToSupportersStats(View view){
        Intent SupportersStats = new Intent(this, SupportersStatsActivity.class);
        startActivity(SupportersStats);
    }

    public void GoToUsersStats(View view){
        Intent UsersStats = new Intent(this, UsersStatsActivity.class);
        startActivity(UsersStats);
    }

}



