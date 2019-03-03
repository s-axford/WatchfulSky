package ca.mun.engi5895.watchfulsky.AndroidAestheticAdditions;

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
import java.util.ArrayList;

/**
 * Class responsible for handling adding satellites as a favorite
 */

public class Favorites {

    // Constructor
    public Favorites() {
    }

    /**
     * Method to adda satellite to the favorites list.
     * Writes the name and TLE data of the selected satellite to a specific file
     * Filename is dependant on the type of satellite.
     * It was necessary to implement in this way because of how the expandable list view is populated.
     *
     * @param name     Name of Satellite
     * @param line1    TLE line 1
     * @param line2    TLE line 2
     * @param fileName File containing satellite
     */
    public static void addFavorite(String name, String line1, String line2, String fileName, Context context) {
        try {

            boolean favExists = Favorites.contains(name, fileName, context);

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

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static boolean contains(String name, String fileName, Context context) {

        if (fileName.contains("favorites_")){
            fileName = fileName.replace("favorites_", "");
        }

        File test1 = new File(context.getFilesDir(), "favorites_" + fileName);

        try {
            if (test1.exists()) {
                // Open file streams
                System.out.println("Filename: " + fileName);
                FileInputStream stream = context.openFileInput("favorites_" + fileName);
                InputStreamReader sreader = new InputStreamReader(stream);
                BufferedReader breader = new BufferedReader(sreader);

                String line;

                // Parse the file and check to see if the satellite is already added asa  favorite
                while ((line = breader.readLine()) != null && line.length() != 0) {
                    String newName = name.trim();
                    System.out.println(newName);
                    if (line.contains(name.trim())) {
                        sreader.close();
                        return true;
                    }
                }
                sreader.close();
            }
        } catch (IOException e) {
            Log.e("Exception", "File find failed: " + e.toString());

        }
        return false;
    }

    public static boolean removeFavorite(String name, String line1, String line2, String fileName, Context context) {
        try {

            if (fileName.contains("favorites_")){
                fileName = fileName.replace("favorites_", "");
            }

            boolean favExists = Favorites.contains(name, fileName, context);

            ArrayList<String> linesToRemove = new ArrayList<>();
            linesToRemove.add(name);
            linesToRemove.add(line1);
            linesToRemove.add(line2);
            String currentLine;

            if (favExists) {

                File tempFile = new File(context.getFilesDir(), "myTempFile.txt");
                File currentFile = new File(context.getFilesDir(), "favorites_" + fileName);

                FileOutputStream fostream = context.openFileOutput(tempFile.getName(), Context.MODE_APPEND);
                FileInputStream fistream = context.openFileInput(currentFile.getName());


                InputStreamReader osreader = new InputStreamReader(fistream);
                OutputStreamWriter oswriter = new OutputStreamWriter(fostream);
                BufferedWriter writer = new BufferedWriter(oswriter);
                BufferedReader reader = new BufferedReader(osreader);


                while ((currentLine = reader.readLine()) != null && currentLine.length() != 0) {
                    // trim newline when comparing with lineToRemove
                    if (currentLine.contains(name.trim())) {
                        reader.readLine();
                        reader.readLine();
                    } else {
                        writer.write(currentLine + "\n");
                    }
                }
                writer.close();
                reader.close();

                File here = new File(".");
                System.out.println(here.getAbsolutePath());

                currentFile.delete();
                return tempFile.renameTo(new File(context.getFilesDir(), "favorites_" + fileName));

            } else {
                System.out.println("Favorite does not exist.");
            }

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        return false;
    }
}
