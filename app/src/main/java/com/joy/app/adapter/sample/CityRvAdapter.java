package com.joy.app.adapter.sample;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.joy.app.R;
import com.joy.app.bean.sample.HotCityItem;
import com.joy.library.adapter.frame.ExRvAdapter;
import com.joy.library.adapter.frame.ExRvViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by KEVIN.DAI on 15/11/10.
 */
public class CityRvAdapter extends ExRvAdapter<CityRvAdapter.ViewHolder, HotCityItem> {

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HotCityItem data = getItem(position);
        holder.sdvPhoto.setImageURI(Uri.parse(data.getPhoto()));
        holder.tvName.setText(data.getCnname());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(inflate(parent, R.layout.t_item_listview));
    }

    public class ViewHolder extends ExRvViewHolder {

        @Bind(R.id.sdvPhoto) SimpleDraweeView sdvPhoto;
        @Bind(R.id.tvName)   TextView         tvName;

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    callbackOnItemViewClickListener(getLayoutPosition(), v);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    callbackOnItemViewLongClickListener(getLayoutPosition(), v);
                    return true;
                }
            });
        }
    }
}