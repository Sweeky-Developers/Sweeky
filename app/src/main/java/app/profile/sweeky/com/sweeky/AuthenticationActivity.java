package app.profile.sweeky.com.sweeky;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.profile.sweeky.com.sweeky.Const.App;
import app.profile.sweeky.com.sweeky.Data.UserDetails;
import de.hdodenhof.circleimageview.CircleImageView;

public class AuthenticationActivity extends AppCompatActivity {


    //Views
    private CircleImageView profilePictureImageView;
    private EditText userNameEditText;
    private EditText userStatusEditText;
    private Button startButton;

    //Variables
    private static final String TAG = "AuthenticationActivity";
    private int RC_SIGN_IN = 1;
    private static String user;
    private String photoUrl = "https://homepages.cae.wisc.edu/~ece533/images/arctichare.png";

    //Firebase
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;

    //Intent
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Log.d(TAG, "onCreate: OnCreate inside");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        //[AUTH UI START]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());


        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_launcher)
                        .build(),
                RC_SIGN_IN);

        //[AUTH UI END]


        //Initializing views
        profilePictureImageView = findViewById(R.id.profilePictureImageView);
        userNameEditText = findViewById(R.id.userNameEditText);
        userStatusEditText = findViewById(R.id.userStatusEditText);
        startButton = findViewById(R.id.startButton);


        //Setting OnClickListener
        startButton.setOnClickListener(listener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: User id = " + user);

                // Successfully signed in
//                startActivity(new Intent(this, MainActivity.class));

                //Getting user ID
                user = FirebaseAuth.getInstance().getUid();

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.d(TAG, "onActivityResult: Firebase signin failed");
            }
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(AuthenticationActivity.this, MainActivity.class);
            Map<String, String> data = new HashMap<>();
            Map<String, String> photo = new HashMap<String, String>();
            UserDetails userDetails = new UserDetails();

            userDetails.setPhoto(photoUrl);
            userDetails.setUserName(userNameEditText.getText().toString());
            userDetails.setStatus(userStatusEditText.getText().toString());
            userDetails.setUserId(user);

            intent.putExtra(App.INTENT_USER_DETAILS, userDetails);

            data.put("user_name", userNameEditText.getText().toString());
            data.put("user_status", userStatusEditText.getText().toString());
            data.put("user_id", user);

            photo.put("thump", photoUrl);
            photo.put("profile_picture", photoUrl);

            Map<String, Object> user_details = new HashMap<String, Object>();

            user_details.put("/Data/user_details", data);
            user_details.put("/Data/user_photos", photo);

            //User details reference for batch writing
            DocumentReference details_reference = firestore
                    .collection("Users")
                    .document(user)
                    .collection("Data")
                    .document("user_details");

            //User profile photo reference for batch writing
            DocumentReference photo_reference = firestore
                    .collection("Users")
                    .document(user)
                    .collection("Data")
                    .document("user_photos");

            //Creating batch
            WriteBatch batch = firestore.batch();

            batch.set(details_reference, data);
            batch.set(photo_reference, photo);

            batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Batch written successfully
                    Toast.makeText(AuthenticationActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });




        }
    };


}
