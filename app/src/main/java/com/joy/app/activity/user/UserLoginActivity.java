package com.joy.app.activity.user;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.library.activity.BaseHttpUiActivity;
import com.android.library.activity.BaseUiActivity;
import com.android.library.httptask.ObjectRequest;
import com.android.library.httptask.ObjectResponse;
import com.android.library.utils.TextUtil;
import com.android.library.utils.ToastUtil;
import com.joy.app.JoyApplication;
import com.joy.app.R;
import com.joy.app.bean.User;
import com.joy.app.eventbus.LoginStatusEvent;
import com.joy.app.utils.http.ReqFactory;
import com.joy.app.utils.http.UserHtpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 用户登录注册
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-17
 */
public class UserLoginActivity extends BaseHttpUiActivity<String> implements View.OnClickListener {


    @Bind(R.id.tvInfo)
    TextView mTvInfo;

    @Bind(R.id.llLoginDiv)
    View mLoginDiv;

    @Bind(R.id.etPhone)
    EditText mEtPhone;

    @Bind(R.id.etCode)
    EditText mEtCode;

    @Bind(R.id.tvGetCode)
    TextView mTvGetCode;

    @Bind(R.id.tvButton)
    TextView mTvButton;

    View mDecorView;//根目录
    private String mSubmitPhone;//提交的电话号码.
    private int mDurationMillis = 200;//动画时间
    private float mDefaultLoginY;//默认的登录view的坐标
    DisTime mDisTime;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_login);
    }

    @Override
    @TargetApi(16)
    protected void onDestroy() {
        super.onDestroy();
        try {
            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutListener);
        } catch (Exception ex) {

        }
    }

    @Override
    protected void initTitleView() {
        super.initTitleView();
        addTitleLeftBackView();
    }

    @Override
    protected void initData() {


        ButterKnife.bind(this);
        super.initData();
    }

    @Override
    protected void initContentView() {

        super.initContentView();

        mDecorView = getWindow().getDecorView();
        mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);

        mTvGetCode.setOnClickListener(this);
        mTvButton.setOnClickListener(this);

        mEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtil.isEmpty(mEtCode.getText().toString()) && !TextUtil.isEmpty(mSubmitPhone)) {

                    mTvButton.setEnabled(true);
                    mTvButton.setBackgroundResource(R.drawable.selector_bg_rectangle_accent_fill);

                } else {
                    mTvButton.setEnabled(false);
                    mTvButton.setBackgroundResource(R.drawable.shape_bg_rectangle_dark);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEtCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==KeyEvent.ACTION_DOWN|| actionId== EditorInfo.IME_ACTION_DONE){
                    toLogin();
                    return true;
                }
                return false;
            }
        });
    }

    ViewTreeObserver.OnGlobalLayoutListener mLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            mDecorView.getWindowVisibleDisplayFrame(rect);
            int displayHight = rect.bottom - rect.top;
            int hight = mDecorView.getHeight();
            boolean visible = (double) displayHight / hight < 0.8;
            if (mDefaultLoginY <= 0) {
                mDefaultLoginY = mLoginDiv.getY();
            }
            if (mTvInfo.getAnimation() != null)
                mTvInfo.getAnimation().cancel();
            if (mLoginDiv.getAnimation() != null)
                mLoginDiv.getAnimation().cancel();
            if (visible) {

                //动作向上
                if (mLoginDiv.getY() != mTvInfo.getY()) {
                    animatorInfoView(mTvInfo.getAlpha(), 0f);
                    animatorLoginView(mDefaultLoginY, mTvInfo.getY());
                }
            } else {
                //动画向下

                if (mLoginDiv.getY() != mDefaultLoginY) {
                    animatorInfoView(mTvInfo.getAlpha(), 1f);
                    animatorLoginView(mLoginDiv.getY(), mDefaultLoginY);
                }

            }
        }
    };

    /**
     * 处理info的动画view
     *
     * @param startValue
     * @param endValue
     */
    private void animatorInfoView(final float startValue, final float endValue) {

        ObjectAnimator alphaOA = ObjectAnimator.ofFloat(mTvInfo, "alpha", startValue, endValue);
        alphaOA.setDuration(mDurationMillis);
        alphaOA.setInterpolator(new DecelerateInterpolator());
        alphaOA.start();

    }

    /**
     * 处理info的动画view
     *
     * @param startY
     * @param endY
     */
    private void animatorLoginView(float startY, float endY) {

        ObjectAnimator alphaOA = ObjectAnimator.ofFloat(mLoginDiv, "y", startY, endY);
        alphaOA.setDuration(mDurationMillis);
        alphaOA.setInterpolator(new DecelerateInterpolator());

        alphaOA.start();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.tvButton:
                toLogin();
                break;
        }
    }

    /**
     * 登录
     */
    private void toLogin() {

        if (TextUtil.isEmpty(mSubmitPhone)) {
            ToastUtil.showToast(R.string.login_phone_empty);
            return;
        }
        String code = mEtCode.getText().toString();
        if (TextUtil.isEmpty(code)) {
            ToastUtil.showToast(R.string.login_code_empty);
            return;
        }
        showLoading();

        ObjectRequest<User> req = ReqFactory.newPost(UserHtpUtil.URL_USER_LOGIN, User.class, UserHtpUtil.userLogin(mSubmitPhone, code));
        //        User u = new User();
        //        u.setUser_id("ssss");
        //        u.setMobile("18888888888");
        //        u.setCreate_time(new Date().getTime() / 1000);
        //        u.setNickname("liulong");
        //        u.setToken("sadfasdfas");
        //        req.setData(u);
        req.setResponseListener(new ObjectResponse<User>() {


            @Override
            public void onSuccess(Object tag, User u) {

                if (u != null) {

                    //写入xml,发送广播
                    JoyApplication.getCommonPrefs().writeUser(u);
                    JoyApplication.setUser(u);
                    EventBus.getDefault().post(new LoginStatusEvent(true, u));
                    ToastUtil.showToast(R.string.login_success);
                    hideLoading();
                    finish();
                } else {
                    onError(null, "");
                }
            }

            @Override
            public void onError(Object tag, String msg) {
                super.onError(tag, msg);
                hideLoading();
                if (TextUtil.isEmpty(msg)) {
                    ToastUtil.showToast(R.string.request_error);
                } else {
                    ToastUtil.showToast(msg);
                }
            }
        });
        JoyApplication.getRequestQueue().add(req);
    }

    /**
     * 获取验证码
     */
    private void getCode() {


        String tmp = mEtPhone.getText().toString();
        if (TextUtil.isEmpty(tmp) || tmp.length() < 11) {
            showToast(R.string.login_no_phone);
            return;
        }
        mSubmitPhone = tmp;
        mTvButton.setEnabled(false);
        showLoading();
        ObjectRequest req = ReqFactory.newPost(UserHtpUtil.URL_USER_GETCODE, String.class, UserHtpUtil.getCode(mSubmitPhone));
//                req.setData("");
        req.setResponseListener(new ObjectResponse() {

            @Override
            public void onSuccess(Object tag, Object o) {

                ToastUtil.showToast(R.string.login_phone_code_success);
                startCountdown(60);


                hideLoading();
            }

            @Override
            public void onError(Object tag, String msg) {
                super.onError(tag, msg);
                if (TextUtil.isEmpty(msg)) {
                    ToastUtil.showToast(R.string.request_error);
                } else {
                    ToastUtil.showToast(msg);
                }
                mTvButton.setEnabled(true);
                hideLoading();
            }
        });
        JoyApplication.getRequestQueue().add(req);
    }

    @Override
    protected boolean invalidateContent(String s) {
        return false;
    }

    @Override
    protected ObjectRequest<String> getObjectRequest() {
        return null;
    }

    /* 定义一个倒计时的内部类 */
    class DisTime extends CountDownTimer {
        public DisTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mDisTime.cancel();
            updateVerificationResendView(0);
            setCanUse(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            updateVerificationResendView((int) (millisUntilFinished / 1000));
        }
    }

    //设置控件和通知外部是否可用
    private void setCanUse(boolean use) {
        mTvGetCode.setEnabled(use);
    }

    //直接进行倒数
    public void startCountdown(int time) {

        updateVerificationResendView(time);
        setCanUse(false);
        startDisTime(time);
    }

    /**
     * 更新界面
     *
     * @param time
     */
    private void updateVerificationResendView(int time) {

        if (time > 0) {
            mTvGetCode.setText(getString(R.string.login_reget_code, time));
            mTvGetCode.setTextColor(getResources().getColor(R.color.black_trans80));
        } else {

            mTvGetCode.setText(getString(R.string.login_get_code));
            mTvGetCode.setTextColor(getResources().getColor(R.color.text_pink));
        }
    }

    /**
     * 启动倒计时
     *
     * @param time
     */
    private void startDisTime(int time) {

        if (mDisTime != null) {
            mDisTime.cancel();
            mDisTime = null;
        }
        mDisTime = new DisTime(time * 1000, 1000);
        mDisTime.start();
    }


    public static void startActivity(Context context) {

        context.startActivity(new Intent(context, UserLoginActivity.class));
    }
}
