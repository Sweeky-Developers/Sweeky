package app.profile.sweeky.com.sweeky.Util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DisplayUtilities {

    //Variables
    int NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
    int LARGE_DISPLAY_WIDTH = 1500;
    int SMALL_DISPLAY_WIDTH = 800;
    int displayWidth = 0;

    //Constructors
    public DisplayUtilities() {}

    public DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public int getDisplayHight() {
        return getDisplayMetrics().heightPixels;
    }

    public int getDisplayWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public int recyclerViewColumnWidthDecider() {

        //Assigning height to variable
        displayWidth = getDisplayWidth();

        //Checking display width and deciding number of columns to show in RecyclerView
        if (displayWidth>=LARGE_DISPLAY_WIDTH) {
            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 4;
        } else if (displayWidth<=SMALL_DISPLAY_WIDTH) {
            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 2;
        } else {
            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
        }

        recalculateRecyclerViewSize();

        return displayWidth/NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW;
    }

    public void recalculateRecyclerViewSize() {
        if (displayWidth>=LARGE_DISPLAY_WIDTH) {
            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 4;
        } else if (displayWidth<=SMALL_DISPLAY_WIDTH) {
            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 2;
        } else {
            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
        }

    }

}
