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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.status);
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


    private TextView textView;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String string = bundle.getString(saveFileIntent.FILEPATH);
                int resultCode = bundle.getInt(saveFileIntent.RESULT);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this,
                            "Download complete. Download URI: " + string,
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download done");
                } else {
                    Toast.makeText(MainActivity.this, "Download failed",
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download failed");
                }
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                saveFileIntent.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void onClick(View view) {

        Intent intent = new Intent(this, saveFileIntent.class);
        // add infos for the service which file to download and where to store
        intent.putExtra(saveFileIntent.FILENAME, "stations.txt");
        intent.putExtra(saveFileIntent.URL,
                "https://www.celestrak.com/NORAD/elements/stations.txt");
        startService(intent);
        textView.setText("Service started");
    }


   public void checkFiles(View v) {
        File folder = getFilesDir();
        File[] listOfFiles = folder.listFiles();


        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
              //  System.out.println("File " + listOfFiles[i].getName());
                Toast toast = Toast.makeText(getApplicationContext(), "File " + listOfFiles[i].getName(), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
/*
    public void test(String sUrl, Context context, String fileName) {
        try {
            InputStream inputStream = new URL(sUrl).openStream();   // Download Image from UR
            System.out.println("new input stream");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder out = new StringBuilder();
            System.out.println("new string builder");

            String line;

            while ((line = reader.readLine()) != null) {
                out.append(line + "\n");
            }
            inputStream.close();

            String fileString = out.toString();
            String testString = out.substring(0, 10);
           System.out.println(testString);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(fileString);
            outputStreamWriter.close();

        } catch (Exception e) {
            Log.d("saveFile", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
        }





    public void saveFile(Context context, String fileName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveFile", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }


    private class DownloadFile extends AsyncTask<String, Void, File> {
        private String TAG = "DownloadFile";


        private File downloadFileContents(String sUrl) {
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from UR
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return File;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveFile(getApplicationContext(),"my_image.png");
        }
    }*/



}
