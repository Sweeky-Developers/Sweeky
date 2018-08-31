package app.profile.sweeky.com.sweeky;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import app.profile.sweeky.com.sweeky.Data.Profiles;

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
    private static int NUMBER_OF_COLUMNS_FOR_RECYCLER_VIEW = 3;
    private static int NUMBER_OF_PROFILES_TO_SHOW = 10;

    //Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //Getting child of the firebase reference
        Query query = databaseReference.child("Profiles").limitToFirst(NUMBER_OF_PROFILES_TO_SHOW);

        //Creating Adapter
        FirebaseRecyclerAdapter<Profiles, MainActivity.ViewHolder> adapter;
        adapter = new FirebaseRecyclerAdapter<Profiles, ViewHolder>(
                Profiles.class,
                R.layout.layout_profile_view,
                ViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Profiles model, int position) {
                viewHolder.userNameTextView.setText(model.getUserName());
                Picasso.get().load(model.getPhotoUrl()).into(viewHolder.profileImageView);
            }
        };

        //Setting the adapter to RecyclerView
        userProfileListRecyclerView.setAdapter(adapter);

    }


    //ViewHolder class for firebase RecyclerView adapter
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView;
        ImageView profileImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
        }
    }
}
