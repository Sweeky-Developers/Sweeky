package app.profile.sweeky.com.sweeky;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import app.profile.sweeky.com.sweeky.Data.Profiles;

/*
* Welcome to Sweeky Profile App Project
*
* Propose of this app is allowing users to create their
* photo collections as a profile
*
*/

public class MainActivity extends AppCompatActivity {

    //Views
    private RecyclerView userProfileListRecyclerView;

    //Variables
    private static String LOG_TAG = "TAG";
    private static int NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
    private static int RECYCLERVIEW_GRID_VIEW_HEIGHT = 0;
    private static int RECYCLERVIEW_GRID_VIEW_WIDTH = 0;
    private static int LARGE_DISPLAY_WIDTH = 1500;
    private static int SMALL_DISPLAY_WIDTH = 800;
    private static int NUMBER_OF_PROFILES_TO_SHOW = 10;
    private int displayWidth = 0;

    /*DisplayMatrix*/
    private DisplayMetrics displayMetrics;

    //Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //[START OF DISPLAY WIDTH CALCULATION]

        /*Initializing of DisplayMetrix
        * DisplayMetrix can be used to get the display height & width
        * With that height and width we can calculate how much width
        * we want to give to each views in RecyclerView*/
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //Assigning height to variable
        displayWidth = displayMetrics.widthPixels;
        Log.d(LOG_TAG, "Display width is " + displayWidth);

        //Checking display width and deciding number of columns to show in RecyclerView
        if (displayWidth>=LARGE_DISPLAY_WIDTH) {
            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 4;
        } else if (displayWidth<=SMALL_DISPLAY_WIDTH) {
            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 2;
        } else {
            /*This else part is for resetting number
            * of columns to show when returning from
            * large or small screen.
            * Example: When screen user rotate the device
            * to land scape and again return back to portrait
            * this else statement will set value to default*/
            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
        }

        //Calculating
        RECYCLERVIEW_GRID_VIEW_WIDTH = displayWidth/NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW;

        //[END OF DISPLAY WIDTH CALCULATION]



        //Initialization of views
        userProfileListRecyclerView = findViewById(R.id.userProfileListRecyclerView);

        //Initializing Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Setting RecyclerView properties
        userProfileListRecyclerView.setHasFixedSize(true);
        userProfileListRecyclerView.setLayoutManager(new GridLayoutManager(
                this, NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW));

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Getting child of the firebase reference
        Query query = databaseReference.child("Profiles");

        //Creating Adapter
        FirebaseRecyclerAdapter<Profiles, MainActivity.ViewHolder> adapter;
        adapter = new FirebaseRecyclerAdapter<Profiles, ViewHolder>(
                Profiles.class,
                R.layout.layout_profile_view,
                ViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Profiles model, int position) {
                viewHolder.frameLayout.setLayoutParams(
                        new FrameLayout.LayoutParams(RECYCLERVIEW_GRID_VIEW_WIDTH, RECYCLERVIEW_GRID_VIEW_WIDTH));
                viewHolder.userNameTextView.setText(model.getUserName());
                Picasso.get().load(model.getPhotoUrl()).into(viewHolder.profileImageView);
            }
        };

        //Setting the adapter to RecyclerView
        userProfileListRecyclerView.setAdapter(adapter);

    }


    //ViewHolder class for firebase RecyclerView adapter
    public static class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frameLayout;
        TextView userNameTextView;
        ImageView profileImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            frameLayout = itemView.findViewById(R.id.frameLayout);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
        }
    }
}
