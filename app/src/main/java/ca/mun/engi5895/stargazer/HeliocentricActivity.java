package ca.mun.engi5895.stargazer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
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

public class HeliocentricActivity extends AppCompatActivity {

    //private Pair<Integer, XYSeries> selection;
    //private TextLabelWidget selectionWidget;
    //private static final String NO_SELECTION_TXT = "Touch bar to select.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heliocentric);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        setTitle("Heliocentric Orbit");
        XYPlot plot = findViewById(R.id.plot);
        plot.setTitle("Current Location of Planets");
        plot.getLegend().setVisible(false);
        plot.setRangeLabel("");
        plot.setDomainLabel("Astronimical Units");
        plot.getOuterLimits().set(-40, 40, -40, 40);
        PanZoom.attach(plot); //make plot able to pan and zoom


        /*selectionWidget = new TextLabelWidget(plot.getLayoutManager(), NO_SELECTION_TXT,
                new SizeMetrics(
                        PixelUtils.dpToPix(100), SizeLayoutType.ABSOLUTE,
                        PixelUtils.dpToPix(100), SizeLayoutType.ABSOLUTE),
                TextOrientationType.HORIZONTAL);

        selectionWidget.getLabelPaint().setTextSize(PixelUtils.dpToPix(16));

        // add a dark, semi-transparent background to the selection label widget:
        Paint p = new Paint();
        p.setARGB(100, 0, 0, 0);
        selectionWidget.setBackgroundPaint(p);

        selectionWidget.position(
                0, XLayoutStyle.RELATIVE_TO_CENTER,
                PixelUtils.dpToPix(45), YLayoutStyle.ABSOLUTE_FROM_TOP,
                AnchorPosition.TOP_MIDDLE);
        selectionWidget.pack();
        */



        //Formatting for orbit lines and celestial body points
        LineAndPointFormatter formatOrbits = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);
        LineAndPointFormatter formatSol = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_sun);
        LineAndPointFormatter formatMercury = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_mercury);
        LineAndPointFormatter formatVenus = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_venus);
        LineAndPointFormatter formatEarth = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_earth);
        LineAndPointFormatter formatMars = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_mars);
        LineAndPointFormatter formatJupiter = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_jupiter);
        LineAndPointFormatter formatSaturn = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_saturn);
        LineAndPointFormatter formatUranus = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_uranus);
        LineAndPointFormatter formatNeptune = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_neptune);

        formatSol.setPointLabeler(new PointLabeler() {
            @Override
            public String getLabel(XYSeries series, int index) {
                if(index == 0) {
                    return "Sol";
                }
                return "";
            }
        });

        formatMercury.setPointLabeler(new PointLabeler() {
            @Override
            public String getLabel(XYSeries series, int index) {
                if(index == 0) {
                    return "Mercury";
                }
                return "";
            }
        });

        formatVenus.setPointLabeler(new PointLabeler() {
            @Override
            public String getLabel(XYSeries series, int index) {
                if(index == 0) {
                    return "Venus";
                }
                return "";
            }
        });

        formatEarth.setPointLabeler(new PointLabeler() {
            @Override
            public String getLabel(XYSeries series, int index) {
                if(index == 0) {
                    return "Earth";
                }
                return "";
            }
        });

        formatMars.setPointLabeler(new PointLabeler() {
            @Override
            public String getLabel(XYSeries series, int index) {
                if(index == 0) {
                    return "Mars";
                }
                return "";
            }
        });

        formatJupiter.setPointLabeler(new PointLabeler() {
            @Override
            public String getLabel(XYSeries series, int index) {
                if(index == 0) {
                    return "Jupiter";
                }
                return "";
            }
        });

        formatSaturn.setPointLabeler(new PointLabeler() {
            @Override
            public String getLabel(XYSeries series, int index) {
                if(index == 0) {
                    return "Saturn";
                }
                return "";
            }
        });

        formatUranus.setPointLabeler(new PointLabeler() {
            @Override
            public String getLabel(XYSeries series, int index) {
                if(index == 0) {
                    return "Uranus";
                }
                return "";
            }
        });

        formatNeptune.setPointLabeler(new PointLabeler() {
            @Override
            public String getLabel(XYSeries series, int index) {
                if(index == 0) {
                    return "Neptune";
                }
                return "";
            }
        });


        //making solar system (array of CelestialBodies
        CelestialBody[] solarSystem = new CelestialBody[9];

        try {
            solarSystem = new CelestialBody[]{CelestialBodyFactory.getSun(), CelestialBodyFactory.getMercury(), CelestialBodyFactory.getVenus(), CelestialBodyFactory.getEarth(), CelestialBodyFactory.getMars(), CelestialBodyFactory.getJupiter(), CelestialBodyFactory.getSaturn(), CelestialBodyFactory.getUranus(), CelestialBodyFactory.getNeptune()};
        } catch (OrekitException e) {
            e.printStackTrace();
        }

        //Number[] xyPosition = new Number[]{0,0}; //interweave xy points of body locations
        Number[][] planetPositions = new Number[2* solarSystem.length][2];

        double[] radius = new double[solarSystem.length];
        //int index1 = 0;
        int index = 0;
        for (CelestialBody b : solarSystem) { // for all bodies in the solar system
            try {
                planetPositions[index][0] = mtoAU(findPlanetPosition(b))[0];
                planetPositions[index][1] = mtoAU(findPlanetPosition(b))[1];
                radius[index] = Math.sqrt(Math.pow((double) planetPositions[index][0],2) + Math.pow((double) planetPositions[index][1],2)); //find radius of orbit using x y position of planet
                System.out.println("Body:");
                System.out.println(index);
                System.out.println(mtoAU(findPlanetPosition(b))[0]);
                System.out.println(mtoAU(findPlanetPosition(b))[1]);
                System.out.println(radius[index]);
                System.out.println(Math.pow(mtoAU(findPlanetPosition(b))[0],2));
                System.out.println(Math.pow(mtoAU(findPlanetPosition(b))[1],2));

                index++;
            } catch (OrekitException e) {
                e.printStackTrace();
            }
        }

        //get XYSeries object for plotting of body locations
        XYSeries Sol = drawPlanet(0.00464913034, planetPositions[0]);
        XYSeries Mercury =drawPlanet(1.63083872*Math.pow(10, -5) ,planetPositions[1]);
        XYSeries Venus = drawPlanet(4.04537843*Math.pow(10, -5), planetPositions[2]);
        XYSeries Earth = drawPlanet(4.26349651*Math.pow(10, -5) ,planetPositions[3]);
        XYSeries Mars = drawPlanet(2.27075425*Math.pow(10, -5), planetPositions[4]);
        XYSeries Mars_point = this.drawBodyPosition(planetPositions[4]);
        XYSeries Jupiter = drawPlanet(0.000477895, planetPositions[5]);
        XYSeries Saturn = drawPlanet(0.000402866697 ,planetPositions[6]);
        XYSeries Uranus = drawPlanet(0.000170851362, planetPositions[7]);
        XYSeries Neptune = drawPlanet(0.000165537115, planetPositions[8]);

        formatOrbits.setInterpolationParams(new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
        //for all radius of each bodies orbit
        for (double b : radius) {
            XYSeries orbits = this.drawAverageOrbit(b); //get XYSeries of the orbit ( assume circular and sun centered );
            plot.addSeries(orbits, formatOrbits); //plot orbits
        }

        // plot body locations
        plot.addSeries(Sol, formatSol);
        plot.addSeries(Mercury, formatMercury);
        plot.addSeries(Venus, formatVenus);
        plot.addSeries(Earth, formatEarth);
        plot.addSeries(Mars, formatMars);
        plot.addSeries(Mars_point, formatMars);
        plot.addSeries(Jupiter, formatJupiter);
        plot.addSeries(Saturn, formatSaturn);
        plot.addSeries(Uranus, formatUranus);
        plot.addSeries(Neptune, formatNeptune);

        /*plot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PointF click = new PointF(motionEvent.getX(), motionEvent.getY());
                if(plot.getGraph().containsPoint(click)) {
                    onPlotClicked(click);
                }
                return false;
            }
        });*/
    }

    /*private void onPlotClicked(PointF point) {

        // make sure the point lies within the graph area.  we use gridrect
        // because it accounts for margins and padding as well.
        if (plot.getGraph().getGridRect().contains(point.x, point.y)) {
            Number x = plot.getXVal(point);
            Number y = plot.getYVal(point);


            selection = null;
            double xDistance = 0;
            double yDistance = 0;

            // find the closest value to the selection:
            for (XYSeries series : plot.getRegistry().getSeriesList()) {
                for (int i = 0; i < series.size(); i++) {
                    Number thisX = series.getX(i);
                    Number thisY = series.getY(i);
                    if (thisX != null && thisY != null) {
                        double thisXDistance =
                                Region.measure(x, thisX).doubleValue();
                        double thisYDistance =
                                Region.measure(y, thisY).doubleValue();
                        if (selection == null) {
                            selection = new Pair<Integer, XYSeries>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance < xDistance) {
                            selection = new Pair<Integer, XYSeries>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance == xDistance &&
                                thisYDistance < yDistance &&
                                thisY.doubleValue() >= y.doubleValue()) {
                            selection = new Pair<Integer, XYSeries>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        }
                    }
                }
            }

        } else {
            // if the press was outside the graph area, deselect:
            selection = null;
        }

        if(selection == null) {
            selectionWidget.setText(NO_SELECTION_TXT);
        } else {
            selectionWidget.setText("Selected: " + selection.second.getTitle() +
                    " Value: " + selection.second.getY(selection.first));
        }

        plot.redraw();
    }*/

    public void clickPlanet(CelestialBody body, String name) throws OrekitException {
        RelativeLayout buttons = findViewById(R.id.topButtons);
        buttons.setVisibility(View.GONE);
        RelativeLayout info = findViewById(R.id.PlanetInfo);
        info.setVisibility(View.VISIBLE);
        TextView pos = findViewById(R.id.PlanetPositionValue);
        TextView vel = findViewById(R.id.PlanetVelocityValue);
        TextView n = findViewById(R.id.PlanetName);

        double[] position = findPlanetPosition(body);
        double radius = Math.sqrt(Math.pow(mtoAU(position)[0],2) + Math.pow(mtoAU(position)[1],2));
        double velocity = getPlanetVelocity(body);


        pos.setText(String.format("%.2f", radius) + "AU");
        vel.setText(String.format("%.2f", velocity) + "m/s");
        n.setText(name);
    }

    public void clickBack(View view) throws OrekitException {
        RelativeLayout buttons = findViewById(R.id.topButtons);
        buttons.setVisibility(View.VISIBLE);
        RelativeLayout info = findViewById(R.id.PlanetInfo);
        info.setVisibility(View.INVISIBLE);
    }

    public void clickMercury(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getMercury(), "Mercury");}
    public void clickVenus(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getVenus(), "Venus");}
    public void clickEarth(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getEarth(), "Earth");}
    public void clickMars(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getMars(), "Mars");}
    public void clickJupiter(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getJupiter(), "Jupiter");}
    public void clickSaturn(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getSaturn(), "Saturn");}
    public void clickUranus(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getUranus(), "Uranus");}
    public void clickNeptune(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getNeptune(), "Neptune");}


    protected static TimeStampedPVCoordinates getPlanetCoordinates(CelestialBody body) throws OrekitException {
        TimeScale timeZone = TimeScalesFactory.getUTC(); // get UTC time scale
        Date date = new Date(); //creates date
        Calendar calendar = GregorianCalendar.getInstance(); //sets calendar
        calendar.setTime(date); //updates date and time
        AbsoluteDate abDate = new AbsoluteDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), timeZone); //creates orekit absolute date from calender
      
        return body.getPVCoordinates(abDate, CelestialBodyFactory.getSun().getBodyOrientedFrame());
    }

    protected static double[] findPlanetPosition(CelestialBody body) throws OrekitException {
        TimeStampedPVCoordinates pv = getPlanetCoordinates(body);
        Vector3D position = pv.getPosition();

        return new double[]{position.getX(), position.getY()};//, position.getZ()};
    }

    protected static double getPlanetVelocity(CelestialBody body) throws OrekitException {
        TimeStampedPVCoordinates pv = getPlanetCoordinates(body);
        Vector3D v = pv.getVelocity();

        return Math.sqrt(Math.pow(v.getX(),2) + Math.pow(v.getY(),2) + Math.pow(v.getZ(),2));

    }

    protected XYSeries drawAverageOrbit(double averageOrbitRadius) {
        // initialize our XYPlot reference:

        Number[] yVals = new Number[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Number[] xVals = new Number[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int index = 0;
        for(double i = 0; i <= 2*Math.PI; i += Math.PI/32) {
            yVals[index] = averageOrbitRadius * Math.sin(i);
            xVals[index] = averageOrbitRadius * Math.cos(i);
            index++;
        }
            yVals[index] = averageOrbitRadius*Math.sin(0);
            xVals[index] = averageOrbitRadius*Math.cos(0);

        return new SimpleXYSeries(Arrays.asList(xVals), Arrays.asList(yVals), "Orbit");
    }

    protected static XYSeries drawPlanet(double radius, Number[] xy) {
        // initialize our XYPlot reference:

        Number[] yVals = new Number[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Number[] xVals = new Number[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int index = 0;
        for(double i = 0; i <= 2*Math.PI; i += Math.PI/32) {
            yVals[index] = (double) xy[1] + (radius * Math.sin(i));
            xVals[index] = (double) xy[0] + (radius * Math.cos(i));
            index++;
        }
        yVals[index] = (double) xy[1] + radius*Math.sin(0);
        xVals[index] = (double) xy[0] + radius*Math.cos(0);

        /*for(int i = 0 ; i < xVals.length && i < yVals.length ; i++){
            if((double) xy[1] < 0){
                xVals[i] = (double) xVals[i] + radius;
            }
            else if((double) xy[1] > 0){
                xVals[i] = (double) xVals[i] - radius;
            }
            if((double) xy[0] < 0){
                yVals[i] = (double) yVals[i] + radius;
            }
            else if((double) xy[0] > 0){
                yVals[i] = (double) yVals[i] - radius;
            }
        }*/
        /*for(int i = 0 ; i < xVals.length ; i++){
            xVals[i] = (double) xVals[i] + (double) xy[0];
        }
        for(int i = 0 ; i < yVals.length ; i++){
            yVals[i] = (double) yVals[i] + (double) xy[1];
        }*/

        return new SimpleXYSeries(Arrays.asList(xVals), Arrays.asList(yVals), "Planet");
    }

    protected static double[] mtoAU(double[] values){
        double[] result = new double[values.length];
        for (int i = 0 ; i < values.length ; i++){
            result[i] = values[i]*6.68459*Math.pow(10, -12);
        }
        return result;
    }

   protected XYSeries drawBodyPosition(Number[] xyPositions) {
       return new SimpleXYSeries(Arrays.asList(xyPositions), SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, "CelestialBodies");
   }
}
