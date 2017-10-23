package com.appdoptame.appdoptame.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.appdoptame.appdoptame.model.Profile;
import com.appdoptame.appdoptame.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by jufarangoma on 17/09/17.
 */

public class PostFragment extends Fragment {

    private static final int RC_PHOTO_PICKER = 2;

    private String user;
    private ImageButton mPhoto;
    private EditText description;
    private EditText age;
    private EditText name;
    private EditText location;
    private Button sendButton;
    private Button femaleButton;
    private Button maleButton;
    private EditText breed;
    private String genre;
    private ArrayList<String> photos;
    private List<Image> images;
    private String cardPhoto;
    private int count;
    //Firebase
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    public PostFragment() {
        // Required empty public constructor
    }


    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.upload_post, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mPhoto = (ImageButton) view.findViewById(R.id.ib_photo);
        description = (EditText) view.findViewById(R.id.et_description);
        age = (EditText) view.findViewById(R.id.et_age);
        name = (EditText) view.findViewById(R.id.et_name);
        sendButton = (Button) view.findViewById(R.id.btn_send);
        maleButton = (Button) view.findViewById(R.id.btn_male);
        femaleButton = (Button) view.findViewById(R.id.btn_female);
        location = (EditText) view.findViewById(R.id.et_location);
        breed = (EditText) view.findViewById(R.id.et_breed);

        photos = new ArrayList<>();
        //Firebase inicialization
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("posts");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("animals_photos");
        sendButton.setEnabled(false);


        femaleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                genre = "Female";
            }
        });

        maleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                genre = "Male";
            }
        });

        // TODO conecction between front and back
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                while(count<=images.size()){
//                    Log.d("count", String.valueOf(count));
//                }

                cardPhoto = photos.get(0);
                Log.d("photo", String.valueOf(photos.size()));

//                Profile profile = new Profile(user, name.getText().toString(), genre , age.getText().toString(), photos, location.getText().toString(), breed.getText().toString(), description.getText().toString(),cardPhoto);
//                databaseReference.push().setValue(profile);
                // Clear input box
                InputMethodManager inputManager =
                        (InputMethodManager) getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                removeFragment();

            }

        });


        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Puedes subir máximo 5 fotos!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(),AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 5); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                if (profile != null)
                    SwipeFragment.profileList.add(profile);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);




        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            count=images.size();
            photos = new ArrayList<String>();
            for (int i = 0; i < images.size(); i++) {
                Uri selectedImage = Uri.fromFile(new File(images.get(i).path));
                final StorageReference photoRef = storageReference.child(selectedImage.getLastPathSegment());
                photoRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri download = taskSnapshot.getDownloadUrl();
                        photos.add(download.toString());
                        count--;
                        String str = "Faltan " + String.valueOf(count) + " Fotos por subir.";
                        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                        if(count==0) sendButton.setEnabled(true);
                    }

                });
            }
        }

//        super.onActivityResult(requestCode, resultCode, data);
//        Uri selectedImage = data.getData();
//        final StorageReference photoRef = storageReference.child(selectedImage.getLastPathSegment());
//        photoRef.putFile(selectedImage).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Uri download = taskSnapshot.getDownloadUrl();
//                photoUrl = download.toString();
//            }
//        });
    }

    private void removeFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this).commit();
    }
}