package ca.mun.engi5895.watchfulsky.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import ca.mun.engi5895.watchfulsky.OrekitDataInstallation.LoadData;
import ca.mun.engi5895.watchfulsky.R;

/**
 * Settings activity
 */
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

    public void updateFiles(View view){
        purgeFiles(view);
        LoadData task = new LoadData(this);
        task.execute();
        checkFiles(view);
    }
}
