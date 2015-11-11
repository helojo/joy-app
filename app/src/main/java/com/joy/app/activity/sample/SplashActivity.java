package com.joy.app.activity.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.joy.app.R;

/**
 * 闪屏页面
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_act_splash);
        delayStartMainActivity();
    }

    private void delayStartMainActivity() {

        new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if (!isFinishing()) {

                    finishToEnterActivity();
                }
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }

    private void finishToEnterActivity() {

//        LvTestActivity.startActivity(this);
        TabTestActivity.startActivity(this);
        finish();
    }
}
