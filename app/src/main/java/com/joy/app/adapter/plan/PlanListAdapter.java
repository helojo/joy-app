package com.joy.app.adapter.plan;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.android.library.adapter.ExRvAdapter;
import com.android.library.adapter.ExRvViewHolder;
import com.android.library.utils.TextUtil;
import com.android.library.utils.ViewUtil;
import com.android.library.widget.JTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joy.app.R;
import com.joy.app.bean.plan.PlanItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author litong  <br>
 * @Description 行程规划列表    <br>
 */
public class PlanListAdapter extends ExRvAdapter<PlanListAdapter.ViewHolder, PlanItem> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(inflate(parent, R.layout.item_plan_list));
    }

    public class ViewHolder extends ExRvViewHolder<PlanItem> {

        @Bind(R.id.sdv_poi_photo)   SimpleDraweeView sdvPoiPhoto;
        @Bind(R.id.jtv_cnname)      JTextView jtvCnname;
        @Bind(R.id.jtv_enname)      JTextView jtvEnname;
        @Bind(R.id.jtv_price)       JTextView jtvPrice;
        @Bind(R.id.jtv_day)         JTextView jtvDay;

        public ViewHolder(final View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    callbackOnItemViewClickListener(getLayoutPosition(), itemView);
                }
            });
        }

        @Override
        protected void invalidateItemView(int position, PlanItem planItem) {

            sdvPoiPhoto.setImageURI(Uri.parse(planItem.getPic_url()));
            jtvCnname.setText(planItem.getCn_name());
            jtvEnname.setText(planItem.getEn_name());
            jtvPrice.setText(planItem.getPrice());

            if (planItem.hasBefore_day()){
                jtvDay.setText(jtvDay.getContext().getString(R.string.plan_list_before_day,planItem.getBefore_day()));
                ViewUtil.showView(jtvDay);
            }else{
                ViewUtil.hideView(jtvDay);
            }

        }
    }
}