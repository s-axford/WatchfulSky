package ca.mun.engi5895.stargazer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AreocentricActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areocentric);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        try {
            expandableListDetail = celestrakData.getSatData(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new MyListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

     /*   expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });*/

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                String satType = expandableListTitle.get(groupPosition);
                String satChosen = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                System.out.println("Type of satellite: " + satType);
                System.out.println("Chosen sat: " + satChosen);
                if (satType == "Space Stations") {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("CHOSEN_SAT_NAME", satChosen);
                    startActivity(intent);
                }

                return false;
            }
        });
    }

}
