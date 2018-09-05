package app.profile.sweeky.com.sweeky;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import app.profile.sweeky.com.sweeky.Util.DisplayUtilities;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends Activity {

    //Views
    private ImageView profilePhotoImageView;
    private ImageView starImageView;
    private TextView fragmentUserNameTextView;
    private LikeButton likeButton;
    private CoordinatorLayout coordinatorLayout;
    private CircleImageView circularProfilePictureImageView;
    private FrameLayout frameLayout;
    private CoordinatorLayout profileContainerCordinatorLayout;

    //Variables
    private static String LOG_TAG = "TAG";
    private String userName;
    private String photoUrl;
    private int PEAK_HEIGHT = 62;

    //MediaPlayer
    private MediaPlayer mediaPlayer;

    //Object Animator
    ObjectAnimator objectAnimatorTextView;
    ObjectAnimator objectAnimatorTextViewReverse;
    ObjectAnimator objectAnimatorImageView;
    ObjectAnimator objectAnimatorImageViewReverse;

    //Bundle
    private Bundle recievData;

    //Display Utility
    DisplayUtilities displayUtilities;

    //Log
    private static final String TAG = "UserProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enabling activity transition animation for SDK above LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setEnterTransition(new Fade());
        }

        setContentView(R.layout.activity_user_profile);



        //Initialization views
        profilePhotoImageView = findViewById(R.id.profilePhotoImageView);
        likeButton = findViewById(R.id.star_button);
        fragmentUserNameTextView = findViewById(R.id.layoutUserNameTextView);
        circularProfilePictureImageView = findViewById(R.id.circularProfilePictureImageView);
        frameLayout = findViewById(R.id.containerFrameLayout);
        profileContainerCordinatorLayout = findViewById(R.id.profileContainerCordinatorLayout);

        //Getting shared values
        recievData = getIntent().getExtras();
        userName = recievData.getString("userName");
        photoUrl = recievData.getString("photoUrl");

        //Loading photo to image view of Activity
        Picasso.get().load(photoUrl).into(profilePhotoImageView);

        //Loading user name and photo to fragment
        Picasso.get().load(photoUrl).into(circularProfilePictureImageView);
        fragmentUserNameTextView.setText(userName);

        //Initializing MediaPlayer
        mediaPlayer = MediaPlayer.create(UserProfileActivity.this, R.raw.sweeky_star);
        coordinatorLayout = findViewById(R.id.profileContainerCordinatorLayout);

        //Initializing Display utility
        displayUtilities = new DisplayUtilities();

        //Initializing object animators
        objectAnimatorTextView = ObjectAnimator.ofFloat(fragmentUserNameTextView, "translationY", displayUtilities.convertDpToPixel(110));
        objectAnimatorImageView = ObjectAnimator.ofFloat(circularProfilePictureImageView, View.ALPHA, 0, 1);
        objectAnimatorTextViewReverse = ObjectAnimator.ofFloat(fragmentUserNameTextView, "translationY", 0f);
        objectAnimatorImageViewReverse = ObjectAnimator.ofFloat(circularProfilePictureImageView, View.ALPHA, 1, 0);

        objectAnimatorTextView.setDuration(200);
        objectAnimatorImageView.setDuration(1000);
        objectAnimatorTextViewReverse.setDuration(100);
        objectAnimatorImageViewReverse.setDuration(1000);

        //Click listener for star button
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //Playing sound on click
                mediaPlayer.start();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        //Bottom sheet behaviour
        View bottomSheet = coordinatorLayout.findViewById(R.id.fragmentContainerLinearLayout);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);

        behavior.setPeekHeight(displayUtilities.convertDpToPixel(PEAK_HEIGHT));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change

                //STATE EXPANDED
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                    //TODO: Make sure if you want enable this in onStop
                    likeButton.setClickable(false);
                    likeButton.setEnabled(false);

                    objectAnimatorTextView.start();
                    circularProfilePictureImageView.setVisibility(View.VISIBLE);
                    objectAnimatorImageView.start();

                }

                //STATE COLLAPSED
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {

                    //TODO: Make sure if you want enable this in onStop
                    likeButton.setClickable(true);
                    likeButton.setEnabled(true);

                    objectAnimatorImageViewReverse.start();
                    circularProfilePictureImageView.setVisibility(View.GONE);
                    objectAnimatorTextViewReverse.start();
                    Log.d(TAG, "onStateChanged: STATE_COLLAPSED " + displayUtilities.convertDpToPixel(behavior.getPeekHeight()));
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, final float slideOffset) {
                // React to drag event

            }
        });

    }

}
