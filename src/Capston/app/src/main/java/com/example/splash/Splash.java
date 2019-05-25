package com.example.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.login.LoginActivity;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(500);
        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
