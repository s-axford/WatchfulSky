package ca.mun.engi5895.stargazer.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.mun.engi5895.stargazer.DataInstallation.ErrorDialogFragment;
import ca.mun.engi5895.stargazer.DataInstallation.Install;
import ca.mun.engi5895.stargazer.DataInstallation.OrekitInit;
import ca.mun.engi5895.stargazer.DataInstallation.celestrakData;
import ca.mun.engi5895.stargazer.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Download data
        celestrakData.downloadData(this);

        //Install required Orekit data
        Install.installApkData(this);

        //Initialize Orekit with the data files
        OrekitInit.init(Install.getOrekitDataRoot(this));

    }


    /**
     * Displays an error dialog
     * @param message
     * @param canIgnore
     */
    public void showErrorDialog(String message, boolean canIgnore) {
        DialogFragment newFragment = ErrorDialogFragment.newInstance(message, canIgnore);
        newFragment.setCancelable(false);
        newFragment.show(getFragmentManager(), "error");
    }

    public void geoGo(View view) {
        Intent intent = new Intent(this, activity_satellite_sel.class);
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
