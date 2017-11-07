package com.appdoptame.appdoptame.Base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.appdoptame.appdoptame.Auth.Login;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.fragments.NotificationFragment;
import com.appdoptame.appdoptame.fragments.PostFragment;
import com.appdoptame.appdoptame.fragments.SwipeFragment;
import com.appdoptame.appdoptame.fragments.UserProfileFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DrawerLayout drawer;
    SwipeFragment swipeFragment;


    /**
     * Not working because the views are inside a header
     */
    //@BindView(R.id.userImage) ImageView profileImage;
    //@BindView(R.id.userName) TextView userName;
    //@BindView(R.id.userEmail) TextView userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        

        ButterKnife.bind(this);

        if(user != null){
            View view =  navigationView.getHeaderView(0); //image view

            CircleImageView profileImage = (CircleImageView) view.findViewById(R.id.userImage);


            try {
                Bitmap returned_bitmap = new convertUrlToBitmap().execute().get();
                profileImage.setImageBitmap(returned_bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            TextView userName = (TextView) view.findViewById(R.id.userName); // username view
            userName.setText(user.getDisplayName());

            TextView userEmail = (TextView) view.findViewById(R.id.userEmail); // email view
            userEmail.setText(user.getEmail());
        }

        swipeFragmentInvocation();
    }

    private void swipeFragmentInvocation(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        swipeFragment = SwipeFragment.newInstance();
        fragmentTransaction.add(R.id.fragment_container, swipeFragment,"swipe_fragment");
        fragmentTransaction.commit();
    }

    private void notificationFragmentInvocation(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NotificationFragment notificationFragment = NotificationFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("profile0",swipeFragment.getProfileList().get(0));
        bundle.putSerializable("profile1",swipeFragment.getProfileList().get(1));
        notificationFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fragment_container, notificationFragment,"notification_fragment");
        fragmentTransaction.commit();
    }

    private void postFragmentInvocation(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PostFragment postFragment = PostFragment.newInstance();
        fragmentTransaction.add(R.id.fragment_container, postFragment,"post_fragment");
        fragmentTransaction.commit();
    }

    private void userprofileFragmentInvocation(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UserProfileFragment userprofileFragment = UserProfileFragment.newInstance();
        fragmentTransaction.add(R.id.fragment_container, userprofileFragment,"userprofile_fragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }
    /*
     * Hides the three dots right menu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profileapp) {
            Fragment dummy = getSupportFragmentManager().findFragmentByTag("userprofile_fragment");
            if(dummy == null) {
                userprofileFragmentInvocation();
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dummy).commit();
            }
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            try{
                Intent out = new Intent(this, Login.class);
                startActivity(out);
                finish();
            }catch(Exception e){
                e.printStackTrace();
            }
        } else if(id == R.id.nav_notifications) {
            Fragment dummy = getSupportFragmentManager().findFragmentByTag("notification_fragment");
            if(dummy == null) {
                notificationFragmentInvocation();
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dummy).commit();
            }

        } else if(id == R.id.nav_upload){
            Fragment dummy = getSupportFragmentManager().findFragmentByTag("post_fragment");
            if(dummy == null) {
                postFragmentInvocation();
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dummy).commit();
            }

        } else if(id == R.id.nav_home){
            Fragment dummy = getSupportFragmentManager().findFragmentByTag("swipe_fragment");
            if(dummy == null) {
                swipeFragmentInvocation();
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dummy).commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
