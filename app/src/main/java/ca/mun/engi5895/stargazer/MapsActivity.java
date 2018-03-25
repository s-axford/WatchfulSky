package ca.mun.engi5895.stargazer;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import org.orekit.errors.OrekitException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static ca.mun.engi5895.stargazer.activity_satellite_sel.getSelectedSat;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int hour;
    private int minute;

    private TextView outSat;
    private TextView velocity_txt;
    private TextView period_txt;
    private TextView height_txt;
    private TextView perigee_txt;
    private TextView apogee_txt;
    private TextView inclination_txt;
    private ArrayList<Entity> selectedSat;

    private  ArrayList<Object> list = new ArrayList<Object>();

    TextView sat_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //selectedSat = activity_satellite_sel.getSelectedSat();

        /*
        try {
            getSatsCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater item = getMenuInflater();
        item.inflate(R.menu.actionbar, menu);
        setTitle("StarGazer");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        //Button but = (Button) findViewById(R.id.actionbar_clock);

        switch (item.getItemId()) {
            case R.id.actionbar_clock:
                //mTextMessage.setText(R.string.title_satellites);
                View clock = findViewById(R.id.timePicker);
                if (clock.getVisibility() == View.INVISIBLE) {
                    clock.setVisibility(View.VISIBLE);
                } else {
                    clock.setVisibility(View.INVISIBLE);
                }
                sat_Name = (TextView) findViewById(R.id.textView5);
                sat_Name.setText(getCurrentTime());

                return true;
            case R.id.actionbar_fav:

                for (int i = 0 ; i < selectedSat.size() ; i++)
                    try {
                        Favorites.addFavorite(selectedSat.get(i).getName(), selectedSat.get(i).getLine1(), selectedSat.get(i).getLine2());
                        return true;
                    } catch (OrekitException e) {
                        e.printStackTrace();
                    }
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentTime(){
        String currentTime = "0";
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {

                if (i1 != 0){
                    timePicker.setVisibility(View.INVISIBLE);
                }

            }
        });
        currentTime="Current Time: "+timePicker.getHour()+":"+timePicker.getMinute();

        //int[] array = {timepicker.getHour(), timepicker.getMinute()};
        return currentTime;
    }


    /*
    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    hour = i;
                    minute = i1;
                    findViewById(R.id.timePicker).setVisibility(View.INVISIBLE);

                    TextView name = (TextView) findViewById(R.id.textView5);

                    name.setText(hour);

                }
            };
    */

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double a = 43.5;
        double b = -54.5;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(47.58, -52.71);

        LatLng pointa = new LatLng(a+5,b*2);
        LatLng pointb = new LatLng(a,b*3);
        LatLng pointc = new LatLng(a-10,b);
        LatLng pointd = new LatLng(a,-b/3);
        LatLng pointe = new LatLng(a,b/9);
        LatLng pointf = new LatLng(a-5,-b*9);

        Polyline line = googleMap.addPolyline(new PolylineOptions()
            .add(sydney)
                .add(pointa)
                .add(pointb)
                .add(pointc)
                .width(5)
            .color(Color.RED)
        );

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.addMarker(new MarkerOptions().position(pointa).title("Marker a"));
        mMap.addMarker(new MarkerOptions().position(pointb).title("Marker b"));
        //mMap.addMarker(new MarkerOptions().position(pointc).title("Marker c"));
        //mMap.addMarker(new MarkerOptions().position(pointd).title("Marker d"));
        //mMap.addMarker(new MarkerOptions().position(pointe).title("Marker e"));
        //mMap.addMarker(new MarkerOptions().position(pointf).title("Marker f"));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(pointb));
    }

    public void getSatsCreate() throws IOException {
        /*
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
        */
    }

}

