package ca.mun.engi5895.stargazer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


    public void checkFiles(View v) {
        File folder = getFilesDir();
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles.length == 0 ) {
            Toast toast = Toast.makeText(getApplicationContext(), "Directory is empty", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }


        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                //  System.out.println("File " + listOfFiles[i].getName());
                Toast toast = Toast.makeText(getApplicationContext(), "File " + listOfFiles[i].getName(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void purgeFiles(View v) {
        File folder = getFilesDir();

        for(File file: folder.listFiles())
            if (!file.isDirectory())
                file.delete();

        Toast toast = Toast.makeText(getApplicationContext(), "Directory Cleaned", Toast.LENGTH_SHORT);
        toast.show();

    }

    public void onClick(View view) {

        Intent intent = new Intent(this, saveFileIntent.class);
        // add infos for the service which file to download and where to store
        intent.putExtra(saveFileIntent.FILENAME, "stations.txt");
        intent.putExtra(saveFileIntent.URL,
                "https://www.celestrak.com/NORAD/elements/stations.txt");
        startService(intent);
        // textView.setText("Service started");
        // linearLayout.setVisibility(View.INVISIBLE);
    }
}
