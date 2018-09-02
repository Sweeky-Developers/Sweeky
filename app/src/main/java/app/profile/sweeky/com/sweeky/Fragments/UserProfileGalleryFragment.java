package app.profile.sweeky.com.sweeky.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import app.profile.sweeky.com.sweeky.Data.Profiles;
import app.profile.sweeky.com.sweeky.R;
import app.profile.sweeky.com.sweeky.UserProfileGalleryActivity;
import app.profile.sweeky.com.sweeky.Util.DisplayUtilities;

public class UserProfileGalleryFragment extends Fragment {

    //Views
    private TextView userNameTextView;
    private ImageView profilePictureImageView;
    private RecyclerView userPhotosRecyclerView;
    private FrameLayout frameLayout;

    //Variables
    private static String LOG_TAG = "TAG";
    private String userName;
    private String photoUrl;
    private static int NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
    private static int RECYCLERVIEW_GRID_VIEW_SIZE = 0;

    //Display Utilities Class
    private DisplayUtilities displayUtilities;

    //Database reference
    private DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // /Inflating view
        View view = inflater.inflate(R.layout.activity_user_profile_gallery, container, false);

        //Initializing views
        userNameTextView = view.findViewById(R.id.layoutUserNameTextView);
        profilePictureImageView = view.findViewById(R.id.profilePictureImageView);
        userPhotosRecyclerView = view.findViewById(R.id.userPhotosRecyclerView);

        //Checking display width and deciding number of columns to show in RecyclerView
        displayUtilities = new DisplayUtilities();
        RECYCLERVIEW_GRID_VIEW_SIZE = displayUtilities.recyclerViewColumnWidthDecider();
        NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = displayUtilities
                .recalculateRecyclerViewSize(displayUtilities.getDisplayWidth());

        //Initializing firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Setting RecyclerView properties
        userPhotosRecyclerView.setHasFixedSize(true);
        userPhotosRecyclerView.setLayoutManager(
                new GridLayoutManager(getActivity().getApplicationContext(),
                        NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW));

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        //TODO: The query is loading sample data for testing [NEED TO CHANGE FOR PARTICULAR USERS]
        //Getting child list of the firebase reference
        Query query = databaseReference.child("Images").child("gallery_samples");
        Log.d(LOG_TAG, "Query is: " + query);

        //Creating Adapter
        FirebaseRecyclerAdapter<Profiles, UserProfileGalleryActivity.ViewHolder> adapter;
        adapter = new FirebaseRecyclerAdapter<Profiles, UserProfileGalleryActivity.ViewHolder>(
                Profiles.class,
                R.layout.layout_profile_view,
                UserProfileGalleryActivity.ViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(UserProfileGalleryActivity.ViewHolder viewHolder, Profiles model, int position) {
                viewHolder.frameLayout.setLayoutParams(
                        new FrameLayout.LayoutParams(RECYCLERVIEW_GRID_VIEW_SIZE, RECYCLERVIEW_GRID_VIEW_SIZE));
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
