package com.joy.app.adapter.poi;

import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.library.adapter.ExRvAdapter;
import com.android.library.adapter.ExRvViewHolder;
import com.android.library.utils.MathUtil;
import com.joy.app.R;
import com.joy.app.bean.poi.CommentItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品评论列表adapter
 * Created by xiaoyu.chen on 15/11/20.
 */
public class CommentRvAdapter extends ExRvAdapter<CommentRvAdapter.ViewHolder, CommentItem> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(inflate(parent, R.layout.item_poi_comment));
    }

    public class ViewHolder extends ExRvViewHolder<CommentItem> {

        @BindView(R.id.acRatingBar)
        AppCompatRatingBar acRatingBar;

        @BindView(R.id.tvComment)
        TextView tvComment;

        @BindView(R.id.tvCommentUserDate)
        TextView tvCommentUserDate;

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void invalidateItemView(int position, CommentItem commentItem) {

            if (commentItem != null) {

                acRatingBar.setRating(MathUtil.parseFloat(commentItem.getComment_level(), 0));
                tvComment.setText(commentItem.getComment());
                tvCommentUserDate.setText(commentItem.getComment_user() + " - " + commentItem.getComment_date());
            }
        }
    }
}
