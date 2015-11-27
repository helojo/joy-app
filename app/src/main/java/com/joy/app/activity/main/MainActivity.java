package com.joy.app.activity.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.joy.app.JoyApplication;
import com.joy.app.R;
import com.joy.app.activity.common.HolterDayPickerActivity;
import com.joy.app.activity.setting.SettingActivity;
import com.android.library.activity.BaseTabActivity;
import com.android.library.activity.BaseUiFragment;
import com.joy.app.view.dateView.SimpleMonthAdapter;

import java.util.Calendar;
import java.util.List;

/**
 * 专题列表
 * 推送.或则外部应用打开,在这里处理
 * 模块控制油mMainActivityBC来处理
 * 推送等其他和ui唔关的由mMainActivityHelper 处理
 * 本activity相关的ui.直接在这里进行处理
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-09
 */
public class MainActivity extends BaseTabActivity {

    private MainActivityBC mMainActivityBC;
    private MainActivityHelperBC mMainActivityHelper;
    private long mLastPressedTime; //最后一次按返回的按钮

    public static void startActivity(Context context) {

        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mMainActivityBC = new MainActivityBC(this);
        mMainActivityHelper = new MainActivityHelperBC(this);
        super.onCreate(savedInstanceState);

        mMainActivityHelper.onCreate(savedInstanceState);
        mMainActivityBC.onCreate(savedInstanceState);
        Calendar calendar=Calendar.getInstance();
        calendar.set(2016,1,2);

        Calendar calendar1=Calendar.getInstance();
        calendar1.set(2016,1,3);
        HolterDayPickerActivity.startHotelDayPickerForResult(this,true,calendar.getTimeInMillis(),calendar1.getTimeInMillis(),1);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        mMainActivityHelper.onNewIntent(intent);
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        mMainActivityHelper.onRestart();
        mMainActivityBC.onRestart();
    }

    @Override
    protected void onPause() {

        super.onPause();
        mMainActivityHelper.onPause();
        mMainActivityBC.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mMainActivityHelper.onDestroy();
        mMainActivityBC.onDestroy();
    }

    @Override
    protected void initData() {

        super.initData();
        mMainActivityBC.initData();
    }

    @Override
    protected void initTitleView() {

        super.initTitleView();

        setTitle(null);
        setTitleLogo(R.drawable.ic_logo);

        View v = inflateLayout(R.layout.view_avatar);
        SimpleDraweeView sdvAvatar = (SimpleDraweeView) v.findViewById(R.id.sdvAvatar);
        sdvAvatar.setImageURI(Uri.parse("http://static.qyer.com/data/avatar/000/66/51/28_avatar_big.jpg?v=1423838207"));
        addTitleRightView(v, new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SettingActivity.startActivity(MainActivity.this);
            }
        });
        mMainActivityBC.initTitleView();
    }

    @Override
    protected void initContentView() {

        super.initContentView();
        setTabIndicatorHeight(DP_1_PX * 3);
        mMainActivityBC.initContentView();
    }

    @Override
    protected List<? extends BaseUiFragment> getFragments() {

        return mMainActivityBC.getFragments();
    }

    @Override
    public void onBackPressed() {

        long currentPressedTime = System.currentTimeMillis();
        if (currentPressedTime - mLastPressedTime > 2000) {

            mLastPressedTime = currentPressedTime;
            showToast(R.string.toast_exit_tip);
        } else {

            super.onBackPressed();
            JoyApplication.releaseForExitApp();
        }
    }
}