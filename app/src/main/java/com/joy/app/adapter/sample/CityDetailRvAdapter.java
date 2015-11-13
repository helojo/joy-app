package com.joy.app.adapter.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.joy.app.R;
import com.joy.app.bean.sample.CityDetail;
import com.joy.app.bean.sample.Trip;
import com.joy.library.adapter.frame.ExRvMultipleAdapter;
import com.joy.library.utils.DimenCons;
import com.joy.library.utils.LogMgr;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by KEVIN.DAI on 15/11/12.
 */
public class CityDetailRvAdapter extends ExRvMultipleAdapter {

    private CityDetail mCityDetail;
    private int mPaletteColor = -1;
    private RecyclerView mAttachedView;

    public CityDetailRvAdapter(Context context) {

        super(context);
        mHeaderCount = 1;
        mBottomCount = 1;
    }

    public void setAttachedView(RecyclerView v) {

        mAttachedView = v;
    }

    public void setData(CityDetail data) {

        mCityDetail = data;
    }

    @Override
    public int getContentItemCount() {

        return mCityDetail == null ? 0 : mCityDetail.getNew_trip().size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {

            if (mCityDetail != null) {

                ArrayList<String> photos = mCityDetail.getPhotos();
                final HeaderViewHolder vh = ((HeaderViewHolder) holder);

                if (mPaletteColor == -1) {

                    Postprocessor processor = new BasePostprocessor() {

                        @Override
                        public CloseableReference<Bitmap> process(final Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {

                            Palette.generateAsync(sourceBitmap, new Palette.PaletteAsyncListener() {

                                @Override
                                public void onGenerated(Palette palette) {

                                    if (palette != null) {

                                        Palette.Swatch vibrant = palette.getMutedSwatch();
                                        if (vibrant != null) {

                                            mPaletteColor = vibrant.getRgb();
                                            mAttachedView.setBackgroundColor(mPaletteColor);
                                            vh.sdvPhoto.setImageBitmap(sourceBitmap);
                                        }
                                    }
                                }
                            });
                            return null;
                        }

                        @Override
                        public void process(Bitmap destBitmap, Bitmap sourceBitmap) {

//                            super.process(destBitmap, sourceBitmap);
                        }

                        @Override
                        public void process(Bitmap bitmap) {

//                            super.process(bitmap);
                        }
                    };
                    ImageRequest request = ImageRequestBuilder
                            .newBuilderWithSource(Uri.parse(photos.get(0)))
                            .setPostprocessor(processor)
                            .build();
                    PipelineDraweeController controller = (PipelineDraweeController) Fresco
                            .newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(vh.sdvPhoto.getController())
                            .build();
                    vh.sdvPhoto.setController(controller);
                } else {

                    vh.sdvPhoto.setImageURI(Uri.parse(photos.get(0)));
                }

                vh.tvName.setText(mCityDetail.getChinesename() + "\n" + mCityDetail.getEnglishname());
                vh.sdvSubPhoto1.setImageURI(Uri.parse(photos.get(1)));
                vh.sdvSubPhoto2.setImageURI(Uri.parse(photos.get(2)));
                vh.sdvSubPhoto3.setImageURI(Uri.parse(photos.get(3)));
                vh.sdvSubPhoto4.setImageURI(Uri.parse(photos.get(4)));
            }
        } else if (holder instanceof ContentViewHolder) {

            if (mCityDetail != null) {

                ArrayList<Trip> trips = mCityDetail.getNew_trip();
                int pos = position - mHeaderCount;
                ContentViewHolder vh = ((ContentViewHolder) holder);
                vh.sdvPhoto.setImageURI(Uri.parse(trips.get(pos).getPhoto()));
                vh.tvTitle.setText(trips.get(pos).getTitle());
                vh.sdvAvatar.setImageURI(Uri.parse(trips.get(pos).getAvatar()));
                vh.tvName.setText(trips.get(pos).getUsername());
            }
        } else if (holder instanceof BottomViewHolder) {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderView(ViewGroup parent) {

        return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.t_header_view_detail, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentView(ViewGroup parent) {

        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.t_item_card_rv_detail, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateBottomView(ViewGroup parent) {

        View view = new View(parent.getContext());
        view.setMinimumHeight(DimenCons.DP_1_PX * 16);
        return new BottomViewHolder(view);
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sdvPhoto)
        SimpleDraweeView sdvPhoto;
        @Bind(R.id.tvName)
        TextView tvName;
        @Bind(R.id.sdvSubPhoto1)
        SimpleDraweeView sdvSubPhoto1;
        @Bind(R.id.sdvSubPhoto2)
        SimpleDraweeView sdvSubPhoto2;
        @Bind(R.id.sdvSubPhoto3)
        SimpleDraweeView sdvSubPhoto3;
        @Bind(R.id.sdvSubPhoto4)
        SimpleDraweeView sdvSubPhoto4;

        HeaderViewHolder(View view) {

            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.tvName)
        void onItemClick() {

            if (LogMgr.isDebug())
                LogMgr.d("HeaderViewHolder", "onClick--> position = " + getPosition());
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sdvPhoto)
        SimpleDraweeView sdvPhoto;
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.sdvAvatar)
        SimpleDraweeView sdvAvatar;
        @Bind(R.id.tvName)
        TextView tvName;

        ContentViewHolder(View view) {

            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.tvName)
        void onItemClick() {

            if (LogMgr.isDebug())
                LogMgr.d("ContentViewHolder", "onClick--> position = " + getPosition());
        }
    }

    public static class BottomViewHolder extends RecyclerView.ViewHolder {

        BottomViewHolder(View view) {

            super(view);
        }
    }
}