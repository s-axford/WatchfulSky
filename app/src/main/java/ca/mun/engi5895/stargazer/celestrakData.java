package ca.mun.engi5895.stargazer;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by john on 2018-03-27.
 */

public class celestrakData {
    private static Context c;

    public static HashMap<String, List<String>> getSatData(Context aContext) throws IOException {

        c = aContext;
        HashMap<String, List<String>> celestrakMap = new HashMap<String, List<String>>();

        List<String> stations = new ArrayList<String>();
        List<String> stationsList = getNames("stations.txt", c);
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


        celestrakMap.put("Space Stations", stations);
        celestrakMap.put("Newly Launched Satellites", thirtyDays);
        celestrakMap.put("GPS Satellites", gps);
        celestrakMap.put("Communications Satellites", geo);
        celestrakMap.put("Intelsat Satellites", intelsat);
        celestrakMap.put("Science Satellites", science);
        return celestrakMap;
    }

    //Returns list of satellite names from file
    private static ArrayList<String> getNames(String filename, Context context) throws IOException {

        ArrayList<String> list = new ArrayList<>();
        System.out.println("getFilesDir: " + context.getFilesDir());
        System.out.println("about to open: " + filename);

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

}
