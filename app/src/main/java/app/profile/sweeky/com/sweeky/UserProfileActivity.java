package app.profile.sweeky.com.sweeky;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends Activity {

    //Views
    private ImageView profilePhotoImageView;
    private ImageView starImageView;
    private TextView userNameTextView;
    private LikeButton likeButton;
    private CoordinatorLayout coordinatorLayout;

    //Variables
    private static String LOG_TAG = "TAG";
    private String userName;
    private String photoUrl;

    //MediaPlayer
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enabling activity transition animation for SDK above LOLLIPOP
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
//            getWindow().setEnterTransition(new Fade());
//        }

        setContentView(R.layout.activity_user_profile);

        //Getting shared values
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName");
        photoUrl = bundle.getString("photoUrl");

        Log.d(LOG_TAG, "Name: " + userName);
        Log.d(LOG_TAG, "Photo URL: " + photoUrl);

        //Initialization of views
        profilePhotoImageView = findViewById(R.id.profilePhotoImageView);
//        userNameTextView = findViewById(R.id.userNameTextView);
        likeButton = findViewById(R.id.star_button);

        //Setting username to the texView
//        userNameTextView.setText(userName);

        //Loading photo to image view
        Picasso.get().load(photoUrl).into(profilePhotoImageView);

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

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, final float slideOffset) {
                // React to drag event

            }
        });

    }
}
