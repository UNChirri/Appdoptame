package com.appdoptame.appdoptame.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.appdoptame.appdoptame.FBAuth.FBLogin;
import com.appdoptame.appdoptame.model.Profile;
import com.appdoptame.appdoptame.R;
import com.facebook.login.LoginManager;
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


/**
 * Created by jufarangoma on 17/09/17.
 */

public class PostActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 2;

    private String user;
    private ImageButton mPhoto;
    private EditText description;
    private EditText genre;
    private EditText age;
    private EditText name;
    private String photoUrl;
    private String location;
    private Button sendButton;

    //Firebase
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_post);
        savedInstanceState = getIntent().getExtras();

        user = savedInstanceState.getString("Username");
        mPhoto = (ImageButton) findViewById(R.id.ib_photo);
        description = (EditText) findViewById(R.id.et_description);
        genre = (EditText) findViewById(R.id.et_genre);
        age = (EditText) findViewById(R.id.et_age);
        name = (EditText) findViewById(R.id.et_name);
        sendButton = (Button) findViewById(R.id.btn_send);

        //Firebase inicialization
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("posts");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("animals_photos");


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                Log.d("message", photoUrl);
                Profile profile = new Profile(user.toString(), description.getText().toString(), genre.getText().toString(), age.getText().toString(), name.getText().toString(), photoUrl);
                databaseReference.push().setValue(profile);
                // Clear input box

                description.setText("");
                genre.setText("");
                age.setText("");
                name.setText("");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage = data.getData();
        final StorageReference photoRef = storageReference.child(selectedImage.getLastPathSegment());
        photoRef.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri download = taskSnapshot.getDownloadUrl();
                photoUrl = download.toString();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.new_post:
                intent = new Intent(this, TestActivity.class);
                intent.putExtra("Username", user);
                startActivity(intent);
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                try{
                    intent = new Intent(this, FBLogin.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.view_posts:
                intent = new Intent(this, SwipeActivity.class);
                intent.putExtra("Username", user);
                startActivity(intent);
                return true;
        }
        return false;
    }
}