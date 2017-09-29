package com.appdoptame.appdoptame.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.appdoptame.appdoptame.Auth.Login;
import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.adapter.ChatArrayAdapter;
import com.appdoptame.appdoptame.model.ChatMessage;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


public class ChatActivity extends Activity {
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    private String userName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        savedInstanceState = getIntent().getExtras();
        userName = savedInstanceState.getString("Username");
        buttonSend = (Button) findViewById(R.id.send);
        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage(chatText.getText().toString());
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage(chatText.getText().toString());
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        sendChatMessage("Hola, veo que estás interesado en mi mascota. Puedes hablarme al Whatsapp 3124567891");

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

    private boolean sendChatMessage(String text) {
        chatArrayAdapter.add(new ChatMessage(side, text));
        chatText.setText("");
        side = !side;
        return true;
    }
}