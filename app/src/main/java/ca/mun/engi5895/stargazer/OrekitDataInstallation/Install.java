package ca.mun.engi5895.stargazer.OrekitDataInstallation;

import android.app.Activity;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ca.mun.engi5895.stargazer.Activities.MainActivity;

/**
 * Created by Spencer on 3/8/2018.
 */

public class Install {


    /**
     * Returns the File pointing to the root of the Orekit data in the device storage
     * @param activity
     * @return
     */
    public static File getOrekitDataRoot(Activity activity){
        return new File(activity.getFilesDir()+File.separator+orekitDataPath);
    }

    private static String orekitDataPath = "orekit-data";
    private static String[] orekitDataFolders = {
            "Others",
            "DE-430-ephemerides",
            "Earth-Orientation-Parameters"+File.separator+"IAU-1980",
            "Earth-Orientation-Parameters"+File.separator+"IAU-2000",
            "MSAFE",
            "Potential"
    };

    /**
     * Installs the default Orekit data files in the device
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
    private static boolean copyAssets(Activity activity) {
        boolean installed = true;
        AssetManager assetManager = activity.getAssets();
        for(String foldername : orekitDataFolders) {
            String[] files = null;
            try {
                files = assetManager.list(orekitDataPath+File.separator+foldername);
            } catch (IOException e) {
                //Log.e("tag", "Failed to get asset file list.", e);
                installed=false;
            }
            for(String filename : files) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(orekitDataPath+File.separator+foldername+File.separator+filename);
                    System.out.println("File copying is: " + orekitDataPath+File.separator+foldername+File.separator+filename);
                    File outFile = new File(activity.getFilesDir()+File.separator+orekitDataPath+File.separator+foldername, filename);
                    outFile.getParentFile().mkdirs();
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch(IOException e) {
                    //Log.e("tag", "Failed to copy asset file: " + filename, e);
                    installed=false;
                }
            }
        }
        return installed;
    }
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


}