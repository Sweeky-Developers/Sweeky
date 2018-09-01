package app.profile.sweeky.com.sweeky;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class UserProfileActivity extends Activity {

    //Views
    private ImageView profilePhotoImageView;
    private ImageView starImageView;
    private TextView userNameTextView;

    //Variables
    private static String LOG_TAG = "TAG";
    private String userName;
    private String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enabling activity transition animation for SDK above LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setEnterTransition(new Fade());
        }

        setContentView(R.layout.activity_user_profile);

        //Getting shared values
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName");
        photoUrl = bundle.getString("photoUrl");

        Log.d(LOG_TAG, "Name: " + userName);
        Log.d(LOG_TAG, "Photo URL: " + photoUrl);

        //Initialization of views
        profilePhotoImageView = findViewById(R.id.profilePhotoImageView);
        userNameTextView = findViewById(R.id.userNameTextView);

        //Setting username to the texView
        userNameTextView.setText(userName);

        //Loading photo to image view
        Picasso.get().load(photoUrl).into(profilePhotoImageView);
    }
}
