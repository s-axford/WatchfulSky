package ca.mun.engi5895.stargazer;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GeocentricActivity extends AppCompatActivity {

    private ListView listView;
    private TextView outSat;

    private  ArrayList<String> list = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocentric);
        setTitle("Geocentric Orbit");
        listView = (ListView) findViewById(R.id.lvid2);
        try {
            getSatsCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadStations(View v) throws IOException {

        FileInputStream stream = openFileInput("stations.txt");
        InputStreamReader sreader = new InputStreamReader(stream);
        BufferedReader breader = new BufferedReader(sreader);

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = breader.readLine()) != null) {
            sb.append(line + System.getProperty("line.separator"));

        }

        String fileString = sb.toString();
        String testString = sb.substring(0, 10);
        Toast.makeText(getApplicationContext(), testString, 1);
        System.out.println(fileString);



    }

    //used with button
    public void getSats(View v) throws IOException {

        FileInputStream stream = openFileInput("stations.txt");
        InputStreamReader sreader = new InputStreamReader(stream);
        BufferedReader breader = new BufferedReader(sreader);

       // StringBuilder sb = new StringBuilder();

        String line;
        int lineNumber = 0;

        while ((line = breader.readLine()) != null) {
            if ((lineNumber%3 == 0) || (lineNumber == 0)) {
            list.add(line);
            System.out.println(line);
            }
            //sb.append(line + System.getProperty("line.separator"));
            lineNumber++;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list );

        listView.setAdapter(arrayAdapter);
    }

    public void getSatsCreate() throws IOException {

        FileInputStream stream = openFileInput("stations.txt");
        InputStreamReader sreader = new InputStreamReader(stream);
        BufferedReader breader = new BufferedReader(sreader);

        // StringBuilder sb = new StringBuilder();

        String line;
        int lineNumber = 0;

        while ((line = breader.readLine()) != null) {
            if ((lineNumber%3 == 0) || (lineNumber == 0)) {
                list.add(line);
                System.out.println(line);
            }
            //sb.append(line + System.getProperty("line.separator"));
            lineNumber++;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list );

        listView.setAdapter(arrayAdapter);

         AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                System.out.println(o.toString());
                listView.setVisibility(listView.GONE);

                outSat = (TextView) findViewById(R.id.textView2);
                outSat.setText(o.toString());
                outSat.setVisibility(View.VISIBLE);


            }
        };

        listView.setOnItemClickListener(mMessageClickedHandler);
    }






}
