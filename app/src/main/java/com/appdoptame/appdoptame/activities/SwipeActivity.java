package com.appdoptame.appdoptame.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.appdoptame.appdoptame.Auth.Login;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.model.Profile;
import com.appdoptame.appdoptame.utils.Utils;
import com.appdoptame.appdoptame.view.Card;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private String userName;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public static List<Profile> profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        //Firebase inicialization
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("posts");
        profileList = new ArrayList<>();

        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();
        savedInstanceState = getIntent().getExtras();
        userName = savedInstanceState.getString("Username");
        int bottomMargin = Utils.dpToPx(160);
        Point windowSize = Utils.getDisplaySize(getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_out_msg_view));


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Profile profile = child.getValue(Profile.class);
                    Log.d("Profile",profile.toString());
                    profileList.add(profile);
                }
                cardView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        findViewById(R.id.undoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.undoLastSwipe();
            }
        });
    }

    public void cardView(){
        for(Profile profile : profileList){
            mSwipeView.addView(new Card(mContext, profile, mSwipeView));
        }
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
                intent.putExtra("Username", userName);
                startActivity(intent);
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                try{
                    intent = new Intent(this, Login.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.view_posts:
                intent = new Intent(this, SwipeActivity.class);
                intent.putExtra("Username", userName);
                startActivity(intent);
                return true;
            case R.id.view_notifications:
                intent = new Intent(this, NotificationActivity.class);
                intent.putExtra("Username", userName);
                intent.putExtra("profile0", profileList.get(0));
                intent.putExtra("profile1", profileList.get(1));
                startActivity(intent);
                return  true;
        }
        return false;
    }
}
