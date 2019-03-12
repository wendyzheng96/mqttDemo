package com.zyf.ws.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zyf.ws.R;
import com.zyf.ws.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PHONE_STATE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btnWebService.setOnClickListener(this);
        binding.btnMqtt.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
            Log.e("permission","动态申请READ_PHONE_STATE权限");
        } else {
            Log.e("permission","READ_PHONE_STATE权限已授权");
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_web_service:
                intent = new Intent(this, WebServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_mqtt:
                intent = new Intent(this, PushActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_STATE && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.e("permission", "READ_PHONE_STATE权限授权成功");
        } else {
            Log.e("permission", "READ_PHONE_STATE权限授权失败");
        }
    }
}
