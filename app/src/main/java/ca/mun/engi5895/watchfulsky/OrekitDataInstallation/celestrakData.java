package ca.mun.engi5895.watchfulsky.OrekitDataInstallation;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that handles the population of the expandable list view for regular and favorited satellites.
 */


public class celestrakData {

    /**
     * @param context application context
     * @return a hash map representing all of the satellite data.
     * The keys of the hash map represent the the expandable list categories.
     * The values of the hash map represent the satellite names.
     * @throws IOException whoops
     */
    public static HashMap<String, List<String>> getSatData(Context context) throws IOException {

        // Create empty hash map
        HashMap<String, List<String>> celestrakMap = new HashMap<>();

        List<String> stationsList;
        List<String> thirtyDaysList;
        List<String> gpsList;
        try {
            stationsList = getNames("stations.txt", context); // Add names of satellites to list
        } catch (IOException e) {
            e.printStackTrace();
            stationsList = new ArrayList<>();
        }
        List<String> stations = new ArrayList<>(stationsList);

        thirtyDaysList = getNames("tle-new.txt", context);
        List<String> thirtyDays = new ArrayList<>(thirtyDaysList);

        gpsList = getNames("gps-ops.txt", context);
        List<String> gps = new ArrayList<>(gpsList);

        List<String> intelsatList = getNames("intelsat.txt", context);
        List<String> intelsat = new ArrayList<>(intelsatList);

        List<String> geoList = getNames("geo.txt", context);
        List<String> geo = new ArrayList<>(geoList);

        List<String> scienceList = getNames("science.txt", context);
        List<String> science = new ArrayList<>(scienceList);

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
     * @param context application context
     * @return Hash map representing the favorited satellites
     * The keys of the hash map represent the the expandable list categories.
     * The values of the hash map represent the satellite names.
     * @throws IOException Whoops
     */
    public static HashMap<String, List<String>> getSatDataFavorites(Context context) throws IOException {

        // create empty hash map
        HashMap<String, List<String>> celestrakMap = new HashMap<>();

        // Create files for each of the favorite files
        File test1 = new File(context.getFilesDir(), "favorites_stations.txt");
        File test2 = new File(context.getFilesDir(), "favorites_tle-new.txt");
        File test3 = new File(context.getFilesDir(), "favorites_gps-ops.txt");
        File test4 = new File(context.getFilesDir(), "favorites_intelsat.txt");
        File test5 = new File(context.getFilesDir(), "favorites_geo.txt");
        File test6 = new File(context.getFilesDir(), "favorites_science.txt");

        // Create an arraylist for the space station names
        List<String> stations = new ArrayList<>();
        if (test1.exists()) { // If the file exists, aka there is data in the favorites file
            List<String> stationsList = getNames("favorites_stations.txt", context); // Populate a variable with all the names of the file
            stations.addAll(stationsList);
        } else if (!test1.exists()) { // If file doesn't exist, add "no data" to the map
            stations.add("No data");
        }


        List<String> thirtyDays = new ArrayList<>();
        if (test2.exists()) {
            List<String> thirtyDaysList = getNames("favorites_tle-new.txt", context);
            thirtyDays.addAll(thirtyDaysList);
        } else if (!test2.exists()) {
            thirtyDays.add(" No data");
        }

        List<String> gps = new ArrayList<>();
        if (test3.exists()) {
            List<String> gpsList = getNames("favorites_gps-ops.txt", context);
            gps.addAll(gpsList);
        } else if (!test3.exists()) {
            gps.add(" No data");
        }

        List<String> intelsat = new ArrayList<>();
        if (test4.exists()) {
            List<String> intelsatList = getNames("favorites_intelsat.txt", context);
            intelsat.addAll(intelsatList);
        } else if (!test4.exists()) {
            intelsat.add(" No data");
        }

        List<String> geo = new ArrayList<>();
        if (test5.exists()) {
            List<String> geoList = getNames("favorites_geo.txt", context);
            geo.addAll(geoList);
        } else if (!test5.exists()) {
            geo.add(" No data");
        }

        List<String> science = new ArrayList<>();
        if (test6.exists()) {
            List<String> scienceList = getNames("favorites_science.txt", context);
            science.addAll(scienceList);
        } else if (!test6.exists()) {
            science.add(" No data");
        }

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
     * @param filename Files that is to be parsed
     * @param context  application context
     * @return Arraylist containing all the names of the satellites from a specific internal file
     * @throws IOException Whoops
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
     * @param aContext Method responsible for downloading the satellite data from celestrak.com
     *                 Method is ran whenever the app is first started
     */
    static void downloadData(Context aContext) {

        saveFile("stations.txt", "https://www.celestrak.com/NORAD/elements/stations.txt", aContext);
        saveFile("tle-new.txt", "https://www.celestrak.com/NORAD/elements/tle-new.txt", aContext);
        saveFile("gps-ops.txt", "https://www.celestrak.com/NORAD/elements/gps-ops.txt", aContext);
        saveFile("intelsat.txt", "https://www.celestrak.com/NORAD/elements/intelsat.txt", aContext);
        saveFile("geo.txt", "https://www.celestrak.com/NORAD/elements/geo.txt", aContext);
        saveFile("science.txt", "https://www.celestrak.com/NORAD/elements/science.txt", aContext);

    }

    private static void saveFile(String filename, String urlPath, Context context) {

        File output = new File(context.getFilesDir(), filename); // Get file output

        if (output.exists()) { // if file already exists, delete it
            boolean deleted = output.delete();
            System.out.println("former file deleted: " + deleted);
        }

        InputStream stream = null;
        FileOutputStream fos;

        try {
            java.net.URL url = new URL(urlPath);
            stream = url.openConnection().getInputStream(); // Open a URLconnection
            InputStreamReader reader = new InputStreamReader(stream);
            fos = new FileOutputStream(output.getPath());
            int next;
            while ((next = reader.read()) != -1) {  // Read the input stream and output it to a fileoutputstream
                fos.write(next);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) { // close the stream
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
