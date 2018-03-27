package ca.mun.engi5895.stargazer;

/**
 * The Entity class is a representation of a space entity, with many methods used to interpret
 its data. It is important to note here that the data on each satellite is in TLE (Two Line
 Element) format. Many of the Entity class methods call various methods from the TLE,
 TLEPropagator, PVCoordinates and TimeScaleFactory classes. As previously mentioned,
 the Orekit open source library is used for the data calculations. The TLE, TLEPropagator,
 and TimescaleFactory classes are both part of the Orekit library, as well as Vector3D,
 BodyCenterPointing, AttitudeProvider, AbsoluteDate, TimeScale, PVCoordinates, Ellipsoid,
 and Frame. Lots of these classes must communicate with one another for the data calculations
 to execute correctly and for entity to be able to access the required information.
 */

import org.orekit.errors.OrekitException;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.frames.Frame;
import org.orekit.utils.PVCoordinates;
import org.orekit.time.UTCScale;

import org.orekit.utils.TimeStampedPVCoordinates;
/*
import org.orekit.propagation.conversion.TLEPropagatorBuilder;
import org.orekit.propagation.Propagator;
import org.orekit.attitudes.Attitude;
import org.orekit.orbits.PositionAngle;
import org.orekit.bodies.Ellipsoid;
import org.orekit.utils.AngularCoordinates;
*/

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Entity {

    private static TLE entity; //TLE object for satellite
    private static TLEPropagator tleProp; //orbit propagator
    private static String entity_name;

    //private Calendar calendar;
    //private double[] array;
    //private int avgMass = 200;
    //private Attitude att;
    //private PVCoordinates currentPV;
    //private TLEPropagatorBuilder builder;


    //constructor
    Entity(String name, String line1, String line2) throws OrekitException{

        //System.out.println(line1);
        //System.out.println(line2);
        entity = new TLE(line1, line2); //creates TLE object
        tleProp = TLEPropagator.selectExtrapolator(entity); //extrapolates proper propagation for orbit as TLEPropagator

        entity_name = name;
       //TimeScale timeZone = TimeScalesFactory.getUTC();
       //Date date = new Date();
       //calendar = GregorianCalendar.getInstance();
       //calendar.setTime(date);
       //Frame frame = FramesFactory.getGCRF();

       //BUILD TLEPropagator Builder
       //builder = new TLEPropagatorBuilder(entity.getSatelliteNumber(), entity.getClassification(), entity.getLaunchYear(), entity.getLaunchNumber(), entity.getLaunchPiece(), entity.getElementNumber(), entity.getRevolutionNumberAtEpoch());

       //BUILD TLEPropagator


       //Ellipsoid el = new Ellipsoid(frame, 1,1,1);
       //AngularCoordinates orientation = new AngularCoordinates();
       //AbsoluteDate initialDate = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), timeZone);
       //currentPV = new PVCoordinates();
       //att = new Attitude(initialDate, frame, orientation);
       //Attitude att = new Attitude( initialDate, frame, orientation);

        //Propagator propagator = builder.buildPropagator(initialDate, array);

    }

    //In design documentation this is referred to as "update()"
    //returns updated coordinates of satellite
    private TimeStampedPVCoordinates getPVCoordinates() throws OrekitException {
        TimeScale timeZone = TimeScalesFactory.getUTC(); // get UTC time scale
        Date date = new Date(); //creates date
        Calendar calendar = GregorianCalendar.getInstance(); //sets calendar
        calendar.setTime(date); //updates date and time
        AbsoluteDate abDate = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), timeZone); //creates orekit absolute date from calender
        Frame frame = FramesFactory.getTEME();
        tleProp.propagate(abDate);
        return tleProp.getPVCoordinates(abDate, frame); //returns coordinates
    }

    private TimeStampedPVCoordinates getPVCoordinates(AbsoluteDate date) throws OrekitException {

        Frame frame = FramesFactory.getTEME();
        //SpacecraftState state = tleProp.propagate(date);
        //Orbit orbit = state.getOrbit();
        //TimeStampedPVCoordinates coordinates = orbit.getPVCoordinates();
        tleProp.propagate(date);

        return tleProp.getPVCoordinates(date, frame); //returns coordinates
    }
    public Vector3D getVector(AbsoluteDate date){
        TimeStampedPVCoordinates pv = null;
        try {
            pv = this.getPVCoordinates(date);
        } catch (OrekitException e) {
            e.printStackTrace();
        }

        return pv.getPosition();
    }
    public double getX(AbsoluteDate date){
        double xVal = 0;
        xVal = getVector(date).getX();

        return xVal;
    }
    public double getY(AbsoluteDate date){
        double yVal = 0;
        yVal = getVector(date).getY();

        return yVal;
    }
    public double getZ(AbsoluteDate date){
        double zVal = 0;
        zVal = getVector(date).getZ();

        return zVal;
    }
    public SpacecraftState getOrbit(AbsoluteDate date){

        try {
            return tleProp.propagate(date);
        } catch (OrekitException e) {
            return null;
        }
    }

    //Returns the magnitude of the velocity
    public double getVelocity() throws OrekitException {
        PVCoordinates coord = this.getPVCoordinates(); //gets up to date coordinates
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
        System.out.println("Mean Motion: " + meanMotion);
        return 2*Math.PI / meanMotion; //returns period
    }

    //gets Height of satellite from ground
    public double getHeight() throws OrekitException{
        PVCoordinates coord = this.getPVCoordinates(); //gets up to date coordinates
        Vector3D position = coord.getPosition();
        return position.getZ(); // MAY BE WRONG VARIABLE !! MAY NEED TO USE getX() or getY()
    }

    public double getPerigee(){
        double period = 1 / entity.getMeanMotion(); //returns period
        double semiMajorAxis = Math.pow(((period/(2*Math.PI)*(2*Math.PI))*TLEPropagator.getMU()), (double) 1/3); //semi major axis of rotation
        return semiMajorAxis*(1 - entity.getE()) - 6371; //returns perigee (6371 is earths means radius)
    }

    public double getApogee() {
        double period = 1 / entity.getMeanMotion(); //returns period
        double semiMajorAxis = Math.pow(((period/(2*Math.PI)*(2*Math.PI))*TLEPropagator.getMU()), (double) 1/3); //semi major axis of rotation
        return semiMajorAxis*(1 + entity.getE()) - 6371; //returns apogee (6371 is earths means radius)
    }


    public double getInclination(){
        return entity.getI(); //return inclination angle
    }

    public String getLine1() throws OrekitException {return entity.getLine1();}

    public String getLine2() throws OrekitException {return entity.getLine2();}

    public String getName() {return entity_name;}


}
