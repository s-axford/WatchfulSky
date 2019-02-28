package ca.mun.engi5895.watchfulsky.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ca.mun.engi5895.watchfulsky.OrekitDataInstallation.Install;
import ca.mun.engi5895.watchfulsky.OrekitDataInstallation.OrekitInit;
import ca.mun.engi5895.watchfulsky.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Install the Orekit files
       Install.installApkData(this);

        //Initialize Orekit with the data files
        OrekitInit.init(Install.getOrekitDataRoot(this));

        ImageView earth_icon = findViewById(R.id.earth_icon);
        ImageView sun_icon = findViewById(R.id.sun_icon);
        ImageView earth_bg = findViewById(R.id.earth_layout);

        Picasso.get().load(R.drawable.earth).resize(72,66).centerCrop().into(earth_icon);
        Picasso.get().load(R.drawable.sun).resize(72,66).centerCrop().into(sun_icon);
        Picasso.get().load(R.drawable.earth_bg).resize(500, 500).centerCrop().into(earth_bg);
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
