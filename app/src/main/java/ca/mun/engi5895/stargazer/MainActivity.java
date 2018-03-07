package ca.mun.engi5895.stargazer;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //File orekitData = new File("C:\\Users\\Chair\\AndroidStudioProjects\\StarGazer\\app\\orekit-data");
        //DataProvidersManager manager = DataProvidersManager.getInstance();
        //try {
        //    manager.addProvider(new DirectoryCrawler(orekitData));
        //} catch (OrekitException e) {
        //    e.printStackTrace();
        //}
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
    }

    public void geoGo(View view) {
        Intent intent = new Intent(this, GeocentricActivity.class);
        startActivity(intent);
    }

    public void helioGo(View view) {
        Intent intent = new Intent(this, HeliocentricActivity.class);
        startActivity(intent);
    }

    public void settingsGo(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
