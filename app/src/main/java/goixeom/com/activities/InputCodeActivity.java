package goixeom.com.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.KeyboardUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.Discount;
import goixeom.com.utils.Constants;
import retrofit2.Call;

public class InputCodeActivity extends BaseActivity {


    @BindView(R.id.edt_promotion)
    AutoCompleteTextView edtPromotion;
    private List<Discount> listDiscounts;
    private int typeVihcile;

    @Override
    public void pingNotification(String title, String content) {
    }
    @Override
    protected void onSoketConnected() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);
        ButterKnife.bind(this);
        listDiscounts = new ArrayList<>();
        if (getIntent() != null) {
            typeVihcile = getIntent().getIntExtra(Constants.TYPE_VEHICLE,0);
        }
        getListPromotion(typeVihcile);
        edtPromotion.clearFocus();
        KeyboardUtils.hideSoftInput(this, edtPromotion);
    }
    ArrayAdapter<String> adapter;
    ArrayList<String> listPromotion = new ArrayList<>();
    private void getListPromotion(int typeVihcile) {
        getDialogProgress().showDialog();
        Call<ApiResponse<List<Discount>>> getPromotions = getmApi().getPromotions(ApiConstants.API_KEY, getmSetting().getString(Constants.ID),typeVihcile);
        getPromotions.enqueue(new CallBackCustom<ApiResponse<List<Discount>>>(this,getDialogProgress(), new OnResponse<ApiResponse<List<Discount>>>() {
            @Override
            public void onResponse(ApiResponse<List<Discount>> object) {
                if (object.getData() != null) {
                    listDiscounts.addAll(object.getData());
                    for (Discount item : listDiscounts) {
                        listPromotion.add(item.getPr_code());
                    }
                    adapter = new ArrayAdapter<String>(InputCodeActivity.this, android.R.layout.simple_list_item_1, listPromotion);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    edtPromotion.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }
    @OnClick({R.id.btn_back, R.id.btn_accept,R.id.fab_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.fab_back:
                onBackPressed();
                break;
            case R.id.btn_accept:
                String promotionInput = edtPromotion.getText().toString();
                if (promotionInput.isEmpty()) {
                    showDialogErrorContent("Mã khuyến mại không được để trống");
                    return;
                }
                boolean isHas = false;
                for (final Discount item : listDiscounts) {
                    if (promotionInput.toLowerCase().equals(item.getPr_code().toLowerCase())) {
                        new MaterialDialog.Builder(this).title("Chúc mừng")
                                .content("Mã khuyến mại có giá trị " + NumberFormat.getNumberInstance(Locale.GERMAN).format(Double.parseDouble(item.getValue())) + "VND sẽ được áp dụng cho chuyến đi của bạn.")
                                .positiveColor(Color.GRAY)
                                .positiveText(getString(R.string.dismis))
                                .cancelable(false)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent i = new Intent();
                                        Bundle b = new Bundle();
                                        b.putParcelable(Constants.PROMOTION_VALUE, item);
                                        i.putExtra(Constants.BUNDLE, b);
                                        setResult(RESULT_OK, i);
                                        finish();
                                    }
                                }).show();
                        isHas = true;
                        break;
                    }
                }
                if (!isHas) {
                    showDialogErrorContent(getString(R.string.error_promotion));
                }
                break;
        }
    }
}
