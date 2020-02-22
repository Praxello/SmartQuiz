package com.praxello.smartquiz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.praxello.smartquiz.AllKeys;
import com.praxello.smartquiz.CommonMethods;
import com.praxello.smartquiz.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvTitle = findViewById(R.id.tv_app_title);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/greatvibes-regular.otf");
        tvTitle.setTypeface(face);
        //Toast.makeText(this, "token" + token, Toast.LENGTH_SHORT).show();

      //  requestPermissions();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(CommonMethods.getPrefrence(MainActivity.this, AllKeys.USER_ID).equals(AllKeys.DNF)){
                    Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }else{
                    Intent mainIntent = new Intent(MainActivity.this, DashBoardActivity.class);
                    startActivity(mainIntent);
                    finish();
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }
            }
        }, SPLASH_DISPLAY_DURATION);
    }




}
