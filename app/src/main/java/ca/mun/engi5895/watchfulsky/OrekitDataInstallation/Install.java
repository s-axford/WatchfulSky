package ca.mun.engi5895.watchfulsky.OrekitDataInstallation;

import android.app.Activity;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import ca.mun.engi5895.watchfulsky.Activities.MainActivity;

/**
 * Created by Spencer on 3/8/2018.
 */

public class Install {


    private static String orekitDataPath = "orekit-data";
    private static String[] orekitFolders = {
            "Others",
            "DE-430-ephemerides",
            "Earth-Orientation-Parameters"+File.separator+"IAU-1980",
            "Earth-Orientation-Parameters"+File.separator+"IAU-2000",
            "MSAFE",
            "Potential"
    };


    /**
     * Returns file pointing to root directory of the orekit data
     * @param activity
     */
    public static File getOrekitDataRoot(Activity activity){
        return new File(activity.getFilesDir()+File.separator+orekitDataPath);
    }



    /**
     * Installs the orekit files in the internal storage of the application
     * @param activity
     */
    public static void installApkData(MainActivity activity){

       File check = new File(activity.getFilesDir() + File.separator + "orekit-data");
       System.out.println(check.getAbsolutePath());
       if(!check.exists()) {
           copyAssets(activity);
       } else
           System.out.println("Files already in storage.");

    }

    /**
     * Copies the orekit files from the assets folder to internal device storage
     * @param activity
     * @return
     */
    private static boolean copyAssets(Activity activity) {
        boolean installed = true;
        AssetManager assetManager = activity.getAssets();
        for(String foldername : orekitFolders) {
            String[] files = null;
            try {
                files = assetManager.list(orekitDataPath+File.separator+foldername);
            } catch (IOException e) {
                installed=false;
            }
            for(String filename : Objects.requireNonNull(files)) {
                InputStream instream = null;
                OutputStream out = null;
                try {
                    instream = assetManager.open(orekitDataPath+File.separator+foldername+File.separator+filename);
                    File outFile = new File(activity.getFilesDir()+File.separator+orekitDataPath+File.separator+foldername, filename);
                    outFile.getParentFile().mkdirs();
                    out = new FileOutputStream(outFile);
                    copyFile(instream, out);
                    instream.close();
                    instream = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch(IOException e) {
                    installed=false;
                }
            }
        }
        return installed;
    }

    /**
     * Copies a file from one place to another
     * @param in
     * @param out
     * @throws IOException
     */
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


}