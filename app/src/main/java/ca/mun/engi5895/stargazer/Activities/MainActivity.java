package ca.mun.engi5895.stargazer.Activities;

import android.app.DialogFragment;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ca.mun.engi5895.stargazer.OrekitDataInstallation.Install;
import ca.mun.engi5895.stargazer.OrekitDataInstallation.OrekitInit;
import ca.mun.engi5895.stargazer.OrekitDataInstallation.celestrakData;
import ca.mun.engi5895.stargazer.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Install the orekit files
       Install.installApkData(this);

        //Initialize Orekit with the data files
        OrekitInit.init(Install.getOrekitDataRoot(this));

    }



 // Button that goes to the geocentric activity
    public void geoGo(View view) {
        Intent intent = new Intent(this, SatelliteSelectActivity.class);
        startActivity(intent);
    }
    // Button that goes to the heliocentric activity
    public void helioGo(View view) {
        Intent intent = new Intent(this, HeliocentricActivity.class);
        startActivity(intent);
    }
    // Button that goes to the settings activity
    public void settingsGo(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
