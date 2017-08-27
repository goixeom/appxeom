package goixeom.com.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import goixeom.com.AdapterFragment;
import goixeom.com.FragmentCode;
import goixeom.com.FragmentEmail;
import goixeom.com.FragmentForget;
import goixeom.com.FragmentLogin;
import goixeom.com.FragmentPass;
import goixeom.com.FragmentUpdate;
import goixeom.com.R;
import goixeom.com.utils.CommonUtils;
import goixeom.com.utils.Constants;
import goixeom.com.views.AlertDialogCustom;


public class MainActivity extends BaseAuthActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 10000;
    //    private NonSwipeableViewPager viewPager;
    AdapterFragment adapterFragment;
    private static MainActivity main;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.ll_root)
    RelativeLayout llRoot;

    private SimpleFacebook simpleFacebook;
    private GoogleApiClient mGoogleApiClient;
    FragmentManager mFragmentManager;
    AlertDialog dialogNetwork;
    BroadcastReceiver receiverConnectionNetwork = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isConnected()) {
                CommonUtils.disable(llRoot);
                if (dialogNetwork == null)
                    dialogNetwork = AlertDialogCustom.dialogMessage(MainActivity.this);
                dialogNetwork.show();
            } else {
                CommonUtils.enable(llRoot);
                if (dialogNetwork != null) dialogNetwork.dismiss();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(this);
        main = this;
//        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.contentview, new FragmentLogin()).commit();

        if (getmSetting().getString(Constants.ID) != null && !getmSetting().getString(Constants.ID).isEmpty()) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
            finish();
        }
        final View activityRootView = findViewById(R.id.ll_root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(MainActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    scroll.scrollTo(0, 200);
                }
            }
        });
        if (getIntent() != null) {
            boolean isKickOut = getIntent().getBooleanExtra(Constants.MSG, false);
            if (isKickOut) {
                showDialogErrorContent("Tài khoản của bạn được đăng nhập tại một thiết bị khác.");
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showDialogPermission();
        }
    }

    private void showDialogPermission() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this).title(getString(R.string.error))
                .content("Bạn cần cấp quyền truy cập ứng dụng để tiếp tục sử dụng dịch vụ")
                .positiveColor(Color.GRAY)
                .positiveText("Đồng ý")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                    }
                }).show();
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


    public void trantoTab(Fragment tab) {
        //ref : https://stackoverflow.com/questions/21026409/fragment-transaction-animation-slide-in-and-slide-out
        mFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.slide_in, R.anim.slide_out).replace(R.id.contentview, tab).addToBackStack(null).commit();
//        mFragmentManager.beginTransaction().add(R.id.contentview,tab).commit();
    }
    public void tranToTabRoot(Fragment tab) {
        //ref : https://stackoverflow.com/questions/21026409/fragment-transaction-animation-slide-in-and-slide-out
        mFragmentManager.beginTransaction().replace(R.id.ll_root, tab).addToBackStack(null).commit();
//        mFragmentManager.beginTransaction().add(R.id.contentview,tab).commit();
    }
    public void trantoTabNoBackStack(Fragment tab) {
        mFragmentManager.beginTransaction().replace(R.id.contentview, tab).commit();
    }

    public void removeAllTab() {
        for (int i = 0; i < mFragmentManager.getBackStackEntryCount() - 1; i++) {
            mFragmentManager.popBackStack();
        }
    }

    public boolean isCanBack = true;

    @Override
    public void onBackPressed() {
        if (isCanBack) super.onBackPressed();


//        if (mFragmentManager.getBackStackEntryCount() > 1) {
//            return;
//        }
//        finish();
//        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.simpleFacebook != null) {
            this.simpleFacebook.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onResume() {
        super.onResume();
        this.simpleFacebook = SimpleFacebook.getInstance(this);

        IntentFilter iConnection = new IntentFilter();
        iConnection.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.receiverConnectionNetwork, iConnection);


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.receiverConnectionNetwork);

    }


    private void setupViewPager(ViewPager viewPager) {
        adapterFragment = new AdapterFragment(getSupportFragmentManager());
        adapterFragment.addFragment(new FragmentLogin());
        adapterFragment.addFragment(new FragmentEmail());
        adapterFragment.addFragment(new FragmentForget());
        adapterFragment.addFragment(new FragmentPass());
        adapterFragment.addFragment(new FragmentUpdate());
        adapterFragment.addFragment(new FragmentCode());
        viewPager.setAdapter(adapterFragment);
        viewPager.setOffscreenPageLimit(1);
    }

    public static MainActivity getInstance() {
        return main;
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtils.e(connectionResult.getErrorMessage());
    }
    private void signInFacebook() {
        this.simpleFacebook.login(new OnLoginListener() {
            public void onLogin(String accessToken, List<Permission> list, List<Permission> list2) {
                if (accessToken != null && !accessToken.isEmpty()) {
                    //          getDialogProgress().showDialog();
                    Profile.Properties properties = new Profile.Properties.Builder()
                            .add(Profile.Properties.ID)
                            .add(Profile.Properties.EMAIL)
                            .add(Profile.Properties.NAME)
                            .build();
                    simpleFacebook.getProfile(properties, new OnProfileListener() {
                        @Override
                        public void onComplete(Profile response) {
                            super.onComplete(response);
                            LogUtils.e("id facebook :" + response.getId() + " \t email : " + response.getEmail() + "\t name : " + response.getName());
                            //     singInSocial(TYPE_FACEBOOK, response.getName(), response.getId(), response.getEmail());
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            super.onException(throwable);
                            throwable.fillInStackTrace();
                            ToastUtils.showShort(throwable.getMessage());

                            //       getDialogProgress().hideDialog();
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


}
