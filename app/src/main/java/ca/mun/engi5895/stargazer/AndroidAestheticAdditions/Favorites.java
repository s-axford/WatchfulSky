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
 * Class responsible for handling the favorites function
 */

public class Favorites {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter adapterList;
    private static Context context;
    private ListView listView;

    public Favorites(Context inContext){

        context = inContext;
    }

    private ArrayAdapter<String> getFavSats() throws IOException {
        //open file stations.txt
        FileInputStream stream = context.openFileInput("favoriteSats.txt");
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                list);
        //se the adapter
        //listView.setAdapter(arrayAdapter);
        //close streams
        breader.close();
        sreader.close();
        stream.close();

        return arrayAdapter;
        /*
        //Class that handles clicking on a list item
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position); //Gets clicked option as java object
                selectedSats.add(o);
                System.out.println(o.toString()); //Output to console as string
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                //listView.setVisibility(listView.GONE); //Hide the list cause its no longer needed

                //Updates textview to the picked satellite name. Used for testing.
                //outSat = (TextView) findViewById(R.id.textView2);
                //outSat.setText(o.toString());
                //outSat.setVisibility(View.VISIBLE);


                //Start the re-parsing of the text file for the TLE data for chosen satellite
                FileInputStream stream1 = null;
                try {
                    stream1 = openFileInput("stations.txt"); //openFileInput auto opens from getFilesDir() directory
                    // getFilesDir() is directory of internal app storage
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                InputStreamReader sreader1 = new InputStreamReader(stream1);
                BufferedReader breader1 = new BufferedReader(sreader1);

                String line1;
                String TLE1 = new String();
                String TLE2 = new String();

                //Read each lne of file, if its equal to the one chosen from the list, update TLE strings and break loop
                try {
                    while ((line1 = breader1.readLine()) != null) {
                        if (line1.equals(o.toString())) { //If the current line is the one we chose from the list
                            TLE1 = breader1.readLine();
                            TLE2 = breader1.readLine();
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Creating new entity

            }

        };
        listView.setOnItemClickListener(mMessageClickedHandler);
        */


        //Retrieve favorites file from memory
        //Creates private list
    }

    public ArrayAdapter getFavorites(){return adapterList;}

    public static void addFavorite(String name, String line1, String line2, String fileName){
        try {
            //Creates buffered Writer to add new sat to list
            System.out.println("about to write to: "+ "favorites_" + fileName );
            System.out.println("Name: " + name);
            System.out.println("Tle1: " + line1);
            System.out.println("Tle2: " + line2);

           /*File favTest = new File(context.getFilesDir(), "favorites_" + fileName);

            if (!favTest.exists()) {
                try {
                    favTest.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (favTest.exists()) {
                System.out.println(fileName+ " favorites file still doesnt exist");
            }*/
            /*FileInputStream stream1 = null;
            try {
                stream1 = context.openFileInput("favorites_"+fileName); //openFileInput auto opens from getFilesDir() directory
                // getFilesDir() is directory of internal app storage
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            InputStreamReader sreader1 = new InputStreamReader(stream1);
            BufferedReader breader1 = new BufferedReader(sreader1);
            String line;
            //Read each lne of file, if its equal to the one chosen from the list, update TLE strings and break loop
            try {
                while ((line = breader1.readLine()) != null) {
                    System.out.println(breader1.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            stream1.close(); sreader1.close();breader1.close();*/

           /* System.out.println("Printing previous Contents of File: " + "favorites_" + fileName);


            try (BufferedReader br = new BufferedReader(new FileReader(context.getFilesDir() + System.lineSeparator() + "favorites_" + fileName))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();}*/


           // File stat = new File(context.getFilesDir(), "favorites_" + fileName);

            FileOutputStream fostream = context.openFileOutput( "favorites_" + fileName, Context.MODE_APPEND);

            OutputStreamWriter oswriter = new OutputStreamWriter(fostream);
           BufferedWriter bwriter = new BufferedWriter(oswriter);

            //FileWriter w = new FileWriter(stat, true);
           // BufferedWriter bwriter = new BufferedWriter(w);


            //added each element of data to list
            //bwriter.newLine();
            //oswriter.write(name);
            //oswriter.write(line1);
           // oswriter.write(line2);
            bwriter.write(name);
            bwriter.write("\n");
           // bwriter.newLine();
            bwriter.write(line1);
            bwriter.write("\n");
           // bwriter.newLine();
           bwriter.write(line2);
            bwriter.write("\n");

           bwriter.flush();

            //close all streams


            File test1 = new File(context.getFilesDir(), "favorites_" + fileName);
            if (test1.exists()) {
                System.out.println("Successfully wrote file: " + "favorites_" + fileName );
            } else
                System.out.println("DID NTO success wrote file: " + "favorites_" + fileName );

           /* System.out.println("Printing new Contents of File");

            try (BufferedReader br = new BufferedReader(new FileReader(context.getFilesDir() + System.lineSeparator() + "favorites_" + fileName))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }

            FileInputStream stream2 = null;
            try {
                stream2 = context.openFileInput("favorites_"+fileName); //openFileInput auto opens from getFilesDir() directory
                // getFilesDir() is directory of internal app storage
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            InputStreamReader sreader2 = new InputStreamReader(stream2);
            BufferedReader breader2 = new BufferedReader(sreader2);
            String linee;
            //Read each lne of file, if its equal to the one chosen from the list, update TLE strings and break loop
            try {
                while ((linee = breader2.readLine()) != null) {
                    System.out.println(breader2.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/



           // stream1.close(); sreader1.close();breader1.close();



        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /*
    public String getFavorite(){
        return "0";
    }
    */

}
