package com.appdoptame.appdoptame.Auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.appdoptame.appdoptame.Base.BaseActivity;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.fragments.FBLoginFragment;
import com.appdoptame.appdoptame.fragments.GPLoginFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity implements FBLoginFragment.OnFragmentInteractionListener {

    GPLoginFragment gpLogicFragment;
    FBLoginFragment fbLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //  checkSession();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        gpLogicFragment = GPLoginFragment.newInstance();
        fragmentTransaction.add(R.id.gp_fragment_container, gpLogicFragment);

        fbLoginFragment = FBLoginFragment.newInstance();
        fragmentTransaction.add(R.id.fb_fragment_container, fbLoginFragment);

        fragmentTransaction.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPLoginFragment.SIGN_IN_CODE) {
            gpLogicFragment.onActivityResult(requestCode, resultCode, data);
        }else {
            fbLoginFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    private void goMainPage(FirebaseUser user){
        Intent intent = new Intent(this, BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Username", user.getUid());
        startActivity(intent);
    }

    private void checkSession(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            goMainPage(firebaseUser);
            finish();
        }
    }
}
