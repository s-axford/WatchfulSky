package ca.mun.engi5895.stargazer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HeliocentricActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heliocentric);
        setTitle("Heliocentric Orbit");
        try {
        CelestialBody Mercury = CelestialBodyFactory.getMercury();
        CelestialBody Venus = CelestialBodyFactory.getVenus();
        CelestialBody Earth = CelestialBodyFactory.getEarth();
        CelestialBody Mars = CelestialBodyFactory.getMars();
        CelestialBody Jupiter = CelestialBodyFactory.getJupiter();
        CelestialBody Saturn = CelestialBodyFactory.getSaturn();
        CelestialBody Uranus = CelestialBodyFactory.getUranus();
        CelestialBody Neptune = CelestialBodyFactory.getNeptune();
        } catch (OrekitException e) {
            e.printStackTrace();
        }


    }


}
