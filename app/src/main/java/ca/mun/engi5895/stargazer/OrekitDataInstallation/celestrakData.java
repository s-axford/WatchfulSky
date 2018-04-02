package ca.mun.engi5895.stargazer.OrekitDataInstallation;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that handles the population of the expandable list view for regular and favorited satellites.
 */


public class celestrakData {
    private static Context c;

    /**
     *
     * @param context
     * @return a hash map representing all of the satellite data.
     * The keys of the hash map represent the the expandable list categories.
     * The values of the hash map represent the satellite names.
     * @throws IOException
     */
    public static HashMap<String, List<String>> getSatData(Context context) throws IOException {

        c = context;
        // Create empty hash map
        HashMap<String, List<String>> celestrakMap = new HashMap<String, List<String>>();

        List<String> stations = new ArrayList<String>();
        List<String> stationsList = getNames("stations.txt", c); // Add names of satellites to list
        for (String s : stationsList) {
            stations.add(s);
        }

        List<String> thirtyDays = new ArrayList<String>();
        List<String> thirtyDaysList = getNames("tle-new.txt", c);
        for (String s : thirtyDaysList) {
            thirtyDays.add(s);
        }

        List<String> gps = new ArrayList<String>();
        List<String> gpsList = getNames("gps-ops.txt", c);
        for (String s : gpsList) {
            gps.add(s);
        }

        List<String> intelsat = new ArrayList<String>();
        List<String> intelsatList = getNames("intelsat.txt", c);
        for (String s : intelsatList) {
            intelsat.add(s);
        }

        List<String> geo = new ArrayList<String>();
        List<String> geoList = getNames("geo.txt", c);
        for (String s : geoList) {
            geo.add(s);
        }

        List<String> science = new ArrayList<String>();
        List<String> scienceList = getNames("science.txt", c);
        for (String s : scienceList) {
            science.add(s);
        }

        // Populate hash map with satellite names and corresponding keys
        celestrakMap.put("Space Stations", stations);
        celestrakMap.put("Newly Launched Satellites", thirtyDays);
        celestrakMap.put("GPS Satellites", gps);
        celestrakMap.put("Communications Satellites", geo);
        celestrakMap.put("Intelsat Satellites", intelsat);
        celestrakMap.put("Science Satellites", science);
        return celestrakMap;
    }

    /**
     *
     * @param context
     * @return Hash map representing the favorited satellites
     * The keys of the hash map represent the the expandable list categories.
     * The values of the hash map represent the satellite names.
     * @throws IOException
     */
    public static HashMap<String, List<String>> getSatDataFavorites(Context context) throws IOException {

        c = context;
        // create empty hash map
        HashMap<String, List<String>> celestrakMap = new HashMap<String, List<String>>();

        // Create files for each of the favorite files
        File test1 = new File(c.getFilesDir(), "favorites_stations.txt");
        File test2 = new File(c.getFilesDir(), "favorites_tle-new.txt");
        File test3 = new File(c.getFilesDir(), "favorites_gps-ops.txt");
        File test4 = new File(c.getFilesDir(), "favorites_intelsat.txt");
        File test5 = new File(c.getFilesDir(), "favorites_geo.txt");
        File test6 = new File(c.getFilesDir(), "favorites_science.txt");

        // Create an arraylist for the space station names
        List<String> stations = new ArrayList<String>();
        if (test1.exists()) { // If the file exists, aka there is data in the favorites file
            List<String> stationsList = getNames("favorites_stations.txt", c); // Populate a variable with all the names of the file
            for (String s : stationsList) {
                stations.add(s);
            }} else if (!test1.exists()){ // If file doesn't exist, add "no data" to the map
            stations.add("No data");}


        List<String> thirtyDays = new ArrayList<String>();
        if (test2.exists()) {
            List<String> thirtyDaysList = getNames("favorites_tle-new.txt", c);
            for (String s : thirtyDaysList) {
                thirtyDays.add(s);
            }} else if (!test2.exists()) {
            thirtyDays.add(" No data");}

        List<String> gps = new ArrayList<String>();
        if (test3.exists()) {
            List<String> gpsList = getNames("favorites_gps-ops.txt", c);
            for (String s : gpsList) {
                gps.add(s);
            }} else if (!test3.exists()) {
            gps.add(" No data");}

        List<String> intelsat = new ArrayList<String>();
        if (test4.exists()) {
            List<String> intelsatList = getNames("favorites_intelsat.txt", c);
            for (String s : intelsatList) {
                intelsat.add(s);
            }} else if (!test4.exists()) {
            intelsat.add(" No data");}

        List<String> geo = new ArrayList<String>();
        if (test5.exists()) {
            List<String> geoList = getNames("favorites_geo.txt", c);
            for (String s : geoList) {
                geo.add(s);
            }} else if (!test5.exists()) {
            geo.add(" No data");}

        List<String> science = new ArrayList<String>();
        if (test6.exists()) {
            List<String> scienceList = getNames("favorites_science.txt", c);
            for (String s : scienceList) {
                science.add(s);
            }} else if (!test6.exists()) {
            science.add(" No data");}

        // Add lists to the hash map with corresponding keys for list categories
        celestrakMap.put("Space Stations", stations);
        celestrakMap.put("Newly Launched Satellites", thirtyDays);
        celestrakMap.put("GPS Satellites", gps);
        celestrakMap.put("Communications Satellites", geo);
        celestrakMap.put("Intelsat Satellites", intelsat);
        celestrakMap.put("Science Satellites", science);
        return celestrakMap;
    }


    /**
     *
     * @param filename
     * @param context
     * @return Arraylist containing all the names of the satellites from a specific internal file
     * @throws IOException
     */
    public static ArrayList<String> getNames(String filename, Context context) throws IOException {

        ArrayList<String> list = new ArrayList<>();

        // Open file streams
        FileInputStream stream = context.openFileInput(filename);
        InputStreamReader sreader = new InputStreamReader(stream);
        BufferedReader breader = new BufferedReader(sreader);

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

        return list;
    }

    /**
     *
     * @param aContext
     *
     * Method responsible for downloading the satellite data from celestrak.com
     * Method is ran whenever the app is first started
     */
    public static void downloadData(Context aContext) {

        Intent intent = new Intent(aContext, saveFileIntent.class);
        // add information for the service: which file to download and where to store
        intent.putExtra(saveFileIntent.FILENAME,"stations.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/stations.txt");
        aContext.startService(intent);

        intent.putExtra(saveFileIntent.FILENAME,"tle-new.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/tle-new.txt");
        aContext.startService(intent);

        intent.putExtra(saveFileIntent.FILENAME,"gps-ops.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/gps-ops.txt");
        aContext.startService(intent);

        intent.putExtra(saveFileIntent.FILENAME,"intelsat.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/intelsat.txt");
        aContext.startService(intent);

        intent.putExtra(saveFileIntent.FILENAME,"geo.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/geo.txt");
        aContext.startService(intent);

        intent.putExtra(saveFileIntent.FILENAME,"science.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/science.txt");
        aContext.startService(intent);
    }

}
