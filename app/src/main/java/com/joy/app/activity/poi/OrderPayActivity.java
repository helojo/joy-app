package com.joy.app.activity.poi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.library.ui.activity.BaseHttpUiActivity;
import com.android.library.httptask.ObjectRequest;
import com.android.library.httptask.ObjectResponse;
import com.android.library.utils.TextUtil;
import com.android.library.view.fresco.FrescoIv;
import com.joy.app.R;
import com.joy.app.bean.poi.OrderDetail;
import com.joy.app.eventbus.OrderStatusEvent;
import com.joy.app.utils.JTextSpanUtil;
import com.joy.app.utils.http.OrderHtpUtil;
import com.joy.app.utils.http.ReqFactory;
import com.pingplusplus.android.PaymentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 订单支付页
 * <p/>
 * Created by xiaoyu.chen on 15/12/4.
 */
public class OrderPayActivity extends BaseHttpUiActivity<OrderDetail> {

    private static final int REQUEST_CODE_PAYMENT = 1;

    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";

    private String mId;
    private OrderDetail mOrderDetail;

    @BindView(R.id.sdvPhoto)
    FrescoIv sdvPhoto;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.jtv_order_id)
    TextView jtvOrderId;

    @BindView(R.id.ll_date_item)
    LinearLayout llDateItem;

    @BindView(R.id.jtv_order_day)
    TextView jtvOrderDay;

    @BindView(R.id.v_date_splite)
    View vDateSplite;

    @BindView(R.id.ll_selected_item)
    LinearLayout llSelectedItem;

    @BindView(R.id.jtv_order_item)
    TextView jtvOrderItem;

    @BindView(R.id.v_item_spilt)
    View vItemSpilt;

    @BindView(R.id.jtv_order_count)
    TextView jtvOrderCount;

    @BindView(R.id.jtv_order_name)
    TextView jtvOrderName;

    @BindView(R.id.jtv_order_phone)
    TextView jtvOrderPhone;

    @BindView(R.id.jtv_order_email)
    TextView jtvOrderEmail;

    @BindView(R.id.jtv_order_total)
    TextView jtvOrderTotal;

    @BindView(R.id.acrgPayment)
    RadioGroup acrgPayment;

    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;

    @BindView(R.id.acbNext)
    AppCompatButton acbNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order_pay);
        ButterKnife.bind(this);

        if (mOrderDetail == null)
            executeRefreshOnly();
        else
            invalidateContent(mOrderDetail);
    }

    @Override
    protected void initData() {

        super.initData();
        mId = TextUtil.filterNull(getIntent().getStringExtra("id"));
        if (getIntent().getParcelableExtra("data") instanceof OrderDetail)
            mOrderDetail = getIntent().getParcelableExtra("data");
    }

    @Override
    protected void initTitleView() {

        super.initTitleView();
        addTitleLeftBackView();
        addTitleMiddleView(R.string.booking);
    }

    @Override
    protected void initContentView() {

        super.initContentView();
        ButterKnife.bind(this);
        acbNext.setText(R.string.pay);
        acbNext.setOnClickListener(payBtnClickListener);
    }

    @Override
    protected boolean invalidateContent(OrderDetail data) {

        if (data == null)
            return false;

        sdvPhoto.setImageURI(data.getProduct_photo());
        tvTitle.setText(data.getProduct_title());
        jtvOrderId.setText(data.getOrder_id());

        if (data.isEmptyItem()) {
            goneView(llSelectedItem);
            goneView(vItemSpilt);
        } else {
            showView(llSelectedItem);
            showView(vItemSpilt);
            jtvOrderItem.setText(data.getSelected_item());
        }

        if (data.hasTravelDate()) {
            showView(llDateItem);
            showView(vDateSplite);
            jtvOrderDay.setText(data.getTravel_date());
        } else {
            goneView(llDateItem);
            goneView(vDateSplite);
        }


        jtvOrderCount.setText(data.getItem_count());
        jtvOrderName.setText(data.getContact_name());
        jtvOrderPhone.setText(data.getContact_phone());
        jtvOrderEmail.setText(data.getContact_email());
        jtvOrderTotal.setText(JTextSpanUtil.getFormatUnitStr(JTextSpanUtil.getUnitFormatPrice(data.getTotal_price())));
        tvTotalPrice.setText(JTextSpanUtil.getFormatUnitStr(JTextSpanUtil.getUnitFormatPrice(data.getTotal_price())));

        return true;
    }

    private View.OnClickListener payBtnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (acrgPayment.getCheckedRadioButtonId() == R.id.acrbWeChat) {

                getOrderChargeToPay(CHANNEL_WECHAT);

            } else if (acrgPayment.getCheckedRadioButtonId() == R.id.acrbAlipay) {

                getOrderChargeToPay(CHANNEL_ALIPAY);

            } else {
                showToast("请选择支付方式");
            }
        }
    };

    @Override
    protected ObjectRequest<OrderDetail> getObjectRequest() {

        ObjectRequest<OrderDetail> obj = ReqFactory.newPost(OrderHtpUtil.URL_POST_ORDER_DETAIL, OrderDetail.class, OrderHtpUtil.getOrderDetailUrl(mId));

        return obj;
    }

    private void getOrderChargeToPay(String channel) {

        ObjectRequest<String> obj = ReqFactory.newPost(OrderHtpUtil.URL_POST_ORDER_PAY_CREATE_CHARGE, String.class, OrderHtpUtil.getOrderPayCreateCharge(mId, channel));
        showLoading();
        //按键点击之后的禁用，防止重复点击
        acbNext.setClickable(false);
        obj.setResponseListener(new ObjectResponse<String>() {

//            @Override
//            public void onPre() {
//
//                showLoading();
//                //按键点击之后的禁用，防止重复点击
//                acbNext.setClickable(false);
//            }

            @Override
            public void onSuccess(Object tag, String orderCharge) {

                hideLoading();
                startPaymentActivity(orderCharge);
            }

            @Override
            public void onError(Object tag, String msg) {

                hideLoading();
                showToast(msg);
                acbNext.setClickable(true);
            }
        });
        addRequestNoCache(obj);
    }

    private void startPaymentActivity(String charge) {

        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        acbNext.setClickable(true);
        //支付页面返回处理
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {

                String result = data.getExtras().getString("pay_result");
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

                takePayResult(result, errorMsg, extraMsg);
            }
        }
    }

    /**
     * 处理返回值
     * "success" - payment succeed
     * "fail"    - payment failed
     * "cancel"  - user canceld
     * "invalid" - payment plugin not installed
     */
    private void takePayResult(String result, String errorMsg, String extraMsg) {


        if ("success".equals(result)) {

            EventBus.getDefault().post(new OrderStatusEvent(OrderStatusEvent.EnumOrderStatus.ORDER_PAY_SUCCESS));
            OrderDetailActivity.startActivity(this, mId);
            finish();

        } else if ("fail".equals(result)) {

            showToast(R.string.toast_pay_failed);

        } else if ("invalid".equals(result)) {

            showToast(errorMsg);

        } else if ("cancel".equals(result)) {
        } else {
        }
    }

    public static void startActivity(Activity act, String order_id, OrderDetail data) {

        Intent intent = new Intent(act, OrderPayActivity.class);
        intent.putExtra("id", order_id);
        intent.putExtra("data", data);
        act.startActivity(intent);
    }
}
