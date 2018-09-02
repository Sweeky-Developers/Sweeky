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
import app.profile.sweeky.com.sweeky.Fragments.UserProfileGalleryFragment;
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
    private static int RECYCLERVIEW_GRID_VIEW_SIZE = 0;

    //Display utility class
    private DisplayUtilities utilities;

    //Firebase
    private DatabaseReference databaseReference;

    //Bundle
    private Bundle dataToFragment;

    //Fragment reference
    private UserProfileGalleryFragment userProfileGalleryFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enabling activity transition animation for SDK above LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setExitTransition(new Explode());
        }
        setContentView(R.layout.activity_main);


        //Checking display width and deciding number of columns to show in RecyclerView
        utilities = new DisplayUtilities();

        RECYCLERVIEW_GRID_VIEW_SIZE = utilities.recyclerViewColumnWidthDecider();
        NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = utilities.recalculateRecyclerViewSize(utilities.getDisplayWidth());


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
                        new FrameLayout.LayoutParams(RECYCLERVIEW_GRID_VIEW_SIZE, RECYCLERVIEW_GRID_VIEW_SIZE));
                viewHolder.userNameTextView.setText(model.getUserName());
                Picasso.get().load(model.getPhotoUrl()).into(viewHolder.profileImageView);

                //Setting onClickListner for each views
                viewHolder.frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        /*Activity Transition Animation
                        * Only Lollipop and above versions support
                        * activity transition animation. So we need to check
                        * the version of device before using animation*/

                        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                        intent.putExtra("userName", model.getUserName());
                        intent.putExtra("photoUrl", model.getPhotoUrl());

                        dataToFragment = new Bundle();

                        dataToFragment.putString("userName", model.getUserName());
                        dataToFragment.putString("photoUrl", model.getPhotoUrl());

                        userProfileGalleryFragment = new UserProfileGalleryFragment();

                        userProfileGalleryFragment.setArguments(dataToFragment);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent, ActivityOptions
                                    .makeSceneTransitionAnimation(MainActivity.this).toBundle());
                        } else {
                            //If older version, use normal activity transition
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
