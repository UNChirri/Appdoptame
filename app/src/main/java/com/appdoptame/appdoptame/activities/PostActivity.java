package com.appdoptame.appdoptame.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.appdoptame.appdoptame.model.Profile;
import com.appdoptame.appdoptame.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jufarangoma on 17/09/17.
 */

public class PostActivity extends Fragment {

    private static final int RC_PHOTO_PICKER = 2;

    private String user;
    private ImageButton mPhoto;
    private EditText description;
    private EditText age;
    private EditText name;
    private String photoUrl;
    private EditText location;
    private Button sendButton;
    private Button femaleButton;
    private Button maleButton;
    private EditText breed;
    String genre;
    //Firebase
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    public PostActivity() {
        // Required empty public constructor
    }


    public static PostActivity newInstance() {
        PostActivity fragment = new PostActivity();
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

        //Firebase inicialization
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("posts");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("animals_photos");
        description.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if(s.toString().trim().length()==0){
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }
        });


        femaleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                maleButton.setEnabled(false);
                genre = "Female";
            }
        });

        maleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                femaleButton.setEnabled(false);
                genre = "Male";
            }
        });

        // TODO conecction between front and back
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Profile profile = new Profile(user, name.getText().toString(), genre , age.getText().toString(), photoUrl, location.getText().toString(), breed.getText().toString(), description.getText().toString());
                databaseReference.push().setValue(profile);
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                if (profile != null)
                    SwipeActivity.profileList.add(profile);
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
        Uri selectedImage = data.getData();
        final StorageReference photoRef = storageReference.child(selectedImage.getLastPathSegment());
        photoRef.putFile(selectedImage).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri download = taskSnapshot.getDownloadUrl();
                photoUrl = download.toString();
            }
        });

    }

    private void removeFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this).commit();
    }
}