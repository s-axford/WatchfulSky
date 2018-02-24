package ca.mun.engi5895.stargazer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GeocentricActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocentric);
    }

    public void loadStations(View v) throws IOException {


        FileInputStream stream = openFileInput("stations.txt");
        InputStreamReader sreader = new InputStreamReader(stream);
        BufferedReader breader = new BufferedReader(sreader);

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = breader.readLine()) != null) {
            sb.append(line + System.getProperty("line.separator"));

        }

        String fileString = sb.toString();
        String testString = sb.substring(0, 10);
        Toast.makeText(getApplicationContext(), testString, 1);
        System.out.println(fileString);



    }
}
