package com.joy.app.view.dialog;

import android.app.Activity;
import android.view.View;

import com.android.library.utils.ToastUtil;
import com.joy.app.R;
import com.joy.library.share.IShareInfo;
import com.joy.library.share.ShareHandler;
import com.joy.library.share.ShareInfo;
import com.joy.library.share.ShareType;
import com.joy.library.share.ShareWeixinUtil;

/**
 * 分享的窗口界面
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-12
 */
public class ShareDialog implements View.OnClickListener {

    private IShareInfo mIShareInfo;
    private ShareHandler mShareHandle;
//    private DialogPlus mDialogPlus;
    private Activity mActivity;
//    private ViewHolder mViewHolder;

    public ShareDialog(Activity activity, IShareInfo ishareInfo) {

        mActivity = activity;
        mIShareInfo = ishareInfo;
        mShareHandle = new ShareHandler(activity);
        initDialog();
    }

    /**
     * 初始化dialog的属性
     */
    private void initDialog() {

//        mViewHolder = new ViewHolder(R.layout.dialog_share);
//        mDialogPlus = DialogPlus.newDialog(mActivity).setContentHolder(mViewHolder).create();
        initContentView();
    }

    public void show() {
//        mDialogPlus.show();
    }


    private void initContentView() {

//        mViewHolder.getInflatedView().findViewById(R.id.tvWeibo).setOnClickListener(this);
//        mViewHolder.getInflatedView().findViewById(R.id.tvWeixin).setOnClickListener(this);
//        mViewHolder.getInflatedView().findViewById(R.id.tvFriend).setOnClickListener(this);
//        //        findViewById(R.id.tvEmail).setOnClickListener(this);
//        //        findViewById(R.id.tvsms).setOnClickListener(this);
//        mViewHolder.getInflatedView().findViewById(R.id.tvShareMore).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (mIShareInfo == null || mShareHandle == null) {
            return;
        }
        ShareInfo info = null;
        int errorStr = 0;
        ShareType type = null;
        switch (v.getId()) {
            case R.id.tvWeibo:
                type = ShareType.SINA;
                break;
            case R.id.tvWeixin:
                if (!ShareWeixinUtil.hasWeChatClient()) {
                    errorStr = R.string.wechat_no_app;
                } else {
                    type = ShareType.WEIXIN;
                }
                break;
            case R.id.tvFriend:
                if (!ShareWeixinUtil.hasWeChatClient()) {
                    errorStr = R.string.wechat_no_app;
                }
                type = ShareType.WEIXIN_CIRCLE;
                break;
            //            case R.id.tvEmail:
            //                type = ShareType.EMAIL;
            //                break;
            //            case R.id.tvsms:
            //                type = ShareType.SMS;
            //                break;
            case R.id.tvShareMore:
                type = ShareType.MORE;
                break;
        }
        if (errorStr <= 0) {
            info = mIShareInfo.getShareInfo(type);

            if (info != null) {
                mShareHandle.handleShare(info, type);
            }
        } else {
            ToastUtil.showToast(errorStr);
        }
//        mDialogPlus.dismiss();
    }


}
