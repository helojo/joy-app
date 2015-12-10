package com.joy.app.bean.sample;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.library.utils.TextUtil;

import java.util.ArrayList;

/**
 * 目的地详情
 * <p/>
 * Created by xiaoyu.chen on 15/11/11.
 * 加Parcelable，打开地图时候需要传递这个对象----李彤
 */
public class PoiDetail implements Parcelable {

    private String product_id = TextUtil.TEXT_EMPTY;
    private String title = TextUtil.TEXT_EMPTY;
    private String sub_title = TextUtil.TEXT_EMPTY;
    private String comment_level = TextUtil.TEXT_EMPTY;//评论星级
    private String comment_num = TextUtil.TEXT_EMPTY;//评论数
    private String price = TextUtil.TEXT_EMPTY;//价格范围	格式：200-300
    private String lat = TextUtil.TEXT_EMPTY;
    private String lon = TextUtil.TEXT_EMPTY;
    private String folder_name	 = TextUtil.TEXT_EMPTY;//收藏的文件夹名称
    private String folder_id = TextUtil.TEXT_EMPTY;//收藏文件夹的id
    private ArrayList<String> highlights;//项目亮点
    private String introduction = TextUtil.TEXT_EMPTY;//项目简介
    private String is_book = TextUtil.TEXT_EMPTY;//是否可预订 1可 0否
    private String booking_before = TextUtil.TEXT_EMPTY;//提前几天预订
    private ArrayList<String> photos;//图片地址
    private String purchase_info = TextUtil.TEXT_EMPTY;//购买须知url


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getComment_level() {
        return comment_level;
    }

    public void setComment_level(String comment_level) {
        this.comment_level = comment_level;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public ArrayList<String> getHighlights() {
        return highlights;
    }

    public void setHighlights(ArrayList<String> highlights) {
        this.highlights = highlights;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public boolean getIs_book() {
        return "1".equals(is_book);
    }

    public void setIs_book(String is_book) {
        this.is_book = is_book;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getPurchase_info() {
        return purchase_info;
    }

    public void setPurchase_info(String purchase_info) {
        this.purchase_info = purchase_info;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public String getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(String folder_id) {
        this.folder_id = folder_id;
    }

    public String getBooking_before() {
        return booking_before;
    }

    public void setBooking_before(String booking_before) {
        this.booking_before = booking_before;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_id);
        dest.writeString(this.title);
        dest.writeString(this.sub_title);
        dest.writeString(this.comment_level);
        dest.writeString(this.comment_num);
        dest.writeString(this.price);
        dest.writeString(this.lat);
        dest.writeString(this.lon);
        dest.writeString(this.folder_name);
        dest.writeString(this.folder_id);
        dest.writeStringList(this.highlights);
        dest.writeString(this.introduction);
        dest.writeString(this.is_book);
        dest.writeString(this.booking_before);
        dest.writeStringList(this.photos);
        dest.writeString(this.purchase_info);
    }

    public PoiDetail() {
    }

    protected PoiDetail(Parcel in) {
        this.product_id = in.readString();
        this.title = in.readString();
        this.sub_title = in.readString();
        this.comment_level = in.readString();
        this.comment_num = in.readString();
        this.price = in.readString();
        this.lat = in.readString();
        this.lon = in.readString();
        this.folder_name = in.readString();
        this.folder_id = in.readString();
        this.highlights = in.createStringArrayList();
        this.introduction = in.readString();
        this.is_book = in.readString();
        this.booking_before = in.readString();
        this.photos = in.createStringArrayList();
        this.purchase_info = in.readString();
    }

    public static final Parcelable.Creator<PoiDetail> CREATOR = new Parcelable.Creator<PoiDetail>() {
        public PoiDetail createFromParcel(Parcel source) {
            return new PoiDetail(source);
        }

        public PoiDetail[] newArray(int size) {
            return new PoiDetail[size];
        }
    };
}
