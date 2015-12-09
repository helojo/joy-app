package com.joy.app.activity.hotel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.android.library.activity.BaseHttpUiActivity;
import com.android.library.adapter.OnItemViewClickListener;
import com.android.library.httptask.ObjectRequest;
import com.android.library.utils.LogMgr;
import com.android.library.utils.TextUtil;
import com.android.library.view.ExBaseWidget;
import com.android.library.view.dialogplus.DialogPlus;
import com.android.library.view.dialogplus.ListHolder;
import com.android.library.view.dialogplus.OnClickListener;
import com.android.library.view.dialogplus.ViewHolder;
import com.joy.app.R;
import com.joy.app.activity.common.DayPickerActivity;
import com.joy.app.adapter.hotel.HotelSortAdapter;
import com.joy.app.bean.hotel.FilterItems;
import com.joy.app.bean.hotel.HotelParams;
import com.joy.app.bean.hotel.HotelSearchFilters;
import com.joy.app.utils.Consts;
import com.joy.app.utils.hotel.HotelTimeUtil;
import com.joy.app.utils.http.HotelHtpUtil;

import java.util.Map;

/**
 * @author litong  <br>
 * @Description 城市酒店列表    <br>
 */
public class CityHotelListActivity extends BaseHttpUiActivity<HotelSearchFilters> implements ExBaseWidget.OnWidgetViewClickListener {
    HotelSearchFilters hotelSearchFilters;
    HotelParams params;
    HotelListFragment hotelListFragment;
    final int REQ_DAY_PICK = 101;
    final int REQ_FILTER = 102;
    FooterWidget footer;

    /**
     * @param activity
     * @param cityId
     * @param cityName
     * @param fromKey  只传入城市信息和Aid
     *                 默认时间为今天和明天
     *                 无onresult的返回
     */
    public static void startActivity(Activity activity, String cityId, String cityName, String fromKey) {

        long[] days = HotelTimeUtil.getLastOffsetDay(Consts.MONTH_DAY_OFFSET);

        startActivity(activity, TextUtil.filterEmpty(cityId,"50"), TextUtil.filterEmpty(cityName,"香港"), fromKey, days[0], days[1]);
    }

    public static void startActivity(Activity activity, String cityId, String cityName, String fromKey, long startDay, long endDay) {

        Intent intent = new Intent(activity, CityHotelListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("cityName", cityName);
        bundle.putString("cityId", cityId);
        bundle.putLong("startDay", startDay);
        bundle.putLong("endDay", endDay);
        bundle.putString("fromKey", fromKey);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_DAY_PICK == requestCode && resultCode == RESULT_OK) {

            long start = data.getLongExtra(DayPickerActivity.REQ_EXTRA_KEY_BEGIN_DATE, 0);
            long end = data.getLongExtra(DayPickerActivity.REQ_EXTRA_KEY_END_DATE, 0);
            footer.initDate(start, end);
            params.setCheckInMills(start);
            params.setCheckOutMills(end);
            hotelListFragment.reLoadHotelList(params);
        } else if (REQ_FILTER == requestCode && resultCode == RESULT_OK) {
            String facilities = data.getStringExtra(HotelSearchFilterActivity.EX_KEY_HOTEL__FACILITIES_TYPE_STR);
            String price[] = data.getStringArrayExtra(HotelSearchFilterActivity.EX_KEY_HOTEL_PRICES_TYPE);
            String star = data.getStringExtra(HotelSearchFilterActivity.EX_KEY_HOTEL_STAR_TYPE_STR);
            params.setFacilities_ids(facilities);
            params.setStar_ids(star);
            params.setPrice_rangs(price);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_hotel_list);
        executeRefreshOnly();
    }

    @Override
    protected void initData() {
        params = new HotelParams();
        params.setCityId(getIntent().getStringExtra("cityId"));
        params.setFrom_key(getIntent().getStringExtra("fromKey"));
        params.setCheckIn(getIntent().getLongExtra("startDay", System.currentTimeMillis()));
        params.setCheckOut(getIntent().getLongExtra("endDay", System.currentTimeMillis()));
    }

    @Override
    protected void initTitleView() {
        addTitleLeftBackView();
        addTitleMiddleView(getIntent().getStringExtra("cityName") + "酒店");
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        footer = new FooterWidget(this);
        footer.setContentView(findViewById(R.id.llFooterView));
        footer.initDate(params.getCheckInMills(), params.getCheckOutMills());
        footer.setOnWidgetViewClickListener(this);
        hotelListFragment = HotelListFragment.instantiate(this, params);
        addFragment(R.id.fl_content, hotelListFragment);
    }

    DialogPlus mDialog;
    private void showDialog() {
        HotelSortAdapter adapter = new HotelSortAdapter();
        adapter.setData(hotelSearchFilters.getOrderby());
        adapter.setSelect(params.getOrderby());
        adapter.setOnItemViewClickListener(new OnItemViewClickListener<FilterItems>() {
            @Override
            public void onItemViewClick(int position, View clickView, FilterItems filterItems) {
                LogMgr.i("filterItems:"+filterItems);
                params.setOrderby(filterItems.getValue());
                mDialog.dismiss();
                hotelListFragment.reLoadHotelList(params);
            }
        });
        mDialog = DialogPlus.newDialog(this)
                .setContentHolder(new ListHolder())
                .setCancelable(true)
                .setAdapter(adapter)
                .create();
        mDialog.show();
    }

    @Override
    protected boolean invalidateContent(HotelSearchFilters hotelSearchFilters) {
        this.hotelSearchFilters = hotelSearchFilters;
        return true;
    }

    @Override
    protected ObjectRequest getObjectRequest() {

        return HotelHtpUtil.getFilterRequest(HotelSearchFilters.class);
    }

    @Override
    public void onWidgetViewClick(View v) {
        switch (v.getId()) {
            case R.id.rl_select_time:
                DayPickerActivity.startHotelDayPickerForResult(this, true, params.getCheckInMills(), params.getCheckOutMills(), REQ_DAY_PICK);
                break;
            case R.id.ll_hotel_filter:
                //TODO
                HotelSearchFilterActivity.startActivityForResult(this,REQ_FILTER,hotelSearchFilters.getFacilities(),hotelSearchFilters.getStars(),params.getFacilities_ids(),params.getStar_ids(),params.getPrice());
                break;
            case R.id.ll_hotel_list_sort:
                showDialog();
            default:

        }

    }
}
