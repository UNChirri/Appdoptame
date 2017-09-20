package com.appdoptame.appdoptame.FBAuth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.TestActivity;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class FBLogin extends AppCompatActivity implements FBLoginLogic.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblogin);
        checkSession();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FBLoginLogic fbLogicFragment = FBLoginLogic.newInstance();
        fragmentTransaction.add(R.id.fragment_container, fbLogicFragment);
        fragmentTransaction.commit();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fb_logic_fragment);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    private void goMainPage(FirebaseUser user){
        Intent intent = new Intent(this, TestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Username", user.getUid());
        startActivity(intent);
    }

    private void checkSession(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            goMainPage(firebaseUser);
        }
    }
}
