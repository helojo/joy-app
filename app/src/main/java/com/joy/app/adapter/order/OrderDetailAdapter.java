package com.joy.app.adapter.order;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.library.adapter.ExRvAdapter;
import com.android.library.adapter.ExRvViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joy.app.R;
import com.joy.app.bean.plan.PlanFolder;
import com.joy.app.bean.poi.OrderDetail;

import java.net.URI;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author litong  <br>
 * @Description 订单详情页    <br>
 */
public class OrderDetailAdapter extends ExRvAdapter <OrderDetailAdapter.ViewHolder,OrderDetail>{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent, R.layout.item_order_view));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetail data = getItem(position);

        if (data != null) {

            holder.initData(data);
        }
    }

    public class ViewHolder extends ExRvViewHolder {

        @Bind(R.id.jtv_title)
        TextView Title;//状态

        @Bind(R.id.jtv_infor)
        TextView Infor;//状态详情

        @Bind(R.id.sdv_order_photo)
        SimpleDraweeView photo;//图片


        @Bind(R.id.jtv_order_title)
        TextView orderTitle;//订单名称

        @Bind(R.id.jtv_order_id)
        TextView orderId;//订单编号

        @Bind(R.id.jtv_order_item)
        TextView orderItem;//项目

        @Bind(R.id.jtv_order_count)
        TextView orderCount;//数量

        @Bind(R.id.jtv_order_name)
        TextView orderName;//姓名

        @Bind(R.id.jtv_order_phone)
        TextView orderPhone;//电话

        @Bind(R.id.jtv_order_email)
        TextView orderEmail;//Email

        @Bind(R.id.jtv_order_total)
        TextView orderTotal;//总额



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void initData(OrderDetail order){
            Title.setText(order.getOrderTitle());
            Infor.setText(order.getOrderInfor());

//            photo.setImageURI(URI.create(order.get));
            orderName.setText(order.getContact_name());
            orderId.setText(order.getOrder_id());
            orderTitle.setText(order.getProduct_title());
            orderItem.setText(order.getSelected_item());
            orderEmail.setText(order.getContact_email());
            orderPhone.setText(order.getContact_phone());
            orderTotal.setText(order.getTotal_price_Str());
            orderCount.setText(order.getFormatCountStr());
        }
    }
}
