package ca.mun.engi5895.stargazer;

import android.app.Application;

/**
 * Created by john on 2018-03-29.
 */

public class GlobalClass extends Application {

    public Favorites favorites = new Favorites(getApplicationContext());

    public Favorites getGlobalFavorites() {

        return favorites;
    }
}
