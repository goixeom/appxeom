package goixeom.com.apis;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;

import goixeom.com.interfaces.OnResponse;
import goixeom.com.views.ProgressDialogCustom;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MyPC on 11/6/2016.
 */

public class CallBackCustomNoDelay<T> implements Callback<T> {
    OnResponse<T> t;
    ProgressDialogCustom dialogCustom;
    Context context;
    private boolean showDialogError = true;
    public CallBackCustomNoDelay(Context context, ProgressDialogCustom dialogCustom, OnResponse<T> t) {
        this.dialogCustom = dialogCustom;
        this.t = t;
        this.context = context;
    }
    public CallBackCustomNoDelay(Context context, ProgressDialogCustom dialogCustom,boolean showDialogError, OnResponse<T> t) {
        this.showDialogError = showDialogError;
        this.dialogCustom = dialogCustom;
        this.t = t;
        this.context = context;
    }
    public CallBackCustomNoDelay(Context context, OnResponse<T> t) {
        this.showDialogError = showDialogError;
        this.context = context;
        this.t = t;
    }
    public CallBackCustomNoDelay(Context context,boolean showDialogError, OnResponse<T> t) {
        this.showDialogError = showDialogError;
        this.context = context;
        this.t = t;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            if (dialogCustom != null)
                dialogCustom.hideDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response.isSuccessful()) {
            t.onResponse(response.body());


        } else {
            if(showDialogError) {
//                new MaterialDialog.Builder(context).title(R.string.error)
//                        .content(context.getString(R.string.unknow_error))
//                        .positiveText(R.string.dismis)
//                        .positiveColor(Color.GRAY)
//                        .show();
            }
            LogUtils.e("Error onRespone " + response.code());
//            RLog.e(call.request().body().contentType().toString());
//            RLog.e(call.request().headers().toString());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if(!NetworkUtils.isConnected()) return;
        try {
            if (dialogCustom != null)
                dialogCustom.hideDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.e("error - callback " + t.getMessage());
    }
}
