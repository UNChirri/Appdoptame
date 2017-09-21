package com.appdoptame.appdoptame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.R.attr.data;

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
                Post post = new Post(user.toString(), description.getText().toString(), genre.getText().toString(), age.getText().toString(), name.getText().toString(), photoUrl);
                databaseReference.push().setValue(post);
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
}