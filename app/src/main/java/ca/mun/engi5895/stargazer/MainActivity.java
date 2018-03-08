package ca.mun.engi5895.stargazer;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent orekit = new Intent(this, saveFileIntent.class);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/orekit-data/");
        System.out.println(file);


        //File[] flist = file.listFiles();
        /*
        for (File filep : flist) {
            if (filep.isFile()) {
                System.out.println(file.getName());
            }
        }
        */

        File orekitData = file;
        DataProvidersManager manager = DataProvidersManager.getInstance();

        try {
            manager.addProvider(new DirectoryCrawler(orekitData));
        } catch (OrekitException e) {
            e.printStackTrace();
        }
        //File orekitData = new File("C:\\Users\\Chair\\AndroidStudioProjects\\StarGazer\\app\\orekit-data");
        //DataProvidersManager manager = DataProvidersManager.getInstance();
        //try {
        //    manager.addProvider(new DirectoryCrawler(orekitData));
        //} catch (OrekitException e) {
        //    e.printStackTrace();
        //}

        //Intent orekit = new Intent(this, saveFileIntent.class);
        // add infos for the service which file to download and where to store
        //orekit.putExtra(saveFileIntent.FILENAME, "orekit-data.zip");
        //orekit.putExtra(saveFileIntent.URL,
        //        "https://www.orekit.org/forge/attachments/download/677/orekit-data.zip");
        //startService(orekit);
        //String target = getFilesDir().getName() + "orekit-data";
        //unpackZip(getFilesDir().getName(), "orekit-data.zip");



    }

    /*
    private void unpackZip(String path, String zipname) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
           // return false;
        }

        //return true;
    }
    */

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
