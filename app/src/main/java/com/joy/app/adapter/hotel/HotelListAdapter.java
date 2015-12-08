package com.joy.app.adapter.hotel;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.library.adapter.ExRvAdapter;
import com.android.library.adapter.ExRvMultipleAdapter;
import com.android.library.adapter.ExRvViewHolder;
import com.android.library.adapter.ExViewHolder;
import com.android.library.utils.CollectionUtil;
import com.android.library.widget.JTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joy.app.R;
import com.joy.app.bean.hotel.HotelEntity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author litong  <br>
 * @Description 酒店列表    <br>
 */
public class HotelListAdapter extends ExRvMultipleAdapter {

    public HotelListAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderView(ViewGroup parent) {
        return new HeaderViewHolder(inflate(parent, R.layout.item_hotel_header));
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentView(ViewGroup parent) {
        return new ViewHolder(inflate(parent, R.layout.item_hotel_search_list));
    }

    @Override
    public RecyclerView.ViewHolder onCreateBottomView(ViewGroup parent) {
        return null;
    }

    @Override
    public int getContentItemCount() {
        return CollectionUtil.isEmpty(getData()) ? 0 : getData().size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            int pos = position - mHeaderCount;
            ((ViewHolder) holder).invalidateItemView(pos,(HotelEntity) getData().get(pos));
        }
    }

    public class HeaderViewHolder extends ExRvViewHolder<Object>{

        public HeaderViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    callbackOnItemViewClickListener(getLayoutPosition(), itemView);
                }
            });
        }

        @Override
        protected void invalidateItemView(int position, Object o) {

        }
    }
    public class ViewHolder extends ExRvViewHolder<HotelEntity>  {
        @Bind(R.id.topline)
        View topline;

        @Bind(R.id.iv_hotel_item_Photo)
        SimpleDraweeView ivHotelItemPhoto;
        @Bind(R.id.fl_image_container)
        FrameLayout flImageContainer;
        @Bind(R.id.tv_hotel_city_cnname)
        JTextView tvHotelCityCnname;
        @Bind(R.id.tv_hotel_city_enname)
        JTextView tvHotelCityEnname;
        @Bind(R.id.ll_infos)
        LinearLayout llInfos;
        @Bind(R.id.tv_hotel_city_star)
        JTextView tvHotelCityStar;
        @Bind(R.id.tv_hotel_item_price)
        JTextView tvHotelItemPrice;
        @Bind(R.id.linearLayout)
        LinearLayout linearLayout;

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
        protected void invalidateItemView(int position, HotelEntity hotelEntity) {
            ivHotelItemPhoto.setImageURI(Uri.parse(hotelEntity.getPhoto()));
            tvHotelCityCnname.setText(hotelEntity.getCnname());
            tvHotelCityEnname.setText(hotelEntity.getEnname());
            tvHotelItemPrice.setText(hotelEntity.getPrice()+"");
            tvHotelCityStar.setText(hotelEntity.getStar());
        }
    }
}
