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

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Entity {

    int avgMass = 200;
    TLE entity;
    Calendar calendar;
    Attitude att;
    PVCoordinates currentPV;

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

    }

    public double getVelocity(){

        return 0;
    }
    public double getPeriod(){

        return 0;
    }

    public double getHeight(){

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