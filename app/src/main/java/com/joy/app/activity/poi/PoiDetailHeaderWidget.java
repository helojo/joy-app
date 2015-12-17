package com.joy.app.activity.poi;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.library.utils.MathUtil;
import com.android.library.utils.TextUtil;
import com.android.library.utils.ViewUtil;
import com.android.library.view.ExLayoutWidget;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joy.app.R;
import com.joy.app.bean.sample.PoiDetail;
import com.joy.app.utils.JTextSpanUtil;

import butterknife.ButterKnife;


/**
 * poi详情的头部内容（头图、国家名称、评星、去过、想去）
 * Created by xiaoyu.chen on 15/11/18.
 */
public class PoiDetailHeaderWidget extends ExLayoutWidget implements View.OnClickListener {

    private SimpleDraweeView mSdvPhoto;
    private TextView mTvTitle;
    private TextView mTvPrice;
    private AppCompatRatingBar mAcRatingBar;
    private TextView mTvPoiCommentNum;
    private LinearLayout llAddPlanDiv;
    private TextView mBtnAddToPlan;
    private TextView mTvName;

    public PoiDetailHeaderWidget(Activity activity) {

        super(activity);
    }


    @Override
    protected View onCreateView(Activity activity, Object... args) {

        View contentView = activity.getLayoutInflater().inflate(R.layout.view_poi_detail_header, null);

        initContentViews(contentView);

        return contentView;
    }

    private void initContentViews(View contentView) {

        mSdvPhoto = (SimpleDraweeView) contentView.findViewById(R.id.sdvPhoto);
        mTvTitle = (TextView) contentView.findViewById(R.id.tvTitle);
        mAcRatingBar = (AppCompatRatingBar) contentView.findViewById(R.id.acRatingBar);
        llAddPlanDiv = (LinearLayout) contentView.findViewById(R.id.llAddPlanDiv);
        llAddPlanDiv.setOnClickListener(this);
        mBtnAddToPlan = (TextView) contentView.findViewById(R.id.tvAddToPlan);
        mTvName = (TextView) contentView.findViewById(R.id.tvName);
        mTvPrice = (TextView) contentView.findViewById(R.id.tvPrice);
        mTvPoiCommentNum = (TextView) contentView.findViewById(R.id.tvPoiCommentNum);

    }

    protected void invalidate(final PoiDetail data) {

        if (data == null)
            return;

        try {
            mSdvPhoto.setImageURI(Uri.parse(data.getPhotos().get(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtil.isNotEmpty(data.getFolder_id())) {

            mBtnAddToPlan.setText(R.string.added);
            mBtnAddToPlan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_poi_add_plan_selected, 0, 0, 0);
            mTvName.setText(data.getFolder_name());
            llAddPlanDiv.setSelected(true);
        }

        if (data.getIs_book()) {
            mTvPrice.setText(JTextSpanUtil.getFormatUnitStr(getActivity().getString(R.string.unit, data.getPrice())));
            ViewUtil.showView(mTvPrice);
        }

        mTvTitle.setText(data.getTitle());
        mAcRatingBar.setRating(MathUtil.parseFloat(data.getComment_level(), 0));
        mTvPoiCommentNum.setText(getActivity().getResources().getString(R.string.kuohao, TextUtil.isEmpty(data.getComment_num()) ? "0" : data.getComment_num()));

    }

    @Override
    public void onClick(View v) {

        callbackWidgetViewClickListener(v);
    }
}
