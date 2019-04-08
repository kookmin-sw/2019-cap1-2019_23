package com.example.capston;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(1500);
        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
