package com.joy.app.utils.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.library.utils.LogMgr;
import com.joy.app.bean.map.MapPoiDetail;
import com.joy.app.bean.sample.PoiDetail;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author litong  <br>
 * @Description 地图页使用的    <br>
 */
public class MapUtil {

    public static void startGoogleMapApp(Activity mAct, PoiDetail mCurrItem) {
        if (mCurrItem == null) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
                        + mCurrItem.getLat() + "," + mCurrItem.getLon()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        i.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        mAct.startActivity(i);
    }

    public static void startGoogleMapApp(Activity mAct, MapPoiDetail mCurrItem) {
        if (mCurrItem == null) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
                        + mCurrItem.getLatitude() + "," + mCurrItem.getLongitude()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        i.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        mAct.startActivity(i);
    }

    public static void startMapApp(Activity mAct, PoiDetail mCurrItem) {

        Uri uri = Uri.parse("geo:" + mCurrItem.getLat() + ","
                + mCurrItem.getLon() + "?q=" + mCurrItem.getLat() + "," + mCurrItem.getLon());

        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        mAct.startActivity(it);
    }

    public static void startMapApp(Activity mAct, MapPoiDetail mCurrItem) {

        Uri uri = Uri.parse("geo:" + mCurrItem.getLatitude() + ","
                + mCurrItem.getLongitude() + "?q=" + mCurrItem.getLatitude() + "," + mCurrItem.getLongitude());

        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        mAct.startActivity(it);
    }

    public static void setMoveCameraAndZoomByOverlay(List<JoyMapOverlayItem> list, MapView mapview) {

        ArrayList<Double> latListSort = new ArrayList<>();
        ArrayList<Double> lngListSort = new ArrayList<>();
        for (JoyMapOverlayItem item : list) {
            latListSort.add(item.getPoint().getLatitude());
            lngListSort.add(item.getPoint().getLongitude());
        }
        setMoveCameraAndZoom(latListSort, lngListSort, mapview);
    }

    public void setMoveCameraAndZoomByPoi(List<MapPoiDetail> list, MapView mapview) {

        ArrayList<Double> latListSort = new ArrayList<>();
        ArrayList<Double> lngListSort = new ArrayList<>();
        for (MapPoiDetail item : list) {
            latListSort.add(item.getLatitude());
            lngListSort.add(item.getLongitude());
        }
        setMoveCameraAndZoom(latListSort, lngListSort, mapview);
    }

    public static void setMoveCameraAndZoom(List<Double> latListSort, List<Double> lngListSort, MapView mapview) {

        Collections.sort(latListSort, new LatComparator());
        Collections.sort(lngListSort, new LatComparator());

        double maxX = latListSort.get(0);
        double minX = latListSort.get(latListSort.size() - 1);
        double maxY = lngListSort.get(0);
        double minY = lngListSort.get(lngListSort.size() - 1);

        double[] middle = new double[]{(maxX + minX) / 2, (maxY + minY) / 2};

        DisplayMetrics metrics = mapview.getContext().getResources().getDisplayMetrics();
        int mWidth = metrics.widthPixels;
        int mHeight = metrics.heightPixels;

        float xLimit = mWidth / 2 - mWidth * 0.1f;
        float yLimit = mHeight / 2 - mHeight * 0.05f;

        int minLevel = 0;
        for (int i = 18; i >= 1; --i) {
            Point pCenter = TileSystem.LatLongToPixelXY(middle[0], middle[1],
                    i, null);
            Point pMin = TileSystem.LatLongToPixelXY(minX, minY, i, null);
            Point pMax = TileSystem.LatLongToPixelXY(maxX, maxY, i, null);

            if ((Math.abs(pCenter.x - pMin.x) < xLimit && Math.abs(pCenter.y
                    - pMin.y) < yLimit)
                    && (Math.abs(pMax.x - pCenter.x) < xLimit && Math
                    .abs(pMax.y - pCenter.y) < yLimit)) {
                minLevel = i;
                break;
            }
        }

        mapview.getController().setZoom(minLevel);

        mapview.getController().animateTo(new GeoPoint(middle[0], middle[1]));

        latListSort.clear();
        lngListSort.clear();
    }

    public static void initAccuracyLocation(AMapLocationClient locationClient, AMapLocationListener listener) {

        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        locationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        locationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(listener);
    }

    public static void showLocationInfor(AMapLocation aMapLocation){
        LogMgr.i("定位成功回调信息，设置相关消息");
        LogMgr.i("获取当前定位结果来源，如网络定位结果，详见定位类型表:"+aMapLocation.getLocationType());
        LogMgr.i("获取经度:"+aMapLocation.getLatitude()+" 获取纬度:"+aMapLocation.getLongitude()+" 获取精度信息:"+aMapLocation.getAccuracy());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(aMapLocation.getTime());
        LogMgr.i("定位时间："+df.format(date));

        LogMgr.i("地址："+aMapLocation.getAddress());
        LogMgr.i("国家信息："+aMapLocation.getCountry() +" 省信息:"+aMapLocation.getProvince()+" 城市信息:"+aMapLocation.getCity());

        //定位成功回调信息，设置相关消息
//        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//        aMapLocation.getLatitude();//获取经度
//        aMapLocation.getLongitude();//获取纬度
//        aMapLocation.getAccuracy();//获取精度信息

//        aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
//        aMapLocation.getCountry();//国家信息
//        aMapLocation.getProvince();//省信息
//        aMapLocation.getCity();//城市信息
//        aMapLocation.getDistrict();//城区信息
//        aMapLocation.getRoad();//街道信息
//        aMapLocation.getCityCode();//城市编码
//        aMapLocation.getAdCode();//地区编码
    }

    private static class LatComparator implements Comparator<Double> {

        @Override
        public int compare(Double lhs, Double rhs) {
            // an integer < 0 if lhs is less than rhs, 0 if they are equal, and
            // > 0 if lhs is greater than rhs.
            if (lhs < rhs) {
                return 1;
            } else if (lhs > rhs) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
