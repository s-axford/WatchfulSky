package ca.mun.engi5895.stargazer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.orekit.errors.OrekitException;

import ca.mun.engi5895.stargazer.activity_satellite_sel;

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
    private TextView velocity_txt;
    private TextView period_txt;
    private TextView height_txt;
    private TextView perigee_txt;
    private TextView apogee_txt;
    private TextView inclination_txt;
    private ImageView earthMap;

    private  ArrayList<String> list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocentric);
        setTitle("Geocentric Orbit");
        //listView = (ListView) findViewById(R.id.lvid2);

        try {
            getSatsCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onBackPressed() {
        activity_satellite_sel.clearSatsList();
        this.finish();
    }

    //Creates list of satellites from file and does all the shit to them
    //Should end up splitting up i think, too many responsibilities
    public void getSatsCreate() throws IOException {
        //open file stations.txt
        FileInputStream stream = openFileInput("stations.txt");
        InputStreamReader sreader = new InputStreamReader(stream);
        BufferedReader breader = new BufferedReader(sreader);

        // StringBuilder sb = new StringBuilder();

        String line;
        int lineNumber = 0;

        //While the next line exists, check to see if it is the 0th line or every 3rd line (satellite names)
        //If it is a name, add it to the list ArrayList
        while ((line = breader.readLine()) != null) {
            if ((lineNumber%3 == 0) || (lineNumber == 0)) {
                list.add(line);
            }
            lineNumber++;
        }
        //Needed to convert it to a ListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list );
        //set the adapter
        //listView.setAdapter(arrayAdapter);
        //close streams
        breader.close();
        sreader.close();
        stream.close();

                ArrayList<Object> sats = activity_satellite_sel.getSelectedSats();
                Object o = sats.get(0); //listView.getItemAtPosition(position); //Gets clicked option as java object
                System.out.println(o.toString()); //Output to console as string
                //listView.setVisibility(listView.GONE); //Hide the list cause its no longer needed


                //Updates textview to the picked satellite name. Used for testing.
                outSat = (TextView) findViewById(R.id.textView2);
                outSat.setText(o.toString());
                outSat.setVisibility(View.VISIBLE);


                //Start the re-parsing of the text file for the TLE data for chosen satellite
                FileInputStream stream1 = null;
                try {
                    stream1 = openFileInput("stations.txt"); //openFileInput auto opens from getFilesDir() directory
                                                                    // getFilesDir() is directory of internal app storage
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                InputStreamReader sreader1 = new InputStreamReader(stream1);
                BufferedReader breader1 = new BufferedReader(sreader1);

                String line1;
                String TLE1 = new String();
                String TLE2 = new String();

                //Read each lne of file, if its equal to the one chosen from the list, update TLE strings and break loop
                try {
                    while ((line1 = breader1.readLine()) != null) {
                        if (line1.equals(o.toString())) { //If the current line is the one we chose from the list
                            TLE1 = breader1.readLine();
                            TLE2 = breader1.readLine();
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                double velocity = 0;
                String velocity_string = "error";

                double period = 0;
                String period_string = "error";

                double height = 0;
                String height_string = "error";

                double perigee = 0;
                String perigee_string = "error";

                double apogee = 0;
                String apogee_string = "error";

                double inclination = 0;
                String inclination_string = "error";

                //Creating new entity
                Entity newSat;
                try {
                    newSat = new Entity(TLE1, TLE2);
                    //velocity = newSat.getVelocity();
                    //period = newSat.getPeriod();
                    //height = newSat.getHeight();
                    //perigee = newSat.getPerigee();
                    //apogee = newSat.getApogee();
                    //inclination = newSat.getInclination();

                } catch (OrekitException e) {
                    e.printStackTrace();
                }

                velocity_string = "Velocity: " + Double.toString(velocity);
                period_string = "Period: " + Double.toString(period);
                height_string = "Height: " + Double.toString(height);
                perigee_string = "Perigee: " + Double.toString(perigee);
                apogee_string = "Apogee: " + Double.toString(apogee);
                inclination_string = "Inclination: " + Double.toString(inclination);

                earthMap = (ImageView) findViewById(R.id.imageView);
                earthMap.setVisibility(View.VISIBLE);

                //SET VELOCITY ON UI
                velocity_txt = (TextView) findViewById(R.id.VelocityText);
                velocity_txt.setText(velocity_string);
                velocity_txt.setVisibility(View.VISIBLE);

                //SET PERIOD ON UI
                period_txt = (TextView) findViewById(R.id.PeriodText);
                period_txt.setText(period_string);
                period_txt.setVisibility(View.VISIBLE);

                //SET HEIGHT ON UI
                height_txt = (TextView) findViewById(R.id.HeightText);
                height_txt.setText(height_string);
                height_txt.setVisibility(View.VISIBLE);

                //SET PERIGEE ON UI
                perigee_txt = (TextView) findViewById(R.id.PerigeeText);
                perigee_txt.setText(perigee_string);
                perigee_txt.setVisibility(View.VISIBLE);

                //SET APOGEE ON UI
                apogee_txt = (TextView) findViewById(R.id.ApogeeText);
                apogee_txt.setText(apogee_string);
                apogee_txt.setVisibility(View.VISIBLE);

                //SET INCLINATION ON UI
                inclination_txt = (TextView) findViewById(R.id.InclinationText);
                inclination_txt.setText(inclination_string);
                inclination_txt.setVisibility(View.VISIBLE);

            }

        }

