package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.os.Parcelable;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        ArrayList<Planner> plannerList = XMLOperator.parseXml(ViewActivity.this);
        printPlanner(plannerList);
    }

    private void printPlanner(ArrayList<Planner> plannerList) {
        recyclerView = findViewById(R.id.recyclerView);

        PlannerAdapter plannerAdapter = new PlannerAdapter(this, plannerList);
        recyclerView.setItemViewCacheSize(0);
        recyclerView.setAdapter(plannerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}