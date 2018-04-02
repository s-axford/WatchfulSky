package ca.mun.engi5895.stargazer.AndroidAestheticAdditions;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Class responsible for handling adding satellites as a favorite
 */

public class Favorites {

    private static Context context;

    // Constructor
    public Favorites(Context inContext){

        context = inContext;
    }

    /**
     * Method to adda satellite to the favorites list.
     * Writes the name and TLE data of the selected satellite to a specific file
     * Filename is dependant on the type of satellite.
     * It was necessary to implement in this way because of how the expandable list view is populated.
     * @param name
     * @param line1
     * @param line2
     * @param fileName
     */
    public static void addFavorite(String name, String line1, String line2, String fileName){
        try {

            FileOutputStream fostream = context.openFileOutput( "favorites_" + fileName, Context.MODE_APPEND);

           OutputStreamWriter oswriter = new OutputStreamWriter(fostream);
           BufferedWriter bwriter = new BufferedWriter(oswriter);


            // Write the name of the satellite to the favorites file
            bwriter.write(name);
            bwriter.write("\n");

            // Write the first TLE line to the favorites file
            bwriter.write(line1);
            bwriter.write("\n");

            // Write the second TLE line to the favorites file
            bwriter.write(line2);
            bwriter.write("\n");

            // flush the writer
            bwriter.flush();

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
