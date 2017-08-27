package goixeom.com.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.FontCache;
import goixeom.com.R;
import goixeom.com.utils.Constants;

public class ActivityPrivacy extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    private void makeCollapsingToolbarLayoutLooksGood(CollapsingToolbarLayout collapsingToolbarLayout) {
        boolean isTerm = getIntent().getBooleanExtra(Constants.MSG,false);
        if(isTerm){
            tvTitle.setText("Điều khoản và điều kiện");
            collapsingToolbarLayout.setTitle("Điều khoản và điều kiện");
            web.loadUrl("file:///android_asset/term.html");
        }else{
            tvTitle.setText("Quyền riêng tư");
            collapsingToolbarLayout.setTitle("Quyền riêng tư");
            web.loadUrl("file:///android_asset/privacy.html");
        }
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTypeface(FontCache.getTypeface(this,"MyriadPro-Regular.otf"));
        collapsingToolbarLayout.setCollapsedTitleTypeface(FontCache.getTypeface(this,"MyriadPro-Regular.otf"));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        makeCollapsingToolbarLayoutLooksGood(toolbar_layout);


    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
