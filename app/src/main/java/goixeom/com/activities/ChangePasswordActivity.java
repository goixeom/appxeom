package goixeom.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import goixeom.com.CustomTextView;
import goixeom.com.MainApplication;
import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.apis.CallBackCustomNoDelay;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.User;
import goixeom.com.utils.Constants;
import retrofit2.Call;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.img_avt)
    CircleImageView imgAvt;
    @BindView(R.id.tv_name)
    CustomTextView tvName;
    @BindView(R.id.tv_phone)
    CustomTextView tvPhone;
    @BindView(R.id.edt_new_pass)
    EditText edtNewPass;
    @BindView(R.id.edt_re_pass)
    EditText edtRePass;
    @BindView(R.id.btn_change_pass)
    Button btnChangePass;

    User user;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_toolbar)
    CustomTextView txtToolbar;
    @BindView(R.id.etPasswordLayout)
    TextInputLayout etPasswordLayout;
    private boolean needBackToMap;

    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    protected void onSoketConnected() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
//        getInfor(getmSetting().getString(Constants.ID));
        user = MainApplication.getInstance().getmUser();
        tvName.setText(user.getName());
        tvPhone.setText(user.getPhone());
        if (user.getAvatarSocial() != null && !user.getAvatarSocial().isEmpty())
            user.setU_avatar(user.getAvatarSocial());
        if (user.getU_avatar() != null && !user.getU_avatar().isEmpty())
        Glide.with(this).load("https://" + user.getU_avatar()).into(imgAvt);
        if (getIntent() != null) {
            needBackToMap = getIntent().getBooleanExtra(Constants.NEED_TO_BACK_TO_MAP, false);
        }
    }


    @OnClick(R.id.btn_change_pass)
    public void onViewClicked() {
        String password = edtNewPass.getText().toString();
        String repassword = edtRePass.getText().toString();
        if (password.isEmpty()) {
            showDialogErrorContent("Vui lòng điền mật khẩu mới");
        } else if (password.length() < 6) {
            showDialogErrorContent("Mật khẩu phải từ 6 ký tự trở lên");
        } else if (!password.equals(repassword)) {
            showDialogErrorContent("Hai mật khẩu không trùng khớp");
        } else {
            getDialogProgress().showDialog();
            Call<ApiResponse> changePass = getmApi().changePass(ApiConstants.API_KEY, getmSetting().getString(Constants.ID), password);
            changePass.enqueue(new CallBackCustom<ApiResponse>(this, getDialogProgress(), new OnResponse<ApiResponse>() {
                @Override
                public void onResponse(ApiResponse object) {
                    if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                        Toast.makeText(getApplicationContext(), "Cập nhật mật khẩu thành công!", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    } else {
                        showDialogErrorContent(object.getMessage());
                    }
                }
            }));
        }
    }

    private void getInfor(String id) {
        getDialogProgress().showDialog();
        Call<ApiResponse<User>> apiGetInfo = getmApi().getInfor(ApiConstants.API_KEY, id);
        apiGetInfo.enqueue(new CallBackCustomNoDelay<ApiResponse<User>>(this, getDialogProgress(), new OnResponse<ApiResponse<User>>() {
            @Override
            public void onResponse(ApiResponse<User> object) {
                if (object.getData() != null
                        && object.getData().getEmail() != null && !object.getData().getEmail().isEmpty()
                        && object.getData().getPhone() != null && !object.getData().getPhone().isEmpty()
                        && object.getData().getName() != null && !object.getData().getName().isEmpty()) {

                    LogUtils.e("Updated infor user");
                    MainApplication.getInstance().setmUser(object.getData());
                    tvName.setText(object.getData().getName());
                    tvPhone.setText(object.getData().getPhone());
                    if (object.getData().getAvatarSocial() != null && !object.getData().getAvatarSocial().isEmpty())
                        object.getData().setU_avatar(object.getData().getAvatarSocial());
                    if (object.getData().getU_avatar() != null && !object.getData().getU_avatar().isEmpty())
                        Glide.with(getApplicationContext()).load("https://" + object.getData().getU_avatar()).asBitmap().into(imgAvt);
//                    else
//                        Glide.with(getApplicationContext()).load("https://" + object.getData().getU_avatar()).asBitmap().into(mAvt);

//                    if (object.getData().getChange_password() == 0 && !getmSetting().getBoolean(Constants.TYPE_PASSWORD_MAP)) {
//                        getmSetting().put(Constants.TYPE_PASSWORD_MAP, true);
//                        startActivity(new Intent(MapsActivity.this, ChangePasswordActivity.class));
//                    }
                } else {
                    logout();
                    LogUtils.e("Counld not Updated infor user");

                }
            }
        }));
    }

    @OnClick(R.id.img_back)
    public void onViewBackClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (needBackToMap) {
            startActivity(new Intent(ChangePasswordActivity.this, MapsActivity.class));
        }
        finish();
    }
}
