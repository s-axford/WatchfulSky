package ca.mun.engi5895.stargazer.Activities;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.orekit.errors.OrekitException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ca.mun.engi5895.stargazer.AndroidAestheticAdditions.MyListAdapter;
import ca.mun.engi5895.stargazer.OrbitingBodyCalculations.Entity;
import ca.mun.engi5895.stargazer.OrekitDataInstallation.celestrakData;
import ca.mun.engi5895.stargazer.R;


public class SatelliteSelectActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView listView;

    private ProgressBar progressBar;
    //private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapterList;
    private ArrayAdapter<String> favoriteList;

    public static ArrayList<Entity> selectedSats = new ArrayList<>();
    private static ArrayList<Object> satList = new ArrayList<>();
    private static ArrayList<Object> favoriteSats = new ArrayList<>();

    private static String TLE1;
    private static String TLE2;
    private static Entity currentEntity = null;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    ExpandableListView expandableListView_fav;
    ExpandableListAdapter expandableListAdapter_fav;
    List<String> expandableListTitle_fav;
    HashMap<String, List<String>> expandableListDetail_fav;

   // Favorites fav = new Favorites(SatelliteSelectActivity.this);
  //  favoriteList = fav.getFavorites();\

  //  Context context = SatelliteSelectActivity.this;
  //  Favorites fav = new Favorites(context);
   // favoriteList = fav.getFavorites();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    //mTextMessage.setText(R.string.title_satellites);
                  /*  try {
                        getSatsCreate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    expandableListView.setVisibility(View.VISIBLE);
                    expandableListView_fav.setVisibility(View.INVISIBLE);


                    //  listView.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    System.out.println("Clicked Favourites");

                   //Context context = SatelliteSelectActivity.this;
                 //  Favorites fav = new Favorites(context);
                //    favoriteList = fav.getFavorites();

                    expandableListView.setVisibility(View.INVISIBLE);

                    getFavData();

                    //   ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                   //         context,
                    //        android.R.layout.simple_list_item_1,
             //               list);
                    //se the adapter
                //    listView.setAdapter(favoriteList);
                //    listView.setVisibility(View.VISIBLE);
                    return true;

                //case R.id.navigation_notifications:



                    //return true;

            }
            return false;
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satellite_sel);

      //  mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
       navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //listView = findViewById(R.id.lvid2);

        //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // Find the expandable list views, one for all satellites and one for favorite satellites
        expandableListView = findViewById(R.id.expandableListView);
        expandableListView_fav = findViewById(R.id.expandableListView_fav);

        // Get the satellite data from the internal files
        // Represents the child titles of the expandable list view
        try {
            expandableListDetail = celestrakData.getSatData(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Get the satellite categories for the expandable list view
        // Stored as the keys of the hash map
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        // Set the custom adapter for the expandable list view
        expandableListAdapter = new MyListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        //expandableListView.setVisibility(View.VISIBLE);


        // Run the getSatsCreate method and make the expandable list view appear
        try {
            getSatsCreate();
            expandableListView.setVisibility(View.VISIBLE);

            // listView.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }

      //  listView = (ListView) findViewById(R.id.lvid2);
    }

    // Method that populates the expandable list view with the favorite satellites
    // Also handles clicking a favorite satellite and doing calculations with its data
    // Ran when the "favorites" button is clicked
    public void getFavData() {

        // Get the favorites data for the expandable list view
        try {
            expandableListDetail_fav = celestrakData.getSatDataFavorites(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Get the list titles for favorite satellites
        expandableListTitle_fav = new ArrayList<>(expandableListDetail_fav.keySet());
        // Get custom adapter, passing in the hashmap keys and data
        expandableListAdapter_fav = new MyListAdapter(this, expandableListTitle_fav, expandableListDetail_fav);
        // Set the adapter and make the list visible
        expandableListView_fav.setAdapter(expandableListAdapter_fav);
        expandableListView_fav.setVisibility(View.VISIBLE);

        // Set a click listener for the expandable list view
        expandableListView_fav.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //Type of satellite is set to the parent chosen
                String satType = expandableListTitle_fav.get(groupPosition);
                // Satellite chosen is set to whatever name was chosen
                String satChosen = expandableListDetail_fav.get(expandableListTitle_fav.get(groupPosition)).get(childPosition);
                // Add the chosen satellite to the satList
                satList.add(satChosen);

                String fileName = null;

                // Need to know the filename corresponding to the chosen satellite
                if (satType.equals("Space Stations")) {
                    fileName = "favorites_stations.txt";
                } else if (satType.equals("Newly Launched Satellites"))
                    fileName = "favorites_tle-new.txt";
                else if (satType.equals("GPS Satellites")) {
                    fileName = "favorites_gps-ops.txt";
                } else if (satType.equals("Communications Satellites"))
                    fileName = "favorites_geo.txt";
                else if (satType.equals("Intelsat Satellites"))
                    fileName = "favorites_intelsat.txt";
                else if (satType.equals("Science Satellites"))
                    fileName = "favorites_science.txt";

                // Navigate to the MapsActivity screen
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                // Send the chosen satellite name and filename into the MapsActivity
                intent.putExtra("CHOSEN_SAT_NAME", satChosen);
                intent.putExtra("filename", fileName);
                startActivity(intent);


                //Start the re-parsing of the text file for the TLE data for chosen satellite
                FileInputStream stream1 = null;
                try {
                    stream1 = openFileInput(fileName); //openFileInput auto opens from getFilesDir() directory
                    // getFilesDir() is directory of internal app storage
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                InputStreamReader sreader1 = new InputStreamReader(Objects.requireNonNull(stream1));
                BufferedReader breader1 = new BufferedReader(sreader1);

                String line1;
                String TLE1 = "";
                String TLE2 = "";

                //Read each lne of file, if its equal to the one chosen from the list, update TLE strings and break loop
                try {
                    while ((line1 = breader1.readLine()) != null) {
                        if (line1.equals(satChosen)) { //If the current line is the name of the one we chose from the list
                            TLE1 = breader1.readLine(); // Next line is first TLE string
                            TLE2 = breader1.readLine(); // Next line is second TLE string

                            break; //Break loop, data successfully acquired
                        }
                    }
                  //  breader1.close();  sreader1.close();stream1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    currentEntity = new Entity(satChosen, TLE1, TLE2); //Create a new sat entity using the satellite name and two TLE strings
                    selectedSats.add(currentEntity); // Add entity to selectedSats list
                    SatelliteSelectActivity.getSelectedSat();
                } catch (OrekitException e) {
                    e.printStackTrace();
                }

                return false;

            }

        });


    }

    public void getSatsCreate() throws IOException {

        //open file stations.txt
      /*  FileInputStream stream = openFileInput("stations.txt");
        InputStreamReader sreader = new InputStreamReader(stream);
        BufferedReader breader = new BufferedReader(sreader);

        // StringBuilder sb = new StringBuilder();

        String line;
        int lineNumber = 0;

        //While the next line exists, check to see if it is the 0th line or every 3rd line (satellite names)
        //If it is a name, add it to the list ArrayList
        while ((line = breader.readLine()) != null) {
            if ((lineNumber % 3 == 0) || (lineNumber == 0)) {
                list.add(line);
            }
            lineNumber++;
        }
        //Needed to convert it to a ListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list);
        //se the adapter
        listView.setAdapter(arrayAdapter);
        //close streams
        breader.close();
        sreader.close();
        stream.close();*/

        //Class that handles clicking on a list item
       // AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
           // public void onItemClick(AdapterView parent, View v, int position, long id) {

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //   Object o = listView.getItemAtPosition(position); //Gets clicked option as java object
                //    satList.add(o);
                //    System.out.println(o.toString()); //Output to console as string
                //    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                // //   intent.putExtra("CHOSEN_SAT_NAME", o.toString());
                //   startActivity(intent);

                //listView.setVisibility(listView.GONE); //Hide the list cause its no longer needed

                //Updates textview to the picked satellite name. Used for testing.
                //outSat = (TextView) findViewById(R.id.textView2);
                //outSat.setText(o.toString());
                //outSat.setVisibility(View.VISIBLE);


                //Handles the click of an item from the list
                String satType = expandableListTitle.get(groupPosition);
                String satChosen = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                satList.add(satChosen);

                String fileName = null;


                if (satType.equals("Space Stations")) {
                    fileName = "stations.txt";
                } else if (satType.equals("Newly Launched Satellites"))
                    fileName = "tle-new.txt";
                else if (satType.equals("GPS Satellites"))
                    fileName = "gps-ops.txt";
                else if (satType.equals("Communications Satellites"))
                    fileName = "geo.txt";
                else if (satType.equals("Intelsat Satellites"))
                    fileName = "intelsat.txt";
                else if (satType.equals("Science Satellites"))
                    fileName = "science.txt";

                System.out.println("Filename: "+ fileName);

                /*Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("CHOSEN_SAT_NAME", satChosen);
                intent.putExtra(MapsActivity.FILENAME, fileName);
                startActivity(intent);*/


                //Start the re-parsing of the text file for the TLE data for chosen satellite

                FileInputStream stream1 = null;
                try {
                    stream1 = openFileInput(fileName); //openFileInput auto opens from getFilesDir() directory
                    // getFilesDir() is directory of internal app storage
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                InputStreamReader sreader1 = new InputStreamReader(Objects.requireNonNull(stream1));
                BufferedReader breader1 = new BufferedReader(sreader1);

                String line1;
                String TLE1 = "";
                String TLE2 = "";

                //Read each lne of file, if its equal to the one chosen from the list, update TLE strings and break loop


                try {
                    while ((line1 = breader1.readLine()) != null) {
                        if (line1.equals(satChosen)) { //If the current line is the one we chose from the list
                            TLE1 = breader1.readLine();
                            TLE2 = breader1.readLine();

                            break;
                        }
                    }
                    breader1.close();sreader1.close();stream1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Tle 1: " + TLE1);
                System.out.println("Tle 2: " + TLE2);

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("CHOSEN_SAT_NAME", satChosen);
                intent.putExtra(MapsActivity.FILENAME, fileName);
                startActivity(intent);


                try {
                    currentEntity = new Entity(satChosen, TLE1, TLE2);
                    selectedSats.add(currentEntity);
                    SatelliteSelectActivity.getSelectedSat();
                } catch (OrekitException e) {
                    e.printStackTrace();
                }

                return false;

            }

        });
        //listView.setOnItemClickListener(mMessageClickedHandler);
    }


    public static ArrayList<Entity> getSelectedSat() { //ArrayList<String> getSelectedSat(){
        System.out.println(selectedSats.size());

        return selectedSats;
    }

    public static void clearSelectedSats() {
        selectedSats.clear();
        satList.clear();

    }
    public static ArrayList<Object> getFavoriteSats(){return favoriteSats;}

    public static void removeFavSat() {
        boolean found = false;
            //create dialog box

    }

    /*
    public static void clearSatsList() {
        selectedSats.clear();
    }
    */
    public void geoGo(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
