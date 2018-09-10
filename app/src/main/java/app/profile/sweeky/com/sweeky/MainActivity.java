package app.profile.sweeky.com.sweeky;

import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import app.profile.sweeky.com.sweeky.Fragments.DiscoverFragment;
import app.profile.sweeky.com.sweeky.Fragments.PhotoFragment;
import app.profile.sweeky.com.sweeky.Fragments.MyProfileFragment;
import de.hdodenhof.circleimageview.CircleImageView;

/*
 * Welcome to Sweeky Profile App Project
 *
 * Purpose of this app is allowing users to create their
 * photo collections as a profile
 *
 */

public class MainActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private RecyclerView userPhotosRecyclerView;
    private FrameLayout frameLayout;
    private CircleImageView circularProfilePictureImageView;

    //Variable
    private String uid;

    //Firebase reference
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    //Fragment
    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enabling activity transition animation for SDK above LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setExitTransition(new Explode());
        }
        setContentView(R.layout.activity_main);

        //Initializing views
        userNameTextView = findViewById(R.id.layoutUserNameTextView);
        userPhotosRecyclerView = findViewById(R.id.userPhotosRecyclerView);
        circularProfilePictureImageView = findViewById(R.id.circularProfilePictureImageView);

        //Getting user id
        uid = FirebaseAuth.getInstance().getUid();

        //Tabs
        ViewPager myViewPager = findViewById(R.id.mainViewPager);
        pagerAdapter = new myPagerAdapter(getSupportFragmentManager());

        myViewPager.setAdapter(pagerAdapter);

        //Tab Position
        myViewPager.setCurrentItem(1);


    }

    //For Sweepable Fragments
    public static class myPagerAdapter extends FragmentPagerAdapter {
        public myPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch (i)
            {
                case 0:
                    return DiscoverFragment.newInstance();
                case 1:
                    return PhotoFragment.newInstance();
                case 2:
                    return MyProfileFragment.newInstance();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


//        firestore.collection("Users")
//                .document(uid)
//                .collection("Data")
//                .document("user_photos")
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
////                        String photoUrl = documentSnapshot.getString("profile_photo");
////
////                        Picasso.get().load(photoUrl).into(circularProfilePictureImageView);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });

//        DocumentReference documentReference = firestore
//                .collection("Users")
//                .document(uid)
//                .collection("Data")
//                .document("user_photos");
//
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                String value = documentSnapshot.getString("thump");
//
//                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                Log.d("log", value);
//
////                Picasso.get().load(value).into(circularProfilePictureImageView);
//            }
//        });

    }
}