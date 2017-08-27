package goixeom.com.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import goixeom.com.CustomTextView;
import goixeom.com.FontCache;
import goixeom.com.MainApplication;
import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustomNoDelay;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.User;
import goixeom.com.utils.Constants;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.img_avt)
    CircleImageView imgAvt;
    @BindView(R.id.tv_name)
    CustomTextView tvName;
    @BindView(R.id.tv_phone)
    CustomTextView tvPhone;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.ll_root_change_pass)
    LinearLayout llRootChangePass;
    @BindView(R.id.notification)
    ImageView notification;
    @BindView(R.id.sw_noti)
    com.kyleduo.switchbutton.SwitchButton swNoti;
    @BindView(R.id.ll_root_sw)
    LinearLayout llRootSw;
    @BindView(R.id.btn_dang_xuat)
    Button btnDangXuat;
    Unbinder unbinder;
    @BindView(R.id.img_back)
    ImageView imgBack;

    @BindView(R.id.ll_root)
    RelativeLayout llRoot;
    private User user;
    private Handler handler = new Handler();
    public SettingActivity() {
    }

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
        setContentView(R.layout.content_personal);
        unbinder = ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        makeCollapsingToolbarLayoutLooksGood(toolbar_layout);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                user = MainApplication.getInstance().getmUser();
                tvName.setText(user.getName());
                tvPhone.setText(user.getPhone());
                edtEmail.setText(user.getEmail().substring(0,user.getEmail().indexOf('@')+1)+"...");
                edtName.setText(user.getName());
                edtPhone.setText(user.getPhone());
                edtPhone.setEnabled(false);
                edtEmail.setEnabled(false);
                edtName.setEnabled(false);
                Glide.with(SettingActivity.this).load("https://" + user.getU_avatar()).into(imgAvt);
                swNoti.setChecked(getmSetting().getBoolean(Constants.RECEIVE_NOTI,true));
                swNoti.setOnCheckedChangeListener(SettingActivity.this);
            }
        },500);

        KeyboardUtils.hideSoftInput(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @OnClick({R.id.ll_root_change_pass, R.id.ll_root_sw, R.id.btn_dang_xuat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_root_change_pass:
                startActivity(new Intent(SettingActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.ll_root_sw:
                swNoti.setChecked(!swNoti.isChecked());
                break;
            case R.id.btn_dang_xuat:
                setResult(Constants.RESULT_LOGOUT);
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        getmSetting().put(Constants.RECEIVE_NOTI, b);
        if (b) {
            showSnackBar("Đã bật thông báo", "#25b45b");
        } else {
            showSnackBar("Đã tắt thông báo", "#FFBE08");

        }
    }

    private void updateInformation(String phone, String firstName, String email) {
        getDialogProgress().showDialog();
        Call<ApiResponse<String>> updateProfile = getmApi().updateInformation(ApiConstants.API_KEY, phone, getmSetting().getString(Constants.ID), firstName, email);
        updateProfile.enqueue(new CallBackCustomNoDelay<ApiResponse<String>>(this, getDialogProgress(), new OnResponse<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    Toast.makeText(getApplicationContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_LONG).show();
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

    public void showSnackBar(String content, String color) {
        Crouton.cancelAllCroutons();
        Style croutonStyle = new Style.Builder().setBackgroundColorValue(Color.parseColor(color)).build();
//        Crouton crouton = Crouton.makeText(this, content, croutonStyle)
//                .setConfiguration(new Configuration.Builder().setDuration(Configuration.DURATION_INFINITE).build());
//        crouton.show();

        Crouton.makeText(this, content, croutonStyle, llRoot).show();
    }

}
