package goixeom.com.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.FontCache;
import goixeom.com.R;
import goixeom.com.adapters.AdapterNews;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.News;
import retrofit2.Call;

public class NewsActivity extends BaseActivity implements OnResponse<Integer>{
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.nest_scroll)
    NestedScrollView nestScroll;
    @BindView(R.id.rcv)
    RecyclerView rcv;

    private AdapterNews adapter;
    private List<News> listNews;

    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    protected void onSoketConnected() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        makeCollapsingToolbarLayoutLooksGood(toolbar_layout);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setHasFixedSize(true);
        listNews = new ArrayList<>();
        adapter = new AdapterNews(this, listNews,this);
        rcv.setAdapter(adapter);
        rcv.setNestedScrollingEnabled(false);
        getNews();
    }

    private void getNews() {
        Call<ApiResponse<List<News>>> call = getmApi().getNews(ApiConstants.API_KEY);
        call.enqueue(new CallBackCustom<ApiResponse<List<News>>>(this, new OnResponse<ApiResponse<List<News>>>() {
            @Override
            public void onResponse(ApiResponse<List<News>> object) {
                loading.setVisibility(View.GONE);
                if (object.getData() != null) {
                    listNews.addAll(object.getData());
                    adapter.notifyDataSetChanged();
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    private void makeCollapsingToolbarLayoutLooksGood(CollapsingToolbarLayout collapsingToolbarLayout) {
        collapsingToolbarLayout.setTitle(getTitle());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTypeface(FontCache.getTypeface(this, "MyriadPro-Regular.otf"));
        collapsingToolbarLayout.setCollapsedTitleTypeface(FontCache.getTypeface(this, "MyriadPro-Regular.otf"));
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onResponse(Integer object) {
        rcv.scrollToPosition(object);
    }
}
