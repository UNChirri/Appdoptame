package com.appdoptame.appdoptame.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.appdoptame.appdoptame.Auth.Login;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.model.Notification;
import com.appdoptame.appdoptame.model.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class NotificationActivity extends AppCompatActivity {

    ListView listView;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        listView = (ListView) findViewById(R.id.list);
        savedInstanceState = getIntent().getExtras();
        userName = savedInstanceState.getString("Username");

        // Defined Array values to show in ListView
        Bundle extras = getIntent().getExtras();
        Profile profile0 = (Profile)extras.getSerializable("profile0");
        Profile profile1 = (Profile)extras.getSerializable("profile1");

        Notification[] notifications = new Notification[2];
        notifications[0] = new Notification(userName, profile0, true, false);
        notifications[1] = new Notification(userName, profile1, true, true);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<Notification> adapter = new ArrayAdapter<Notification>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, notifications);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                Notification notification = (Notification) listView.getItemAtPosition(position);

                // Show Alert
                if (!notification.isAnswered())
                    Toast.makeText(getApplicationContext(),"Esta petición aún no recibe respuesta" , Toast.LENGTH_LONG).show();

                else {
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra("Username", userName);
                    startActivity(intent);
                }
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
                startActivity(intent);
                return  true;
        }
        return false;
    }
}
