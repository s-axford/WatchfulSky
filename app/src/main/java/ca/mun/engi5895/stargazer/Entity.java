package ca.mun.engi5895.stargazer;

/**
 * Created by noahg on 2/28/2018.
 */
import android.nfc.TagLostException;
import java.util.calendar;
import org.orekit.propagation.analytical.tle;

public class Entity {

    int avgMass = 200;
    Entity(){

        TLE entity = new TLE(string line1, string line2);

        TimeScale timeZone = new UTCScale();
        Calendar date = new Calendar()
        AbsoluteDate initialDate = new AbsoluteDate(year, month, day, hour, min, sec, timeZone);
        TLEPropagator calc = new TLEPropagator(entity, ,avgMass);


    }

}
