package ca.mun.engi5895.stargazer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;




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
        String name = folder.getAbsolutePath();

        File[] listOfFiles = folder.listFiles(); //Array of files in storage

        //If array is empty, there are no files
        if (listOfFiles.length == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Directory is empty", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //Iterate through file array and outputs each one's name as a toast
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getAbsolutePath().contains(".txt")) { //This checks and makes sure its a file and not folder, not necessary here but not bad to have
                Toast toast = Toast.makeText(getApplicationContext(), "File " + listOfFiles[i].getName(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    //redacted
    public void checkExternalFiles(View v) {

        File folder = getFilesDir(); //Gets internal storage directory

        for (File file : folder.listFiles()) //Iterates through files in folder and deletes them
            if (!file.isDirectory() && file.getAbsolutePath().contains("favorites"))
                file.delete();

        //Creates a toast saying its cleaned
        Toast toast = Toast.makeText(getApplicationContext(), "Favorites Cleaned", Toast.LENGTH_SHORT);
        Toast toast1 = Toast.makeText(getApplicationContext(), "Cleaning Unsuccesful", Toast.LENGTH_SHORT);
        toast.show();

        for (File file : folder.listFiles()) //Iterates through files in folder and deletes them
            if (!file.isDirectory() && file.getAbsolutePath().contains("favorites"))
                toast1.show();



    }

    //Function that clears out internal app storage directory
    public void purgeFiles(View v) {
        File folder = getFilesDir(); //Gets internal storage directory

        for (File file : folder.listFiles()) //Iterates through files in folder and deletes them
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
        intent.putExtra(saveFileIntent.FILENAME,"stations.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/stations.txt");
        startService(intent);

        intent.putExtra(saveFileIntent.FILENAME,"tle-new.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/tle-new.txt");
        startService(intent);

        intent.putExtra(saveFileIntent.FILENAME,"gps-ops.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/gps-ops.txt");
        startService(intent);

        intent.putExtra(saveFileIntent.FILENAME,"intelsat.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/intelsat.txt");
        startService(intent);

        intent.putExtra(saveFileIntent.FILENAME,"geo.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/geo.txt");
        startService(intent);

        intent.putExtra(saveFileIntent.FILENAME,"science.txt");
        intent.putExtra(saveFileIntent.URL,"https://www.celestrak.com/NORAD/elements/science.txt");
        startService(intent);

        /*ZipUtil.explode(new File(getFilesDir().getName() + File.separator + "orekit-data.zip"));
        File orekitData = new File(getFilesDir().getName() + "/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();

/*
        Intent orekit = new Intent(this, saveFileIntent.class);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/orekit-data/");
        System.out.println(file);

        File orekitData = file;
        DataProvidersManager manager = DataProvidersManager.getInstance();


        try {
            manager.addProvider(new DirectoryCrawler(orekitData));
        } catch (OrekitException e) {
            e.printStackTrace();
        }
*/

        /*
        Intent orekit = new Intent(this, saveFileIntent.class);
        // add infos for the service which file to download and where to store
        orekit.putExtra(saveFileIntent.FILENAME, "orekit-data.zip");
        orekit.putExtra(saveFileIntent.URL,
                "https://www.orekit.org/forge/attachments/download/677/orekit-data.zip");
        startService(orekit);

        //String target = getFilesDir().getName() + "orekit-data";
        //saveFileIntent.unzip("orekit-data.zip", getFilesDir().getName());




        /*
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
