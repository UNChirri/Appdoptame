package com.appdoptame.appdoptame.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdoptame.appdoptame.Base.BaseActivity;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.adapter.ProfileClickAdapter;
import com.appdoptame.appdoptame.model.Profile;
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by crow on 6/11/17.
 */

public class UserProfileFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public static List<Profile> profileList;
    View view;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_userprofile, container, false);
        ImageView profilepic = (ImageView) view.findViewById(R.id.profilepic);

        try {
            Bitmap returned_bitmap = new convertUrlToBitmap().execute().get();
            profilepic.setImageBitmap(returned_bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        TextView profileName = (TextView) view.findViewById(R.id.profilename);
        profileName.setText(user.getDisplayName());
        profileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_profile,0,0,0);

        TextView profileEmail = (TextView) view.findViewById(R.id.profile_email);
        profileEmail.setText(user.getEmail());
        profileEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email,0,0,0);

        TextView profileBannerPets = (TextView) view.findViewById(R.id.profile_pets);
        profileBannerPets.setText(R.string.banner_profile_pets);
        profileBannerPets.setCompoundDrawablesWithIntrinsicBounds(R.drawable.banner_profile_pets,0,0,0);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("posts");
        profileList = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Profile profile = child.getValue(Profile.class);
                    if(profile.getUser() != null) {
                        if (profile.getUser().equals(user.getUid())) {
                            profileList.add(profile);
                        }
                    }
                }
                createRecycler((ArrayList<Profile>) profileList);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }

    private void createRecycler(ArrayList<Profile> list){
        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.recyclerPets);
        //SpannedGridLayoutManager spannedGridLayoutManager = new SpannedGridLayoutManager(
         //       SpannedGridLayoutManager.Orientation.VERTICAL, 4);
        //recyclerview.setLayoutManager(spannedGridLayoutManager);
        recyclerview.setLayoutManager(new LinearLayoutManager(
                getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        ProfileClickAdapter adapter = new ProfileClickAdapter(list);
        adapter.setOnEntryClickListener(new ProfileClickAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {

            }
        });
        recyclerview.setAdapter(adapter);
    }

    private class convertUrlToBitmap extends AsyncTask<Void,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap myBitmap = null;
            try {
                URL url = new URL(user.getPhotoUrl().toString());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

            }catch(Exception e){
                e.printStackTrace();
            }
            return myBitmap;
        }
    }

}
