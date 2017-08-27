package goixeom.com.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import goixeom.com.utils.Constants;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class FAQActivity extends BaseActivity {


    @BindView(R.id.edt_faq)
    EditText edtFaq;
    @BindView(R.id.send_help)
    Button sendHelp;
    Unbinder unbinder;
    @BindView(R.id.img_back)
    ImageView imgBack;

    @BindView(R.id.tv_conlai)
    CustomTextView tvConlai;


    @Override
    public void pingNotification(String title, String content) {

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
        setContentView(R.layout.content_help);
        unbinder = ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        makeCollapsingToolbarLayoutLooksGood(toolbar_layout);
        tvConlai.setText(String.format(getString(R.string.c_n_l_i_200_k_t), 200));
        edtFaq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvConlai.setText(String.format(getString(R.string.c_n_l_i_200_k_t), 200 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @OnClick(R.id.send_help)
    public void onViewClicked() {
        getDialogProgress().showDialog();
        Call<ApiResponse> feedback = getmApi().feedback(ApiConstants.API_KEY, getmSetting().getString(Constants.ID), edtFaq.getText().toString());
        feedback.enqueue(new CallBackCustom<ApiResponse>(this, getDialogProgress(), new OnResponse<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    Toast.makeText(getApplicationContext(), "Ý kiến đã  được gửi tới người quản trị.Cám ơn bạn !", Toast.LENGTH_LONG).show();
                    getmSocket().feedback(edtFaq.getText().toString());
                    finish();

                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    @OnClick(R.id.img_back)
    public void onViewCancelClicked() {
        finish();
    }

    @OnClick(R.id.phone)
    public void onViewCallClicked() {
        CommonUtils.intentToCall("02462931011", this);
    }
}
