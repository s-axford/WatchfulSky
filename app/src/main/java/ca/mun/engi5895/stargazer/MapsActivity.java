package ca.mun.engi5895.stargazer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int hour;
    private int minute;

    public static final String FILENAME = "filename";

    private TextView outSat;
    private TextView velocity_txt;
    private TextView period_txt;
    private TextView height_txt;
    private TextView perigee_txt;
    private TextView apogee_txt;
    private TextView inclination_txt;
    private ArrayList<Entity> selectedSat;

    private String timePickerTime;

    Date date;
    Date d1;

    private  ArrayList<Object> list = new ArrayList<Object>();

    private Frame earthFixedFrame;      //Declares a frame of the earth
    private OneAxisEllipsoid earth;     //Creates another elliptical frame of the earth
    private TimeScale utc;              //Sets the timescale used to utc
    SpacecraftState scs;                //Declares variable to hold state of the current entity
    GeodeticPoint pointPlot = null;


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
        //Entity satChosen = selectedSat.get(0);

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

    }

    public void onBackPressed() {
        //activity_satellite_sel.clearSelectedSats();
        selectedSat.clear();
        System.out.println("DONE WITH THE MAP");
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater item = getMenuInflater();
        item.inflate(R.menu.actionbar, menu);
        setTitle("StarGazer");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //Called when icon in the action bar is selected

        switch (item.getItemId()) {
            case R.id.actionbar_clock:      //Clock icon selected

                date = getCreatedTime();
             d1 = getCreatedTime();
                timePickerTime = getCurrentTime(date);

                sat_Name = findViewById(R.id.textView5);
                sat_Name.setText(timePickerTime);

                View s = findViewById(R.id.actionbar_clock);

                item.setIcon(android.R.drawable.ic_menu_save);

                View clock = findViewById(R.id.timePicker);         //Sets a view to the clock
                if (clock.getVisibility() == View.INVISIBLE) {
                    clock.setVisibility(View.VISIBLE);      //If the clock is invisible, make it different
                } else {
                    item.setIcon(R.drawable.clockicon);

                    clock.setVisibility(View.INVISIBLE);
                }

                if (date == d1) {
                    System.out.println("fuck finals");
                }

                updateMap(date);

               //date.


                return true;
            case R.id.actionbar_fav:


               // setTitle("StarGazer (Favorites)");

             //   list = activity_satellite_sel.getSelectedSat();

                selectedSat = activity_satellite_sel.getSelectedSat();
                String fileName = getIntent().getStringExtra(FILENAME);

                Favorites favorite = new Favorites(getApplicationContext());

                item.setIcon(R.drawable.favoritesicon_filled);

                for (int i = 0 ; i < selectedSat.size() ; i++) //for all satellites being displayed
                    try {
                        System.out.println(selectedSat.get(i).getName());
                        System.out.println(selectedSat.get(i).getLine1());
                        System.out.println(selectedSat.get(i).getLine2());
                        System.out.println(fileName);
                        favorite.addFavorite(selectedSat.get(i).getName(), selectedSat.get(i).getLine1(), selectedSat.get(i).getLine2(), fileName); //adds the Entity info to the list of favorite satellites
                        return true;
                    } catch (OrekitException e) {
                        e.printStackTrace();
                    }
            
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentTime(final Date d){
        String currentTime = "0";

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {

                d.setHours(i);
                d.setMinutes(i1);
                System.out.println("Hours: " + i + "Minutes: " + i1);
                //if (i1 != 0){
                 //   timePicker.setVisibility(View.INVISIBLE);
               // }

            }
        });
        currentTime="Current Time: "+timePicker.getHour()+":"+timePicker.getMinute();

        //int[] array = {timepicker.getHour(), timepicker.getMinute()};
        return currentTime;
    }

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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

            mapUpdater();    //Plots the orbit for the given time period

    }

    public void mapUpdater(){
        /*
        class updater extends TimerTask {

            public void run() {
          */
                updateMap(getCreatedTime());
    /*
    }

        }
        Timer timer = new Timer();
        timer.schedule(new updater(), 0, 10000);
    */
    }


    private Date getCreatedTime(){
        //date.getTime();

        return new Date();
    }

    private void updateMap(Date dateTime){

        System.out.println("Entering update map");
        if (dateTime == null){
            dateTime = getCreatedTime();
        }
        AbsoluteDate date;

        //for (int i = 0; i <= selectedSat.size(); i++) {
        int i = 0;
            //for (int j = 0; j < 10; j++) {

            //dateTime.setMonth(dateTime.getMonth() + 1);

            //DETERMINES NECESSARY TIME
                try {
                    utc = TimeScalesFactory.getUTC();       //Creates new timeScale
                } catch (OrekitException e) {
                    System.out.println("ERROR IN UPDATE MAP");
                    e.printStackTrace();
                }

                //Declares Latitude and Longitude
                double longitude;
                double latitude;

                //Creates info for multiple points
                ArrayList<LatLng> plotPoints = new ArrayList<>();       //Holds values to later create polyline
                Calendar calendar = GregorianCalendar.getInstance(); //sets calendar

                double satPeriod = selectedSat.get(i).getPeriod();      //Finds the satellites period of orbiting earth
                int period = (int) Math.round(satPeriod);
                int periodMin = (period / 100);
                System.out.println("Interval: " + periodMin);
                calendar.setTime(dateTime);     //Shifts the time

                System.out.println(calendar.get(Calendar.YEAR));
                calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                date = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), utc); //creates orekit absolute date from calender


                Date endDate = new Date();
                //endDate.getTime();
                //endDate.getDate();

        this.initializeFrames();        //Updates satellites orbit


            for (int k = 0; k < 300; k++) {     //Gets 100 points to create visual orbit of position of the satellite over 1 period of the earth
                //calendar.setTime(dateTime);     //Shifts the time
                //date = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), utc); //creates orekit absolute date from calender
                //date = new AbsoluteDate(date, periodMin);
                System.out.println("DATE: " + date);
                   //this.initializeFrames(date);        //Updates satellites orbit
                this.updatePosition(date, i);
                System.out.println("FRAGMENT (width, height): " + findViewById(R.id.map).getWidth() + "   " + findViewById(R.id.map).getHeight());
                longitude = pointPlot.getLongitude() * 180 / Math.PI;       //Finds longitude
                latitude = pointPlot.getLatitude() * 180 / Math.PI;         //Finds latitude
                LatLng point_a = new LatLng(latitude, longitude);           //Creates map points
                System.out.println(pointPlot);
                MarkerOptions pointA = new MarkerOptions().position(point_a);   //Creates a marker

                if (k == 0) {
                    pointA.title(selectedSat.get(i).getName()); //Creates a marker of the entities current location and names it correspondingly
                    mMap.addMarker(pointA);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(point_a));
                }

                plotPoints.add(point_a);        //Adds point to polyline
                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);
                //dateTime.setSeconds(dateTime.getSeconds() + periodMin);     //Moves to the next location
                System.out.println("PERIOD: " + periodMin);
                date = new AbsoluteDate(date, periodMin);
            }
        Polyline line = mMap.addPolyline(new PolylineOptions().addAll(plotPoints));     //Plots polyline (Orbit)
        line.setWidth(5);       //Sets width of polyline
        line.setColor(Color.RED);   //Sets color of polyline
                /*
                try {
                    wait(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
            //}
        //}

    }

    private void initializeFrames(){//AbsoluteDate date, int i) {

        try {
            Frame f;
            f = CelestialBodyFactory.getEarth().getBodyOrientedFrame();
            //f = FramesFactory.getTEME();

            earthFixedFrame = FramesFactory.getTEME();

            earth = new OneAxisEllipsoid(
                    Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                    Constants.WGS84_EARTH_FLATTENING,
                    f); //earthFixedFrame);
        }catch (OrekitException e) {
            e.printStackTrace();
        }
    }

    private void updatePosition(AbsoluteDate date, int i){

        Frame gcrf;

        scs = selectedSat.get(i).updateState(date);
        System.out.println(scs);
        System.out.println(scs.getPVCoordinates().getPosition());
        System.out.println(scs.getDate());
        System.out.println(earthFixedFrame);

        try {
            gcrf = FramesFactory.getTEME();
            //Vector3D earthPoint = earth.projectToGround(selectedSat.get(i).getVector(date), date, gcrf);
            pointPlot = earth.transform(selectedSat.get(i).getVector(date), earthFixedFrame, date);
        }  catch (OrekitException e){
            System.out.println("FAILED PLOTTING POINT");
            e.printStackTrace();
        }

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

        AbsoluteDate abDate = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), timeZone); //creates orekit absolute date from calender




        return abDate;
    }

}

