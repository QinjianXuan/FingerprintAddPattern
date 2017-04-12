package com.example.xuanqinjian.fingerprintaddpattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


@SuppressWarnings("MissingPermission")
public class FingerprintActivity extends AppCompatActivity {

    @BindView(R.id.tv_finger_print_result)
    TextView mFingerprintResult;

    private FingerprintManager mFingerprintManager;
    private CancellationSignal mCancellationSignal;
    private FingerValidateCallback mFingerValidateCallback;
    private boolean mIsExit;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    mFingerprintResult.setText("指纹解锁失败,1秒后跳转手势密码解锁");
                    Message message = Message.obtain();
                    message.what = 1;
                    sendMessageDelayed(message, 1000);
                    break;
                case 1:
                    goPattern();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        ButterKnife.bind(this);
        initFingerprint();
        startValidate();
    }

    /**
     * 初始化指纹识别
     */
    private void initFingerprint() {
        mFingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        mCancellationSignal = new CancellationSignal();
        mFingerValidateCallback = new FingerValidateCallback();

    }

    /**
     * 开始指纹验证
     */
    private void startValidate() {
        mFingerprintResult.setText("点击进行指纹解锁");
        mFingerprintManager.authenticate(null, mCancellationSignal, 0 /* flags */, mFingerValidateCallback, null);
    }


    @OnClick({R.id.tv_other_validate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_other_validate://其他验证方式

                goPattern();
                break;
        }
    }

    private void goPattern() {

        Intent intent = new Intent(this, PatternActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    /**
     * 双击返回键退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.TAG_EXIT, true);
                startActivity(intent);

            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    class FingerValidateCallback extends FingerprintManager.AuthenticationCallback {


        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);

            mFingerprintResult.setText("指纹解锁失败,2秒后跳转手势密码解锁");
            mFingerprintResult.setTextColor(Color.RED);
            mCancellationSignal.cancel();
            Message msg = Message.obtain();
            msg.what = 0;
            mHandler.sendMessageDelayed(Message.obtain(), 1000);


        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            mFingerprintResult.setText(helpString);
            mFingerprintResult.setTextColor(Color.RED);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            mFingerprintResult.setText("验证成功");
            mFingerprintResult.setTextColor(Color.GREEN);
            mCancellationSignal.cancel();

            finish();
            Intent intent = new Intent(FingerprintActivity.this, ContentActivity.class);
            startActivity(intent);


        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();

            mFingerprintResult.setText("指纹无法识别，请重按");
            mFingerprintResult.setTextColor(Color.RED);

        }
    }


}
