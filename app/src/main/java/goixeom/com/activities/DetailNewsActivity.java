package goixeom.com.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.CustomTextView;
import goixeom.com.FontCache;
import goixeom.com.R;
import goixeom.com.models.News;
import goixeom.com.utils.Constants;

public class DetailNewsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.img_news)
    ImageView imgNews;
    @BindView(R.id.tv_news_photo_date)
    CustomTextView tvNewsPhotoDate;
    @BindView(R.id.tv_title_news_photo)
    CustomTextView tvTitleNewsPhoto;
    @BindView(R.id.tv_content_news)
    CustomTextView tvContentNews;
    @BindView(R.id.ll_card)
    CardView llCard;
    @BindView(R.id.nest_scroll)
    NestedScrollView nestScroll;

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
        setContentView(R.layout.activity_detail_news);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        makeCollapsingToolbarLayoutLooksGood(toolbarLayout);
        if (getIntent() != null) {
            Bundle b = getIntent().getBundleExtra(Constants.BUNDLE);
            News item = b.getParcelable(Constants.MSG);
            if (item != null) {
                tvContentNews.setText(item.getContent());
                tvNewsPhotoDate.setText(item.getDate());
                tvTitleNewsPhoto.setText(item.getTitle());
                Picasso.with(this).load(item.getImage()).into(imgNews);
            }
        }
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
