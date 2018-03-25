package ca.mun.engi5895.stargazer;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by noahg on 3/24/2018.
 */

public class Favorites {

    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter adapterList;
    private Context context;
    private ListView listView;

    Favorites(Context inContext){

        context = inContext;
        try {
            adapterList = this.getFavSats();
        } catch (IOException e) {

        }
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
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

    public ArrayAdapter<String> getFavorites(){return adapterList;}

    public void addFavorite(String name, String line1, String line2){



    }

    /*
    public String getFavorite(){
        return "0";
    }
    */

}
