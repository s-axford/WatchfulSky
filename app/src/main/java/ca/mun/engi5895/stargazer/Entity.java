package ca.mun.engi5895.stargazer;

/**
 * Created by noahg on 2/28/2018.
 */
import android.nfc.TagLostException;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.orekit.propagation.analytical.tle;

public class Entity {

    int avgMass = 200;
  
    Entity(){

        TLE entity = new TLE(string line1, string line2);

        TimeScale timeZone = new UTCScale();
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);

        AbsoluteDate initialDate = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), timeZone);

        TLEPropagator calc = new TLEPropagator(entity, ,avgMass);

    }

    public double getVelocity(){

        return 0;
    }
    public double getPeriod(){

        return 0;
    }

    public double getHieght(){

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