package com.joy.app.utils.http;

import android.os.Bundle;

import com.android.library.httptask.ObjectRequest;
import com.android.library.utils.TextUtil;
import com.joy.app.bean.hotel.HotelParams;

import java.util.Map;

/**
 * @author litong  <br>
 * @Description 酒店    <br>
 */
public class HotelHtpUtil extends BaseHtpUtil {

    public static ObjectRequest getHotelListRequest(HotelParams hotelParams, int page, int count, Class clazz) {

        Map<String, String> params = getBaseParams();
        params.put("checkin", hotelParams.getCheckIn()); 
        params.put("checkout", hotelParams.getCheckOut()); 
        params.put("from_key", hotelParams.getFrom_key()); 
        params.put("orderby", String.valueOf(hotelParams.getOrderby())); 
        if (!TextUtil.isEmpty(hotelParams.getHotel())) { 
            params.put("hotel", hotelParams.getHotel()); }
          if (hotelParams.getArea_id() != 0) { 
            params.put("area_id", String.valueOf(hotelParams.getArea_id())); }  
        if (!TextUtil.isEmpty(hotelParams.getFacilities_ids())) { 
            params.put("facilities_ids", hotelParams.getFacilities_ids()); } 
        if (!TextUtil.isEmpty(hotelParams.getStar_ids())) { 
            params.put("star_ids", hotelParams.getStar_ids());}  
        if (!TextUtil.isEmpty(hotelParams.getPrice_rangs())) { 
            params.put("price_rangs", hotelParams.getPrice_rangs()); }   
        params.put(KEY_COUNT, count + "");
        params.put(KEY_PAGE, page + "");

        return createGetRequest(URL_GET_HOTEL_LIST, params, clazz);
    }

    /**
     *
     *
     * @return
     */
    public static ObjectRequest getFilterRequest(Class clazz) {

        Map<String, String> params = getBaseParams();

        return createGetRequest(URL_GET_SEARCH_FILTERS, params, clazz);

    }
}
