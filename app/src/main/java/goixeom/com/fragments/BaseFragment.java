package goixeom.com.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.SPUtils;

import goixeom.com.R;
import goixeom.com.apis.ApiUtils;
import goixeom.com.apis.GoiXeOmAPI;
import goixeom.com.utils.Constants;
import goixeom.com.views.ProgressDialogCustom;

/**
 * Created by DuongKK on 6/14/2017.
 */

public class BaseFragment extends Fragment{
    private ProgressDialogCustom dialogProgress;
    private GoiXeOmAPI mGoiXeOmApi;
    private SPUtils mSetting = new SPUtils(Constants.SETTING);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogProgress=new ProgressDialogCustom(getContext());
        mGoiXeOmApi= ApiUtils.getRootApi().create(GoiXeOmAPI.class);
    dialogError =     new MaterialDialog.Builder(getContext()).title(R.string.error)
                .content(getString(R.string.unknow_error))
                .positiveText(R.string.dismis)
                .positiveColor(Color.GRAY)
                .build();
    }
    public void showDialogErrorContent (String content){
        if(dialogError!=null) {
            dialogError.setContent(content);
            dialogError.show();
        }else{
            dialogError =     new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content(content)
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
        }
    }
    private MaterialDialog dialogError;

    public SPUtils getmSetting() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getDialogProgress()!=null && getDialogProgress().isShowing()) getDialogProgress().hideDialog();
        if(dialogError!=null &&dialogError.isShowing()) dialogError.dismiss();

    }
}
