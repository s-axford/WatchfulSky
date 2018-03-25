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
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

        selectedSat = activity_satellite_sel.getSelectedSat();
        System.out.println(selectedSat.get(0).getName());
        sat_Name = (TextView) findViewById(R.id.textView5);
        double inclination = selectedSat.get(0).getInclination();
        sat_Name.setText(String.valueOf(inclination)); //selectedSat.get(0).getName());
        //boolean is = selectedSat.isEmpty();

        /*
        try {
            sat_Name.setText(selectedSat.getLine1());
        } catch (OrekitException e) {
            e.printStackTrace();
        }
        */

        try {
            getSatLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

                Favorites favorite = new Favorites(MapsActivity.this);
                selectedSat = activity_satellite_sel.getSelectedSat();
                /*
                  for (int i = 0 ; i < selectedSat.size() ; i++)
                    try {
                        favorite.addFavorite(selectedSat.get(i).getName(), selectedSat.get(i).getLine1(), selectedSat.get(i).getLine2());
                        return true;
                    } catch (OrekitException e) {
                        e.printStackTrace();
                    }
                */
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

        AbsoluteDate date = getTime();
        ArrayList<LatLng> points = new ArrayList<>();

        for (int i = 0; i < selectedSat.size(); i++) {
            double a = selectedSat.get(i).getX(date);
            double b = selectedSat.get(i).getY(date);
            points.add(new LatLng(a, b));
            System.out.println(a);
            System.out.println(b);
        }

        for (int j = 0; j < points.size(); j++) {

        }

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(a, b);

        //LatLng pointa = new LatLng(a+5,b*2);
        //LatLng pointb = new LatLng(a,b*3);
        //LatLng pointc = new LatLng(a-10,b);
        //LatLng pointd = new LatLng(a,-b/3);
        //LatLng pointe = new LatLng(a,b/9);
        //LatLng pointf = new LatLng(a-5,-b*9);

        /*
        Polyline line = googleMap.addPolyline(new PolylineOptions()
            .add(sydney)
                //.add(pointa)
                //.add(pointb)
                //.add(pointc)
                .width(5)
            .color(Color.RED)
        );
        */

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.addMarker(new MarkerOptions().position(pointa).title("Marker a"));
        //mMap.addMarker(new MarkerOptions().position(pointb).title("Marker b"));
        //mMap.addMarker(new MarkerOptions().position(pointc).title("Marker c"));
        //mMap.addMarker(new MarkerOptions().position(pointd).title("Marker d"));
        //mMap.addMarker(new MarkerOptions().position(pointe).title("Marker e"));
        //mMap.addMarker(new MarkerOptions().position(pointf).title("Marker f"));


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private AbsoluteDate getTime(){

        TimeScale timeZone = null;

        try {
            timeZone = TimeScalesFactory.getUTC(); // get UTC time scale
        } catch (OrekitException e) {
            e.printStackTrace();
        }

        Date date = new Date(); //creates date
        Calendar calendar = GregorianCalendar.getInstance(); //sets calendar
        calendar.setTime(date); //updates date and time
        AbsoluteDate abDate = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), timeZone); //creates orekit absolute date from calender

        System.out.println();

        return abDate;
    }

    private void getSatLocation() throws IOException {



    }

}

