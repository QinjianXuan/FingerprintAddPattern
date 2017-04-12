package com.example.xuanqinjian.fingerprintaddpattern;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatternActivity extends AppCompatActivity {
    @BindView(R.id.patter_lock_view)
    PatternLockView mPatternLockView;


    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));
            String patternToString = PatternLockUtils.patternToString(mPatternLockView, pattern);
            if(patternToString.equals("01258")){
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                Intent intent = new Intent(PatternActivity.this,ContentActivity.class);
                startActivity(intent);

                finish();
            }else{
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
            }
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);
        ButterKnife.bind(this);

        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setNormalStateColor(Color.GRAY);
        mPatternLockView.setWrongStateColor(Color.GREEN);
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);

//        RxPatternLockView.patternComplete(mPatternLockView)
//                .subscribe(new Consumer<PatternLockCompleteEvent>() {
//                    @Override
//                    public void accept(PatternLockCompleteEvent patternLockCompleteEvent) throws Exception {
//                        Log.d(getClass().getName(), "Complete: " + patternLockCompleteEvent.getPattern().toString());
//                    }
//                });
//
//        RxPatternLockView.patternChanges(mPatternLockView)
//                .subscribe(new Consumer<PatternLockCompoundEvent>() {
//                    @Override
//                    public void accept(PatternLockCompoundEvent event) throws Exception {
//                        if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_STARTED) {
//                            Log.d(getClass().getName(), "Pattern drawing started");
//                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_PROGRESS) {
//                            Log.d(getClass().getName(), "Pattern progress: " +
//                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
//                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_COMPLETE) {
//                            Log.d(getClass().getName(), "Pattern complete: " +
//                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
//                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_CLEARED) {
//                            Log.d(getClass().getName(), "Pattern has been cleared");
//                        }
//                    }
//                });
    }
}
