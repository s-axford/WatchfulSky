package ca.mun.engi5895.stargazer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
    }


    //lists all files in internal app storage
    public void checkFiles(View v) {
        File folder = getFilesDir(); //Internal storage
        File[] listOfFiles = folder.listFiles(); //Array of files in storage

        //If array is empty, there are no files
        if(listOfFiles.length == 0 ) {
            Toast toast = Toast.makeText(getApplicationContext(), "Directory is empty", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //Iterate through file array and outputs each one's name as a toast
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) { //This checks and makes sure its a file and not folder, not necessary here but not bad to have
                Toast toast = Toast.makeText(getApplicationContext(), "File " + listOfFiles[i].getName(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    //Function that clears out internal app storage directory
    public void purgeFiles(View v) {
        File folder = getFilesDir(); //Gets internal storage directory

        for(File file: folder.listFiles()) //Iterates through files in folder and deletes them
            if (!file.isDirectory())
                file.delete();

        //Creates a toast saying its cleaned
        Toast toast = Toast.makeText(getApplicationContext(), "Directory Cleaned", Toast.LENGTH_SHORT);
        toast.show();

    }

    //method that runs the saveFileIntent class, responsible for downloading file
    public void onClick(View view) {

        Intent intent = new Intent(this, saveFileIntent.class);
        // add infos for the service which file to download and where to store
        intent.putExtra(saveFileIntent.FILENAME, "stations.txt");
        intent.putExtra(saveFileIntent.URL,
                "https://www.celestrak.com/NORAD/elements/stations.txt");
        startService(intent);
        // textView.setText("Service started");
        // linearLayout.setVisibility(View.INVISIBLE);
        /*
        Intent orekit = new Intent(this, saveFileIntent.class);
        // add infos for the service which file to download and where to store
        orekit.putExtra(saveFileIntent.FILENAME, "orekit-data.zip");
        orekit.putExtra(saveFileIntent.URL,
                "https://www.orekit.org/forge/attachments/download/677/orekit-data.zip");
        startService(orekit);
        //String target = getFilesDir().getName() + "orekit-data";
        saveFileIntent.unzip("orekit-data.zip", getFilesDir().getName() + "/orekit-data");
        File orekitData = new File(getFilesDir().getName() + "/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        try {
            manager.addProvider(new DirectoryCrawler(orekitData));
        } catch (OrekitException e) {
            e.printStackTrace();
        }
        */
    }
}
