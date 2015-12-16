package com.joy.app.activity.plan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.library.activity.BaseHttpRvActivity;
import com.android.library.adapter.OnItemViewClickListener;
import com.android.library.adapter.OnItemViewLongClickListener;
import com.android.library.httptask.ObjectRequest;
import com.joy.app.R;
import com.joy.app.activity.map.ListPoiMapActivity;
import com.joy.app.activity.poi.PoiDetailActivity;
import com.joy.app.adapter.plan.PlanListAdapter;
import com.joy.app.bean.plan.PlanItem;
import com.joy.app.eventbus.DeleteEvent;
import com.joy.app.utils.http.PlanHtpUtil;
import com.joy.app.utils.plan.DialogUtil;
import com.joy.app.utils.plan.FolderRequestListener;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author litong  <br>
 * @Description 用户行程规划    <br>
 */
public class UserPlanListActivity extends BaseHttpRvActivity<List<PlanItem>> implements FolderRequestListener {


    private String mFolderID;
    private DialogUtil dialogUtil;

    public static void startActivityById(Activity act, String FolderID, String mFolderName, int code) {
        Intent intent = new Intent(act, UserPlanListActivity.class);
        intent.putExtra("FolderID", FolderID);
        intent.putExtra("FolderName", mFolderName);
        act.startActivityForResult(intent, code);
    }

    public static void startActivityById(Context act, String FolderID, String mFolderName) {
        Intent intent = new Intent(act, UserPlanListActivity.class);
        intent.putExtra("FolderID", FolderID);
        intent.putExtra("FolderName", mFolderName);
        act.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executeRefreshOnly();
    }

    @Override
    protected void initData() {

        mFolderID = getIntent().getStringExtra("FolderID");
    }

    @Override
    protected void initTitleView() {

        addTitleLeftBackView();
        setTitle(getIntent().getStringExtra("FolderName"));

        addTitleRightView(R.drawable.ic_plan_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRequest)return;
                dialogUtil.showDeleteFolderDialog(mFolderID);
            }
        });
        addTitleRightView(R.drawable.ic_plan_map, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRequest ||getAdapter() == null || getAdapter().getData() == null)
                    return;
                ListPoiMapActivity.startActivityByPoiList(UserPlanListActivity.this, ((PlanListAdapter) getAdapter()).getContent());
            }
        });
    }


    @Override
    protected void initContentView() {

        PlanListAdapter adapter = new PlanListAdapter();
        adapter.setOnItemViewClickListener(new OnItemViewClickListener<PlanItem>() {

            @Override
            public void onItemViewClick(int position, View clickView, PlanItem planItem) {
                if (isRequest)return;
                PoiDetailActivity.startActivity(UserPlanListActivity.this, planItem.getProduct_id());
            }
        });
        adapter.setOnItemViewLongClickListener(new OnItemViewLongClickListener<PlanItem>() {
            @Override
            public void onItemViewLongClick(int position, View clickView, PlanItem planItem) {
                if (isRequest)return;
                dialogUtil.showDeletePoiDialog(mFolderID,planItem.getProduct_id());
            }
        });
        setAdapter(adapter);
        getRecyclerView().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        dialogUtil = new DialogUtil(this, this);
    }
    boolean isRequest;
    @Override
    public void onRequest(dialog_category category, Object obj) {
        showLoading();
        isRequest = true;
    }

    @Override
    public void onSuccess(dialog_category category, Object obj) {
        hideLoading();
        isRequest = false;
        if (obj instanceof  String && obj.equals("Folder")){
            showToast("删除成功");
            setResult(Activity.RESULT_OK);
            finish();
        }else{
            showToast("删除成功");
            getAdapter().clear();
            executeRefreshOnly();
        }
        //首页需要刷新POI数量
        EventBus.getDefault().post(new DeleteEvent(DeleteEvent.DELETE_ORDER));
    }

    @Override
    public void onfaild(dialog_category category, String msg) {
        hideLoading();
        isRequest = false;
        showToast(msg);
    }

    @Override
    protected List<?> getListInvalidateContent(List<PlanItem> planItems) {
        return super.getListInvalidateContent(planItems);
    }

    @Override
    protected ObjectRequest<List<PlanItem>> getObjectRequest(int pageIndex, int pageLimit) {

        ObjectRequest obj = PlanHtpUtil.getUserPlanListRequest(mFolderID,pageLimit,pageIndex, PlanItem.class);
        return obj;

    }
}
