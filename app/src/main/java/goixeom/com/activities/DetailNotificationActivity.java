package goixeom.com.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.CustomTextView;
import goixeom.com.FontCache;
import goixeom.com.R;
import goixeom.com.models.NotificationData;
import goixeom.com.utils.Constants;

public class DetailNotificationActivity extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tv_date)
    CustomTextView tvDate;
    @BindView(R.id.tv_title)
    CustomTextView tvTitle;
    @BindView(R.id.tv_content)
    CustomTextView tvContent;
    @BindView(R.id.tv_back)
    CustomTextView tvBack;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.tv_time)
    CustomTextView tvTime;

    private void makeCollapsingToolbarLayoutLooksGood(CollapsingToolbarLayout collapsingToolbarLayout) {
        collapsingToolbarLayout.setTitle(getTitle());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTypeface(FontCache.getTypeface(this, "MyriadPro-Regular.otf"));
        collapsingToolbarLayout.setCollapsedTitleTypeface(FontCache.getTypeface(this, "MyriadPro-Regular.otf"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notification);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        makeCollapsingToolbarLayoutLooksGood(toolbarLayout);
        if (getIntent() != null) {
            Bundle b = getIntent().getBundleExtra(Constants.BUNDLE);
            NotificationData data = b.getParcelable(Constants.MSG);
            if (data != null) {
                tvContent.setText(data.getContent());
                tvDate.setText("Ngày " + data.getDate());
                tvTitle.setText(data.getTitle());
                tvTime.setText("Lúc "+data.getTime());
            }
        }
    }

    @OnClick({R.id.img_back, R.id.tv_back})
    public void onViewClicked(View view) {
        onBackPressed();
    }
}
