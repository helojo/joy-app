package com.joy.app.activity.poi;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.android.library.activity.BaseHttpRvActivity;
import com.android.library.activity.BaseHttpUiActivity;
import com.android.library.adapter.OnItemViewClickListener;
import com.android.library.httptask.ObjectRequest;
import com.android.library.httptask.ObjectResponse;
import com.android.library.utils.DensityUtil;
import com.android.library.widget.JTextView;
import com.joy.app.R;
import com.joy.app.adapter.order.OrderDetailAdapter;
import com.joy.app.adapter.plan.UserPlanAdapter;
import com.joy.app.bean.plan.PlanFolder;
import com.joy.app.bean.poi.OrderDetail;
import com.joy.app.bean.sample.Special;
import com.joy.app.utils.http.OrderHtpUtil;
import com.joy.app.utils.http.PlanHttpUtil;
import com.joy.app.utils.http.sample.TestHtpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author litong  <br>
 * @Description 订单详情页    <br>
 */
public class OrderDetailActivity extends BaseHttpRvActivity<OrderDetail> {
    String Url;
    View PayButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executeRefresh();
    }

    @Override
    protected void initTitleView() {
        super.initTitleView();
        addTitleLeftBackView();
        addTitleMiddleView(R.string.order_detail_title);
        //TODO 右边的点点点图标
    }

    @Override
    protected void initContentView() {
        super.initContentView();

        OrderDetailAdapter adapter = new OrderDetailAdapter();
        setAdapter(adapter);
    }

    private void showPayButton(){
        if (PayButton != null)return;
        FrameLayout root = getRootView();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout layout = new RelativeLayout(this);
        JTextView tv = new JTextView(this);
        tv.setBackgroundResource(R.drawable.selector_bg_rectangle_accent);
        RelativeLayout.LayoutParams tvpaParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(50));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPayMassage();
            }
        });
        layout.addView(tv,tvpaParams);
        PayButton = layout;
        root.addView(layout,params);
    }

    private void removePayButton(){
        if (PayButton == null)return;
        getRootView().removeView(PayButton);
        PayButton = null;
    }

    @Override
    protected List<?> getListInvalidateContent(OrderDetail orderDetail) {
        if (orderDetail.isUnpayState()){
            showPayButton();
        }else{
            removePayButton();
        }
        switch (orderDetail.getStatus()){
            case 0://待支付
                orderDetail.setOrderInfor(getString(R.string.order_detail_create_infor));
                orderDetail.setOrderTitle(getString(R.string.order_detail_create_title));
                break;
            case 1://订单过期或被取消
                orderDetail.setOrderInfor(getString(R.string.order_detail_create_infor));
                orderDetail.setOrderTitle(getString(R.string.order_detail_create_title));
                break;
            case 2://支付成功 订单处理中
                orderDetail.setOrderInfor(getString(R.string.order_detail_create_infor));
                orderDetail.setOrderTitle(getString(R.string.order_detail_create_title));
                break;
            case 3://订单已确认
                orderDetail.setOrderInfor(getString(R.string.order_detail_create_infor));
                orderDetail.setOrderTitle(getString(R.string.order_detail_create_title));
                break;
            default://默认
                orderDetail.setOrderInfor(getString(R.string.order_detail_create_infor));
                orderDetail.setOrderTitle(getString(R.string.order_detail_create_title));

        }
        ArrayList<OrderDetail> list= new ArrayList<OrderDetail>();
        list.add(orderDetail);
        getAdapter().clear();
        return list;
    }

    private void SendPayMassage(){

    }
    private void SendCancelMassage(){
        ObjectRequest<Object> req = new ObjectRequest<>(OrderHtpUtil.getCancelOrderUrl(Url), Object.class);
        req.setResponseListener(new ObjectResponse<Object>() {

            @Override
            public void onSuccess(Object tag, Object obj) {

            }
        });
        addRequest2QueueNoCache(req, req.getIdentifier());
    }

    @Override
    protected ObjectRequest<OrderDetail> getObjectRequest() {
        ObjectRequest obj = new ObjectRequest(OrderHtpUtil.getOrderDetailUrl(Url), OrderDetail.class);
        return obj ;
    }
}
