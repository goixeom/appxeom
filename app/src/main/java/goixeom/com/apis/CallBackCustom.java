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

public class CallBackCustom<T> implements Callback<T> {
    OnResponse<T> t;
    ProgressDialogCustom dialogCustom;
    Context context;

    public CallBackCustom(Context context, ProgressDialogCustom dialogCustom, OnResponse<T> t) {
        this.dialogCustom = dialogCustom;
        this.t = t;
        this.context = context;
    }

    public CallBackCustom(Context context, OnResponse<T> t) {
        this.context = context;
        this.t = t;
    }

    @Override
    public void onResponse(Call<T> call, final Response<T> response) {
        try {
            if (dialogCustom != null)
                dialogCustom.hideDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.isSuccessful()) {
            t.onResponse(response.body());
        } else {
            LogUtils.e("Error onRespone " + response.code());
        }

    }

    @Override
    public void onFailure(Call<T> call, final Throwable t) {
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
