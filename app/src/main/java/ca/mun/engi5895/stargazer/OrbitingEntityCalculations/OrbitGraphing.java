package ca.mun.engi5895.stargazer.OrbitingEntityCalculations;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.TimeStampedPVCoordinates;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//Class used by HelioCentric activity for orbital calculations and XY series generation
public class OrbitGraphing {

    //plots planets as a single point
    //xyPositions should contain xy coordinates interweave into a single array
    protected XYSeries drawBodyPosition(Number[] xyPositions) {
        return new SimpleXYSeries(Arrays.asList(xyPositions), SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, "CelestialBodies");
    }

    //gets PVCoordinates for current time for a particular body  based on a sun centered reference frame
    protected static TimeStampedPVCoordinates getPlanetCoordinates(CelestialBody body) throws OrekitException {
        TimeScale timeZone = TimeScalesFactory.getUTC(); // get UTC time scale
        Date date = new Date(); //creates date
        Calendar calendar = GregorianCalendar.getInstance(); //sets calendar
        calendar.setTime(date); //updates date and time
        AbsoluteDate abDate = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), timeZone); //creates orekit absolute date from calender

        return body.getPVCoordinates(abDate, CelestialBodyFactory.getSun().getBodyOrientedFrame());
    }

    //Finds current position of particular body as double array in m/s
    public static double[] findPlanetPosition(CelestialBody body) throws OrekitException {
        TimeStampedPVCoordinates pv = getPlanetCoordinates(body);
        Vector3D position = pv.getPosition();

        return new double[]{position.getX(), position.getY()};//, position.getZ()};
    }

    //provides a single velocity term in m/s
    public static double getPlanetVelocity(CelestialBody body) throws OrekitException {
        TimeStampedPVCoordinates pv = getPlanetCoordinates(body);
        Vector3D v = pv.getVelocity();

        return Math.sqrt(Math.pow(v.getX(), 2) + Math.pow(v.getY(), 2) + Math.pow(v.getZ(), 2));
    }

    //creates XYSeries for XYPlot for the orbits of CelestialBodies based on the average orbit radius (assumes inclination and eccentricity are 0)
    public static XYSeries drawAverageOrbit(double averageOrbitRadius) {
        // initialize our XYPlot reference:

        Number[] yVals = new Number[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Number[] xVals = new Number[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int index = 0;
        for (double i = 0; i <= 2 * Math.PI; i += Math.PI / 32) {
            yVals[index] = averageOrbitRadius * Math.sin(i);
            xVals[index] = averageOrbitRadius * Math.cos(i);
            index++;
        }
        yVals[index] = averageOrbitRadius * Math.sin(0);
        xVals[index] = averageOrbitRadius * Math.cos(0);

        return new SimpleXYSeries(Arrays.asList(xVals), Arrays.asList(yVals), "Orbit");
    }

    //creates XYSeries for XYPlot of a given body based on the planets current position as an interweave double array and the radius of the body
    public static XYSeries drawPlanet(double radius, Number[] xy) {
        // initialize our XYPlot reference:

        Number[] yVals = new Number[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Number[] xVals = new Number[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int index = 0;
        for (double i = 0; i <= 2 * Math.PI; i += Math.PI / 32) {
            yVals[index] = (double) xy[1] + (radius * Math.sin(i));
            xVals[index] = (double) xy[0] + (radius * Math.cos(i));
            index++;
        }
        yVals[index] = (double) xy[1] + radius * Math.sin(0);
        xVals[index] = (double) xy[0] + radius * Math.cos(0);

        return new SimpleXYSeries(Arrays.asList(xVals), Arrays.asList(yVals), "CelestialBody");
    }
}
