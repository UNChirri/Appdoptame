package com.appdoptame.appdoptame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.appdoptame.appdoptame.FBAuth.FBLogin;
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!checkForToken()) goLoginScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!checkForToken()) goLoginScreen();
    }

    private void goLoginScreen(){
        Intent intent = new Intent(this, FBLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private Boolean checkForToken(){
        return AccessToken.getCurrentAccessToken() == null ? false : true;
    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        try{
            Intent out = new Intent(this, FBLogin.class);
            startActivity(out);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
