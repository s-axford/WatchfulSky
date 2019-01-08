package ca.mun.engi5895.stargazer.AndroidAestheticAdditions;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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

            boolean favExists = false;
            File test1 = new File(context.getFilesDir(), "favorites_" + fileName);

            if(test1.exists()) {

                // Open file streams

                System.out.println("Filename: " + fileName);
                FileInputStream stream = context.openFileInput("favorites_" + fileName);
                InputStreamReader sreader = new InputStreamReader(stream);
                BufferedReader breader = new BufferedReader(sreader);

                String line;

                // Parse the file and check to see if the satellite is already added asa  favorite
                while ((line = breader.readLine()) != null) {
                    if (line.contains(name)) {
                        favExists = true;
                    }
                }
            }


            if (!favExists) {
                FileOutputStream fostream = context.openFileOutput("favorites_" + fileName, Context.MODE_APPEND);

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
            } else {
                System.out.println("Favorite already exists.");
            }

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
