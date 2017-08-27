package goixeom.com.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import goixeom.com.FontCache;
import goixeom.com.R;
import goixeom.com.adapters.AdapterNotification;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.NotificationData;
import goixeom.com.utils.Constants;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class NotificationActivity extends BaseActivity {


    Unbinder unbinder;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rcv)
    RecyclerView rcv;
    AdapterNotification adapterNotification;
    List<NotificationData> list;
    @BindView(R.id.img_empty)
    ImageView imgEmpty;

    public NotificationActivity() {
    }

    @Override
    public void pingNotification(String title, String content) {
        getNotification();
    }

    @Override
    protected void onSoketConnected() {

    }
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    private void makeCollapsingToolbarLayoutLooksGood(CollapsingToolbarLayout collapsingToolbarLayout) {
        collapsingToolbarLayout.setTitle(getTitle());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTypeface(FontCache.getTypeface(this,"MyriadPro-Regular.otf"));
        collapsingToolbarLayout.setCollapsedTitleTypeface(FontCache.getTypeface(this,"MyriadPro-Regular.otf"));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list_notification);
        unbinder = ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        makeCollapsingToolbarLayoutLooksGood(toolbar_layout);
        list = new ArrayList<>();
        adapterNotification = new AdapterNotification(this, list);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setHasFixedSize(true);
        rcv.setAdapter(adapterNotification);
        getNotification();
    }

    private void getNotification() {
        getDialogProgress().showDialog();
        Call<ApiResponse<List<NotificationData>>> getNoti = getmApi().getListNotification(ApiConstants.API_KEY, getmSetting().getString(Constants.ID));
        getNoti.enqueue(new CallBackCustom<ApiResponse<List<NotificationData>>>(this, getDialogProgress(), new OnResponse<ApiResponse<List<NotificationData>>>() {
            @Override
            public void onResponse(ApiResponse<List<NotificationData>> object) {
                if (object.getData() != null) {
                    list.clear();
                    list.addAll(object.getData());
                    adapterNotification.notifyDataSetChanged();
                    if (object.getData().size() > 0) {
                        imgEmpty.setVisibility(View.GONE);

                    } else {
                        imgEmpty.setVisibility(View.VISIBLE);
                    }
                }else{
                    imgEmpty.setVisibility(View.VISIBLE);

                }
            }
        }));
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
