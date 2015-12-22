package com.joy.app.activity.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.library.activity.BaseUiActivity;
import com.android.library.utils.LogMgr;
import com.android.library.utils.TextUtil;
import com.android.library.utils.ViewUtil;
import com.joy.app.R;
import com.joy.app.eventbus.LoginStatusEvent;
import com.joy.app.utils.ActivityUrlUtil;
import com.joy.app.utils.share.WebViewShare;
import com.joy.app.view.dialog.ShareDialog;
import com.joy.app.view.webview.BaseWebView;
import com.joy.app.view.webview.WebViewBaseWidget;
import com.joy.app.view.webview.WebViewNativeWidget;

import de.greenrobot.event.EventBus;

/**
 * 统一处理webview的展现
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-10
 */
public class WebViewActivity extends BaseUiActivity implements WebViewBaseWidget.WebViewListener, View.OnClickListener {

    //--大于100有分享的意思
    public static final int TYPE_CITY = 101;//城市详情

    public static final int TYPE_ABOUT = 1;//关于界面

    private WebViewBaseWidget mWebViewWidget;
    private ShareDialog mShareDialog;
    private WebViewShare mWebViewShare;
    private int mType = 0;
    private String mTitle;
    private boolean mUseBottomBanner = false;//使用底部的banner
    private String mUrl;
    private boolean mNoJump = false;//是否会进行打开新页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentFullScreenWebView(true);
    }

    @Override
    protected void onPause() {

        super.onPause();
        mWebViewWidget.onPause();
    }

    @Override
    protected void initData() {
        mWebViewShare = new WebViewShare();
        mWebViewShare.setInfo("http://www.qq.com");
        EventBus.getDefault().register(this);
        mWebViewWidget.setUserCookie(false);
        mUrl = getIntent().getStringExtra("url");

        if (mUrl.indexOf("booking.com") > 0) {
            mNoJump = true;
        }
        mWebViewWidget.loadUrl(mUrl);
        mType = getIntent().getIntExtra("type", 0);
        mUseBottomBanner = getIntent().getBooleanExtra("usebootom", false);


    }

    @Override
    protected void initTitleView() {

        if (!mUseBottomBanner) {
            addTitleLeftBackView();
            //            mTitle = getIntent().getStringExtra("title");
            //            addTitleMiddleView(mTitle);

            if (mType > 100) {
                addTitleRightView(R.drawable.ic_share_pink, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mShareDialog == null) {

                            mShareDialog = new ShareDialog(WebViewActivity.this, mWebViewShare);
                        }
                        mShareDialog.show();
                    }
                });
            }
        }
    }

    @Override
    protected void initContentView() {

        if (mUseBottomBanner) {
            ViewUtil.showView(findViewById(R.id.llTool));
            findViewById(R.id.ivBack).setOnClickListener(this);
            findViewById(R.id.ivFoward).setOnClickListener(this);
            findViewById(R.id.ivClose).setOnClickListener(this);
            findViewById(R.id.ivRefresh).setOnClickListener(this);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 登录的回掉
     *
     * @param event
     */
    public void onEventMainThread(LoginStatusEvent event) {

        onUserLoginStatusChanged(event.isLogin());
    }

    /**
     * 设置webview的实体类
     *
     * @param isNativeMode
     */
    protected void setContentFullScreenWebView(boolean isNativeMode) {

        setWebWidget(new WebViewNativeWidget(this));

        setContentView(R.layout.act_web_browser);

        RelativeLayout root = (RelativeLayout) getContentView();
        RelativeLayout.LayoutParams ls = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ls.addRule(RelativeLayout.ABOVE, R.id.llTool);
        root.addView(getWebWidget().getContentView(), ls);
    }

    protected void setWebWidget(WebViewBaseWidget widget) {

        mWebViewWidget = widget;
        mWebViewWidget.setWebViewListener(this);
    }

    public WebViewBaseWidget getWebWidget() {

        return mWebViewWidget;
    }

    /*
        webview方法回调区
     */

    @Override
    public void onWebViewPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onWebViewReceiveTitle(String title) {

        //        if (TextUtil.isEmpty(mTitle) && !TextUtil.isEmpty(title) && !mUseBottomBanner) {
        //            setTitle(title);
        //        }
    }

    @Override
    public void onWebViewProgressChanged(int newProgress) {

    }

    @Override
    public void onWebViewReceivedError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onWebViewPageFinished(String url, boolean isUrlLoadError) {

    }

    @Override
    public void onWebViewHtmlSource(String html) {

    }

    @Override
    public boolean onWebViewShouldOverrideUrlLoading(String url) {

        if (LogMgr.isDebug())
            LogMgr.d("webviewActivity", "onWebViewShouldOverrideUrlLoading url  = " + url);
        if (mNoJump || mUrl.equals(url)) {
            loadUrl(url);
        } else if (!ActivityUrlUtil.startActivityByHttpUrl(this, url)) {
            loadUrl(url);

        }
        return true;
    }

    /*
        子类调用方法区--------------------------------------------------------------------------------
     */

    public FrameLayout getFrameView() {

        return mWebViewWidget.getContentView();
    }

    public void setWebViewHtmlSourceEnable(boolean enable) {

        mWebViewWidget.setWebViewHtmlSourceEnable(enable);
    }

    public void setWebViewCacheMode(int cacheMode) {

        mWebViewWidget.setWebViewCacheMode(cacheMode);
    }

    public void setWebViewBuiltInZoomControls(boolean enabled) {

        mWebViewWidget.setWebViewBuiltInZoomControls(enabled);
    }

    public void setWebViewOnTouchListener(View.OnTouchListener lisn) {

        mWebViewWidget.setWebViewOnTouchListener(lisn);
    }

    public void setWebViewOnScrollListener(BaseWebView.OnScrollListener lisn) {

        mWebViewWidget.setWebViewOnScrollListener(lisn);
    }

    public void reloadUrl() {

        mWebViewWidget.reloadUrl();
    }

    public void loadUrl(String url) {

        mWebViewWidget.loadUrl(url);
    }

    public void onUserLoginStatusChanged(boolean isLogin) {

        if (!isFinishing())
            mWebViewWidget.reloadUrlByLoginStateChanged();
    }

    private void onGoBackClick(boolean needFinish) {

        if (getWebWidget().canGoBack()) {

            if (getWebWidget().isCookieStatusNone()) {

                //如果没有种cookie，直接back
                getWebWidget().goBack();
            } else {

                //如果加载过cookie，则最早的历史纪录是cookieurl，不能back
                WebBackForwardList list = getWebWidget().copyBackForwardList();
                if (list != null && list.getCurrentIndex() > 1) {

                    if (mUseBottomBanner && list.getCurrentIndex() == 2) {// TODO 跳过loading页，直接关闭。暂时先这么写

                        finish();
                        return;
                    }

                    getWebWidget().goBack();
                } else {

                    if (needFinish)
                        finish();
                }
            }

        } else {

            if (needFinish)
                finish();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivBack:
                onGoBackClick(false);

                break;
            case R.id.ivFoward:
                getWebWidget().goForward();
                break;
            case R.id.ivClose:
                finish();
                break;
            case R.id.ivRefresh:
                getWebWidget().reloadUrl();

                break;

        }
    }

    /**
     * 不需要分享的调这
     */
    public static void startActivity(Context context, String url) {

        startActivity(context, url, "", 0);

    }

    /**
     * 不需要分享的调这
     */
    public static void startActivity(Context context, String url, String title) {

        startActivity(context, url, title, 0);

    }

    /**
     * 酒店打开的方式
     *
     * @param context
     * @param url
     */
    public static void startHotelActivity(Context context, String url) {

        startActivity(context, url, "", 0, true);
    }

    /**
     * 有分享的调用这
     *
     * @param context
     * @param url
     * @param title
     * @param type
     */
    public static void startActivity(Context context, String url, String title, int type) {

        startActivity(context, url, title, type, false);

    }

    /**
     * @param context
     * @param url
     * @param title
     * @param type
     * @param useBottomBanner 就不使用标题栏了,直接底部
     */
    public static void startActivity(Context context, String url, String title, int type, boolean useBottomBanner) {

        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        intent.putExtra("usebootom", useBottomBanner);
        intent.setClass(context, WebViewActivity.class);
        context.startActivity(intent);

    }

}
