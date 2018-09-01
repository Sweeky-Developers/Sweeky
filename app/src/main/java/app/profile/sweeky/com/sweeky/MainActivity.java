package app.profile.sweeky.com.sweeky;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import app.profile.sweeky.com.sweeky.Data.Profiles;
import app.profile.sweeky.com.sweeky.Util.DisplayUtilities;

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

    /*Display Utilities*/
    private DisplayUtilities utilities;

    //Display Matrix
    private DisplayMetrics displayMetrics;

    //Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enabling activity transition animation for SDK above LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setExitTransition(new Explode());
        }

        setContentView(R.layout.activity_main);


//        //[START OF DISPLAY WIDTH CALCULATION]
//
//        /*Initializing of DisplayMetrix
//        * DisplayMetrix can be used to get the display height & width
//        * With that height and width we can calculate how much width
//        * we want to give to each views in RecyclerView*/
//        displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//        //Assigning height to variable
//        displayWidth = displayMetrics.widthPixels;
//        Log.d(LOG_TAG, "Display width is " + displayWidth);
//
//        //Checking display width and deciding number of columns to show in RecyclerView
//        if (displayWidth>=LARGE_DISPLAY_WIDTH) {
//            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 4;
//        } else if (displayWidth<=SMALL_DISPLAY_WIDTH) {
//            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 2;
//        } else {
//            /*This else part is for resetting number
//            * of columns to show when returning from
//            * large or small screen.
//            * Example: When screen user rotate the device
//            * to land scape and again return back to portrait
//            * this else statement will set value to default*/
//            NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
//        }
//
//        //Calculating
//        RECYCLERVIEW_GRID_VIEW_WIDTH = displayWidth/NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW;
//
//        //[END OF DISPLAY WIDTH CALCULATION]

        //TODO: Shifting display calculations to seperate class process
        utilities = new DisplayUtilities();
        RECYCLERVIEW_GRID_VIEW_WIDTH = utilities.recyclerViewColumnWidthDecider();
        utilities.recalculateRecyclerViewSize();
        RECYCLERVIEW_GRID_VIEW_WIDTH = utilities.recyclerViewColumnWidthDecider();
        Log.d(LOG_TAG, "WIDTH IS: " + RECYCLERVIEW_GRID_VIEW_WIDTH);

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

        //Getting child list of the firebase reference
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
            protected void populateViewHolder(ViewHolder viewHolder, final Profiles model, final int position) {
                viewHolder.frameLayout.setLayoutParams(
                        new FrameLayout.LayoutParams(RECYCLERVIEW_GRID_VIEW_WIDTH, RECYCLERVIEW_GRID_VIEW_WIDTH));
                viewHolder.userNameTextView.setText(model.getUserName());
                Picasso.get().load(model.getPhotoUrl()).into(viewHolder.profileImageView);

                //Setting onClickListner for each views
                viewHolder.frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, "Clicked " + position + " | Name " + model.getUserName());

                        /*Activity Transition Animation
                        * Only Lollipop and above versions support
                        * activity transition animation. So we need to check
                        * the version of device before using animation*/
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                            intent.putExtra("userName", model.getUserName());
                            intent.putExtra("photoUrl", model.getPhotoUrl());
                            startActivity(intent, ActivityOptions
                                    .makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        } else {
                            //If older version, use normal activity transition
                            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                            startActivity(intent);
                        }

                    }
                });
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
            userNameTextView = itemView.findViewById(R.id.layoutUserNameTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
        }
    }
}
