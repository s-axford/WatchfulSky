package ca.mun.engi5895.stargazer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.TimeStampedPVCoordinates;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HeliocentricActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heliocentric);
        setTitle("Heliocentric Orbit");

        CelestialBody[] SolarSystem = new CelestialBody[8];
        try {
            SolarSystem = new CelestialBody[]{CelestialBodyFactory.getMercury(), CelestialBodyFactory.getVenus(), CelestialBodyFactory.getEarth(), CelestialBodyFactory.getMars(), CelestialBodyFactory.getJupiter(), CelestialBodyFactory.getSaturn(), CelestialBodyFactory.getUranus(), CelestialBodyFactory.getNeptune()};
        } catch (OrekitException e) {
            e.printStackTrace();
        }
        for(CelestialBody b : SolarSystem) {
            try {
                double[] PositionVector = findPlanetPostions(b);
            } catch (OrekitException e) {
                e.printStackTrace();
            }
        }
    }

    protected double[] findPlanetPostions(CelestialBody body) throws OrekitException {
        TimeScale timeZone = TimeScalesFactory.getUTC(); // get UTC time scale
        Date date = new Date(); //creates date
        Calendar calendar = GregorianCalendar.getInstance(); //sets calendar
        calendar.setTime(date); //updates date and time
        AbsoluteDate abDate = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), timeZone); //creates orekit absolute date from calender
        TimeStampedPVCoordinates pv = body.getPVCoordinates(abDate, body.getBodyOrientedFrame());
        Vector3D position = pv.getPosition();

        return new double[]{position.getX(), position.getY(), position.getZ()};
    }
}