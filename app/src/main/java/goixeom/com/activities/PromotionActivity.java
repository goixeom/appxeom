package goixeom.com.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import goixeom.com.CustomTextView;
import goixeom.com.R;
import goixeom.com.adapters.AdapterDiscountMenu;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.Discount;
import goixeom.com.utils.Constants;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class PromotionActivity extends BaseActivity {


    @BindView(R.id.rcv_promotion)
    PullToRefreshRecyclerView rcvPromotion;
    @BindView(R.id.loading)
    ProgressBar loading;
    Unbinder unbinder;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_toolbar)
    CustomTextView txtToolbar;
    @BindView(R.id.img_empty)
    ImageView imgEmpty;
    private ArrayList<Discount> listDiscounts;
    private AdapterDiscountMenu adapterDiscount;
    private int page;
    private boolean needToHold;


    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    protected void onSoketConnected() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_code_discount);
        unbinder = ButterKnife.bind(this);
        rcvPromotion.setLayoutManager(new LinearLayoutManager(this));
        listDiscounts = new ArrayList<>();
        adapterDiscount = new AdapterDiscountMenu(this, listDiscounts);
        rcvPromotion.setAdapter(adapterDiscount);
        getListPromotion();
        txtToolbar.setText(Html.fromHtml(getString(R.string.txt_toolbar_promo)));
        rcvPromotion.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // do refresh here
                page = 0;
                getListPromotion();
                needToHold = false;
            }
        });
    }

    private void getListPromotion() {
//        getDialogProgress().showDialog();
        Call<ApiResponse<List<Discount>>> getPromotions = getmApi().getPromotions(ApiConstants.API_KEY, getmSetting().getString(Constants.ID));
        getPromotions.enqueue(new CallBackCustom<ApiResponse<List<Discount>>>(this, new OnResponse<ApiResponse<List<Discount>>>() {
            @Override
            public void onResponse(ApiResponse<List<Discount>> object) {
                loading.setVisibility(View.GONE);

                if (!needToHold) {
                    listDiscounts.clear();
                    rcvPromotion.setSwipeEnable(true);
                }
                if (object.getData() != null) {
                    listDiscounts.addAll(object.getData());
                    adapterDiscount.notifyDataSetChanged();
                    rcvPromotion.onFinishLoading(true, false);
                    if (object.getData().size() > 0) {
                        imgEmpty.setVisibility(View.GONE);

                    } else {
                        imgEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    showDialogErrorContent(object.getMessage());
                }


                rcvPromotion.setOnRefreshComplete();
            }
        }));
    }


    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
