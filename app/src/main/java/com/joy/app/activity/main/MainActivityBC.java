package com.joy.app.activity.main;

import android.os.Bundle;

import com.android.library.activity.BaseUiFragment;
import com.joy.app.R;
import com.joy.app.activity.sample.RvTestFragment;
import com.joy.app.eventbus.LoginStatusEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 这里是做代码分离,完成首页列表的列表展现等操作
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-09
 */
public class MainActivityBC {

    MainActivity mMainActivity;

    public MainActivityBC(MainActivity mainActivity) {

        mMainActivity = mainActivity;
    }

    //---activty的生命周期
    public void onCreate(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
    }

    public void onRestart() {
    }

    public void onPause() {
    }

    public void onDestroy() {

        EventBus.getDefault().unregister(this);
        mMainActivity = null;
    }

    //---activty的生命周期
    //--activity的页面处理
    public void initData() {

    }

    public void initTitleView() {

    }

    public void initContentView() {

    }

    /**
     * 登录的回调 处理fragment的界面
     *
     * @param event
     */
    public void onEventMainThread(LoginStatusEvent event) {
        //        EventBus.getDefault().post( new LoginStatusEvent("FirstEvent btn clicked"));
    }
    //--activity的页面处理

    /**
     * 获取当前界面对应的fragment,如果没登录,就返回空白的列表
     *
     * @return
     */
    public List<? extends BaseUiFragment> getFragments() {

        List<BaseUiFragment> fragments = new ArrayList<>();
        fragments.add(MainFragment.instantiate(mMainActivity).setLableText(R.string.destination));
        fragments.add(TravelPlanFragment.instantiate(mMainActivity).setLableText(R.string.travel_plan));
        fragments.add(OrderFragment.instantiate(mMainActivity).setLableText(R.string.booking));
        return fragments;
    }
}