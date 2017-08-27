package goixeom.com.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import goixeom.com.CustomTextView;
import goixeom.com.FontCache;
import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.utils.CommonUtils;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class AboutUsActivity extends BaseActivity {


    @BindView(R.id.tv_tilte_notification)
    CustomTextView tvTilteNotification;
    @BindView(R.id.notification_txt)
    CustomTextView notificationTxt;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.ll_rate_app)
    RelativeLayout llRateApp;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.ll_invite)
    RelativeLayout llInvite;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.feedback)
    RelativeLayout feedback;
    Unbinder unbinder;
    @BindView(R.id.img_back)
    ImageView imgBack;


    public AboutUsActivity() {
    }

    @Override
    public void pingNotification(String title, String content) {
        tvTilteNotification.setText(title);
        notificationTxt.setText(content);
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
        setContentView(R.layout.content_notification);
        unbinder = ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        makeCollapsingToolbarLayoutLooksGood(toolbar_layout);
        getNotification();
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvTilteNotification.setText(String.format("Version %s", version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_rate_app, R.id.ll_invite, R.id.feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_rate_app:
                CommonUtils.launchMarket(this, this.getPackageName());
                break;
            case R.id.ll_invite:
                CommonUtils.shareSimpleText("https://play.google.com/store/apps/details?id=" + this.getPackageName(), this);
                break;
            case R.id.feedback:
                startActivity(new Intent(AboutUsActivity.this, FAQActivity.class));
                break;
        }
    }


    private void getNotification() {
        getDialogProgress().showDialog();
        Call<ApiResponse<String>> getNoti = getmApi().getInfoApp(ApiConstants.API_KEY);
        getNoti.enqueue(new CallBackCustom<ApiResponse<String>>(this, getDialogProgress(), new OnResponse<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> object) {
                if (object.getData() != null) {
                    notificationTxt.setText(object.getData());
                }
            }
        }));
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
