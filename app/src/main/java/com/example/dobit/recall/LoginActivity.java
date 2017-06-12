package com.example.dobit.recall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by dobit on 5/22/2017.
 */


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recall_login);
//        startService(new Intent(this, NotificationService.class));
        login = (Button) findViewById(R.id.logInBtn);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
        startActivity(intent);
    }
}
