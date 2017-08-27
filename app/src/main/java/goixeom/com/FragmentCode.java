package goixeom.com;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import goixeom.com.activities.ChangePasswordActivity;
import goixeom.com.activities.MainActivity;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.fragments.BaseFragment;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.User;
import goixeom.com.models.VerifyCode;
import goixeom.com.utils.Constants;
import retrofit2.Call;

/**
 * Created by Huy on 6/12/2017.
 */

public class FragmentCode extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.code_0)
    EditText code0;
    @BindView(R.id.code_1)
    EditText code1;
    @BindView(R.id.code_2)
    EditText code2;
    @BindView(R.id.code_3)
    EditText code3;

    Unbinder unbinder;
    private ImageView img_next2;
    //    private ViewPager viewpager;
    private TextView txt, txt4;
    private String strCode;
    private CountDownTimer countDownTimer;
    private String mPhone;
    private User mUserNeedToUpdate;
    private String codeVerify;
    private CountDownTimer countDownTimerLater;
    private boolean fragmentActivation = true;

    public FragmentCode() {
    }

    public static final String SMS_BUNDLE = "pdus";
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle intentExtras = intent.getExtras();
            if (intentExtras != null) {
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                String smsMessageStr = "";
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                    String smsBody = smsMessage.getMessageBody();
                    String address = smsMessage.getOriginatingAddress();
                    smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += smsBody + "\n";

                    if (smsBody.contains("Mật khẩu Gọi Xe Ôm của bạn là")) {
                        String numberCode = smsBody.substring(smsBody.indexOf(":") + 2, smsBody.length());
                        if (numberCode.length() == 4) {
                            code0.setText(numberCode.substring(0, 1));
                            code1.setText(numberCode.substring(1, 2));
                            code2.setText(numberCode.substring(2, 3));
                            code3.setText(numberCode.substring(3, 4));
                        }
                        break;
                    }
                }
                LogUtils.e(smsMessageStr);

                //this will update the UI with message

            }
        }
    };

    private void sendSms(String phone) {
        Call<ApiResponse<VerifyCode>> sms = getmApi().sendSMS(ApiConstants.API_KEY, phone);
        getDialogProgress().showDialog();
        sms.enqueue(new CallBackCustom<ApiResponse<VerifyCode>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<VerifyCode>>() {
            @Override
            public void onResponse(ApiResponse<VerifyCode> object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    LogUtils.d("Sent code to device");
                    codeVerify = object.getData().getCode();
                    countDownTimerLater.start();
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_code, container, false);
        img_next2 = (ImageView) view.findViewById(R.id.img_next2);
        txt = (TextView) view.findViewById(R.id.txt);
        txt4 = (TextView) view.findViewById(R.id.txt4);
//        viewpager = (ViewPager) MainActivity.getInstance().findViewById(R.id.viewpager);

        unbinder = ButterKnife.bind(this, view);
        img_next2.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhone = getmSetting().getString(Constants.PHONE);
        long count = Constants.COUNT_DOWN_MILIS;
        long milis = MainApplication.getInstance().getCountDownSendCode();
        if (milis != -1 && milis != 0) {
            count = milis;
        }
        if (getArguments() != null) {
            mUserNeedToUpdate = getArguments().getParcelable(Constants.USER);
            codeVerify = getArguments().getString(Constants.CODE_VERIFY);
            if (mUserNeedToUpdate != null) mPhone = mUserNeedToUpdate.getPhone();

        }

        countDownTimer = new CountDownTimer(count, 1000) {
            @Override
            public void onTick(long l) {
                if (!fragmentActivation) return;
                txt4.setText(Html.fromHtml(String.format(getString(R.string.dem_nguoc), l / 1000 + "s")));
                txt4.setClickable(false);
                MainApplication.getInstance().setCountDownSendCode(l);
            }

            @Override
            public void onFinish() {
                if (!fragmentActivation) return;

                MainApplication.getInstance().setCountDownSendCode(-1);
                txt4.setClickable(true);
                txt4.setText(Html.fromHtml(getString(R.string.guilaima)));
            }
        };
        countDownTimerLater = new CountDownTimer(Constants.COUNT_DOWN_MILIS, 1000) {
            @Override
            public void onTick(long l) {
                if (!fragmentActivation) return;

                txt4.setText(Html.fromHtml(String.format(getString(R.string.dem_nguoc), l / 1000 + "s")));
                txt4.setClickable(false);
                MainApplication.getInstance().setCountDownSendCode(l);
            }

            @Override
            public void onFinish() {
                if (!fragmentActivation) return;

                MainApplication.getInstance().setCountDownSendCode(-1);
                txt4.setClickable(true);
                txt4.setText(Html.fromHtml(getString(R.string.guilaima)));
            }
        };
        if (mPhone != null && !mPhone.isEmpty()) {
//            sendSms(mPhone);
            countDownTimer.start();
            txt.setText(Html.fromHtml(String.format(getString(R.string.txt_code), getmSetting().getString(Constants.PHONE))));
            txt4.setText(Html.fromHtml(String.format(getString(R.string.dem_nguoc), "00:" + Constants.COUNT_DOWN_MILIS / 1000 + "s")));
            txt4.setClickable(false);
            txt4.setOnClickListener(this);
            addWatcherToEditext();
//            KeyboardUtils.showSoftInput(code0);
        }
        img_next2.setEnabled(false);
    }


    private void addWatcherToEditext() {
        code0.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    code1.requestFocus();
                    img_next2.setClickable(true);
                    img_next2.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    code2.requestFocus();
                    img_next2.setClickable(true);
                    img_next2.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    code3.requestFocus();
                    img_next2.setClickable(true);
                    img_next2.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    strCode = code0.getText().toString() + code1.getText().toString() + code2.getText().toString() + code3.getText().toString();
                    if (strCode.length() == 4) {
                        checkCodeNow();
                        img_next2.setClickable(true);
                        img_next2.setEnabled(true);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentActivation = true;

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.RECEIVE_SMS,
                            android.Manifest.permission.READ_SMS},
                    10000);
        } else {
            Log.e("DB", "PERMISSION GRANTED");
            IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            intentFilter.setPriority(1000);
            getContext().registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getContext().unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10000 ){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
                showDialogPermission();
            }else{
                Log.e("DB", "PERMISSION GRANTED");
                IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                intentFilter.setPriority(1000);
                getContext().registerReceiver(broadcastReceiver, intentFilter);
            }
        }
    }
    private void showDialogPermission() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getContext()).title(getString(R.string.error))
                .content("Bạn cần cấp quyền truy cập ứng dụng để tiếp tục sử dụng dịch vụ")
                .positiveColor(Color.GRAY)
                .positiveText("Đồng ý")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.RECEIVE_SMS,
                                        android.Manifest.permission.READ_SMS},
                                10000);
                    }
                }).show();
    }
    @Override
    public void onStop() {
        countDownTimer.cancel();
        super.onStop();
    }

    private boolean verifyPass() {
        strCode = code0.getText().toString() + code1.getText().toString() + code2.getText().toString() + code3.getText().toString();
        if (strCode.length() != 4 || !strCode.equals(codeVerify)) {
            showDialogErrorContent("Mã code xác thực không hợp lệ ! Vui lòng kiểm tra lại...");
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
        fragmentActivation = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt4: {
                sendSms(mPhone);
                break;
            }
            case R.id.img_next2: {
                checkCodeNow();
                break;
            }
        }

    }

    private void checkCodeNow() {
        if (verifyPass()) {
            if (mUserNeedToUpdate != null) {
                updateInformation(mUserNeedToUpdate.getPhone(), mUserNeedToUpdate.getName(), mUserNeedToUpdate.getEmail());
                return;
            }
            if (mPhone == null || mPhone.isEmpty()) return;
            getDialogProgress().showDialog();

            Call<ApiResponse<String>> register = getmApi().register(ApiConstants.API_KEY, mPhone, strCode);
            register.enqueue(new CallBackCustom<ApiResponse<String>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<String>>() {
                @Override
                public void onResponse(ApiResponse<String> object) {
                    if (object.getStatus() == ApiConstants.CODE_SUCESS && object.getData() != null) {
//                        getmSetting().put(Constants.ID, object.getData());        //todo Should not to save id to sp now. because must update info
                        MainApplication.getInstance().setmIdAuth(object.getData());
//                                viewpager.setCurrentItem(Constants.POS_UPDATE);

//                                ((MainActivity)getActivity()).removeAllTab();
                        ((MainActivity) getActivity()).isCanBack = false;
                        ((MainActivity) getActivity()).trantoTab(new FragmentUpdate());

                    } else {
                        new MaterialDialog.Builder(getContext()).title(R.string.error)
                                .content(object.getMessage())
                                .positiveText(R.string.dismis)
                                .positiveColor(Color.GRAY)
                                .show();
                    }
                }
            }));
        }
    }

    private void updateInformation(String phone, String lastName, String email) {
        getDialogProgress().showDialog();
        Call<ApiResponse<String>> updateProfile = getmApi().updateInformation(ApiConstants.API_KEY, phone, MainApplication.getInstance().getmIdAuth(), lastName, email);
        updateProfile.enqueue(new CallBackCustom<ApiResponse<String>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    getmSetting().put(Constants.ID, MainApplication.getInstance().getmIdAuth());
                    MainApplication.getInstance().setmIdAuth("");
                    getmSetting().put(Constants.TYPE_PASSWORD_MAP, true);
                    Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                    intent.putExtra(Constants.NEED_TO_BACK_TO_MAP, true);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    getActivity().finish();
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }


}