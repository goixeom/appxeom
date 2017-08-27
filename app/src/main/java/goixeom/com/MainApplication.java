package goixeom.com;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import goixeom.com.apis.ApiResponse;
import goixeom.com.models.BookingTrip;
import goixeom.com.models.Discount;
import goixeom.com.models.User;
import goixeom.com.utils.Constants;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

/**
 * Created by Huy on 6/13/2017.
 */

public class MainApplication extends MultiDexApplication {
    static MainApplication instance;
    private User mUser;
    private int mIdBooking;
    private int mIdDriver;
    private String mIdAuth;
    private String mDriver;
    Discount discount;
    private boolean isTimeOut;
    int timeExist;
    private Call<ApiResponse<BookingTrip>> requestBooking;
    long countDownSendCode=-1;
    Bundle bundleSendCode;
    private boolean isEnablePhone = true;
    private boolean isEnableEmail  = true;
    public static synchronized MainApplication getInstance() {
        if (instance == null) instance = new MainApplication();
        return instance;
    }
    int type = new Random().nextInt(2);

    public boolean isEnablePhone() {
        return isEnablePhone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setEnablePhone(boolean enablePhone) {
        isEnablePhone = enablePhone;
    }

    public boolean isEnableEmail() {
        return isEnableEmail;
    }

    public void setEnableEmail(boolean enableEmail) {
        isEnableEmail = enableEmail;
    }

    public long getCountDownSendCode() {
        return countDownSendCode;
    }

    public void setCountDownSendCode(long countDownSendCode) {
        this.countDownSendCode = countDownSendCode;
    }

    public Bundle getBundleSendCode() {
        return bundleSendCode;
    }

    public void setBundleSendCode(Bundle bundleSendCode) {
        this.bundleSendCode = bundleSendCode;
    }

    public String getmDriver() {
        return mDriver;
    }

    public void setmDriver(String mDriver) {
        this.mDriver = mDriver;
    }

    public boolean isTimeOut() {
        return isTimeOut;
    }

    public int getTimeExist() {
        if (timeExist < 20)
            return timeExist;
        else
            return 20;
    }

    public Call<ApiResponse<BookingTrip>> getRequestBooking() {
        return requestBooking;
    }

    public void setRequestBooking(Call<ApiResponse<BookingTrip>> requestBooking) {
        this.requestBooking = requestBooking;
    }

    public void setTimeExist(int timeExist) {
        this.timeExist = timeExist;
    }

    public void setTimeOut(boolean timeOut) {
        isTimeOut = timeOut;
    }

    public User getmUser() {
        if (mUser == null) {
            String json = new SPUtils(Constants.SETTING).getString("user_save");
            mUser = new Gson().fromJson(json, User.class);
        }
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
        String jsonUser;
        if (mUser != null) {
            jsonUser = new Gson().toJson(mUser);
        } else {
            jsonUser = "";
        }
        new SPUtils(Constants.SETTING).put("user_save", jsonUser);
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    String phoneNumber;

    public String getmIdAuth() {
        return mIdAuth;
    }

    public void setmIdAuth(String mIdAuth) {
        this.mIdAuth = mIdAuth;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
//        instance = this;
        Utils.init(this);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "OpenSans-Regular.ttf");
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        getHashesKey(this);
//        fb
        FacebookSdk.sdkInitialize(this);
//        getHashesKey(geA());
        Permission[] permissions = new Permission[]{
                Permission.USER_PHOTOS,
                Permission.EMAIL,
                Permission.PUBLISH_ACTION
        };
        SimpleFacebookConfiguration configurationFb = new SimpleFacebookConfiguration.Builder()
                .setAppId(getString(R.string.fb_id))
                .setNamespace("goixeom")
                .setPermissions(permissions)
                .build();
        SimpleFacebook.setConfiguration(configurationFb);
        initLog();
        sendBroadcast(new Intent("chayngam.restart"));
    }
    public static void initLog() {
        LogUtils.Builder builder = new LogUtils.Builder()
                .setLogSwitch(BuildConfig.DEBUG)// 设置log总开关，包括输出到控制台和文件，默认开
                .setGlobalTag(null)// 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                .setLogHeadSwitch(true)// 设置log头信息开关，默认为开
                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
                .setDir("")// 当自定义路径为空时，写入应用的/cache/log/目录中
                .setBorderSwitch(true);// 输出日志是否带边框开关，默认开
        LogUtils.d(builder.toString());
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getmIdBooking() {
        return mIdBooking;
    }

    public void setmIdBooking(int mIdBooking) {
        this.mIdBooking = mIdBooking;
    }

    public int getmIdDriver() {
        return mIdDriver;
    }

    public void setmIdDriver(int mIdDriver) {
        this.mIdDriver = mIdDriver;
    }

    void getHashesKey(Context context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

//            MyLog.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            LogUtils.e("Name not found" + e1.toString());
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("No such an algorithm" + e.toString());
        } catch (Exception e) {
            LogUtils.e("Exception" + e.toString());
        }
    }
}