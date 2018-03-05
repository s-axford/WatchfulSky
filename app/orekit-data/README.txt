This zip file contains some data files useful for
use with the orekit library (http://www.orekit.org/).

In order to use this file, simply unzip it anywhere you
want, note the path of the orekit-data folder that will
be created and add the following lines at the start of
your program:

  File orekitData = new File("/path/to/the/folder/orekit-data");
  DataProvidersManager manager = DataProvidersManager.getInstance();
  manager.addProvider(new DirectoryCrawler(orekitData));

This zip file contains JPL DE 430 ephemerides from 1990
to 2069, IERS Earth orientation parameters from 1973
to November 2017 with predicted date to end 2017 (both IAU-1980
and IAU-2000), UTC-TAI history from 1972 to mid of 2017,
Marshall Solar Activity Futur Estimation from 1999 to fall 2017,
the Eigen 06S gravity field and the FES 2004 ocean tides model.
