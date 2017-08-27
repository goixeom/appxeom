package goixeom.com.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.LogUtils;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.FontCache;
import goixeom.com.R;
import goixeom.com.adapters.AdapterHistory;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.History;
import goixeom.com.utils.Constants;
import goixeom.com.views.DemoLoadMoreView;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class HistoryActivity extends BaseActivity {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rcv)
    PullToRefreshRecyclerView rcv;
    @BindView(R.id.loading)
    ProgressBar loading;
    List<History> list = new ArrayList<>();
    AdapterHistory adapterHistory;
    int page = 0;
    @BindView(R.id.img_empty)
    ImageView imgEmpty;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    private boolean needToHold;
    private boolean isFirstTime = true;

    public HistoryActivity() {
    }

    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    protected void onSoketConnected() {

    }
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
        setContentView(R.layout.content_history);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        makeCollapsingToolbarLayoutLooksGood(toolbar_layout);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        adapterHistory = new AdapterHistory(this, list);
//        rcv.setLoadmoreString("Đang tải");
        rcv.removeHeader();
        DemoLoadMoreView loadMoreView = new DemoLoadMoreView(this, rcv.getRecyclerView());
        loadMoreView.setLoadmoreString("Đang tải");
        loadMoreView.setLoadMorePadding(100);
        rcv.setLoadMoreFooter(loadMoreView);
        loading.setVisibility(View.GONE);
        rcv.setLoadMoreCount(5);
        rcv.setAdapter(adapterHistory);
        rcv.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                //do loadmore here
                page++;
                getListPromotion();
            }
        });
        rcv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // do refresh here
                page = 0;
                getListPromotion();
                needToHold = false;
            }
        });
        getListPromotion();
    }

    private void getListPromotion() {
        getDialogProgress().showDialog();
        Call<ApiResponse<List<History>>> getPromotions = getmApi().getHistories(ApiConstants.API_KEY, getmSetting().getString(Constants.ID), page);
        getPromotions.enqueue(new CallBackCustom<ApiResponse<List<History>>>(this, getDialogProgress(), new OnResponse<ApiResponse<List<History>>>() {
            @Override
            public void onResponse(ApiResponse<List<History>> object) {
                loading.setVisibility(View.GONE);
//                if(!needToHold) {
//                    list.clear();
////                    rcv.setSwipeEnable(true);
//                }
                if (object != null && object.getData() != null && object.getData().size() != 0) {

                    list.addAll(object.getData());
                    rcv.onFinishLoading(true, false);
                    imgEmpty.setVisibility(View.GONE);
                    isFirstTime = false;
                } else {
                    LogUtils.e("No more");
                    rcv.onFinishLoading(false, false);
                    if (isFirstTime)
                        imgEmpty.setVisibility(View.VISIBLE);
                }
                rcv.setOnRefreshComplete();

            }
        }));
    }


    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
