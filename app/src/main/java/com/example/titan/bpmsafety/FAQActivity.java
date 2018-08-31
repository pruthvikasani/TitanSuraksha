package com.example.titan.bpmsafety;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    TextView txt_help_gest;
    ExpandableTextView expandableTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        FAQActivity.this.getSupportActionBar().setTitle("FAQ");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FAQActivity.this,NavDrawer.class));
                finish();
            }
        });

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExapandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("What is safety alert?");
        listDataHeader.add("What kinds of incidents can you report?");
        listDataHeader.add("Who can raise a safety alert?");
        listDataHeader.add("What is unsafe act?");
        listDataHeader.add("What is unsafe condition?");
        listDataHeader.add("What is near miss?");
        listDataHeader.add("Can I attach a photo?");


        // Adding child data
        List<String> que1 = new ArrayList<String>();
        que1.add("Safety alerts are brief reports on incidents describing what happened, " +
                "why it happened, and how to ensure it never happens again.");

        List<String> que2 = new ArrayList<String>();
        que2.add("Anything from near misses, unsafe acts conditions or property damage.");

        List<String> que3 = new ArrayList<String>();
        que3.add("Any person working in the company can raise a safety alert.");

        List<String> que4 = new ArrayList<String>();
        que4.add("The behaviour or activity of a person that deviates from normal accepted safe procedure.");

        List<String> que5 = new ArrayList<String>();
        que5.add("Circumstances which could permit the occurrence of an accident.");

        List<String> que6 = new ArrayList<String>();
        que6.add("Near miss incidents are situations that did not result in personal injury " +
                "or property damage but had the potential to do so.");

        List<String> que7 = new ArrayList<String>();
        que7.add("Yes, you can.");

        listDataChild.put(listDataHeader.get(0), que1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), que2);
        listDataChild.put(listDataHeader.get(2), que3);
        listDataChild.put(listDataHeader.get(3), que4);
        listDataChild.put(listDataHeader.get(4), que5);
        listDataChild.put(listDataHeader.get(5), que6);
        listDataChild.put(listDataHeader.get(6), que7);
    }

    @Override
    public void onBackPressed()
    {

        Intent intent=new Intent(FAQActivity.this,NavDrawer.class);
        startActivity(intent);
        finish();

    }
}
