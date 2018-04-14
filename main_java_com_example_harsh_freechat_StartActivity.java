package com.example.harsh.freechat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private Button mRegBtn;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mRegBtn = findViewById(R.id.start_reg_btn);
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else
                    connected = false;
                if(connected) {
                    Intent RegisterIntent = new Intent(StartActivity.this, RegisterActivity.class);
                    startActivity(RegisterIntent);
                }
                else
                    Toast.makeText(StartActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            }
        });

        mLoginBtn = findViewById(R.id.start_login_btn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).

                        getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).

                                getState() == NetworkInfo.State.CONNECTED)

                {
                    //we are connected to a network
                    connected = true;
                } else
                    connected = false;
                if (connected)

                {
                    Intent LoginInt = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(LoginInt);
                } else
                    Toast.makeText(StartActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        });
    }

}
