package ca.mun.engi5895.stargazer;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

/**
 * Created by john on 2018-02-24.
 */

public class saveFileIntent extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "ca.mun.engi5895.stargazer.";




    public saveFileIntent() {
        super("ca.mun.engi5895.stargazer.saveFileIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);

        File output = new File(getFilesDir(), fileName);

        if (output.exists()) {
            output.delete();
        }


        InputStream stream = null;
        FileOutputStream fos = null;

        try {
            URL url = new URL(urlPath);
            stream = url.openConnection().getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            fos = new FileOutputStream(output.getPath());
            int next = -1;
            while ((next = reader.read()) != -1) {
                fos.write(next);
            }
            // successfully finished
            result = Activity.RESULT_OK;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream!= null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        publishResults(output.getAbsolutePath(), result);
    }


    private void publishResults(String outputPath, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

}
