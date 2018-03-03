package ca.mun.engi5895.stargazer;

/**
 * Created by noahg on 2/28/2018.
 */
import android.nfc.TagLostException;

import org.orekit.attitudes.Attitude;
import org.orekit.attitudes.AttitudeProvider;
import org.orekit.errors.OrekitException;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.propagation.conversion.TLEPropagatorBuilder;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.time.UTCScale;
import org.orekit.attitudes.BodyCenterPointing;
import org.orekit.frames.Frame;
import org.orekit.bodies.Ellipsoid;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.AngularCoordinates;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.orbits.PositionAngle;

import org.hipparchus.geometry.euclidean.threed.Vector3D; //may need hipparchus core

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Entity {

    int avgMass = 200;
    TLE entity;
    Calendar calendar;
    Attitude att;
    PVCoordinates currentPV;

    private int avgMass = 200;
    private TLE entity;
    private Calendar calendar;
    private TLEPropagator tleProp;


    Entity(String line1, String line2){

        entity = new TLE(line1, line2);

        TimeScale timeZone = TimeScalesFactory.getUTC();
        Date date = new Date();
        calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        Frame frame = FramesFactory.getGCRF();
        TLEPropagatorBuilder builder = new TLEPropagatorBuilder(entity, MEAN,1);

        Ellipsoid el = new Ellipsoid(frame, 1,1,1);
        AngularCoordinates orientation = new AngularCoordinates();
        AbsoluteDate initialDate = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), timeZone);

        currentPV = new PVCoordinates();
        att = new Attitude(initialDate, frame, orientation);
        KeplerianPropagator tleProp = new TLEPropagator(entity, att, avgMass);

        Attitude att = AttitudeProvider( , date, frame);
        tleProp = new TLEPropagator(entity, att,avgMass);


    }

    //In design documentation this is referred to as "update()"
    //returns updated coordinates of satellite
    private PVCoordinates getPVCoordinates() throws OrekitException {
        TimeScale timeZone = TimeScalesFactory.getUTC(); // get UTC time scale
        Date date = new Date(); //creates date
        calendar = GregorianCalendar.getInstance(); //sets calendar
        calendar.setTime(date); //updates date and time
        AbsoluteDate abDate = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), timeZone); //creates orekit absolute date from calender
        return tleProp.getPVCoordinates(abDate); //returns coordinates

    }
    //Returns the magnitude of the velocity
    public double getVelocity() throws OrekitException {
        PVCoordinates coord =this.getPVCoordinates(); //gets up to date coordinates
        Vector3D velocity = coord.getVelocity(); //gets velocity vector
        //get components of vector as doubles
        double x = velocity.getX();
        double y = velocity.getY();
        double z = velocity.getZ();
        return Math.sqrt(x*x + y*y + z*z); //returns magnitude
    }
    //returns orbital period of satellite
    public double getPeriod(){

        double meanMotion = entity.getMeanMotion(); //gets mean motion
        double period = 1 / meanMotion; //converts to orbital period
        return period; //returns period
    }

    //gets Height of satellite from ground
    public double getHeight() throws OrekitException{
        PVCoordinates coord =this.getPVCoordinates(); //gets up to date coordinates
        Vector3D position = coord.getPosition();

        return 0;
    }

    public double getPerigee(){

        return 0;
    }

    public double getApogee() {

        return 0;
    }


    public double getInclination(){

        return 0;
    }
}