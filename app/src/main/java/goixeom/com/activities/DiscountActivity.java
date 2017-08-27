package goixeom.com.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.FontCache;
import goixeom.com.R;
import goixeom.com.adapters.AdapterDiscount;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.Discount;
import goixeom.com.utils.Constants;
import retrofit2.Call;

public class DiscountActivity extends BaseActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.rcv_promotion)
    RecyclerView rcvPromotion;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.img_empty)
    ImageView imgEmpty;
    private List<Discount> listDiscounts;
    private AdapterDiscount adapterDiscount;

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
        collapsingToolbarLayout.setExpandedTitleTypeface(FontCache.getTypeface(this, "MyriadPro-Regular.otf"));
        collapsingToolbarLayout.setCollapsedTitleTypeface(FontCache.getTypeface(this, "MyriadPro-Regular.otf"));
    }

    private void makeCollapsingToolbarLayoutLooksGood1(CollapsingToolbarLayout collapsingToolbarLayout) {
        try {
            final Field field = collapsingToolbarLayout.getClass().getDeclaredField("mCollapsingTextHelper");
            field.setAccessible(true);

            final Object object = field.get(collapsingToolbarLayout);
            final Field tpf = object.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            ((TextPaint) tpf.get(object)).setTypeface(FontCache.getTypeface(this, "MyriadPro-Regular.otf"));
            ((TextPaint) tpf.get(object)).setColor(getResources().getColor(R.color.color_pin));
        } catch (Exception ignored) {
        }
    }

    int typeVihcile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        makeCollapsingToolbarLayoutLooksGood(toolbar_layout);
//        makeCollapsingToolbarLayoutLooksGood1(toolbar_layout);
        rcvPromotion.setLayoutManager(new LinearLayoutManager(this));
        rcvPromotion.setHasFixedSize(true);
        listDiscounts = new ArrayList<>();
        adapterDiscount = new AdapterDiscount(this, listDiscounts);
        rcvPromotion.setAdapter(adapterDiscount);
        if (getIntent() != null) {
            typeVihcile = getIntent().getIntExtra(Constants.TYPE_VEHICLE, 2);
        }
        getListPromotion(typeVihcile);
    }

    ArrayAdapter<String> adapter;
    ArrayList<String> listPromotion = new ArrayList<>();

    private void getListPromotion(int type) {
        Call<ApiResponse<List<Discount>>> getPromotions;
        if (type == 2)
            getPromotions = getmApi().getPromotions(ApiConstants.API_KEY, getmSetting().getString(Constants.ID));
        else
            getPromotions = getmApi().getPromotions(ApiConstants.API_KEY, getmSetting().getString(Constants.ID), type);
        getPromotions.enqueue(new CallBackCustom<ApiResponse<List<Discount>>>(this, new OnResponse<ApiResponse<List<Discount>>>() {
            @Override
            public void onResponse(ApiResponse<List<Discount>> object) {
                loading.setVisibility(View.GONE);
                if (object.getData() != null && object.getData().size()>0) {
                    imgEmpty.setVisibility(View.GONE);
                    listDiscounts.addAll(object.getData());
                    adapterDiscount.notifyDataSetChanged();
                    for (Discount item : listDiscounts) {
                        listPromotion.add(item.getPr_code());
                    }
                    adapter = new ArrayAdapter<String>(DiscountActivity.this, android.R.layout.simple_list_item_1, listPromotion);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapter.notifyDataSetChanged();
                } else {
//                    showDialogErrorContent(object.getMessage());
                    imgEmpty.setVisibility(View.VISIBLE);
                }
            }
        }));
    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;

        }
    }
}
