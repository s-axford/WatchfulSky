package ca.mun.engi5895.watchfulsky.OrekitDataInstallation;

import android.app.Application;
import android.util.Log;

/**
 * Class representing the android application
 */
public class MyApplication extends Application {

    /**
     * Ran once, when the applciation starts up
     */
    @Override
    public void onCreate() {
        super.onCreate();
        celestrakData.downloadData(this); // downloads the TLE data

        Log.i("main", "onCreate fired");
    }
}