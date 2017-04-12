package com.example.xuanqinjian.fingerprintaddpattern;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private FingerprintManager mFingerprintManager;

    public static final String TAG_EXIT = "exit";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        mFingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.btn_start_validate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_validate://开始验证

                //检测硬件
                if(!mFingerprintManager.isHardwareDetected()){
                    showPattern();
                    return;
                }


                //判断权限
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    showPattern();
                    return;
                }

                // 如果没有录入指纹，则不能使用指纹识别
                if (!mFingerprintManager.hasEnrolledFingerprints()) {
                    // This happens when no fingerprints are registered.
                    Toast.makeText(this, "您还没有录入指纹, 请在设置界面录入至少一个指纹", Toast.LENGTH_LONG).show();
                    showPattern();
                    return;
                }

                showFingerprint();

                break;
        }
    }

    /**
     * 跳转手势密码
     */
    private void showPattern(){
        Intent intent = new Intent(this,PatternActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转指纹解锁
     */
    private void showFingerprint(){
        Intent intent = new Intent(this,FingerprintActivity.class);
        startActivity(intent);
    }

}
