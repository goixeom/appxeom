package goixeom.com.activities;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import goixeom.com.MainApplication;
import goixeom.com.R;
import goixeom.com.apis.ApiUtils;
import goixeom.com.apis.GoiXeOmAPI;
import goixeom.com.socket.SocketClient;
import goixeom.com.socket.SocketConstants;
import goixeom.com.socket.SocketResponse;
import goixeom.com.utils.CommonUtils;
import goixeom.com.utils.Constants;
import goixeom.com.views.AlertDialogCustom;
import goixeom.com.views.ProgressDialogCustom;

/**
 * Created by DuongKK on 6/16/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public abstract void pingNotification(String title, String content);

    private ProgressDialogCustom dialogProgress;
    private GoiXeOmAPI mGoiXeOmApi;
    private SPUtils mSetting = new SPUtils(Constants.SETTING);
    private boolean mBounded;
    protected SocketClient mSocket;
    ViewGroup view;
    ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
//            LogUtils.e("disconnect socket");
            BaseActivity.this.mBounded = false;
            BaseActivity.this.mSocket = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
//            LogUtils.e("connected umbersk");
            BaseActivity.this.mBounded = true;
            SocketClient.LocalBinder mLocalBinder = (SocketClient.LocalBinder) service;
            BaseActivity.this.mSocket = mLocalBinder.getServerInstance();
            onSoketConnected();
        }
    };

    protected abstract void onSoketConnected();

    public void showSnackBar(String content) {
        Crouton.cancelAllCroutons();
        Style croutonStyle = new Style.Builder().setBackgroundColorValue(Color.parseColor("#25b45b")).build();
        Crouton.makeText(this, content, croutonStyle, view)   .setConfiguration(new Configuration.Builder().setDuration(Configuration.DURATION_INFINITE).build()).show();
    }

    AlertDialog dialogNetwork;
    BroadcastReceiver receiverConnectionNetwork = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isConnected()) {
                if (dialogNetwork == null)
                    dialogNetwork = AlertDialogCustom.dialogMessage(BaseActivity.this);
                dialogNetwork.show();
                CommonUtils.disable(view);
            } else {
                if (dialogNetwork != null) dialogNetwork.dismiss();
              if(getmSocket()!=null)  getmSocket().initSocket();
                CommonUtils.enable(view);

            }

        }
    };


    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                //Connection
                String connection = intent.getStringExtra(SocketConstants.KEY_STATUS_CONNECTION);
                if (connection != null && !connection.isEmpty()) {
                    if (view != null) {
                        if (connection.equals(getString(R.string.disconnect))) {
//                            Crouton.makeText(BaseActivity.this, connection, Style.ALERT, view).show();
                            CommonUtils.disable(view);
                        } else if (connection.equals(getString(R.string.connected))) {
//                            showSnackBar(connection);
                            CommonUtils.enable(view);
                            onSoketConnected();
                        } else {
//                            Crouton.makeText(BaseActivity.this, connection, Style.INFO, view).show();
                            CommonUtils.disable(view);

                        }
                    }
                    return;
                }
                //Profile
                String jsonResponse = intent.getStringExtra(Constants.PROFILE);
                SocketResponse response = new Gson().fromJson(jsonResponse, SocketResponse.class);
                if (response != null && !getmSetting().getString(Constants.ID).isEmpty() && response.getIdUser() == Integer.parseInt(getmSetting().getString(Constants.ID))) {
                    if (response.getImei() != null && !response.getImei().equals(PhoneUtils.getIMEI())) {
                        logout();
                    }
                    return;
                }

                //Notification
                String jsonResponseNotification = intent.getStringExtra(Constants.NOTIFICATION_SOCKET);
                SocketResponse responseNotifcation = new Gson().fromJson(jsonResponseNotification, SocketResponse.class);
                if (responseNotifcation != null) {
                    if (responseNotifcation.getType() == Constants.TYPE_ALL_SYSTEM || responseNotifcation.getType() == Constants.TYPE_USER) {
                        if (responseNotifcation.getTypeDetail() == Constants.TYPE_ALL_USER || responseNotifcation.getTypeDetail() == Constants.TYPE_ALL || responseNotifcation.getTypeDetail() == MainApplication.getInstance().getmUser().getType()) {
                            if (responseNotifcation.getCategory() == Constants.PROMOTION_TYPE) {
                                //     CommonUtils.createNotificationWithMsg(getApplicationContext(), responseNotifcation.getTitle(), responseNotifcation.getContent(), Constants.PROMOTION_TYPE);
                                pingNotification(responseNotifcation.getTitle(), responseNotifcation.getContent());
                            } else {
                                pingNotification(responseNotifcation.getTitle(), responseNotifcation.getContent());
                                //CommonUtils.createNotificationWithMsg(getApplicationContext(), responseNotifcation.getTitle(), responseNotifcation.getContent(), Constants.NOTIFICATION_NORMAL_TYPE);
                            }
                        }
                    }
                    return;
                }

            }
        }
    };

    public void logout() {
        getmSetting().put(Constants.PHONE, "");
        getmSetting().put(Constants.ID, "");
        getmSetting().put(Constants.TYPE_PASSWORD_MAP, false);
        getmSetting().put(Constants.TYPE_USER_STR, -1);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
        MainApplication.getInstance().setmUser(null);
        Intent i = new Intent(BaseActivity.this, MainActivity.class);
        i.putExtra(Constants.MSG, true);
        startActivity(i);
        finishAffinity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogProgress = new ProgressDialogCustom(BaseActivity.this);
        mGoiXeOmApi = ApiUtils.getRootApi().create(GoiXeOmAPI.class);
        dialogError = new MaterialDialog.Builder(this).title(R.string.error)
                .content(getString(R.string.unknow_error))
                .positiveText(R.string.dismis)
                .positiveColor(Color.GRAY)
                .build();

        Intent mIntent = new Intent(this, SocketClient.class);
//        startService(mIntent);
        bindService(mIntent, this.mConnection, BIND_AUTO_CREATE);
    }

    public void showDialogErrorContent(String content) {
        if (dialogError != null) {
            dialogError.setContent(content);
            dialogError.show();
        } else {
            dialogError = new MaterialDialog.Builder(this).title(R.string.error)
                    .content(content)
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
        }
    }

    public void showDialog(String title, String content) {
        if (dialogError != null) {
            dialogError.setTitle(title);
            dialogError.setContent(content);
            dialogError.show();
        } else {
            dialogError = new MaterialDialog.Builder(this).title(title)
                    .content(content)
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
        }
    }

    private MaterialDialog dialogError;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getDialogProgress() != null && getDialogProgress().isShowing())
            getDialogProgress().hideDialog();
        if (dialogError != null && dialogError.isShowing()) dialogError.dismiss();
        if (this.mBounded) {
            unbindService(this.mConnection);
            this.mBounded = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(getmSetting().getString(Constants.ID,"").isEmpty()) {
            Intent i = new Intent(BaseActivity.this, MainActivity.class);
            i.putExtra(Constants.MSG, true);
            startActivity(i);
            finishAffinity();
            return;
        }
        IntentFilter i = new IntentFilter();
        i.addAction(SocketConstants.EVENT_CONNECTION);
        registerReceiver(this.receiver, i);
        IntentFilter iConnection = new IntentFilter();
        iConnection.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.receiverConnectionNetwork, iConnection);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(this.receiver);
            unregisterReceiver(this.receiverConnectionNetwork);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public SPUtils getmSetting() {
        if (mSetting == null) mSetting = new SPUtils(Constants.SETTING);
        return mSetting;
    }

    public void setmSetting(SPUtils mSetting) {
        this.mSetting = mSetting;
    }

    public ProgressDialogCustom getDialogProgress() {
        return dialogProgress;
    }

    public void setDialogProgress(ProgressDialogCustom dialogProgress) {
        this.dialogProgress = dialogProgress;
    }

    public GoiXeOmAPI getmApi() {
        return mGoiXeOmApi;
    }

    public void setmViCoApi(GoiXeOmAPI mCanetsService) {
        this.mGoiXeOmApi = mCanetsService;
    }

    public SocketClient getmSocket() {
        return mSocket;
    }

    public void setmSocket(SocketClient mSocket) {
        this.mSocket = mSocket;
    }

}
