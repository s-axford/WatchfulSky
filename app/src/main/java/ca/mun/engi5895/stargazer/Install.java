package ca.mun.engi5895.stargazer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Spencer on 3/8/2018.
 */

public class Install {

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static File getOrekitDataRoot(Activity activity) {
        return new File(activity.getExternalFilesDir(null) + File.separator + orekitDataPath);
    }

    private static String orekitDataPath = "orekit";
    private static String[] orekitDataFolders = {
            "Others",
            "DE-406-ephemerides",
            "Earth-Orientation-Parameters" + File.separator + "IAU-1980",
            "Earth-Orientation-Parameters" + File.separator + "IAU-2000",
            "MSAFE",
            "Potential"
    };

    /**
     * Installs the default Orekit data files in the device
     *
     * @param activity
     */
    public static void installApkData(MainActivity activity) {
        System.out.println("1");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        System.out.println("2");
        if (!prefs.getBoolean(activity.getString(R.string.pref_key_data_installed), false)) {
            System.out.println("forloop");
            //Log.d("INSTALLER", "Installing Orekit data files...");
            if (isExternalStorageWritable()) {
                System.out.println("3");
                if (copyAssets(activity)) {
                    System.out.println("4");
                    prefs.edit().putBoolean(activity.getString(R.string.pref_key_data_installed), true).commit();
                    //Log.d("INSTALLER", "Installing Orekit data files... OK");
                    //activity.showWelcomeMessage();
                    //activity.flag_show_welcome = true;
                } else {
                    //Log.d("INSTALLER", "Installing Orekit data files... FAIL");
                    activity.showErrorDialog(activity.getString(R.string.error_installing_orekit_default_data), true);
                }
            } else {
                //Log.d("INSTALLER", "Cannot install Orekit data files, external storage not accessible");
                activity.showErrorDialog(activity.getString(R.string.error_installing_orekit_default_data_external_storage_not_accessible), true);
            }
        } else {
            //Log.d("INSTALLER", "Orekit data files are already installed...");
            System.out.println("else statement");
        }
    }

    private static boolean copyAssets(Activity activity) {
        boolean installed = true;
        AssetManager assetManager = activity.getAssets();
        for (String foldername : orekitDataFolders) {
            String[] files = null;
            try {
                files = assetManager.list(orekitDataPath + File.separator + foldername);
            } catch (IOException e) {
                //Log.e("tag", "Failed to get asset file list.", e);
                installed = false;
            }
            for (String filename : files) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(orekitDataPath + File.separator + foldername + File.separator + filename);
                    File outFile = new File(activity.getExternalFilesDir(null) + File.separator + orekitDataPath + File.separator + foldername, filename);
                    outFile.getParentFile().mkdirs();
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch (IOException e) {
                    //Log.e("tag", "Failed to copy asset file: " + filename, e);
                    installed = false;
                }
            }
        }
        return installed;
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}