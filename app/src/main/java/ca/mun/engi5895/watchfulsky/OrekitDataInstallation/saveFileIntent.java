package ca.mun.engi5895.watchfulsky.OrekitDataInstallation;

import android.app.IntentService;
import android.content.Intent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * IntentService that handles the download of files from the web
 */

public class saveFileIntent extends IntentService {

    //These are the parameters sent to the intent when it is called
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";


    public saveFileIntent() {
        super("ca.mun.engi5895.watchfulsky.OrekitDataInstallation.saveFileIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String urlPath = intent.getStringExtra(URL); // Get stringextra parameters
        String fileName = intent.getStringExtra(FILENAME);

        File output = new File(getFilesDir(), fileName); // Get file output

        if (output.exists()) { // if file already exists, delete it
            output.delete();
        }

        InputStream stream = null;
        FileOutputStream fos;

        try {
            URL url = new URL(urlPath);
            stream = url.openConnection().getInputStream(); // Open a URLconnection
            InputStreamReader reader = new InputStreamReader(stream);
            fos = new FileOutputStream(output.getPath());
            int next = -1;
            while ((next = reader.read()) != -1) {  // Read the input stream and output it to a fileoutputstream
                fos.write(next);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) { // close the stream
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



