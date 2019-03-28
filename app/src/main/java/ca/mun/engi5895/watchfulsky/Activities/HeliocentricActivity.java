package ca.mun.engi5895.watchfulsky.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.squareup.picasso.Picasso;

import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;

import ca.mun.engi5895.watchfulsky.R;

import static ca.mun.engi5895.watchfulsky.OrbitingBodyCalculations.OrbitGraphing.drawAverageOrbit;
import static ca.mun.engi5895.watchfulsky.OrbitingBodyCalculations.OrbitGraphing.drawPlanet;
import static ca.mun.engi5895.watchfulsky.OrbitingBodyCalculations.OrbitGraphing.findPlanetPosition;
import static ca.mun.engi5895.watchfulsky.OrbitingBodyCalculations.OrbitGraphing.getPlanetVelocity;

/**
 * Class representing the heliocentric activity
 */
public class HeliocentricActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heliocentric);
        setTitle("The Solar System");

        //Set up planet images
        ImageView mercury_icon = findViewById(R.id.mercury_icon);
        ImageView venus_icon = findViewById(R.id.venus_icon);
        ImageView earth_icon = findViewById(R.id.earth_icon);
        ImageView mars_icon = findViewById(R.id.mars_icon);
        ImageView jupiter_icon = findViewById(R.id.jupiter_icon);
        ImageView saturn_icon = findViewById(R.id.saturn_icon);
        ImageView uranus_icon = findViewById(R.id.uranus_icon);
        ImageView neptune_icon = findViewById(R.id.neptune_icon);

        Picasso.get().load(R.drawable.mercury).resize(75,75).centerCrop().into(mercury_icon);
        Picasso.get().load(R.drawable.venus).resize(75,75).centerCrop().into(venus_icon);
        Picasso.get().load(R.drawable.earth).resize(75, 75).centerCrop().into(earth_icon);
        Picasso.get().load(R.drawable.mars1).resize(75,75).centerCrop().into(mars_icon);
        Picasso.get().load(R.drawable.jupiter).resize(75,75).centerCrop().into(jupiter_icon);
        Picasso.get().load(R.drawable.saturn1).resize(200, 75).centerCrop().into(saturn_icon);
        Picasso.get().load(R.drawable.uranus).resize(75,75).centerCrop().into(uranus_icon);
        Picasso.get().load(R.drawable.neptune).resize(75,75).centerCrop().into(neptune_icon);



        //Set up solar-system plot
        XYPlot plot = findViewById(R.id.plot);
        plot.getLegend().setVisible(false);
//        plot.setDomainLabel("Astronomical Units");
        plot.getOuterLimits().set(-40, 40, -80, 80);
        plot.setRangeBoundaries(-80, 80, BoundaryMode.FIXED);
        plot.setDomainBoundaries(-80, 80, BoundaryMode.FIXED);
        PanZoom.attach(plot); //make plot able to pan and zoom
        //this removes the vertical lines
        plot.getGraph().setDomainGridLinePaint(null);

        //this removes the horizontal lines
        plot.getGraph().setRangeGridLinePaint(null);

        //This gets rid of the gray grid
        plot.getGraph().getGridBackgroundPaint().setColor(Color.TRANSPARENT);

        //This gets rid of the black border (up to the graph) there is no black border around the labels
        plot.getBackgroundPaint().setColor(Color.TRANSPARENT);

        //This gets rid of the black behind the graph
        plot.getGraph().getBackgroundPaint().setColor(Color.TRANSPARENT);

        //With a new release of AndroidPlot you have to also set the border paint
        plot.getBorderPaint().setColor(Color.TRANSPARENT);

        //No Margins
        plot.setPlotMargins(0,0,0,0);

        //No Axis Lines
        plot.getGraph().getDomainOriginLinePaint().setColor(Color.TRANSPARENT);
        plot.getGraph().getRangeOriginLinePaint().setColor(Color.TRANSPARENT);

        plot.setBorderPaint(null);

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

        //create label for each planet
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

        //interweave xy points of body locations
        Number[][] planetPositions = new Number[2* solarSystem.length][2];

        double[] radius = new double[solarSystem.length]; //radius of each body, indexes must match that of solarSystem
        int index = 0;
        for (CelestialBody b : solarSystem) { // for all bodies in the solar system
            try {
                //find current planet positions
                planetPositions[index][0] = mtoAU(findPlanetPosition(b))[0];
                planetPositions[index][1] = mtoAU(findPlanetPosition(b))[1];
                radius[index] = Math.sqrt(Math.pow((double) planetPositions[index][0],2) + Math.pow((double) planetPositions[index][1],2)); //find radius of orbit using x y position of planet
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
        XYSeries Jupiter = drawPlanet(0.000477895, planetPositions[5]);
        XYSeries Saturn = drawPlanet(0.000402866697 ,planetPositions[6]);
        XYSeries Uranus = drawPlanet(0.000170851362, planetPositions[7]);
        XYSeries Neptune = drawPlanet(0.000165537115, planetPositions[8]);

        //make orbit lines rounded
        formatOrbits.setInterpolationParams(new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        //for all radius of each bodies orbit
        for (double b : radius) {
            XYSeries orbits = drawAverageOrbit(b); //get XYSeries of the orbit ( assume circular and sun centered );
            plot.addSeries(orbits, formatOrbits); //plot orbits
        }

        // plot body locations
        plot.addSeries(Sol, formatSol);
        plot.addSeries(Mercury, formatMercury);
        plot.addSeries(Venus, formatVenus);
        plot.addSeries(Earth, formatEarth);
        plot.addSeries(Mars, formatMars);
        plot.addSeries(Jupiter, formatJupiter);
        plot.addSeries(Saturn, formatSaturn);
        plot.addSeries(Uranus, formatUranus);
        plot.addSeries(Neptune, formatNeptune);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     //Creates action bar in the MapsActivity UI

        MenuInflater item = getMenuInflater();     //Specifies the item fit
        item.inflate(R.menu.helio_actionbar, menu);   //Fits the menu to the item fit
        setTitle("The Solar System");                  //Sets the action bar title
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //Called when icon in the action bar is selected
        ConstraintLayout planetInfo = findViewById(R.id.planets);
        if (planetInfo.getVisibility() == View.INVISIBLE) {
            planetInfo.setVisibility(View.VISIBLE);
        } else {
            planetInfo.setVisibility(View.INVISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

        //Operation for swapping buttons for planet info
    private void clickPlanet(CelestialBody body, String name) throws OrekitException {
       //setting relevant visibilities
        LinearLayout buttons = findViewById(R.id.planetButtons);
        buttons.setVisibility(View.GONE);
        RelativeLayout info = findViewById(R.id.PlanetInfo);
        info.setVisibility(View.VISIBLE);
        TextView pos = findViewById(R.id.PlanetPositionValue);
        TextView vel = findViewById(R.id.PlanetVelocityValue);
        TextView n = findViewById(R.id.PlanetName);
        ImageView mercury = findViewById(R.id.mercury_icon);
        ImageView venus = findViewById(R.id.venus_icon);
        ImageView earth = findViewById(R.id.earth_icon);
        ImageView mars = findViewById(R.id.mars_icon);
        ImageView jupiter = findViewById(R.id.jupiter_icon);
        ImageView saturn = findViewById(R.id.saturn_icon);
        ImageView uranus = findViewById(R.id.uranus_icon);
        ImageView neptune = findViewById(R.id.neptune_icon);
        mercury.setVisibility(View.INVISIBLE);
        venus.setVisibility(View.INVISIBLE);
        earth.setVisibility(View.INVISIBLE);
        mars.setVisibility(View.INVISIBLE);
        jupiter.setVisibility(View.INVISIBLE);
        saturn.setVisibility(View.INVISIBLE);
        uranus.setVisibility(View.INVISIBLE);
        neptune.setVisibility(View.INVISIBLE);

        //setting name textView
        switch(name){
            case "Mercury": mercury.setVisibility(View.VISIBLE);
                break;
            case "Venus": venus.setVisibility(View.VISIBLE);
                break;
            case "Earth": earth.setVisibility(View.VISIBLE);
                break;
            case "Mars": mars.setVisibility(View.VISIBLE);
                break;
            case "Jupiter": jupiter.setVisibility(View.VISIBLE);
                break;
            case "Saturn": saturn.setVisibility(View.VISIBLE);
                break;
            case "Uranus": uranus.setVisibility(View.VISIBLE);
                break;
            case "Neptune": neptune.setVisibility(View.VISIBLE);
                break;
        }

        //Calculating planet information
        double[] position = findPlanetPosition(body);
        double radius = Math.sqrt(Math.pow(mtoAU(position)[0],2) + Math.pow(mtoAU(position)[1],2));
        double velocity = getPlanetVelocity(body) * 3.6;

        //set textViews
        pos.setText(String.format("%.2f", radius) + "AU");
        vel.setText(String.format("%.2f", velocity) + "km/h");
        n.setText(name);
    }

    //Sets relevant visibilities when back buttons is pushed
    public void clickBack(View view) {
        LinearLayout buttons = findViewById(R.id.planetButtons);
        buttons.setVisibility(View.VISIBLE);
        RelativeLayout info = findViewById(R.id.PlanetInfo);
        info.setVisibility(View.INVISIBLE);
        ImageView mercury = findViewById(R.id.mercury_icon);
        ImageView venus = findViewById(R.id.venus_icon);
        ImageView earth = findViewById(R.id.earth_icon);
        ImageView mars = findViewById(R.id.mars_icon);
        ImageView jupiter = findViewById(R.id.jupiter_icon);
        ImageView saturn = findViewById(R.id.saturn_icon);
        ImageView uranus = findViewById(R.id.uranus_icon);
        ImageView neptune = findViewById(R.id.neptune_icon);
        mercury.setVisibility(View.INVISIBLE);
        venus.setVisibility(View.INVISIBLE);
        earth.setVisibility(View.INVISIBLE);
        mars.setVisibility(View.INVISIBLE);
        jupiter.setVisibility(View.INVISIBLE);
        saturn.setVisibility(View.INVISIBLE);
        uranus.setVisibility(View.INVISIBLE);
        neptune.setVisibility(View.INVISIBLE);
    }

    //methods for each planet button that feed corresponding CelestialBodies and names into clickPlanet
    public void clickMercury(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getMercury(), "Mercury");}
    public void clickVenus(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getVenus(), "Venus");}
    public void clickEarth(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getEarth(), "Earth");}
    public void clickMars(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getMars(), "Mars");}
    public void clickJupiter(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getJupiter(), "Jupiter");}
    public void clickSaturn(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getSaturn(), "Saturn");}
    public void clickUranus(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getUranus(), "Uranus");}
    public void clickNeptune(View view) throws OrekitException {clickPlanet(CelestialBodyFactory.getNeptune(), "Neptune");}

    //converts meters to AU
    public static double[] mtoAU(double[] values){
        double[] result = new double[values.length];
        for (int i = 0 ; i < values.length ; i++){
            result[i] = values[i]*6.68459*Math.pow(10, -12);
        }
        return result;
    }
}
