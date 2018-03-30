package ca.mun.engi5895.stargazer.OrekitDataInstallation;

/**
 * Created by Spencer on 3/8/2018.
 */

import java.io.File;

import org.orekit.data.DataProvider;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;

public class OrekitInit {

    public static void init(File root) {

        final DataProvidersManager providers_manager = DataProvidersManager.getInstance();
        providers_manager.clearProviders();

        if (providers_manager.getProviders().size() == 0) {

            DataProvider provider;
            try {
                provider = new DirectoryCrawler(root);
                providers_manager.addProvider(provider);
            } catch (OrekitException e) {
                e.printStackTrace();
            }

        }
    }
}