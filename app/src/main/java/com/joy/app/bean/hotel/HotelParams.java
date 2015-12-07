package com.joy.app.bean.hotel;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.library.utils.TimeUtil;

import java.io.Serializable;

/**
 * @author litong  <br>
 * @Description 酒店筛选    <br>
 */
public class HotelParams implements Serializable {

    String cityId,
            checkIn,//入住时间	2004-02-12
            checkOut,//离开时间	2004-02-13
            From_key,//aid	booking跟踪来源
            hotel,//酒店名
            star_ids,//过滤星级
            price_rangs,//价格区间	逗号分割高低价格
            facilities_ids;//过滤标签
    int Orderby,//排序方式 默认智能排序
            area_id;//区域id

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }
    public void setCheckIn(long checkIn) {
        this.checkIn = TimeUtil.getSimpleTime(checkIn);
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
    public void setCheckOut(long checkOut) {
        this.checkOut = TimeUtil.getSimpleTime(checkOut);
    }

    public String getFrom_key() {
        return From_key;
    }

    public void setFrom_key(String from_key) {
        From_key = from_key;
    }

    public void setOrderby(int orderby) {
        Orderby = orderby;
    }

    public int getOrderby() {
        return Orderby;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getStar_ids() {
        return star_ids;
    }

    public void setStar_ids(String star_ids) {
        this.star_ids = star_ids;
    }

    public String getPrice_rangs() {
        return price_rangs;
    }

    public void setPrice_rangs(String price_rangs) {
        this.price_rangs = price_rangs;
    }

    public String getFacilities_ids() {
        return facilities_ids;
    }

    public void setFacilities_ids(String facilities_ids) {
        this.facilities_ids = facilities_ids;
    }

}