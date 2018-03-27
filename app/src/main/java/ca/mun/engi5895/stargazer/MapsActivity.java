package ca.mun.engi5895.stargazer;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import org.apache.commons.math3.analysis.function.Constant;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.Ellipsoid;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.forces.gravity.potential.GravityFieldFactory;
import org.orekit.forces.gravity.potential.NormalizedSphericalHarmonicsProvider;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.models.earth.Geoid;
import org.orekit.models.earth.ReferenceEllipsoid;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.TimeStampedPVCoordinates;

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
import java.util.Timer;
import java.util.TimerTask;

import static ca.mun.engi5895.stargazer.activity_satellite_sel.getSelectedSat;
import static org.apache.commons.math3.analysis.FunctionUtils.add;

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
    TextView sat_Perigee_Title;

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
      
        int  i = 0;
        //for (int i = 0 ; i < selectedSat.size() ; i++) //for all satellites being displayed

                System.out.println(selectedSat.get(i).getName());
                sat_Name = (TextView) findViewById(R.id.textView5);
                sat_Name.setText(selectedSat.get(i).getName());

        try {
            System.out.println("Velocity:");
            System.out.println(selectedSat.get(i).getVelocity());
        } catch (OrekitException e) {
            e.printStackTrace();
        }
        sat_Perigee_Title = (TextView) findViewById(R.id.textView6);
                sat_Perigee_Title.setText("Satellite Perigee");

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

                //list = activity_satellite_sel.getSelectedSat();

                selectedSat = activity_satellite_sel.getSelectedSat();
               
                  for (int i = 0 ; i < selectedSat.size() ; i++) //for all satellites being displayed
                    try {
                        favorite.addFavorite(selectedSat.get(i).getName(), selectedSat.get(i).getLine1(), selectedSat.get(i).getLine2()); //adds the Entity info to the list of favorite satellites
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
    private Frame sunFrame;
    private Frame earthFixedFrame;
    private OneAxisEllipsoid earth;
    private TimeScale utc;
    SpacecraftState scs;
    GeodeticPoint pointPlot = null;

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //updateMap();

            }
        }, 0, 30000);//put here time 1000 milliseconds=1 second

    }

        private void updateMap(){
            //mMap;

            Date dateTime = new Date(); //creates date
            dateTime.getTime();
            dateTime.setMonth(dateTime.getMonth() + 1);
            Calendar calendar = GregorianCalendar.getInstance(); //sets calendar
            calendar.setTime(dateTime);
            try {
                utc = TimeScalesFactory.getUTC();
            } catch (OrekitException e){
                e.printStackTrace();
            }

        AbsoluteDate date = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), utc); //creates orekit absolute date from calender

        //date = date.shiftedBy(3600);
        System.out.println(date.getDate());
        this.initializeFrames(date);

        double satPeriod = selectedSat.get(0).getPeriod();
        System.out.println("Sat Period: " + satPeriod);
        int period = (int)Math.round(satPeriod);
        //dateTime.setMinutes(dateTime.getMinutes() - period);

        calendar.setTime(dateTime);
        date = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), utc); //creates orekit absolute date from calender

        if (pointPlot == null) {
                System.out.println("VALUE IS NULL!!!");
                return;
            }

            this.initializeFrames(date);

            double longitude = pointPlot.getLongitude() * 180 / Math.PI;
            double latitude = pointPlot.getLatitude() * 180 / Math.PI;

            //System.out.println("TESTING: " + height);
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);
            LatLng sydney = new LatLng(latitude, longitude);
            System.out.println(sydney);
            MarkerOptions options = new MarkerOptions().position(sydney).title("CURRENT POSITION");
            System.out.println(options.getPosition());
            System.out.println(mMap);
            mMap.addMarker(options);

            ArrayList<LatLng> plotPoints = new ArrayList<>();
            plotPoints.add(sydney);
            int periodMin = (period / 100);
            System.out.println("Interval: " + periodMin);

        for (int i = 0; i < 100; i++) {
            dateTime.setSeconds(dateTime.getSeconds() + periodMin);
            calendar.setTime(dateTime);
            date = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), utc); //creates orekit absolute date from calender
            this.initializeFrames(date);
            longitude = pointPlot.getLongitude() * 180 / Math.PI;
            latitude = pointPlot.getLatitude() * 180 / Math.PI;
            LatLng point_a = new LatLng(latitude, longitude);
            System.out.println(point_a);
            MarkerOptions pointA = new MarkerOptions().position(point_a);
            //mMap.addMarker(pointA);
            plotPoints.add(point_a);

        }
        Polyline line = mMap.addPolyline(new PolylineOptions().addAll(plotPoints));
        line.setWidth(5);
        line.setColor(Color.RED);



    }

            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    private void initializeFrames(AbsoluteDate date) {

        try {
            /*
            if (sunFrame == null) {
                sunFrame = CelestialBodyFactory.getSun().getInertiallyOrientedFrame();
            }
            */
            //if (earthFixedFrame == null) {
                earthFixedFrame = CelestialBodyFactory.getEarth().getInertiallyOrientedFrame();
            //}
            //if (earth == null && earthFixedFrame == null) {
                earth = new OneAxisEllipsoid(
                        Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                        Constants.WGS84_EARTH_FLATTENING,
                        earthFixedFrame);
            //}

            //if (utc == null) {
                //utc = TimeScalesFactory.getUTC();
            //}

        } catch (OrekitException e) {
            e.printStackTrace();
        }

/*
        Date dateTime = new Date(); //creates date
        Calendar calendar = GregorianCalendar.getInstance(); //sets calendar
        dateTime.setMonth(dateTime.getMonth() + 1);
        calendar.setTime(dateTime);

        AbsoluteDate date = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), utc); //creates orekit absolute date from calender
  */
        scs = selectedSat.get(0).getOrbit(date);
        System.out.println(scs);
        System.out.println(scs.getPVCoordinates().getPosition());
        System.out.println(scs.getDate());
        System.out.println(earthFixedFrame);

        try {
            pointPlot = earth.transform(scs.getPVCoordinates(earthFixedFrame).getPosition(), earthFixedFrame, scs.getDate());
        }  catch (OrekitException e){
            System.out.println("FAILED PLOTTING POINT");
            e.printStackTrace();
        }



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



        return abDate;
    }

    private void getSatLocation() throws IOException {

    }

}

