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
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends Activity {

    //Views
    private ImageView profilePhotoImageView;
    private ImageView starImageView;
    private TextView fragmentUserNameTextView;
    private TextView layoutUserNameTextView;
    private LikeButton likeButton;
    private CoordinatorLayout coordinatorLayout;
    private CircleImageView circularProfilePictureImageView;

    //Variables
    private static String LOG_TAG = "TAG";
    private String userName;
    private String photoUrl;

    //MediaPlayer
    private MediaPlayer mediaPlayer;

    //Object Animator
    ObjectAnimator objectAnimatorTextView;
    ObjectAnimator objectAnimatorTextViewReverse;
    ObjectAnimator objectAnimatorImageView;
    ObjectAnimator objectAnimatorImageViewReverse;

    //Bundle
    private Bundle recievData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enabling activity transition animation for SDK above LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setEnterTransition(new Fade());
        }

        setContentView(R.layout.activity_user_profile);

        Log.d("FRAGLOG", "Inside OnCreate of Activity");

        //Getting shared values
        recievData = getIntent().getExtras();
        userName = recievData.getString("userName");
        photoUrl = recievData.getString("photoUrl");

        Log.d(LOG_TAG, "Name: " + userName);
        Log.d(LOG_TAG, "Photo URL: " + photoUrl);

        //Initialization of views
        profilePhotoImageView = findViewById(R.id.profilePhotoImageView);
        likeButton = findViewById(R.id.star_button);
        fragmentUserNameTextView = findViewById(R.id.layoutUserNameTextView);
        circularProfilePictureImageView = findViewById(R.id.circularProfilePictureImageView);

        //Initializing object animators
        objectAnimatorTextView = ObjectAnimator.ofFloat(fragmentUserNameTextView, "translationY", 340f);
        objectAnimatorImageView = ObjectAnimator.ofFloat(circularProfilePictureImageView, View.ALPHA, 0, 1);
        objectAnimatorTextViewReverse = ObjectAnimator.ofFloat(fragmentUserNameTextView, "translationY", 0f);
        objectAnimatorImageViewReverse = ObjectAnimator.ofFloat(circularProfilePictureImageView, View.ALPHA, 1, 0);

        objectAnimatorTextView.setDuration(200);
        objectAnimatorImageView.setDuration(1000);
        objectAnimatorTextViewReverse.setDuration(100);
        objectAnimatorImageViewReverse.setDuration(1000);

        //Loading photo to image view
        Picasso.get().load(photoUrl).into(profilePhotoImageView);

        //Loading user name and photo to fragment
        Picasso.get().load(photoUrl).into(circularProfilePictureImageView);
        fragmentUserNameTextView.setText(userName);

        //Initializing MediaPlayer
        mediaPlayer = MediaPlayer.create(UserProfileActivity.this, R.raw.sweeky_star);
        coordinatorLayout = findViewById(R.id.profileContainerCordinatorLayout);

        //Click listener for star button
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                mediaPlayer.start();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        //Bottom sheet behaviour
        View bottomSheet = coordinatorLayout.findViewById(R.id.fragmentContainerLinearLayout);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(200);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change

                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    objectAnimatorTextView.start();
                    circularProfilePictureImageView.setVisibility(View.VISIBLE);
                    objectAnimatorImageView.start();
                }

                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    objectAnimatorImageViewReverse.start();
                    circularProfilePictureImageView.setVisibility(View.GONE);
                    objectAnimatorTextViewReverse.start();
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, final float slideOffset) {
                // React to drag event

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("FRAGLOG", "Inside onStart of Activity");
    }
}
