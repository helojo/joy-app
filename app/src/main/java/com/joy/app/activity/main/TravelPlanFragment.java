package com.joy.app.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.library.ui.fragment.BaseHttpRvFragment;
import com.android.library.listener.OnItemClickListener;
import com.android.library.httptask.ObjectRequest;
import com.joy.app.JoyApplication;
import com.joy.app.R;
import com.joy.app.activity.plan.UserPlanListActivity;
import com.joy.app.adapter.plan.UserPlanAdapter;
import com.joy.app.bean.plan.PlanFolder;
import com.joy.app.eventbus.FolderEvent;
import com.joy.app.eventbus.LoginStatusEvent;
import com.joy.app.utils.http.PlanHtpUtil;
import com.joy.app.view.LoginTipView;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 我的旅行计划
 * 如果外部刷新,会通过eventbus来进行通知
 * 这里在加载的时候,需要判断是否已经登录了.没登录的界面逻辑和登录的界面逻辑不一样
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-16
 */
public class TravelPlanFragment extends BaseHttpRvFragment<List<PlanFolder>> {

    private LoginTipView mLoginTipView;
    private boolean mFolderStateChanged, mLoginStateChanged;

    public static TravelPlanFragment instantiate(Context context) {

        return (TravelPlanFragment) Fragment.instantiate(context, TravelPlanFragment.class.getName(), new Bundle());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        setPageLimit(10);
        EventBus.getDefault().register(this);
        refresh(true);
    }

    @Override
    public void onVisible() {

        if (mLoginStateChanged) {

            mLoginStateChanged = false;
            refresh(false);
        } else if (mFolderStateChanged) {

            mFolderStateChanged = false;
            executeSwipeRefresh();
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 处理界面的加载状态还是显示登录界面
     */
    private void refresh(boolean fromOnCreate) {

        if (JoyApplication.isLogin()) {

            // daisw 2016/9/17
//            if (mLoginTipView != null)
//                removeCustomView(mLoginTipView);

            if (fromOnCreate)
                executeCacheAndRefresh();
            else
                executeRefreshAndCache();
        } else {

            //设置界面为提示登录
            setNotLoginView();
        }
    }

    /**
     * 设置没有登录的界面提示
     */
    private void setNotLoginView() {

        if (mLoginTipView == null)
            mLoginTipView = new LoginTipView(this.getActivity(), R.string.travel_no_login, R.string.travel_no_login_sub);
        if (getAdapter() != null && !getAdapter().isEmpty()) {
            getAdapter().clear();
            getAdapter().notifyDataSetChanged();
        }
        // daisw 2016/9/17
//        removeCustomView(mLoginTipView);
//        addCustomView(mLoginTipView);
    }

    /**
     * 登录的回掉
     *
     * @param event
     */
    public void onEventMainThread(LoginStatusEvent event) {

        // 登录状态有变化
        mLoginStateChanged = true;
    }

    public void onEventMainThread(FolderEvent event) {

        // 创建、删除文件夹；添加、删除POI，返回该页面都需要刷新
        mFolderStateChanged = true;
    }

    @Override
    protected void initContentView() {

        UserPlanAdapter adapter = new UserPlanAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener<PlanFolder>() {
            @Override
            public void onItemClick(int position, View clickView, PlanFolder planFolder) {

                if (planFolder != null){
                    UserPlanListActivity.startActivityById(getActivity(), planFolder.getFolder_id(), planFolder.getFolder_name(), 1);
                }
            }
        });
        setAdapter(adapter);
    }

    @Override
    protected ObjectRequest<List<PlanFolder>> getObjectRequest(int pageIndex, int pageLimit) {

        return PlanHtpUtil.getUserPlanFolderRequest(PlanFolder.class, pageLimit, pageIndex);
    }
}
