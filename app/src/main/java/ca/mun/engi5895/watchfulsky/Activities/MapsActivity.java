package ca.mun.engi5895.watchfulsky.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ca.mun.engi5895.watchfulsky.AndroidAestheticAdditions.Favorites;
import ca.mun.engi5895.watchfulsky.OrbitingBodyCalculations.Entity;
import ca.mun.engi5895.watchfulsky.OrekitDataInstallation.celestrakData;
import ca.mun.engi5895.watchfulsky.R;

/**
 * Class representing the maps activity that comes up when a satellite is selected
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final String FILENAME = "filename";

    private ArrayList<Entity> selectedSat;

    String fileName;
    MenuItem favItem;

    Date date;

    private DecimalFormat df = new DecimalFormat("#.00");

    private Frame earthFixedFrame;      //Declares a frame of the earth
    private OneAxisEllipsoid earth;     //Creates another elliptical frame of the earth
    protected GeodeticPoint pointPlot = null;
    protected Marker currentMarker = null;
    protected Polyline line = null;
    protected ArrayList<Marker> markers = new ArrayList<>();

    boolean initial = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        selectedSat = SatelliteSelectActivity.getSelectedSat(); //Pulls a list of selected satellites from the previous activity
        System.out.println(selectedSat.get(0).getName());

        setContentView(R.layout.activity_maps);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(selectedSat.get(0).getName().trim());
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fileName = getIntent().getStringExtra(FILENAME);

        int i = 0;

        System.out.println(selectedSat.get(i).getName());   //Prints Entity Name to the Console

        try {
            System.out.println("Velocity:");
            System.out.println(selectedSat.get(i).getVelocity());       //Prints the velocity of the entity
        } catch (OrekitException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {       //Runs when you exit the activity
        selectedSat.clear();            //Clears the satellite list
        System.out.println("DONE WITH THE MAP");        //Prints to the console that we are done with the Maps activity
        initial = false;                //Boolean variable specifying we are dealing with the first satellite
        Intent intent = new Intent(getApplicationContext(), SatelliteSelectActivity.class);
        startActivity(intent);
        this.finish();                  //Closes the activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     //Creates action bar in the MapsActivity UI

        MenuInflater item = getMenuInflater();     //Specifies the item fit
        item.inflate(R.menu.actionbar, menu);   //Fits the menu to the item fit
        setTitle("WatchfulSky");                  //Sets the action bar title
        favItem = menu.findItem(R.id.actionbar_fav);
        checkifFavorite(); // see if sat is favorite
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //Called when icon in the action bar is selected

        switch (item.getItemId()) {
            case R.id.actionbar_clock:      //Handles the clicking of the clock

                TimeScale utc = null;
                try {
                    utc = TimeScalesFactory.getUTC();
                } catch (OrekitException e) {
                    e.printStackTrace();
                }
                date = getCreatedTime();

                TimePicker timePicker = findViewById(R.id.timePicker);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                    @Override
                    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);
                        System.out.println("Hours: " + i + "Minutes: " + i1);
                    }
                });

                item.setIcon(android.R.drawable.ic_menu_save);
                View clock = findViewById(R.id.timePicker);         //Sets a view to the clock

                if (clock.getVisibility() == View.INVISIBLE) {
                    clock.setVisibility(View.VISIBLE);      //If the clock is invisible, make it different
                } else {
                    item.setIcon(R.drawable.clockicon);
                    clock.setVisibility(View.INVISIBLE);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    cal.set(Calendar.MINUTE, timePicker.getMinute());
                    cal.set(Calendar.SECOND,0);
                    cal.set(Calendar.MILLISECOND,0);

                    Calendar mCalendar = new GregorianCalendar();
                    TimeZone mTimeZone = mCalendar.getTimeZone();
                    long offset = mTimeZone.getRawOffset() + (mTimeZone.inDaylightTime(cal.getTime()) ? mTimeZone.getDSTSavings() : 0);
                    long hourOffset = TimeUnit.MILLISECONDS.toHours(offset);
                    long minuteOffset = TimeUnit.MILLISECONDS.toMinutes(offset) % TimeUnit.HOURS.toMinutes(1);
                    cal.add(Calendar.HOUR, (int) -hourOffset);
                    cal.add(Calendar.MINUTE, (int) -minuteOffset);
                    AbsoluteDate selectedDate = new AbsoluteDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), utc); //creates orekit absolute date from calender

                    GeodeticPoint point = updatePosition(selectedDate, 0);
                    double latitude = point.getLatitude() * 180 / Math.PI;
                    double longitude = point.getLongitude() * 180 / Math.PI;
                    addMarker(latitude, longitude, selectedDate);        //Calls update map to update with the clock selected time
                }
                return true;

            case R.id.actionbar_fav: // Handles the clicking of the favorite button
                selectedSat = SatelliteSelectActivity.getSelectedSat();     //Retrieves list of selected satellites
                String fileName = getIntent().getStringExtra(FILENAME);     // Get file name from previous activity

                if (!Favorites.contains(selectedSat.get(0).getName(), fileName, getApplicationContext())) {
                    item.setIcon(R.drawable.favoritesicon_filled); // Fill in the star
                    for (int i = 0; i < selectedSat.size(); i++) //for all satellites being displayed
                        try { // Add All selected satellites to favorites
                            Favorites.addFavorite(selectedSat.get(i).getName(), selectedSat.get(i).getLine1(), selectedSat.get(i).getLine2(), fileName, getApplicationContext()); //adds the Entity info to the list of favorite satellites
                            return true;
                        } catch (OrekitException e) {
                            e.printStackTrace();
                        }
                } else {
                    item.setIcon(R.drawable.favoritesicon); // un fill in the star
                    for (int i = 0; i < selectedSat.size(); i++) {//for all satellites being displayed
                        try { // Add All selected satellites to favorites
                            boolean removed = Favorites.removeFavorite(selectedSat.get(i).getName(), selectedSat.get(i).getLine1(), selectedSat.get(i).getLine2(), fileName, getApplicationContext()); //adds the Entity info to the list of favorite satellites
                            System.out.print(removed);
                        } catch (OrekitException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }

            case R.id.actionbar_info:
                ConstraintLayout satInfo = findViewById(R.id.satelliteInfo);
                if (satInfo.getVisibility() == View.INVISIBLE) {
                    satInfo.setVisibility(View.VISIBLE);
                } else {
                    satInfo.setVisibility(View.INVISIBLE);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMarker(double latitude, double longitude, AbsoluteDate date) {
        TimeScale utc = null;
        try {
            utc = TimeScalesFactory.getUTC();
        } catch (OrekitException e) {
            e.printStackTrace();
        }
        LatLng point = new LatLng(latitude, longitude);
        MarkerOptions marker = new MarkerOptions().position(point);
        Calendar myCal = new GregorianCalendar();
        myCal.setTime(date.toDate(utc));
        int hour = myCal.get(Calendar.HOUR_OF_DAY);
        int minute = myCal.get(Calendar.MINUTE);
        int second = myCal.get(Calendar.SECOND);
        String markerTime = String.format("%02d:%02d:%02d", hour, minute, second);
        marker.title(selectedSat.get(0).getName().trim() + " at " + markerTime + " UTC");
        Marker mark = mMap.addMarker(marker);
        float color = 130;
        mark.setIcon(BitmapDescriptorFactory.defaultMarker(140));
        markers.add(mark);
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
    public void onMapReady(final GoogleMap googleMap) {     //Called when the map is first created and ready to be modified. Android method
        mMap = googleMap;
        //mMap.setMapType(mMap.MAP_TYPE_HYBRID);

        mapUpdater(getCreatedTime());    //Plots the orbit for the given time period

    }

    private int incr = 0;


    public void mapUpdater(Date date) {      //Updates the map with the entities new position every 10 seconds

        if (date == null) {       //Sets the corresponding date to be used in the updateMap() method
            dateTime = getCreatedTime();    //Date not specified - Create new date with current time
        } else {
            dateTime = date;        //Date Specified by user
        }

        new Thread() {
            public void run() {
                System.out.println("IN NEW THREAD");
                while (incr++ < 1000) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    updateMap();
                                } catch (OrekitException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread.sleep(10000);        //Waits 10 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }


    private Date getCreatedTime() {  //Creates a new date with the current time and returns it
        return new Date();
    }

    private Date dateTime = null;   //Declares date to be used throughout class

    private void updateMap() throws OrekitException {       //Runnable to allow map to be updated every 10 seconds

        System.out.println("Entering update map");
        if (dateTime == null) {              //If no date specified by the calling class
            dateTime = getCreatedTime();    //Use the current date and time
        }
        AbsoluteDate date;

        for (int i = 0; i < selectedSat.size(); i++) {      //For every satellite selected by the user

            double satNum = selectedSat.get(i).getSatNum();                 //Gets the selected entities identifying number - Unique
            TextView satNumberText = findViewById(R.id.satelliteNumber);    //Finds the corresponding UI textbox
            String satelliteNumberString = "Satellite Number: " + String.valueOf(Math.round(satNum));
            satNumberText.setText(satelliteNumberString);   //Outputs the Sat Number to the UI using the selected textbox

            //Sets the timescale used to utc
            TimeScale utc = TimeScalesFactory.getUTC();

            //Declares Latitude and Longitude

            //Creates info for multiple points
            ArrayList<LatLng> plotPoints = new ArrayList<>();       //Holds values to later create polyline
            Calendar calendar = GregorianCalendar.getInstance(); //sets calendar

            double satPeriod = selectedSat.get(i).getPeriod();      //Finds the satellites period of orbiting earth
            int period = (int) Math.round(satPeriod);               //Rounded to the nearest second - Required for methods that require int
            int periodMin = (period / 100);                         //Finds the interval to allow for 100 location points for every orbit of the earth
            System.out.println("Interval: " + periodMin);           //Prints the calculated time step
            calendar.setTime(dateTime);     //Shifts the time

            System.out.println(calendar.get(Calendar.YEAR));                    //Prints the current year - Used for debugging
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));       //Converts any time zone to UTC
            date = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), utc); //creates orekit absolute date from calender

            this.initializeFrames();        //Initializes orbit information

            pointPlot = updatePosition(date, 0);

            String velocityString = df.format(selectedSat.get(i).getVelocity() * 3.6);  //Text to be set as Velocity in UI

            velocityString = "Velocity: " + velocityString + "km/h";

            TextView satVelocity = findViewById(R.id.velocity);         //Velocity textbox (UI)
            satVelocity.setText(velocityString);                //Sets textbox to Velocity String


            //Finds longitude
            double longitude = pointPlot.getLongitude() * 180 / Math.PI;
            String longitudeString = "Longitude: " + df.format(longitude);                                         //Text to be set as position in UI
            TextView satLongitude = findViewById(R.id.satLongitude);    //Longitude position textbox (UI)
            satLongitude.setText(longitudeString);              //Sets textbox to Longitude String


            //Finds latitude
            double latitude = pointPlot.getLatitude() * 180 / Math.PI;
            String latitudeString = "Latitude: " + df.format(latitude); //Text to be set as position in UI
            TextView satLatitude = findViewById(R.id.satLatitude);      //Latitude Position textbox (UI)
            satLatitude.setText(latitudeString);                //Sets textbox to Latitude String

            //Finds Altitude
            double altitude = pointPlot.getAltitude() / 1000;              //Determines the entities altitude at the current date and time
            TextView satAltitude = findViewById(R.id.satAltitude);  //Specifies the textbox to output the Altitude information to the UI
            String altitudeString = "Altitude: " + df.format(altitude) + "km";
            satAltitude.setText(altitudeString);     //Prints the Altitude information to the UI using the previously specified textbox

            System.out.println("Latitude: " + latitude);        //Prints Latitude to the console
            System.out.println("Longitude: " + longitude);      //Prints Longitude to the console
            System.out.println("PERIOD: " + periodMin);         //Prints the Period to the console

            for (int k = 0; k < 300; k++) {     //Plots 300 points of the entities location - The equivalent of 3 orbits of the earth

                System.out.println("DATE: " + date);
                pointPlot = updatePosition(date, i);
                System.out.println("FRAGMENT (width, height): " + findViewById(R.id.map).getWidth() + "   " + findViewById(R.id.map).getHeight());      //Prints the size of the map to the console
                longitude = pointPlot.getLongitude() * 180 / Math.PI;       //Finds longitude and converts to degrees
                latitude = pointPlot.getLatitude() * 180 / Math.PI;         //Finds latitude and converts to degrees

                //Creates map objects
                LatLng point_a = new LatLng(latitude, longitude);           //Creates map point
                MarkerOptions pointA = new MarkerOptions().position(point_a);   //Creates a marker

                if (k == 0) {       //If the point is the current position of the satellite
                    if(currentMarker != null) {
                        currentMarker.remove();
                    }
                    if(line != null) {
                        line.remove();
                    }
//                    mMap.clear();       //Clears all markers and polylines on the map
//                    for (Marker marker : markers) {
//                        mMap.addMarker(marker).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//                    }
                    pointA.title(selectedSat.get(i).getName().trim() + " Current Location"); //Creates a marker of the entities current location and names it correspondingly
                    currentMarker = mMap.addMarker(pointA);     //Adds a new marker to the Google map
                    if (initial) {              //Only move the camera when the map first loads
                        initial = false;        //Prevent the camera from being moved when the map is updating every 10 seconds
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(point_a));        //Moves the camera to the entities current position when the map first loads
                    }
                }
                plotPoints.add(point_a);                            //Adds point to polyline
                date = new AbsoluteDate(date, periodMin);           //Moves the date ahead to the next point
            }

            //Creates Polyline to be plotted on the map (Plots Expected Orbit)
            line = mMap.addPolyline(new PolylineOptions().addAll(plotPoints));     //Plots polyline (Orbit)
            line.setWidth(5);                                                               //Sets width of polyline
            line.setColor(Color.RED);                                                       //Sets color of polyline
        }
    }

        private void initializeFrames() {        //AbsoluteDate date, int i) {

        try {
            Frame f;
            f = CelestialBodyFactory.getEarth().getBodyOrientedFrame();     //Creates a new earth reference frame

            earthFixedFrame = FramesFactory.getTEME();      //Creates TLE Data specific frame

            earth = new OneAxisEllipsoid(                   //Creates representation of earth as a OneAxisEllipsoid using previously defined frame
                    Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                    Constants.WGS84_EARTH_FLATTENING,
                    f);
        } catch (OrekitException e) {
            System.out.println("ERROR IN FRAME CREATION");
            e.printStackTrace();
        }
    }

    /**
     * @param date AbsoluteDate object representing what time the data should be extrapolated to
     * @param i    What satellite in the selectedSat list is being updated
     */
    private GeodeticPoint updatePosition(AbsoluteDate date, int i) {
        GeodeticPoint point = null;
        //Declares variable to hold state of the current entity
        SpacecraftState scs = selectedSat.get(i).updateState(date);
        System.out.println(scs);
        System.out.println(scs.getPVCoordinates().getPosition());       //Uses SpaceCraftState to print the coordinates to the console
        System.out.println(scs.getDate());
        System.out.println(earthFixedFrame);

        try {
            point = earth.transform(selectedSat.get(i).getVector(date), earthFixedFrame, date);     //Finds the Cartesian position of the satellite, then converts to a Geodetic Point
        } catch (OrekitException e) {
            System.out.println("FAILED CARTESIAN POINT CONVERSION");
            e.printStackTrace();
        }
        return point;
    }

    /**
     * Checks to see if the satellite is a favorite. If it is, fill in the star
     */
    private void checkifFavorite() {


        // Handle clicking on a satellite from the favorites menu
        if (fileName.contains("favorites_")) {
            fileName = fileName.replace("favorites_", "");
        }

        // File representing the favorites file of the satellite selected
        File test1 = new File(this.getFilesDir(), "favorites_" + fileName);


        if (test1.exists()) {
            System.out.println("file exists");
            try {
                ArrayList<String> favs = celestrakData.getNames("favorites_" + fileName, this);   // if the file exists, return all the names stored in the favorites

                // If satellite is in list of favorite satellites
                if (favs.contains(selectedSat.get(0).getName())) {
                    System.out.println("already is a favorite");
                    favItem.setIcon(R.drawable.favoritesicon_filled); // Fill in the star
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("file doesnt exist");
        }


    }



}

