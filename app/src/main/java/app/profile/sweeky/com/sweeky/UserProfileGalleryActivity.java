package app.profile.sweeky.com.sweeky;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
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

public class UserProfileGalleryActivity extends Activity {

    //Views
    private ImageView profilePictureImageView;
    private TextView userNameTextView;
    private RecyclerView userPhotosRecyclerView;
    private FrameLayout frameLayout;

    //Variables
    private static String LOG_TAG = "TAG";
    private String userName;
    private String photoUrl;
    private static int NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
    private static int RECYCLERVIEW_GRID_VIEW_HEIGHT = 0;
    private static int RECYCLERVIEW_GRID_VIEW_WIDTH = 0;
    private static int LARGE_DISPLAY_WIDTH = 1500;
    private static int SMALL_DISPLAY_WIDTH = 800;
    private static int NUMBER_OF_PROFILES_TO_SHOW = 10;
    private int displayWidth = 0;

    /*DisplayMatrix*/
    private DisplayMetrics displayMetrics;

    //Database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enabling activity transition animation for SDK above LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
        }

        setContentView(R.layout.activity_user_profile_gallery);

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

        //Initializing views
        profilePictureImageView = findViewById(R.id.profilePictureImageView);
        userNameTextView = findViewById(R.id.layoutUserNameTextView);
        userPhotosRecyclerView = findViewById(R.id.userPhotosRecyclerView);

        //Getting shared data
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName");
        photoUrl = bundle.getString("photoUrl");
        Log.d(LOG_TAG, "User name is: " + userName);
        Log.d(LOG_TAG, "Photo url is: " + photoUrl);


        //Initializing firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Setting RecyclerView properties
        userPhotosRecyclerView.setHasFixedSize(true);
        userPhotosRecyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW));

        //Setting username and profile photo
        userNameTextView.setText(userName);
        Picasso.get().load(photoUrl).into(profilePictureImageView);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO: The query is loading sample data for testing [NEED TO CHANGE FOR PARTICULAR USERS]
        //Getting child list of the firebase reference
        Query query = databaseReference.child("Images").child("gallery_samples");
        Log.d(LOG_TAG, "Query is: " + query);

        //Creating Adapter
        FirebaseRecyclerAdapter<Profiles, UserProfileGalleryActivity.ViewHolder> adapter;
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
                Picasso.get().load(model.getPhotoUrl()).into(viewHolder.photosImageViewVH);
            }
        };

        //Setting the adapter to RecyclerView
        userPhotosRecyclerView.setAdapter(adapter);

    }

    //ViewHolder class for firebase RecyclerView adapter
    public static class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frameLayout;
        ImageView photosImageViewVH;
        TextView userNameTextViewVH;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photosImageViewVH = itemView.findViewById(R.id.profileImageView);
            userNameTextViewVH = itemView.findViewById(R.id.layoutUserNameTextView);
            frameLayout = itemView.findViewById(R.id.frameLayout);
            userNameTextViewVH.setVisibility(View.GONE);
        }
    }
}
