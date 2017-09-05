package goixeom.com;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import goixeom.com.activities.MainActivity;
import goixeom.com.activities.MapsActivity;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.ApiUtils;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.apis.GoiXeOmAPI;
import goixeom.com.fragments.BaseFragment;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.StringResponse;
import goixeom.com.models.User;
import goixeom.com.models.VerifyCode;
import goixeom.com.utils.Constants;
import retrofit2.Call;


/**
 * Created by Huy on 6/9/2017.
 */

public class FragmentLogin extends BaseFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 100;
    @BindView(R.id.edt_phone_login)
    EditText edtPhoneLogin;
    @BindView(R.id.img_next)
    ImageView imgNext;
    @BindView(R.id.layout_header_login)
    LinearLayout layoutHeaderLogin;
    @BindView(R.id.layout_footer)
    LinearLayout layoutFooter;
    Unbinder unbinder;
    @BindView(R.id.btn_signin_facebook)
    LinearLayout btnSigninFacebook;
    @BindView(R.id.btn_signin_gg)
    LinearLayout btnSigninGoogle;
    Unbinder unbinder1;
    //    private ViewPager viewPager;
    private ImageView img_next;
    private GoiXeOmAPI mService;
    private SimpleFacebook simpleFacebook;
    private GoogleApiClient mGoogleApiClient;

    //    SmartLogin smartLogin;
//    SmartLoginConfig config;
    public FragmentLogin() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_login, container, false);
//        viewPager = (ViewPager) MainActivity.getInstance().findViewById(R.id.viewpager);
        edtPhoneLogin = (EditText) view.findViewById(R.id.edt_phone_login);
        img_next = (ImageView) view.findViewById(R.id.img_next);
        img_next.setOnClickListener(this);
        unbinder1 = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mService = ApiUtils.getRootApi().create(GoiXeOmAPI.class);
        FacebookSdk.sdkInitialize(getContext());
//        config  = new SmartLoginConfig(getActivity(),this);
//        config.setFacebookAppId(getString(R.string.fb_id));
//        smartLogin  = SmartLoginFactory.build(LoginType.Facebook);

        KeyboardUtils.hideSoftInput(getContext(), edtPhoneLogin);

    }

    @Override
    public void onDestroyView() {
//        unbinder.unbind();
        super.onDestroyView();
    }


    private boolean verifySucess() {
        String phone = edtPhoneLogin.getText().toString();
        if (phone.isEmpty()) {
            new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content(getString(R.string.empty_phone))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        if ((phone.length() < 10) || phone.length() > 11) {
            new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content(getString(R.string.please_enter_phone))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        if (phone.length() == 10) {
            if (!phone.substring(0, 2).equals("09")) {
                new MaterialDialog.Builder(getContext()).title(R.string.error)
                        .content(getString(R.string.please_enter_phone))
                        .positiveText(R.string.dismis)
                        .positiveColor(Color.GRAY)
                        .show();
                return false;
            }
        } else {
            if (!phone.substring(0, 2).equals("01")) {
                new MaterialDialog.Builder(getContext()).title(R.string.error)
                        .content(getString(R.string.please_enter_phone))
                        .positiveText(R.string.dismis)
                        .positiveColor(Color.GRAY)
                        .show();
                return false;
            }
        }


        return true;
    }

    @Override
    public void onClick(final View view) {
        if (verifySucess()) {
            getDialogProgress().showDialog();
            Call<ApiResponse<StringResponse>> checkPhoneExist = mService.checkPhoneExist(ApiConstants.API_KEY, edtPhoneLogin.getText().toString());
            checkPhoneExist.enqueue(new CallBackCustom<ApiResponse<StringResponse>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<StringResponse>>() {
                @Override
                public void onResponse(ApiResponse<StringResponse> object) {
                    getmSetting().put(Constants.PHONE, edtPhoneLogin.getText().toString());
                    if (object.getData() != null) {
                        MainApplication.getInstance().setPhoneNumber(object.getData().getPhone());
                        User user = new User();
                        user.setPhone(object.getData().getPhone());
                        MainApplication.getInstance().setmUser(user);

//                        viewPager.setCurrentItem(Constants.POS_PASS);
                        ((MainActivity) getActivity()).trantoTab(new FragmentPass());
                    } else {
                        if (object.getStatus() == ApiConstants.CODE_ERROR_NO_EXIST_ACC) {
//                            viewPager.setCurrentItem(Constants.POS_CODE);
                            sendSms(edtPhoneLogin.getText().toString());
                        }
                    }
                }
            }));
        }
    }

    private void sendSms(final String phone) {
        if (MainApplication.getInstance().getCountDownSendCode() != -1
                && MainApplication.getInstance().getmUser() != null
                && MainApplication.getInstance().getBundleSendCode() != null) {
            User user = MainApplication.getInstance().getmUser();
            if (user.getPhone() != null && user.getPhone().equals(phone)) {
                FragmentCode fgCode = new FragmentCode();
                fgCode.setArguments(MainApplication.getInstance().getBundleSendCode());
                ((MainActivity) getActivity()).trantoTab(fgCode);
                return;
            }
        }
        MainApplication.getInstance().setCountDownSendCode(-1);
        Call<ApiResponse<VerifyCode>> sms = getmApi().sendSMS(ApiConstants.API_KEY, phone);
        getDialogProgress().showDialog();
        sms.enqueue(new CallBackCustom<ApiResponse<VerifyCode>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<VerifyCode>>() {
            @Override
            public void onResponse(ApiResponse<VerifyCode> object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    LogUtils.d("Sent code to device");
                    User user = new User();
                    user.setPhone(phone);
                    MainApplication.getInstance().setmUser(user);
                    FragmentCode fgCode = new FragmentCode();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.CODE_VERIFY, object.getData().getCode());
                    fgCode.setArguments(bundle);
                    MainApplication.getInstance().setBundleSendCode(bundle);
                    ((MainActivity) getActivity()).trantoTab(fgCode);
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    @OnClick({R.id.btn_signin_facebook, R.id.btn_signin_gg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signin_facebook:
                signInFacebook();

                break;
            case R.id.btn_signin_gg:
//                getDialogProgress().showDialog();
                if (mGoogleApiClient == null) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
//                            .requestId()
                            .requestProfile()
//                            .requestIdToken(getString(R.string.api_key_gplus))
//                            .requestProfile()
//                            .requestServerAuthCode(getString(R.string.api_key_gplus))
                            .build();
                    mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                            .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                            .build();
                }
                signInGoogle();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.simpleFacebook != null) {
            this.simpleFacebook.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
//        smartLogin.onActivityResult(requestCode, resultCode, data, config);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }

    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.simpleFacebook = SimpleFacebook.getInstance(getActivity());

    }

    private void signInFacebook() {
        this.simpleFacebook.login(new OnLoginListener() {

            public void onLogin(String accessToken, List<Permission> list, List<Permission> list2) {
                if (accessToken != null && !accessToken.isEmpty()) {
                    getDialogProgress().showDialog();
                    PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
                    pictureAttributes.setHeight(ApiConstants.CODE_ERROR_SERVER);
                    pictureAttributes.setWidth(ApiConstants.CODE_ERROR_SERVER);
                    pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);
                    Profile.Properties properties = new Profile.Properties.Builder()
                            .add(Profile.Properties.ID)
                            .add(Profile.Properties.EMAIL)
                            .add(Profile.Properties.NAME)
                            .add(Profile.Properties.PICTURE, pictureAttributes)
                            .build();
                    simpleFacebook.getProfile(properties, new OnProfileListener() {
                        @Override
                        public void onComplete(Profile response) {
                            super.onComplete(response);
//                            if (!(response.getPicture() == null || response.getPicture().isEmpty())) {
//                              String urlAvatarSocial = response.getPicture();
//                            }
                            LogUtils.e("id facebook :" + response.getId() + " \t email : " + response.getEmail() + "\t name : " + response.getName() + "\t avt : " + response.getPicture());
                            singInSocial(TYPE_FACEBOOK, response.getName(), response.getId(), response.getEmail(), response.getPicture());
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            super.onException(throwable);
                            throwable.fillInStackTrace();
                            ToastUtils.showShort(throwable.getMessage());
                            getDialogProgress().hideDialog();
                        }
                    });
                }
            }

            @Override
            public void onCancel() {
                LogUtils.e("cancle");
            }

            @Override
            public void onException(Throwable throwable) {
                ToastUtils.showShort(throwable.getMessage());
                LogUtils.e(throwable.getMessage());

            }


            @Override
            public void onFail(String reason) {
                ToastUtils.showShort(reason);
            }

        });
    }

    public final static int TYPE_GOOGLE = 0;
    public final static int TYPE_FACEBOOK = 1;

    private void singInSocial(int type, String name, String id, String email, String avt) {
        Call<ApiResponse<User>> sigin = null;
        String imei = PhoneUtils.getIMEI();
        if (type == TYPE_FACEBOOK) {
            sigin = getmApi().signInFaccebook(ApiConstants.API_KEY, id, name, email, avt, imei);
        } else {
            sigin = getmApi().signInGoogle(ApiConstants.API_KEY, id, name, email, avt, imei);
        }
        sigin.enqueue(new CallBackCustom<ApiResponse<User>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<User>>() {
            @Override
            public void onResponse(ApiResponse<User> object) {
                if (object.getData() != null) {
                    if (object.getData().getPhone() != null && !object.getData().getPhone().isEmpty()) {
                        getmSetting().put(Constants.PHONE, object.getData().getPhone());
                        getmSetting().put(Constants.ID, object.getData().getId());
                        MainApplication.getInstance().setPhoneNumber(object.getData().getPhone());
                        MainApplication.getInstance().setmUser(object.getData());
                        Intent intent = new Intent(getContext(), MapsActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        getActivity().finish();
                    } else {
                        MainApplication.getInstance().setmUser(object.getData());
                        MainApplication.getInstance().setmIdAuth(object.getData().getId());
//                    getmSetting().put(Constants.ID, object.getData().getId());
                        FragmentUpdate fgUpdate = new FragmentUpdate();
                        Bundle b = new Bundle();
                        b.putBoolean(Constants.ISFROMSOCIAL, true);
                        fgUpdate.setArguments(b);
                        MainApplication.getInstance().setCountDownSendCode(-1);
                        ((MainActivity) getActivity()).trantoTab(fgUpdate);

                    }
                } else {
                        showDialogErrorContent(object.getMessage());
                }
            }

        }));
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.e("handleSignInResult", result.isSuccess() + "-" + result.getStatus().getStatusMessage());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //todo account maybe null LG , HTC
            String urlAvatarSocial = "";
            if (acct.getPhotoUrl() != null) {
                urlAvatarSocial = acct.getPhotoUrl().toString();
                Log.e("GOOGLE", "id facebook :" + acct.getId() + " \t email : " + acct.getEmail() + "\t name : " + acct.getDisplayName() + "\tavt : " + urlAvatarSocial);
            }
            singInSocial(TYPE_GOOGLE, acct.getDisplayName(), acct.getId(), acct.getEmail(), urlAvatarSocial);
        } else {
            // Signed out, show unauthenticated UI.
            Log.e("GOOGLE", "Sign out");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
        Log.e("GOOGLE", connectionResult.getErrorMessage());
    }


}
