package ca.mun.engi5895.watchfulsky.OrekitDataInstallation;

import android.app.Application;
import android.util.Log;

/**
 * Class representing the android application
 */
public class MyApplication extends Application {

    /**
     * Ran once, when the application starts up
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("main", "onCreate fired");
    }
}