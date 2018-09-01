package app.profile.sweeky.com.sweeky.Util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/*
*
* DISPLAY UTILITY CLASS
* This class is for does these things:
* 1. Get current devices display width and height
* 2. Calculate width required for RecyclerView items
*    to properly fit into the display
* 3. Decides how many columns are required for different
*    screen sizes
*
*/

public class DisplayUtilities {

    //Variables
    private int NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;

    //Constructors
    public DisplayUtilities() {}

    private DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public int getDisplayHight() {
        return getDisplayMetrics().heightPixels;
    }

    public int getDisplayWidth() {
        return getDisplayMetrics().widthPixels;
    }

    //This method decides how many columns to show corresponding to display width
    public int recyclerViewColumnWidthDecider() {
        //Assigning height to variable
        int displayWidth = getDisplayWidth();

        recalculateRecyclerViewSize(displayWidth);

        return displayWidth /NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW;
    }

    //Checking display width and deciding number of columns to show in RecyclerView
    public int recalculateRecyclerViewSize(int displayWidth) {
        int LARGE_DISPLAY_WIDTH = 1500;
        int SMALL_DISPLAY_WIDTH = 800;

        if (displayWidth>= LARGE_DISPLAY_WIDTH) {
            return NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 4;
        } else if (displayWidth<= SMALL_DISPLAY_WIDTH) {
            return NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 2;
        } else {
            return NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
        }

    }

    //recalculateRecyclerViewSize() method with cusom screen size limits
    public int recalculateRecyclerViewSize(int displayWidth, int LARGE_Display_SIZE, int SMALL_Display_SIZE) {

        if (displayWidth>= LARGE_Display_SIZE) {
            return NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 4;
        } else if (displayWidth<= SMALL_Display_SIZE) {
            return NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 2;
        } else {
            return NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
        }

    }

}
